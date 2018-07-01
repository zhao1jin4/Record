
Kotlin  成为 Android 官方开发语言

Intellij IDEA 的官方(jetbrains)语言

Kotlin 1.1.51 在  Android Studio 3.0 有集成

Eclipse Marketplace  搜索 Kotlin ,目前最新的是Kotlin Plugin for Eclipse 0.8.2


fun main(args: Array<String>) {
    println("Hello, world!")
}



fun main(args: Array<String>) {
    if (args.size == 0) {
        println("Please provide a name as a command-line argument")
        return
    }
    println("Hello, ${args[0]}!")
}



fun main(args: Array<String>) {
    for (name in args)
        println("Hello, $name!")
}