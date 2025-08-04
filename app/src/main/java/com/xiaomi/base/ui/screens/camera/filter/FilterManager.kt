package com.xiaomi.base.ui.screens.camera.filter

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * Manages camera filters with efficient caching and performance optimization
 */
class FilterManager {
    companion object {
        private const val TAG = "FilterManager"
    }
    
    private val filterConfigs = ConcurrentHashMap<FilterType, FilterConfig>()
    private val shaderCache = ConcurrentHashMap<FilterType, FilterShader>()
    
    init {
        initializeDefaultFilters()
    }
    
    /**
     * Initialize default filters
     */
    private fun initializeDefaultFilters() {
        // Original (no filter)
        addFilter(FilterConfig(
            type = FilterType.ORIGINAL,
            name = "Original",
            shader = OriginalFilterShader()
        ))
        
        // Sepia filter
        addFilter(FilterConfig(
            type = FilterType.SEPIA,
            name = "Sepia",
            shader = SepiaFilterShader()
        ))
        
        // Black & White filter
        addFilter(FilterConfig(
            type = FilterType.BLACK_WHITE,
            name = "B&W",
            shader = BlackWhiteFilterShader()
        ))
        
        // Vintage filter
        addFilter(FilterConfig(
            type = FilterType.VINTAGE,
            name = "Vintage",
            shader = VintageFilterShader()
        ))
        
        // Cool filter
        addFilter(FilterConfig(
            type = FilterType.COOL,
            name = "Cool",
            shader = CoolFilterShader()
        ))
        
        // Warm filter
        addFilter(FilterConfig(
            type = FilterType.WARM,
            name = "Warm",
            shader = WarmFilterShader()
        ))
        
        Log.d(TAG, "Default filters initialized: ${filterConfigs.size} filters")
    }
    
    /**
     * Add filter configuration
     */
    fun addFilter(config: FilterConfig) {
        filterConfigs[config.type] = config
        // Cache shader for performance
        shaderCache[config.type] = config.shader
        Log.d(TAG, "Filter added: ${config.name} (${config.type})")
    }
    
    /**
     * Add custom filter (alias for addFilter)
     */
    fun addCustomFilter(config: FilterConfig) {
        addFilter(config)
    }
    
    /**
     * Get filter configuration
     */
    fun getFilter(type: FilterType): FilterConfig? {
        return filterConfigs[type]
    }
    
    /**
     * Get filter shader (cached)
     */
    fun getFilterShader(type: FilterType): FilterShader? {
        return shaderCache[type]
    }
    
    /**
     * Get all available filter types
     */
    fun getAvailableFilters(): List<FilterType> {
        return filterConfigs.keys.toList().sortedBy { it.ordinal }
    }
    
    /**
     * Get filter configurations for UI
     */
    fun getFilterConfigs(): List<FilterConfig> {
        return filterConfigs.values.toList().sortedBy { it.type.ordinal }
    }
    
    /**
     * Remove filter
     */
    fun removeFilter(type: FilterType) {
        filterConfigs.remove(type)
        shaderCache.remove(type)
        Log.d(TAG, "Filter removed: $type")
    }
    
    /**
     * Clear all custom filters (keep defaults)
     */
    fun clearCustomFilters() {
        val defaultTypes = setOf(
            FilterType.ORIGINAL,
            FilterType.SEPIA,
            FilterType.BLACK_WHITE,
            FilterType.VINTAGE,
            FilterType.COOL,
            FilterType.WARM
        )
        
        val toRemove = filterConfigs.keys.filter { it !in defaultTypes }
        toRemove.forEach { type ->
            filterConfigs.remove(type)
            shaderCache.remove(type)
        }
        
        Log.d(TAG, "Custom filters cleared. Removed: ${toRemove.size} filters")
    }
    
    /**
     * Check if filter exists
     */
    fun hasFilter(type: FilterType): Boolean {
        return filterConfigs.containsKey(type)
    }
    
    /**
     * Get filter count
     */
    fun getFilterCount(): Int {
        return filterConfigs.size
    }
}

/**
 * Filter configuration data class
 */
data class FilterConfig(
    val type: FilterType,
    val name: String,
    val shader: FilterShader,
    val description: String = "",
    val intensity: Float = 1.0f,
    val isCustom: Boolean = false
)

/**
 * Original filter shader (pass-through)
 */
class OriginalFilterShader : FilterShader {
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;

out vec4 fragColor;

void main() {
    fragColor = texture(uTexture, vTextureCoord);
}
"""
    
    override fun getUniformValues(): Map<String, Any> = emptyMap()
    
    override fun getFilterType(): FilterType = FilterType.ORIGINAL
}

/**
 * Sepia filter shader
 */
class SepiaFilterShader : FilterShader {
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;
uniform float uIntensity;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);
    
    // Apply sepia effect
    float r = color.r * 0.393 + color.g * 0.769 + color.b * 0.189;
    float g = color.r * 0.349 + color.g * 0.686 + color.b * 0.168;
    float b = color.r * 0.272 + color.g * 0.534 + color.b * 0.131;
    
    vec3 sepia = vec3(r, g, b);
    fragColor = vec4(mix(color.rgb, sepia, uIntensity), color.a);
}
"""
    
    override fun getUniformValues(): Map<String, Any> = mapOf(
        "uIntensity" to 0.8f
    )
    
    override fun getFilterType(): FilterType = FilterType.SEPIA
}

/**
 * Black & White filter shader
 */
class BlackWhiteFilterShader : FilterShader {
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);
    
    // Luminance calculation (ITU-R BT.709)
    float gray = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722));
    
    fragColor = vec4(gray, gray, gray, color.a);
}
"""
    
    override fun getUniformValues(): Map<String, Any> = emptyMap()
    
    override fun getFilterType(): FilterType = FilterType.BLACK_WHITE
}

/**
 * Vintage filter shader
 */
class VintageFilterShader : FilterShader {
    override fun getFilterType(): FilterType = FilterType.VINTAGE
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;
uniform float uVignetteStrength;
uniform vec3 uTintColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);
    
    // Apply vintage tint
    color.rgb = mix(color.rgb, color.rgb * uTintColor, 0.3);
    
    // Add vignette effect
    vec2 center = vec2(0.5, 0.5);
    float distance = length(vTextureCoord - center);
    float vignette = 1.0 - smoothstep(0.3, 0.8, distance * uVignetteStrength);
    
    color.rgb *= vignette;
    
    // Reduce saturation slightly
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 0.8);
    
    fragColor = color;
}
"""
    
    override fun getUniformValues(): Map<String, Any> = mapOf(
        "uVignetteStrength" to 1.2f,
        "uTintColor" to floatArrayOf(1.1f, 0.9f, 0.7f)
    )
}

/**
 * Cool filter shader
 */
class CoolFilterShader : FilterShader {
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;
uniform float uCoolIntensity;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);
    
    // Cool tone adjustment (increase blue, decrease red)
    color.r *= (1.0 - uCoolIntensity * 0.2);
    color.b *= (1.0 + uCoolIntensity * 0.3);
    
    fragColor = color;
}
"""
    
    override fun getUniformValues(): Map<String, Any> = mapOf(
        "uCoolIntensity" to 0.6f
    )
    
    override fun getFilterType(): FilterType = FilterType.COOL
}

/**
 * Warm filter shader
 */
class WarmFilterShader : FilterShader {
    override val vertexShader: String = """#version 300 es
layout (location = 0) in vec4 aPosition;
layout (location = 1) in vec2 aTextureCoord;

uniform mat4 uMVPMatrix;
uniform mat4 uTexMatrix;

out vec2 vTextureCoord;

void main() {
    gl_Position = uMVPMatrix * aPosition;
    vTextureCoord = (uTexMatrix * vec4(aTextureCoord, 0.0, 1.0)).xy;
}
"""
    
    override val fragmentShader: String = """#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
precision mediump float;

in vec2 vTextureCoord;
uniform samplerExternalOES uTexture;
uniform float uWarmIntensity;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);
    
    // Warm tone adjustment (increase red/yellow, decrease blue)
    color.r *= (1.0 + uWarmIntensity * 0.3);
    color.g *= (1.0 + uWarmIntensity * 0.1);
    color.b *= (1.0 - uWarmIntensity * 0.2);
    
    fragColor = color;
}
"""
    
    override fun getUniformValues(): Map<String, Any> = mapOf(
        "uWarmIntensity" to 0.5f
    )
    
    override fun getFilterType(): FilterType = FilterType.WARM
}