// Spotless configuration for Android Compose Base
// Ensures consistent code formatting across the project

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        
        // Use ktlint for Kotlin formatting
        ktlint("0.50.0")
            .userData(
                mapOf(
                    "android" to "true",
                    "max_line_length" to "120",
                    "indent_size" to "4",
                    "continuation_indent_size" to "4",
                    "insert_final_newline" to "true",
                    "charset" to "utf-8",
                    "trim_trailing_whitespace" to "true",
                    "end_of_line" to "lf",
                    "disabled_rules" to "no-wildcard-imports,import-ordering"
                )
            )
        
        // Remove unused imports
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
        
        // Custom formatting rules for Compose
        custom("compose-formatting") { text ->
            text
                // Ensure proper spacing around Compose annotations
                .replace(Regex("@Composable\\s*\\n\\s*fun"), "@Composable\nfun")
                // Ensure proper spacing in Compose parameter lists
                .replace(Regex("\\)\\s*\\{\\s*\\n"), ") {\n")
        }
    }
    
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("0.50.0")
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
    
    format("xml") {
        target("**/*.xml")
        targetExclude("**/build/**/*.xml")
        eclipseWtp(com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep.XML)
        trimTrailingWhitespace()
        indentWithSpaces(4)
        endWithNewline()
    }
    
    format("misc") {
        target("**/*.md", "**/.gitignore")
        trimTrailingWhitespace()
        indentWithSpaces(2)
        endWithNewline()
    }
}

// Custom tasks for easier usage
tasks.register("formatCode") {
    group = "formatting"
    description = "Format all code using Spotless"
    dependsOn("spotlessApply")
}

tasks.register("checkCodeFormat") {
    group = "verification"
    description = "Check code formatting using Spotless"
    dependsOn("spotlessCheck")
}

// Integrate with build process
tasks.named("preBuild") {
    dependsOn("spotlessCheck")
}