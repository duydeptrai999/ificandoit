package com.xiaomi.base.testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.waitUntil
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Comprehensive test strategy manager for the application
 * Provides utilities for unit testing, integration testing, and UI testing
 */
@Singleton
class TestStrategyManager @Inject constructor() {
    
    companion object {
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val DEFAULT_RETRY_COUNT = 3
        const val DEFAULT_DELAY_MS = 100L
    }
    
    /**
     * Execute a test with retry mechanism
     */
    suspend fun executeWithRetry(
        maxRetries: Int = DEFAULT_RETRY_COUNT,
        delayMs: Long = DEFAULT_DELAY_MS,
        testAction: suspend () -> Unit
    ): TestResult {
        var lastException: Exception? = null
        
        repeat(maxRetries) { attempt ->
            try {
                testAction()
                return TestResult.Success(attempt + 1)
            } catch (e: Exception) {
                lastException = e
                Timber.w(e, "Test attempt ${attempt + 1} failed")
                if (attempt < maxRetries - 1) {
                    delay(delayMs)
                }
            }
        }
        
        return TestResult.Failure(lastException ?: Exception("Unknown test failure"), maxRetries)
    }
    
    /**
     * Measure test execution time
     */
    suspend fun <T> measureTestTime(
        testName: String,
        testAction: suspend () -> T
    ): TestExecutionResult<T> {
        val startTime = System.currentTimeMillis()
        
        return try {
            val result = testAction()
            val executionTime = System.currentTimeMillis() - startTime
            
            Timber.d("Test '$testName' completed in ${executionTime}ms")
            TestExecutionResult.Success(result, executionTime)
        } catch (e: Exception) {
            val executionTime = System.currentTimeMillis() - startTime
            
            Timber.e(e, "Test '$testName' failed after ${executionTime}ms")
            TestExecutionResult.Failure(e, executionTime)
        }
    }
    
    /**
     * Create a test data builder
     */
    fun <T> createTestDataBuilder(): TestDataBuilder<T> {
        return TestDataBuilder()
    }
    
    /**
     * Validate test preconditions
     */
    fun validatePreconditions(vararg conditions: TestCondition): ValidationResult {
        val failedConditions = mutableListOf<TestCondition>()
        
        conditions.forEach { condition ->
            try {
                if (!condition.check()) {
                    failedConditions.add(condition)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to check condition: ${condition.description}")
                failedConditions.add(condition)
            }
        }
        
        return if (failedConditions.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Failure(failedConditions)
        }
    }
    
    /**
     * Generate test report
     */
    fun generateTestReport(testResults: List<TestCaseResult>): TestReport {
        val totalTests = testResults.size
        val passedTests = testResults.count { it.status == TestStatus.PASSED }
        val failedTests = testResults.count { it.status == TestStatus.FAILED }
        val skippedTests = testResults.count { it.status == TestStatus.SKIPPED }
        
        val totalExecutionTime = testResults.sumOf { it.executionTimeMs }
        val averageExecutionTime = if (totalTests > 0) totalExecutionTime / totalTests else 0L
        
        return TestReport(
            totalTests = totalTests,
            passedTests = passedTests,
            failedTests = failedTests,
            skippedTests = skippedTests,
            successRate = if (totalTests > 0) (passedTests.toDouble() / totalTests * 100) else 0.0,
            totalExecutionTimeMs = totalExecutionTime,
            averageExecutionTimeMs = averageExecutionTime,
            testResults = testResults
        )
    }
}

/**
 * Compose UI testing utilities
 */
class ComposeTestUtils {
    
    companion object {
        /**
         * Wait for a composable to appear with timeout
         */
        fun ComposeContentTestRule.waitForNodeWithTag(
            testTag: String,
            timeoutMs: Long = TestStrategyManager.DEFAULT_TIMEOUT_MS
        ): SemanticsNodeInteraction {
            return waitUntil(timeoutMs) {
                onAllNodes(hasTestTag(testTag)).fetchSemanticsNodes().isNotEmpty()
            }.let {
                onNodeWithTag(testTag)
            }
        }
        
        /**
         * Perform click with retry
         */
        fun SemanticsNodeInteraction.performClickWithRetry(
            maxRetries: Int = TestStrategyManager.DEFAULT_RETRY_COUNT
        ): SemanticsNodeInteraction {
            var lastException: Exception? = null
            
            repeat(maxRetries) { attempt ->
                try {
                    return performClick()
                } catch (e: Exception) {
                    lastException = e
                    if (attempt < maxRetries - 1) {
                        runBlocking { delay(TestStrategyManager.DEFAULT_DELAY_MS) }
                    }
                }
            }
            
            throw lastException ?: Exception("Failed to perform click after $maxRetries attempts")
        }
        
        /**
         * Perform text input with validation
         */
        fun SemanticsNodeInteraction.performTextInputWithValidation(
            text: String,
            validateInput: Boolean = true
        ): SemanticsNodeInteraction {
            performTextInput(text)
            
            if (validateInput) {
                assertTextEquals(text)
            }
            
            return this
        }
        
        /**
         * Assert composable state
         */
        fun SemanticsNodeInteraction.assertState(
            isDisplayed: Boolean = true,
            isEnabled: Boolean = true
        ): SemanticsNodeInteraction {
            if (isDisplayed) {
                assertIsDisplayed()
            }
            
            if (isEnabled) {
                assertIsEnabled()
            } else {
                assertIsNotEnabled()
            }
            
            return this
        }
        
        /**
         * Create a test composable wrapper
         */
        fun createTestWrapper(
            content: @Composable () -> Unit
        ): @Composable () -> Unit = {
            // Add any common test setup here
            content()
        }
    }
}

/**
 * Test data builder for creating test objects
 */
class TestDataBuilder<T> {
    private val properties = mutableMapOf<String, Any?>()
    
    fun with(key: String, value: Any?): TestDataBuilder<T> {
        properties[key] = value
        return this
    }
    
    fun build(factory: (Map<String, Any?>) -> T): T {
        return factory(properties.toMap())
    }
    
    fun buildList(count: Int, factory: (Map<String, Any?>, Int) -> T): List<T> {
        return (0 until count).map { index ->
            factory(properties.toMap(), index)
        }
    }
}

/**
 * Test condition interface
 */
interface TestCondition {
    val description: String
    fun check(): Boolean
}

/**
 * Simple test condition implementation
 */
data class SimpleTestCondition(
    override val description: String,
    private val condition: () -> Boolean
) : TestCondition {
    override fun check(): Boolean = condition()
}

/**
 * Test result sealed class
 */
sealed class TestResult {
    data class Success(val attempts: Int) : TestResult()
    data class Failure(val exception: Exception, val attempts: Int) : TestResult()
}

/**
 * Test execution result
 */
sealed class TestExecutionResult<T> {
    data class Success<T>(val result: T, val executionTimeMs: Long) : TestExecutionResult<T>()
    data class Failure<T>(val exception: Exception, val executionTimeMs: Long) : TestExecutionResult<T>()
}

/**
 * Validation result
 */
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Failure(val failedConditions: List<TestCondition>) : ValidationResult()
}

/**
 * Test status enum
 */
enum class TestStatus {
    PASSED,
    FAILED,
    SKIPPED
}

/**
 * Test case result
 */
data class TestCaseResult(
    val testName: String,
    val status: TestStatus,
    val executionTimeMs: Long,
    val errorMessage: String? = null,
    val stackTrace: String? = null
)

/**
 * Test report
 */
data class TestReport(
    val totalTests: Int,
    val passedTests: Int,
    val failedTests: Int,
    val skippedTests: Int,
    val successRate: Double,
    val totalExecutionTimeMs: Long,
    val averageExecutionTimeMs: Long,
    val testResults: List<TestCaseResult>
) {
    fun printSummary(): String {
        return buildString {
            appendLine("=== Test Report Summary ===")
            appendLine("Total Tests: $totalTests")
            appendLine("Passed: $passedTests")
            appendLine("Failed: $failedTests")
            appendLine("Skipped: $skippedTests")
            appendLine("Success Rate: ${"%,.2f".format(successRate)}%")
            appendLine("Total Execution Time: ${totalExecutionTimeMs}ms")
            appendLine("Average Execution Time: ${averageExecutionTimeMs}ms")
            appendLine("=========================")
        }
    }
}

/**
 * Test annotations for categorizing tests
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UnitTest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IntegrationTest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UITest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PerformanceTest

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SecurityTest

/**
 * Test configuration
 */
data class TestConfig(
    val enableRetry: Boolean = true,
    val maxRetries: Int = TestStrategyManager.DEFAULT_RETRY_COUNT,
    val timeoutMs: Long = TestStrategyManager.DEFAULT_TIMEOUT_MS,
    val enableLogging: Boolean = true,
    val enableReporting: Boolean = true
)

/**
 * Mock data generators
 */
object MockDataGenerator {
    
    fun generateRandomString(length: Int = 10): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
    
    fun generateRandomInt(min: Int = 0, max: Int = 100): Int {
        return (min..max).random()
    }
    
    fun generateRandomBoolean(): Boolean {
        return listOf(true, false).random()
    }
    
    fun generateRandomEmail(): String {
        val username = generateRandomString(8)
        val domain = generateRandomString(5)
        return "$username@$domain.com"
    }
    
    fun generateRandomPhoneNumber(): String {
        return "+1${generateRandomInt(100, 999)}${generateRandomInt(100, 999)}${generateRandomInt(1000, 9999)}"
    }
}