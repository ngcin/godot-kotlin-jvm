Godot Kotlin/JVM
# Home 主页
Godot Kotlin/JVM is a Godot module which allows you to write your game or application logic in Kotlin on the JVM.

Godot Kotlin/JVM是一个允许你使用基于JVM的Kotlin语言编写游戏或者应用程序逻辑的Godot模块。
If you are looking for the documentation for Godot Kotlin/Native; you can find it here.
如果你在寻找Godot Kotlin/Native的文档，你可以在[此处](https://godot-kotlin.readthedocs.io/en/latest/)查看。

# state 项目状态
This project is in alpha, it is by no means production ready.
该项目目前处于alpha版本，但绝不是已经准备好用于生产环境。

# What's not working 不起作用
The items in this list are explicitly mentioned here as these will be implemented in future versions. Also consider the Api Differences section for general differences and limitations which will not be or cannot be adressed in the near forseable future or ever.
在此列表中明确提及的项目将在未来的版本中实现。还要考虑到API的差异部分，在可预见的将来或者永远无法解决的一般差异和限制。
- Each registered constructor must have a unique number of arguments, constructor overloading is not yet supported。每个注册的构造函数必须有一个唯一的数字参数，构造函数目前还不支持重载；
- No tool mode (you can set it already in the @RegisterClass annotation but it has no effect yet) 无工具模式（你可以在@RegisterClass注解中设置，但是目前还没生效）
- No plugin support, you cannot use Godot Kotlin/JVM to write plugins and addons yet；没有插件支持，你还不能使用Godot Kotlin/JVM去开发插件。
- Only desktop OS (Linux, MacOS, Windows) and Android are supported for now。目前仅支持桌面操作系统(Linux, MacOS, Windows)和Android系统。

# Bug reporting and Questions BUG提交和问题
If you find bugs, please report an issue on github - but check for duplicates first. If you have questions or need help, you can ask on discord in the channels questions and help respectively. If you don't have discord or don't want to use it, make a issue on github.

如果你发现了bugs，请提交issue到github - 提交之前请先检查是否有重复的。如果你有问题或者需要帮助，你可以分别在discord的问题频道和帮助频道提问和寻求帮助。如果你还没有discord或者你不想使用discord，也可以在github创建一个issue。

# Supported languages 支持的语言
The only language currently supported is Kotlin. That said it is possible to support other Jvm based languages in the future. If you want to add support for another Jvm based language, feel free to open an issue and we'll explain in detail what is necessary to support another language and help you getting started with development.
目前仅支持的语言是kotlin。意味着在将来可能支持其他基于JVM的开发语言。如果你想增加基于JVM的其他开发语言的支持，随时展开议题，我们将会详细解释支持另一种语言的必要条件，并帮助你开始开发。

# Custom engine builds 自定义引擎构建
Get our pre built engine builds and export templates from the latest github release.你可以从github release获取我们预先构建好的引擎和导出开发模版。

# Developer discussion 开发者讨论
Ask questions and **collaborate** on Discord. 在Discord上提问和协作。
