rootProject.name = "godot-kotlin-tests"

includeBuild("../../kt/api-generator") {
    dependencySubstitution {
        substitute(module("com.utopia-rise:api-generator")).with(project(":"))
    }
}
includeBuild("../../kt") {
    dependencySubstitution {
        substitute(module("com.utopia-rise:godot-gradle-plugin")).with(project(":godot-gradle-plugin"))
        substitute(module("com.utopia-rise:godot-annotation-processor")).with(project(":godot-annotation-processor"))
        substitute(module("com.utopia-rise:godot-runtime")).with(project(":godot-runtime"))
        substitute(module("com.utopia-rise:godot-library")).with(project(":godot-library"))
        substitute(module("com.utopia-rise:godot-bootstrap")).with(project(":godot-bootstrap"))
        substitute(module("com.utopia-rise:godot-kotlin-compiler-plugin-common")).with(project(":godot-kotlin-compiler-plugin-common"))
        substitute(module("com.utopia-rise:godot-kotlin-compiler-plugin")).with(project(":godot-kotlin-compiler-plugin"))
        substitute(module("com.utopia-rise:godot-kotlin-entry-generator")).with(project(":godot-kotlin-entry-generator"))
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy.eachPlugin {
        if (requested.id.id == "com.utopia-rise.godot-kotlin-jvm") {
            useModule("com.utopia-rise:godot-gradle-plugin:${requested.version}")
        }
        if (requested.id.id == "com.utopia-rise.api-generator") {
            useModule("com.utopia-rise:api-generator:${requested.version}")
        }
    }
}
