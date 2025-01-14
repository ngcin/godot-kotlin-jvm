// THIS FILE IS GENERATED! DO NOT EDIT IT MANUALLY!
@file:Suppress("PackageDirectoryMismatch", "unused", "FunctionName", "RedundantModalityModifier",
    "UNCHECKED_CAST", "JoinDeclarationAndAssignment", "USELESS_CAST",
    "RemoveRedundantQualifierName", "NOTHING_TO_INLINE")

package godot

import godot.annotation.GodotBaseType
import godot.core.TransferContext
import godot.core.VariantType.LONG
import godot.core.VariantType.NIL
import kotlin.Long
import kotlin.Suppress

/**
 *
 */
@GodotBaseType
open class VisualShaderNodeScalarFunc : VisualShaderNode() {
  /**
   *
   */
  open var function: Long
    get() {
      TransferContext.writeArguments()
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_VISUALSHADERNODESCALARFUNC_GET_FUNCTION, LONG)
      return TransferContext.readReturnValue(LONG, false) as Long
    }
    set(value) {
      TransferContext.writeArguments(LONG to value)
      TransferContext.callMethod(rawPtr,
          ENGINEMETHOD_ENGINECLASS_VISUALSHADERNODESCALARFUNC_SET_FUNCTION, NIL)
    }

  override fun __new() {
    callConstructor(ENGINECLASS_VISUALSHADERNODESCALARFUNC)
  }

  enum class Function(
    id: Long
  ) {
    /**
     *
     */
    FUNC_SIN(0),

    /**
     *
     */
    FUNC_COS(1),

    /**
     *
     */
    FUNC_TAN(2),

    /**
     *
     */
    FUNC_ASIN(3),

    /**
     *
     */
    FUNC_ACOS(4),

    /**
     *
     */
    FUNC_ATAN(5),

    /**
     *
     */
    FUNC_SINH(6),

    /**
     *
     */
    FUNC_COSH(7),

    /**
     *
     */
    FUNC_TANH(8),

    /**
     *
     */
    FUNC_LOG(9),

    /**
     *
     */
    FUNC_EXP(10),

    /**
     *
     */
    FUNC_SQRT(11),

    /**
     *
     */
    FUNC_ABS(12),

    /**
     *
     */
    FUNC_SIGN(13),

    /**
     *
     */
    FUNC_FLOOR(14),

    /**
     *
     */
    FUNC_ROUND(15),

    /**
     *
     */
    FUNC_CEIL(16),

    /**
     *
     */
    FUNC_FRAC(17),

    /**
     *
     */
    FUNC_SATURATE(18),

    /**
     *
     */
    FUNC_NEGATE(19),

    /**
     *
     */
    FUNC_ACOSH(20),

    /**
     *
     */
    FUNC_ASINH(21),

    /**
     *
     */
    FUNC_ATANH(22),

    /**
     *
     */
    FUNC_DEGREES(23),

    /**
     *
     */
    FUNC_EXP2(24),

    /**
     *
     */
    FUNC_INVERSE_SQRT(25),

    /**
     *
     */
    FUNC_LOG2(26),

    /**
     *
     */
    FUNC_RADIANS(27),

    /**
     *
     */
    FUNC_RECIPROCAL(28),

    /**
     *
     */
    FUNC_ROUNDEVEN(29),

    /**
     *
     */
    FUNC_TRUNC(30),

    /**
     *
     */
    FUNC_ONEMINUS(31);

    val id: Long
    init {
      this.id = id
    }

    companion object {
      fun from(value: Long) = values().single { it.id == value }
    }
  }

  companion object {
    /**
     *
     */
    final const val FUNC_ABS: Long = 12

    /**
     *
     */
    final const val FUNC_ACOS: Long = 4

    /**
     *
     */
    final const val FUNC_ACOSH: Long = 20

    /**
     *
     */
    final const val FUNC_ASIN: Long = 3

    /**
     *
     */
    final const val FUNC_ASINH: Long = 21

    /**
     *
     */
    final const val FUNC_ATAN: Long = 5

    /**
     *
     */
    final const val FUNC_ATANH: Long = 22

    /**
     *
     */
    final const val FUNC_CEIL: Long = 16

    /**
     *
     */
    final const val FUNC_COS: Long = 1

    /**
     *
     */
    final const val FUNC_COSH: Long = 7

    /**
     *
     */
    final const val FUNC_DEGREES: Long = 23

    /**
     *
     */
    final const val FUNC_EXP: Long = 10

    /**
     *
     */
    final const val FUNC_EXP2: Long = 24

    /**
     *
     */
    final const val FUNC_FLOOR: Long = 14

    /**
     *
     */
    final const val FUNC_FRAC: Long = 17

    /**
     *
     */
    final const val FUNC_INVERSE_SQRT: Long = 25

    /**
     *
     */
    final const val FUNC_LOG: Long = 9

    /**
     *
     */
    final const val FUNC_LOG2: Long = 26

    /**
     *
     */
    final const val FUNC_NEGATE: Long = 19

    /**
     *
     */
    final const val FUNC_ONEMINUS: Long = 31

    /**
     *
     */
    final const val FUNC_RADIANS: Long = 27

    /**
     *
     */
    final const val FUNC_RECIPROCAL: Long = 28

    /**
     *
     */
    final const val FUNC_ROUND: Long = 15

    /**
     *
     */
    final const val FUNC_ROUNDEVEN: Long = 29

    /**
     *
     */
    final const val FUNC_SATURATE: Long = 18

    /**
     *
     */
    final const val FUNC_SIGN: Long = 13

    /**
     *
     */
    final const val FUNC_SIN: Long = 0

    /**
     *
     */
    final const val FUNC_SINH: Long = 6

    /**
     *
     */
    final const val FUNC_SQRT: Long = 11

    /**
     *
     */
    final const val FUNC_TAN: Long = 2

    /**
     *
     */
    final const val FUNC_TANH: Long = 8

    /**
     *
     */
    final const val FUNC_TRUNC: Long = 30
  }
}
