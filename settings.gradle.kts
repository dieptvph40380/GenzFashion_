pluginManagement {
    repositories {
        google() // Kho chứa các plugin của Android
        mavenCentral() // Kho Maven chính thống
        gradlePluginPortal() // Kho chứa plugin Gradle
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Ngăn chặn các kho cục bộ trong các mô-đun
    repositories {
        google() // Kho chính thức của Android
        mavenCentral() // Kho Maven chính thống
        maven("https://jitpack.io")
    }
}

rootProject.name = "GenZ_Fashion" // Tên dự án
include(":app") // Bao gồm mô-đun chính của ứng dụng
