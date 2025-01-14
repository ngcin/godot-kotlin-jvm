package godot.entrygenerator.generator.property

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName.Companion.member
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import godot.entrygenerator.exceptions.WrongExportUsageException
import godot.entrygenerator.extension.getAnnotationValue
import godot.entrygenerator.extension.toParameterKtVariantType
import godot.entrygenerator.extension.toReturnKtVariantType
import godot.entrygenerator.generator.property.defaultvalue.DefaultValueExtractorProvider
import godot.entrygenerator.generator.property.hintstring.PropertyHintStringGeneratorProvider
import godot.entrygenerator.generator.property.typehint.PropertyTypeHintProvider
import godot.entrygenerator.model.ENUM_FLAG_ANNOTATION
import godot.entrygenerator.model.EXPORT_ANNOTATION
import godot.entrygenerator.model.REGISTER_PROPERTY_ANNOTATION
import godot.entrygenerator.model.REGISTER_PROPERTY_ANNOTATION_RPC_MODE_ARGUMENT
import godot.entrygenerator.model.RegisteredProperty
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isEnum
import org.jetbrains.kotlin.types.typeUtil.supertypes

object PropertyRegistrationGenerator {
    fun registerProperties(
        registeredProperties: MutableList<RegisteredProperty>,
        classSpecificRegistryBuilder: TypeSpec.Builder,
        registerClassControlFlow: FunSpec.Builder,
        className: ClassName,
        bindingContext: BindingContext
    ) {
        registeredProperties
            .forEach { registeredProperty ->
                exportSanityCheck(registeredProperty)
                when {
                    registeredProperty.propertyDescriptor.type.isEnum() -> registerEnum(
                        className,
                        registeredProperty,
                        bindingContext,
                        classSpecificRegistryBuilder,
                        registerClassControlFlow
                    )
                    registeredProperty
                        .propertyDescriptor
                        .type
                        .getJetTypeFqName(false)
                        .matches(Regex("^kotlin\\.collections\\..*Set\$")) &&
                        registeredProperty.propertyDescriptor.type.arguments.firstOrNull()?.type?.isEnum() == true &&
                        registeredProperty
                            .propertyDescriptor
                            .annotations
                            .hasAnnotation(FqName(ENUM_FLAG_ANNOTATION)) -> registerEnumFlag(
                        className,
                        registeredProperty,
                        bindingContext,
                        classSpecificRegistryBuilder,
                        registerClassControlFlow
                    )
                    registeredProperty
                        .propertyDescriptor
                        .type
                        .getJetTypeFqName(false)
                        .matches(Regex("^kotlin\\.collections\\..*")) &&
                        registeredProperty.propertyDescriptor.type.arguments.firstOrNull()?.type?.isEnum() == true ->
                        registerEnumList(
                            className,
                            registeredProperty,
                            bindingContext,
                            classSpecificRegistryBuilder,
                            registerClassControlFlow
                        )
                    else -> registerProperty(
                        className,
                        registeredProperty,
                        bindingContext,
                        classSpecificRegistryBuilder,
                        registerClassControlFlow
                    )
                }
            }
    }

    private fun exportSanityCheck(registeredProperty: RegisteredProperty) {
        if (registeredProperty.propertyDescriptor.annotations.hasAnnotation(FqName(EXPORT_ANNOTATION))) {
            if (
                registeredProperty.propertyDescriptor.type.supertypes().any { it.getJetTypeFqName(false) == "godot.Object" } &&
                !registeredProperty.propertyDescriptor.type.supertypes().any { it.getJetTypeFqName(false) == "godot.Reference" }
            ) {
                throw WrongExportUsageException(registeredProperty.propertyDescriptor)
            }
        }
    }

    private fun registerEnumFlag(
        className: ClassName,
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder,
        registerClassControlFlow: FunSpec.Builder
    ) {
        val defaultValueProvider = generateAndProvideDefaultValueProvider(registeredProperty, bindingContext, classSpecificRegistryBuilder)

        registerClassControlFlow
            .addStatement(
                "enumFlagProperty(%L,·$defaultValueProvider,·%L,·%T.id.toInt())",
                getPropertyReference(registeredProperty.propertyDescriptor, className),
                shouldBeVisibleInEditor(registeredProperty.propertyDescriptor),
                getRpcModeEnum(registeredProperty.propertyDescriptor)
            )
    }

    private fun registerEnumList(
        className: ClassName,
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder,
        registerClassControlFlow: FunSpec.Builder
    ) {
        val defaultValueProvider = generateAndProvideDefaultValueProvider(registeredProperty, bindingContext, classSpecificRegistryBuilder)

        registerClassControlFlow
            .addStatement(
                "enumListProperty(%L,·$defaultValueProvider,·%L,·%T.id.toInt())",
                getPropertyReference(registeredProperty.propertyDescriptor, className),
                shouldBeVisibleInEditor(registeredProperty.propertyDescriptor),
                getRpcModeEnum(registeredProperty.propertyDescriptor)
            )
    }

    private fun registerEnum(
        className: ClassName,
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder,
        registerClassControlFlow: FunSpec.Builder
    ) {

        val defaultValueProvider = generateAndProvideDefaultValueProvider(registeredProperty, bindingContext, classSpecificRegistryBuilder)

        registerClassControlFlow
            .addStatement(
                "enumProperty(%L,·$defaultValueProvider,·%L,·%T.id.toInt())",
                getPropertyReference(registeredProperty.propertyDescriptor, className),
                shouldBeVisibleInEditor(registeredProperty.propertyDescriptor),
                getRpcModeEnum(registeredProperty.propertyDescriptor)
            )
    }

    private fun registerProperty(
        className: ClassName,
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder,
        registerClassControlFlow: FunSpec.Builder
    ) {
        val defaultValueProvider = generateAndProvideDefaultValueProvider(registeredProperty, bindingContext, classSpecificRegistryBuilder)

        val typeFqNameWithNullability = if (registeredProperty.propertyDescriptor.type.isMarkedNullable) {
            "${registeredProperty.propertyDescriptor.type.getJetTypeFqName(false)}?"
        } else {
            registeredProperty.propertyDescriptor.type.getJetTypeFqName(false)
        }

        registerClassControlFlow
            .addStatement(
                "property(%L,·%T,·%T,·%S,·%T,·%S,·$defaultValueProvider,·%L,·%T.id.toInt())",
                getPropertyReference(registeredProperty.propertyDescriptor, className),
                registeredProperty.propertyDescriptor.type.toParameterKtVariantType(),
                registeredProperty.propertyDescriptor.type.toReturnKtVariantType(),
                typeFqNameWithNullability,
                PropertyTypeHintProvider.provide(registeredProperty.propertyDescriptor),
                PropertyHintStringGeneratorProvider
                    .provide(registeredProperty.propertyDescriptor, bindingContext)
                    .getHintString()
                    .replace("?", ""),
                shouldBeVisibleInEditor(registeredProperty.propertyDescriptor),
                getRpcModeEnum(registeredProperty.propertyDescriptor)
            )
    }

    private fun generateDefaultValueProvider(
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder
    ) {
        val (defaultValueStringTemplate, defaultValueStringTemplateValues) = DefaultValueExtractorProvider
            .provide(registeredProperty.propertyDescriptor, bindingContext)
            .getDefaultValue(null)

        val returnType = registeredProperty.propertyDescriptor.returnType
        requireNotNull(returnType)

        val returnTypePackagePath = returnType.getJetTypeFqName(false).substringBeforeLast(".")
        val returnTypeSimpleName = returnType.getJetTypeFqName(false).substringAfterLast(".")
        val tmpClassName = ClassName(returnTypePackagePath, returnTypeSimpleName)
        var returnTypeClassName: TypeName = tmpClassName

        if (returnType.arguments.isNotEmpty()) {
            returnTypeClassName = tmpClassName.parameterizedBy(
                returnType
                    .arguments
                    .map { typeProjection ->
                        val fqName = typeProjection.type.getJetTypeFqName(false)
                        val isNullable = typeProjection.type.isMarkedNullable
                        ClassName(fqName.substringBeforeLast("."), fqName.substringAfterLast("."))
                            .copy(nullable = isNullable)
                    }
            )
        }

        val defaultValuePropertySpec = PropertySpec
            .builder(
                "${registeredProperty.propertyDescriptor.name}DefaultValueProvider",
                LambdaTypeName.get(returnType = returnTypeClassName.copy(nullable = defaultValueStringTemplateValues.all { it is String && it == "null" }))
            )
            .addModifiers(KModifier.OPEN)
            .initializer("{·${defaultValueStringTemplate.replace(" ", "·")}·}", *defaultValueStringTemplateValues)

        if (registeredProperty.isOverriding) {
            defaultValuePropertySpec.addModifiers(KModifier.OVERRIDE)
        }

        classSpecificRegistryBuilder
            .addProperty(defaultValuePropertySpec.build())
    }

    private fun getPropertyReference(propertyDescriptor: PropertyDescriptor, className: ClassName): CodeBlock {
        return className
            .member(propertyDescriptor.name.asString())
            .reference()
    }

    private fun shouldBeVisibleInEditor(propertyDescriptor: PropertyDescriptor): Boolean {
        return propertyDescriptor
            .annotations
            .hasAnnotation(FqName(EXPORT_ANNOTATION))
    }

    private fun getRpcModeEnum(propertyDescriptor: PropertyDescriptor): ClassName {
        val compilerRpcModeEnumRepresentation = getCompilerRpcModeEnumRepresentation(propertyDescriptor)
        val packagePath = compilerRpcModeEnumRepresentation.first.asString().replace("/", ".")
        val name = compilerRpcModeEnumRepresentation.second
        return ClassName(packagePath, name.asString())
    }

    private fun getCompilerRpcModeEnumRepresentation(propertyDescriptor: PropertyDescriptor): Pair<ClassId, Name> {
        return propertyDescriptor
            .annotations
            .getAnnotationValue(
                REGISTER_PROPERTY_ANNOTATION,
                REGISTER_PROPERTY_ANNOTATION_RPC_MODE_ARGUMENT,
                Pair(ClassId(FqName("godot.MultiplayerAPI"), Name.identifier("RPCMode")), Name.identifier("DISABLED"))
            )
    }

    private fun isOfType(type: KotlinType, typeFqName: String): Boolean {
        return if (type.getJetTypeFqName(false) == typeFqName) {
            true
        } else {
            type
                .supertypes()
                .any { it.getJetTypeFqName(false) == typeFqName }
        }
    }

    private fun generateAndProvideDefaultValueProvider(
        registeredProperty: RegisteredProperty,
        bindingContext: BindingContext,
        classSpecificRegistryBuilder: TypeSpec.Builder
    ): String {
        if (shouldBeVisibleInEditor(registeredProperty.propertyDescriptor) && (registeredProperty.isOverriding || !registeredProperty.isInherited)) {
            generateDefaultValueProvider(registeredProperty, bindingContext, classSpecificRegistryBuilder)
        }
        return if (shouldBeVisibleInEditor(registeredProperty.propertyDescriptor)) {
            "${registeredProperty.propertyDescriptor.name}DefaultValueProvider"
        } else {
            "{·null·}"
        }
    }
}
