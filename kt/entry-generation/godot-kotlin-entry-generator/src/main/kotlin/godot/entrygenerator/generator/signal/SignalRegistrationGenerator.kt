package godot.entrygenerator.generator.signal

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName.Companion.member
import godot.entrygenerator.extension.assignmentPsi
import godot.entrygenerator.extension.toReturnKtVariantType
import org.jetbrains.kotlin.backend.common.serialization.findPackage
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType

object SignalRegistrationGenerator {

    fun registerSignals(
        signals: List<PropertyDescriptor>,
        className: ClassName,
        registerClassControlFlow: FunSpec.Builder
    ) {
        signals.forEach { propertyDescriptor ->
            signalSanityCheck(propertyDescriptor)
            registerSignal(propertyDescriptor, className, registerClassControlFlow)
        }
    }

    private fun registerSignal(
        propertyDescriptor: PropertyDescriptor,
        className: ClassName,
        registerClassControlFlow: FunSpec.Builder
    ) {
        val signalArguments = propertyDescriptor
            .type
            .arguments
            .map { it.type }

        val signalArgumentNamesAsLiteralStrings = propertyDescriptor
            .assignmentPsi
            .children
            .last() //value argument list
            .children
            .map { it.text } //use with %L rather than with %S as these strings already are surrounded with ""

        require(signalArguments.size == signalArgumentNamesAsLiteralStrings.size) {
            "Signal type arguments count does not match signal argument name count! This should never happen! Please report a bug with a minimal reproduction sample to https://github.com/utopia-rise/godot-jvm/issues"
        }

        registerClassControlFlow
            .addStatement(
                getStringTemplate(signalArguments),
                *getSignalArguments(propertyDescriptor, className, signalArguments, signalArgumentNamesAsLiteralStrings)
            )
    }

    private fun getStringTemplate(signalArguments: List<KotlinType>): String {
        return buildString {
            append("signal(%L") //signalPropertyReference

            //a KtFunctionArgument per signal argument
            signalArguments.forEach { _ ->
                append(",·%T(%T,·%S,·%L)")
            }

            append(")") //signal closing
        }
    }

    private fun getSignalArguments(
        propertyDescriptor: PropertyDescriptor,
        className: ClassName,
        signalArguments: List<KotlinType>,
        signalArgumentNamesAsLiteralStrings: List<String>
    ): Array<Any> {
        return buildList {
            add(getPropertyReference(propertyDescriptor, className)) //signalPropertyReference

            //a KtFunctionArgument per signal argument
            signalArguments.forEachIndexed { index, type ->
                add(ClassName("godot.runtime", "KtFunctionArgument"))
                add(type.toReturnKtVariantType())
                add(type.getJetTypeFqName(false))
                add(signalArgumentNamesAsLiteralStrings[index]) //out of bounds already checked
            }
        }.toTypedArray()
    }

    private fun getPropertyReference(propertyDescriptor: PropertyDescriptor, className: ClassName): CodeBlock {
        return className
            .member(propertyDescriptor.name.asString())
            .reference()
    }

    private fun signalSanityCheck(propertyDescriptor: PropertyDescriptor) {
        val propertyTypeAsString = propertyDescriptor.type.toString()

        if (!propertyDescriptor.name.asString().startsWith("signal")) {
            throw IllegalStateException("All signals must be prefixed with \"signal\"! Ex: signalButtonPressed. The signal ${propertyDescriptor.fqNameSafe} does not fulfill this criteria.")
        }

        if (propertyTypeAsString.startsWith("Signal")) {
            try {
                propertyTypeAsString.replace("Signal", "").split("<")[0].toInt()
            } catch (e: NumberFormatException) {
                throw IllegalStateException("You annotated ${propertyDescriptor.fqNameSafe} with @RegisterSignal but it's type is no signal! Use \"by signal\" to define signals.")
            }
        } else {
            throw IllegalStateException("You annotated ${propertyDescriptor.fqNameSafe} with @RegisterSignal but it's type is no signal! Use \"by signal\" to define signals.")
        }
    }
}
