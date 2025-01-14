// THIS FILE IS GENERATED! DO NOT EDIT IT MANUALLY!
@file:Suppress("PackageDirectoryMismatch", "unused", "FunctionName", "RedundantModalityModifier",
    "UNCHECKED_CAST", "JoinDeclarationAndAssignment", "USELESS_CAST",
    "RemoveRedundantQualifierName", "NOTHING_TO_INLINE")

package godot

import godot.annotation.GodotBaseType
import godot.core.GodotError
import godot.core.PoolStringArray
import godot.core.TransferContext
import godot.core.VariantArray
import godot.core.VariantType.ANY
import godot.core.VariantType.ARRAY
import godot.core.VariantType.BOOL
import godot.core.VariantType.JVM_INT
import godot.core.VariantType.LONG
import godot.core.VariantType.OBJECT
import godot.core.VariantType.POOL_STRING_ARRAY
import godot.core.VariantType.STRING
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress

/**
 * A class that stores an expression you can execute.
 *
 * An expression can be made of any arithmetic operation, built-in math function call, method call of a passed instance, or built-in type construction call.
 *
 * An example expression text using the built-in math functions could be `sqrt(pow(3,2) + pow(4,2))`.
 *
 * In the following example we use a [godot.LineEdit] node to write our expression and show the result.
 *
 * ```
 * 		onready var expression = Expression.new()
 *
 * 		func _ready():
 * 		    $LineEdit.connect("text_entered", self, "_on_text_entered")
 *
 * 		func _on_text_entered(command):
 * 		    var error = expression.parse(command, [])
 * 		    if error != OK:
 * 		        print(expression.get_error_text())
 * 		        return
 * 		    var result = expression.execute([], null, true)
 * 		    if not expression.has_execute_failed():
 * 		        $LineEdit.text = str(result)
 * 		```
 */
@GodotBaseType
open class Expression : Reference() {
  override fun __new() {
    callConstructor(ENGINECLASS_EXPRESSION)
  }

  /**
   * Executes the expression that was previously parsed by [parse] and returns the result. Before you use the returned object, you should check if the method failed by calling [hasExecuteFailed].
   *
   * If you defined input variables in [parse], you can specify their values in the inputs array, in the same order.
   */
  open fun execute(
    inputs: VariantArray<Any?> = VariantArray(),
    baseInstance: Object? = null,
    showError: Boolean = true
  ): Any? {
    TransferContext.writeArguments(ARRAY to inputs, OBJECT to baseInstance, BOOL to showError)
    TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_EXPRESSION_EXECUTE, ANY)
    return TransferContext.readReturnValue(ANY, true) as Any?
  }

  /**
   * Returns the error text if [parse] has failed.
   */
  open fun getErrorText(): String {
    TransferContext.writeArguments()
    TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_EXPRESSION_GET_ERROR_TEXT, STRING)
    return TransferContext.readReturnValue(STRING, false) as String
  }

  /**
   * Returns `true` if [execute] has failed.
   */
  open fun hasExecuteFailed(): Boolean {
    TransferContext.writeArguments()
    TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_EXPRESSION_HAS_EXECUTE_FAILED, BOOL)
    return TransferContext.readReturnValue(BOOL, false) as Boolean
  }

  /**
   * Parses the expression and returns an [enum Error] code.
   *
   * You can optionally specify names of variables that may appear in the expression with `input_names`, so that you can bind them when it gets executed.
   */
  open fun parse(expression: String, inputNames: PoolStringArray = PoolStringArray()): GodotError {
    TransferContext.writeArguments(STRING to expression, POOL_STRING_ARRAY to inputNames)
    TransferContext.callMethod(rawPtr, ENGINEMETHOD_ENGINECLASS_EXPRESSION_PARSE, LONG)
    return GodotError.values()[TransferContext.readReturnValue(JVM_INT) as Int]
  }
}
