package com.xiaomi.base.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Performance monitoring utility for tracking app performance metrics
 * Includes frame rate monitoring, memory usage tracking, and startup time measurement
 */
@Singleton
class PerformanceMonitor @Inject constructor() {
    
    private val performanceMetrics = ConcurrentHashMap<String, PerformanceMetric>()
    private val frameRateMonitor = FrameRateMonitor()
    private val memoryMonitor = MemoryMonitor()
    private var isMonitoring = false
    private var monitoringJob: Job? = null
    
    /**
     * Start performance monitoring
     */
    fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        monitoringJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive && isMonitoring) {
                collectMetrics()
                delay(1000) // Collect metrics every second
            }
        }
        
        frameRateMonitor.start()
        memoryMonitor.start()
        
        Timber.d("Performance monitoring started")
    }
    
    /**
     * Stop performance monitoring
     */
    fun stopMonitoring() {
        isMonitoring = false
        monitoringJob?.cancel()
        frameRateMonitor.stop()
        memoryMonitor.stop()
        
        Timber.d("Performance monitoring stopped")
    }
    
    /**
     * Record a performance event
     */
    fun recordEvent(eventName: String, duration: Long, metadata: Map<String, Any> = emptyMap()) {
        val metric = PerformanceMetric(
            name = eventName,
            duration = duration,
            timestamp = System.currentTimeMillis(),
            metadata = metadata
        )
        
        performanceMetrics["${eventName}_${System.currentTimeMillis()}"] = metric
        
        Timber.d("Performance event recorded: $eventName took ${duration}ms")
    }
    
    /**
     * Get current performance metrics
     */
    fun getMetrics(): Map<String, PerformanceMetric> = performanceMetrics.toMap()
    
    /**
     * Get current frame rate
     */
    fun getCurrentFrameRate(): Float = frameRateMonitor.getCurrentFrameRate()
    
    /**
     * Get current memory usage
     */
    fun getCurrentMemoryUsage(): MemoryUsage = memoryMonitor.getCurrentMemoryUsage()
    
    /**
     * Clear all collected metrics
     */
    fun clearMetrics() {
        performanceMetrics.clear()
        Timber.d("Performance metrics cleared")
    }
    
    private fun collectMetrics() {
        val currentTime = System.currentTimeMillis()
        
        // Record frame rate
        recordEvent(
            "frame_rate",
            0,
            mapOf(
                "fps" to frameRateMonitor.getCurrentFrameRate(),
                "timestamp" to currentTime
            )
        )
        
        // Record memory usage
        val memoryUsage = memoryMonitor.getCurrentMemoryUsage()
        recordEvent(
            "memory_usage",
            0,
            mapOf(
                "used_memory_mb" to memoryUsage.usedMemoryMB,
                "total_memory_mb" to memoryUsage.totalMemoryMB,
                "available_memory_mb" to memoryUsage.availableMemoryMB,
                "timestamp" to currentTime
            )
        )
    }
}

/**
 * Data class representing a performance metric
 */
data class PerformanceMetric(
    val name: String,
    val duration: Long,
    val timestamp: Long,
    val metadata: Map<String, Any> = emptyMap()
)

/**
 * Frame rate monitoring utility
 */
class FrameRateMonitor {
    private var frameCount = 0
    private var lastFrameTime = 0L
    private var currentFPS = 0f
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    
    private val frameCallback = object : Runnable {
        override fun run() {
            if (!isRunning) return
            
            val currentTime = System.currentTimeMillis()
            frameCount++
            
            if (lastFrameTime == 0L) {
                lastFrameTime = currentTime
            } else {
                val timeDiff = currentTime - lastFrameTime
                if (timeDiff >= 1000) { // Calculate FPS every second
                    currentFPS = (frameCount * 1000f) / timeDiff
                    frameCount = 0
                    lastFrameTime = currentTime
                }
            }
            
            handler.post(this)
        }
    }
    
    fun start() {
        if (isRunning) return
        isRunning = true
        handler.post(frameCallback)
    }
    
    fun stop() {
        isRunning = false
        handler.removeCallbacks(frameCallback)
    }
    
    fun getCurrentFrameRate(): Float = currentFPS
}

/**
 * Memory usage monitoring utility
 */
class MemoryMonitor {
    private val runtime = Runtime.getRuntime()
    private var isRunning = false
    
    fun start() {
        isRunning = true
    }
    
    fun stop() {
        isRunning = false
    }
    
    fun getCurrentMemoryUsage(): MemoryUsage {
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory
        val maxMemory = runtime.maxMemory()
        
        return MemoryUsage(
            usedMemoryMB = usedMemory / (1024 * 1024),
            totalMemoryMB = totalMemory / (1024 * 1024),
            availableMemoryMB = (maxMemory - usedMemory) / (1024 * 1024)
        )
    }
}

/**
 * Data class representing memory usage
 */
data class MemoryUsage(
    val usedMemoryMB: Long,
    val totalMemoryMB: Long,
    val availableMemoryMB: Long
)

/**
 * Composable function to track performance of a composable
 */
@Composable
fun TrackPerformance(
    eventName: String,
    performanceMonitor: PerformanceMonitor,
    content: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var startTime by remember { mutableStateOf(0L) }
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    startTime = System.currentTimeMillis()
                }
                Lifecycle.Event.ON_STOP -> {
                    if (startTime > 0) {
                        val duration = System.currentTimeMillis() - startTime
                        performanceMonitor.recordEvent(eventName, duration)
                    }
                }
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    content()
}

/**
 * Application lifecycle callbacks for performance monitoring
 */
class PerformanceLifecycleCallbacks(
    private val performanceMonitor: PerformanceMonitor
) : Application.ActivityLifecycleCallbacks {
    
    private val activityStartTimes = mutableMapOf<String, Long>()
    
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityName = activity::class.java.simpleName
        activityStartTimes[activityName] = System.currentTimeMillis()
    }
    
    override fun onActivityStarted(activity: Activity) {
        // Activity is becoming visible
    }
    
    override fun onActivityResumed(activity: Activity) {
        val activityName = activity::class.java.simpleName
        val startTime = activityStartTimes[activityName]
        if (startTime != null) {
            val duration = System.currentTimeMillis() - startTime
            performanceMonitor.recordEvent(
                "activity_startup_$activityName",
                duration,
                mapOf("activity" to activityName)
            )
            activityStartTimes.remove(activityName)
        }
    }
    
    override fun onActivityPaused(activity: Activity) {
        // Activity is losing focus
    }
    
    override fun onActivityStopped(activity: Activity) {
        // Activity is no longer visible
    }
    
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Activity is saving state
    }
    
    override fun onActivityDestroyed(activity: Activity) {
        val activityName = activity::class.java.simpleName
        activityStartTimes.remove(activityName)
    }
}

/**
 * Extension function to measure execution time of a block
 */
inline fun <T> PerformanceMonitor.measureTime(
    eventName: String,
    metadata: Map<String, Any> = emptyMap(),
    block: () -> T
): T {
    val startTime = System.currentTimeMillis()
    return try {
        block()
    } finally {
        val duration = System.currentTimeMillis() - startTime
        recordEvent(eventName, duration, metadata)
    }
}