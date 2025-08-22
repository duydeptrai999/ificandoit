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
        addFilter(
            FilterConfig(
                type = FilterType.ORIGINAL,
                name = "Original",
                shader = OriginalFilterShader(),
            ),
        )

        // RETRO FILTERS - PRIORITY DISPLAY AT TOP
        // Retro 70s filter - Heavy grain, warm sepia, scratches
        addFilter(
            FilterConfig(
                type = FilterType.RETRO_70S,
                name = "Retro 70s",
                shader = Retro70sFilterShader(),
            ),
        )

        // Retro 80s Vintage filter - VHS artifacts, color bleeding, scan lines
        addFilter(
            FilterConfig(
                type = FilterType.RETRO_80S_VINTAGE,
                name = "Retro 80s VHS",
                shader = Retro80sVintageFilterShader(),
            ),
        )

        // Retro 90s filter - Digital artifacts, pixelation, early digital camera look
        addFilter(
            FilterConfig(
                type = FilterType.RETRO_90S,
                name = "Retro 90s",
                shader = Retro90sFilterShader(),
            ),
        )

        // Vintage Camera filter - Extreme aging, heavy scratches, dust, light leaks
        addFilter(
            FilterConfig(
                type = FilterType.VINTAGE_CAMERA,
                name = "Vintage Camera",
                shader = VintageCameraFilterShader(),
            ),
        )

        // Sepia filter
        addFilter(
            FilterConfig(
                type = FilterType.SEPIA,
                name = "Sepia",
                shader = SepiaFilterShader(),
            ),
        )

        // Black & White filter
        addFilter(
            FilterConfig(
                type = FilterType.BLACK_WHITE,
                name = "B&W",
                shader = BlackWhiteFilterShader(),
            ),
        )

        // Vintage filter
        addFilter(
            FilterConfig(
                type = FilterType.VINTAGE,
                name = "Vintage",
                shader = VintageFilterShader(),
            ),
        )

        // Cool filter
        addFilter(
            FilterConfig(
                type = FilterType.COOL,
                name = "Cool",
                shader = CoolFilterShader(),
            ),
        )

        // Warm filter
        addFilter(
            FilterConfig(
                type = FilterType.WARM,
                name = "Warm",
                shader = WarmFilterShader(),
            ),
        )

        // Pink Dream filter
        addFilter(
            FilterConfig(
                type = FilterType.PINK_DREAM,
                name = "Pink Dream",
                shader = PinkDreamFilterShader(),
            ),
        )

        // Retro 80s filter
        addFilter(
            FilterConfig(
                type = FilterType.RETRO_80S,
                name = "Retro 80s",
                shader = Retro80sFilterShader(),
            ),
        )

        // Old Film filter
        addFilter(
            FilterConfig(
                type = FilterType.OLD_FILM,
                name = "Old Film",
                shader = OldFilmFilterShader(),
            ),
        )

        // Spring filter
        addFilter(
            FilterConfig(
                type = FilterType.SPRING,
                name = "Spring",
                shader = SpringFilterShader(),
            ),
        )

        // Summer filter
        addFilter(
            FilterConfig(
                type = FilterType.SUMMER,
                name = "Summer",
                shader = SummerFilterShader(),
            ),
        )

        // Autumn filter
        addFilter(
            FilterConfig(
                type = FilterType.AUTUMN,
                name = "Autumn",
                shader = AutumnFilterShader(),
            ),
        )

        // Winter filter
        addFilter(
            FilterConfig(
                type = FilterType.WINTER,
                name = "Winter",
                shader = WinterFilterShader(),
            ),
        )

        // Neon Nights filter
        addFilter(
            FilterConfig(
                type = FilterType.NEON_NIGHTS,
                name = "Neon Nights",
                shader = NeonNightsFilterShader(),
            ),
        )

        // Golden Hour filter
        addFilter(
            FilterConfig(
                type = FilterType.GOLDEN_HOUR,
                name = "Golden Hour",
                shader = GoldenHourFilterShader(),
            ),
        )

        // Cyberpunk filter
        addFilter(
            FilterConfig(
                type = FilterType.CYBERPUNK,
                name = "Cyberpunk",
                shader = CyberpunkFilterShader(),
            ),
        )

        // Cherry Blossom filter
        addFilter(
            FilterConfig(
                type = FilterType.CHERRY_BLOSSOM,
                name = "Cherry Blossom",
                shader = CherryBlossomFilterShader(),
            ),
        )

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
     * Get filter configurations with retro filters prioritized at the top
     */
    fun getFilterConfigsWithRetroFirst(): List<FilterConfig> {
        val retro70s = filterConfigs[FilterType.RETRO_70S]
        val retro80sVintage = filterConfigs[FilterType.RETRO_80S_VINTAGE]
        val retro90s = filterConfigs[FilterType.RETRO_90S]
        val vintageCamera = filterConfigs[FilterType.VINTAGE_CAMERA]
        val original = filterConfigs[FilterType.ORIGINAL]
        
        val priorityFilters = listOfNotNull(original, retro70s, retro80sVintage, retro90s, vintageCamera)
        val otherFilters = filterConfigs.values.filter { config ->
            config.type !in setOf(
                FilterType.ORIGINAL,
                FilterType.RETRO_70S,
                FilterType.RETRO_80S_VINTAGE, 
                FilterType.RETRO_90S,
                FilterType.VINTAGE_CAMERA
            )
        }.sortedBy { it.type.ordinal }
        
        return priorityFilters + otherFilters
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
        val defaultTypes =
            setOf(
                FilterType.ORIGINAL,
                FilterType.RETRO_70S,
                FilterType.RETRO_80S_VINTAGE,
                FilterType.RETRO_90S,
                FilterType.VINTAGE_CAMERA,
                FilterType.SEPIA,
                FilterType.BLACK_WHITE,
                FilterType.VINTAGE,
                FilterType.COOL,
                FilterType.WARM,
                FilterType.PINK_DREAM,
                FilterType.RETRO_80S,
                FilterType.OLD_FILM,
                FilterType.SPRING,
                FilterType.SUMMER,
                FilterType.AUTUMN,
                FilterType.WINTER,
                FilterType.NEON_NIGHTS,
                FilterType.GOLDEN_HOUR,
                FilterType.CYBERPUNK,
                FilterType.CHERRY_BLOSSOM,
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
    val isCustom: Boolean = false,
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

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uIntensity" to 0.8f,
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

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uVignetteStrength" to 1.2f,
            "uTintColor" to floatArrayOf(1.1f, 0.9f, 0.7f),
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

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uCoolIntensity" to 0.6f,
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

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uWarmIntensity" to 0.5f,
        )

    override fun getFilterType(): FilterType = FilterType.WARM
}

/**
 * Pink Dream filter shader - Romantic dreamy pink tones
 */
class PinkDreamFilterShader : FilterShader {
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
uniform float uPinkIntensity;
uniform vec3 uPinkTint;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Enhance pink and magenta tones
    color.r = min(color.r * (1.0 + uPinkIntensity * 0.3), 1.0);
    color.g = min(color.g * (1.0 - uPinkIntensity * 0.1), 1.0);
    color.b = min(color.b * (1.0 + uPinkIntensity * 0.2), 1.0);

    // Add romantic pink overlay
    color.rgb = mix(color.rgb, color.rgb * uPinkTint, 0.4);

    // Soft dreamy glow effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float glow = 1.0 - smoothstep(0.0, 0.7, dist);
    color.rgb += vec3(0.08, 0.03, 0.05) * glow * uPinkIntensity;

    // Increase overall brightness for dreamy effect
    color.rgb *= (1.0 + uPinkIntensity * 0.1);

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uPinkIntensity" to 0.8f,
            "uPinkTint" to floatArrayOf(1.2f, 0.9f, 1.1f),
        )

    override fun getFilterType(): FilterType = FilterType.PINK_DREAM
}

/**
 * Retro 80s filter shader - Neon synthwave aesthetic
 */
class Retro80sFilterShader : FilterShader {
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
uniform float uNeonIntensity;
uniform vec3 uMagentaTint;
uniform vec3 uCyanTint;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // High contrast gamma adjustment
    color.rgb = pow(color.rgb, vec3(0.75));

    // Extreme saturation boost for neon effect
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 1.8 * uNeonIntensity);

    // Split toning: shadows to magenta, highlights to cyan
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    if (luminance < 0.5) {
        // Shadows - add magenta
        color.rgb = mix(color.rgb, color.rgb * uMagentaTint, 0.6 * uNeonIntensity);
    } else {
        // Highlights - add cyan
        color.rgb = mix(color.rgb, color.rgb * uCyanTint, 0.4 * uNeonIntensity);
    }

    // Add neon glow effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float glow = 1.0 - smoothstep(0.2, 0.8, dist);
    color.rgb += vec3(0.15, 0.0, 0.15) * glow * uNeonIntensity;

    // Increase overall brightness
    color.rgb *= (1.0 + uNeonIntensity * 0.3);

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uNeonIntensity" to 0.9f,
            "uMagentaTint" to floatArrayOf(1.4f, 0.6f, 1.2f),
            "uCyanTint" to floatArrayOf(0.7f, 1.2f, 1.4f),
        )

    override fun getFilterType(): FilterType = FilterType.RETRO_80S
}

/**
 * Old Film filter shader - Vintage cinema with natural sepia and subtle grain
 */
class OldFilmFilterShader : FilterShader {
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
uniform float uVintageIntensity;
uniform vec3 uSepiaWarm;
uniform float uFilmGrain;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Slight desaturation for vintage look (not too much)
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 0.85);

    // Apply warm sepia tone (classic old film look)
    mat3 sepiaMatrix = mat3(
        0.393, 0.769, 0.189,
        0.349, 0.686, 0.168,
        0.272, 0.534, 0.131
    );
    vec3 sepiaColor = sepiaMatrix * color.rgb;

    // Mix with warm sepia tint
    sepiaColor *= uSepiaWarm;
    color.rgb = mix(color.rgb, sepiaColor, 0.4 * uVintageIntensity);

    // Add subtle film grain (not too strong)
    float grain = fract(sin(dot(vTextureCoord * 800.0, vec2(12.9898, 78.233))) * 43758.5453);
    color.rgb += (grain - 0.5) * uFilmGrain;

    // Maintain good brightness (don't make it too dark)
    color.rgb *= 0.95;

    // Gentle vignette (not too strong)
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float vignette = 1.0 - smoothstep(0.5, 1.2, dist);
    color.rgb *= mix(0.75, 1.0, vignette);

    // Add warm vintage tint
    color.rgb += vec3(0.03, 0.02, 0.01) * uVintageIntensity;

    // Slight contrast boost for classic film look
    color.rgb = pow(color.rgb, vec3(1.05));

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uVintageIntensity" to 0.6f,
            "uSepiaWarm" to floatArrayOf(1.1f, 0.95f, 0.8f),
            "uFilmGrain" to 0.04f,
        )

    override fun getFilterType(): FilterType = FilterType.OLD_FILM
}

/**
 * Spring filter shader - Fresh green and floral tones
 */
class SpringFilterShader : FilterShader {
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
uniform float uSpringVibrancy;
uniform vec3 uGreenTint;
uniform vec3 uFloralTint;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Brighten the image for fresh spring look
    color.rgb = pow(color.rgb, vec3(0.9));

    // Enhance green tones for foliage
    float greenAmount = max(color.g - max(color.r, color.b), 0.0);
    color.rgb = mix(color.rgb, color.rgb * uGreenTint, greenAmount * uSpringVibrancy);

    // Add fresh spring tint
    color.rgb = mix(color.rgb, color.rgb * uFloralTint, 0.3 * uSpringVibrancy);

    // Soft bloom effect for dreamy spring atmosphere
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float bloom = 1.0 - smoothstep(0.0, 0.6, dist);
    color.rgb += vec3(0.05, 0.08, 0.04) * bloom * uSpringVibrancy;

    // Increase overall brightness
    color.rgb *= (1.0 + uSpringVibrancy * 0.15);

    // Add subtle color variation for natural look
    float variation = sin(vTextureCoord.x * 10.0) * sin(vTextureCoord.y * 10.0);
    color.rgb += vec3(0.02, 0.03, 0.01) * variation * uSpringVibrancy;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uSpringVibrancy" to 0.8f,
            "uGreenTint" to floatArrayOf(0.9f, 1.2f, 0.95f),
            "uFloralTint" to floatArrayOf(1.05f, 1.1f, 0.98f),
        )

    override fun getFilterType(): FilterType = FilterType.SPRING
}

/**
 * Summer filter shader - Bright sunny atmosphere
 */
class SummerFilterShader : FilterShader {
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
uniform float uSunIntensity;
uniform vec3 uGoldenTint;
uniform vec2 uSunPosition;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Bright summer gamma adjustment
    color.rgb = pow(color.rgb, vec3(0.85));

    // Boost saturation for vibrant summer colors
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 1.3 * uSunIntensity);

    // Add warm golden summer tint
    color.rgb = mix(color.rgb, color.rgb * uGoldenTint, 0.4 * uSunIntensity);

    // Sun flare effect
    float sunDist = distance(vTextureCoord, uSunPosition);
    float sunFlare = 1.0 - smoothstep(0.0, 0.4, sunDist);
    color.rgb += vec3(0.25, 0.2, 0.08) * sunFlare * uSunIntensity;

    // Increase overall brightness for sunny day
    color.rgb *= (1.0 + uSunIntensity * 0.25);

    // Add heat shimmer effect
    float shimmer = sin(vTextureCoord.y * 50.0 + vTextureCoord.x * 30.0);
    color.rgb += vec3(0.02, 0.015, 0.005) * shimmer * uSunIntensity;

    // Enhance warm colors (reds, oranges, yellows)
    float warmth = max(max(color.r, color.g) - color.b, 0.0);
    color.rgb += vec3(0.05, 0.03, 0.0) * warmth * uSunIntensity;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uSunIntensity" to 0.9f,
            "uGoldenTint" to floatArrayOf(1.15f, 1.08f, 0.9f),
            "uSunPosition" to floatArrayOf(0.7f, 0.3f),
        )

    override fun getFilterType(): FilterType = FilterType.SUMMER
}

/**
 * Autumn filter shader - Warm orange and red foliage tones
 */
class AutumnFilterShader : FilterShader {
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
uniform float uAutumnIntensity;
uniform vec3 uOrangeTint;
uniform vec3 uRedTint;
uniform vec3 uGoldenTint;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Enhance warm autumn colors
    float warmth = max(max(color.r, color.g) - color.b, 0.0);

    // Apply different tints based on color temperature
    if (color.r > color.g && color.r > color.b) {
        // Red dominant areas - enhance with red tint
        color.rgb = mix(color.rgb, color.rgb * uRedTint, warmth * uAutumnIntensity);
    } else if (color.g > color.b) {
        // Green/yellow areas - turn to orange/gold
        color.rgb = mix(color.rgb, color.rgb * uOrangeTint, warmth * uAutumnIntensity);
    }

    // Add overall golden autumn overlay
    color.rgb = mix(color.rgb, color.rgb * uGoldenTint, 0.3 * uAutumnIntensity);

    // Slightly reduce brightness for cozy autumn feel
    color.rgb *= (1.0 - uAutumnIntensity * 0.1);

    // Add warm atmospheric haze
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float haze = 1.0 - smoothstep(0.3, 0.9, dist);
    color.rgb += vec3(0.08, 0.04, 0.02) * haze * uAutumnIntensity;

    // Enhance contrast for rich autumn colors
    color.rgb = pow(color.rgb, vec3(1.1));

    // Add subtle texture for natural autumn look
    float texture = sin(vTextureCoord.x * 15.0) * cos(vTextureCoord.y * 15.0);
    color.rgb += vec3(0.02, 0.01, 0.005) * texture * uAutumnIntensity;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uAutumnIntensity" to 0.85f,
            "uOrangeTint" to floatArrayOf(1.3f, 0.9f, 0.6f),
            "uRedTint" to floatArrayOf(1.2f, 0.7f, 0.5f),
            "uGoldenTint" to floatArrayOf(1.1f, 0.95f, 0.7f),
        )

    override fun getFilterType(): FilterType = FilterType.AUTUMN
}

/**
 * Winter filter shader - Cool blue and white icy tones
 */
class WinterFilterShader : FilterShader {
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
uniform float uWinterChill;
uniform vec3 uIceTint;
uniform vec3 uSnowTint;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Desaturate for cold winter feel
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 0.7 * (1.0 - uWinterChill));

    // Add cool blue tint to shadows and mid-tones
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    if (luminance < 0.7) {
        color.rgb = mix(color.rgb, color.rgb * uIceTint, 0.5 * uWinterChill);
    } else {
        // Add snow-like highlights
        color.rgb = mix(color.rgb, color.rgb * uSnowTint, 0.3 * uWinterChill);
    }

    // Increase brightness for snow reflection
    color.rgb *= (1.0 + uWinterChill * 0.2);

    // Add icy highlights effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float ice = 1.0 - smoothstep(0.3, 0.8, dist);
    color.rgb += vec3(0.06, 0.08, 0.12) * ice * uWinterChill;

    // Add crystalline texture effect
    float crystal = sin(vTextureCoord.x * 25.0) * cos(vTextureCoord.y * 25.0);
    color.rgb += vec3(0.02, 0.025, 0.03) * crystal * uWinterChill;

    // Enhance cool colors (blues, cyans)
    float coolness = max(color.b - max(color.r, color.g), 0.0);
    color.rgb += vec3(0.0, 0.02, 0.05) * coolness * uWinterChill;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uWinterChill" to 0.8f,
            "uIceTint" to floatArrayOf(0.85f, 0.95f, 1.25f),
            "uSnowTint" to floatArrayOf(1.05f, 1.08f, 1.15f),
        )

    override fun getFilterType(): FilterType = FilterType.WINTER
}

/**
 * Neon Nights filter shader - Cyberpunk neon glow effect
 */
class NeonNightsFilterShader : FilterShader {
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
uniform float uNeonGlow;
uniform vec3 uPinkNeon;
uniform vec3 uCyanNeon;
uniform vec3 uPurpleNeon;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // High contrast for neon effect
    color.rgb = pow(color.rgb, vec3(1.3));

    // Extreme saturation boost
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 2.2 * uNeonGlow);

    // Apply neon color mapping based on luminance
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));

    if (luminance < 0.2) {
        // Dark areas - deep purple/black
        color.rgb *= 0.3;
        color.rgb = mix(color.rgb, color.rgb * uPurpleNeon, 0.7 * uNeonGlow);
    } else if (luminance < 0.6) {
        // Mid-tones - pink/magenta neon
        color.rgb = mix(color.rgb, color.rgb * uPinkNeon, 0.8 * uNeonGlow);
    } else {
        // Highlights - cyan/electric blue
        color.rgb = mix(color.rgb, color.rgb * uCyanNeon, 0.9 * uNeonGlow);
    }

    // Add neon glow pattern
    float glowPattern = sin(vTextureCoord.x * 20.0) * sin(vTextureCoord.y * 20.0);
    color.rgb += vec3(0.1, 0.05, 0.15) * abs(glowPattern) * uNeonGlow;

    // Add electric energy lines
    float energy = sin(vTextureCoord.x * 100.0 + vTextureCoord.y * 50.0);
    color.rgb += vec3(0.05, 0.0, 0.1) * energy * uNeonGlow;

    // Boost overall brightness for neon effect
    color.rgb *= (1.0 + uNeonGlow * 0.4);

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uNeonGlow" to 0.9f,
            "uPinkNeon" to floatArrayOf(1.5f, 0.3f, 1.2f),
            "uCyanNeon" to floatArrayOf(0.2f, 1.3f, 1.5f),
            "uPurpleNeon" to floatArrayOf(0.8f, 0.2f, 1.4f),
        )

    override fun getFilterType(): FilterType = FilterType.NEON_NIGHTS
}

/**
 * Golden Hour filter shader - Warm sunset lighting
 */
class GoldenHourFilterShader : FilterShader {
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
uniform float uGoldenIntensity;
uniform vec3 uGoldenTint;
uniform vec2 uLightDirection;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Warm gamma adjustment for golden hour
    color.rgb = pow(color.rgb, vec3(0.8));

    // Boost saturation for rich golden colors
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 1.2 * uGoldenIntensity);

    // Add golden overlay
    color.rgb = mix(color.rgb, color.rgb * uGoldenTint, 0.4 * uGoldenIntensity);

    // Simulate directional golden light
    vec2 lightVector = normalize(vTextureCoord - uLightDirection);
    float lightAngle = dot(lightVector, vec2(1.0, 0.0));
    float lighting = max(0.0, lightAngle) * 0.4 + 0.6;
    color.rgb *= lighting;

    // Add warm rim lighting effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float rimLight = 1.0 - smoothstep(0.4, 0.9, dist);
    color.rgb += vec3(0.15, 0.1, 0.05) * rimLight * uGoldenIntensity;

    // Enhance warm tones (reds, oranges, yellows)
    float warmth = max(max(color.r, color.g) - color.b, 0.0);
    color.rgb += vec3(0.08, 0.05, 0.02) * warmth * uGoldenIntensity;

    // Add subtle lens flare effect
    float flare = sin(vTextureCoord.x * 8.0) * cos(vTextureCoord.y * 6.0);
    color.rgb += vec3(0.03, 0.02, 0.01) * abs(flare) * uGoldenIntensity;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uGoldenIntensity" to 0.85f,
            "uGoldenTint" to floatArrayOf(1.2f, 0.95f, 0.6f),
            "uLightDirection" to floatArrayOf(0.2f, 0.8f),
        )

    override fun getFilterType(): FilterType = FilterType.GOLDEN_HOUR
}

/**
 * Cyberpunk filter shader - Futuristic tech aesthetic
 */
class CyberpunkFilterShader : FilterShader {
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
uniform float uCyberIntensity;
uniform vec3 uCyanGlow;
uniform vec3 uMagentaGlow;
uniform float uGlitchAmount;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // High contrast cyberpunk look
    color.rgb = pow(color.rgb, vec3(1.4));

    // Extreme saturation for digital look
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 2.0 * uCyberIntensity);

    // Split color channels for cyberpunk aesthetic
    float luminance = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    if (luminance < 0.4) {
        // Dark areas - magenta/purple
        color.rgb = mix(color.rgb, color.rgb * uMagentaGlow, 0.8 * uCyberIntensity);
    } else {
        // Bright areas - cyan/electric blue
        color.rgb = mix(color.rgb, color.rgb * uCyanGlow, 0.7 * uCyberIntensity);
    }

    // Digital glitch effect
    float glitch = step(0.98, fract(sin(dot(vTextureCoord * 100.0, vec2(12.9898, 78.233))) * 43758.5453));
    color.rgb = mix(color.rgb, vec3(0.0, 1.0, 1.0), glitch * uGlitchAmount);

    // Scanline effect for CRT monitor look
    float scanline = sin(vTextureCoord.y * 600.0) * 0.15 + 0.85;
    color.rgb *= scanline;

    // Add chromatic aberration
    vec2 offset = vec2(0.003, 0.0) * uCyberIntensity;
    float r = texture(uTexture, vTextureCoord + offset).r;
    float b = texture(uTexture, vTextureCoord - offset).b;
    color.r = mix(color.r, r, 0.5);
    color.b = mix(color.b, b, 0.5);

    // Add digital noise
    float noise = fract(sin(dot(vTextureCoord * 500.0, vec2(12.9898, 78.233))) * 43758.5453);
    color.rgb += (noise - 0.5) * 0.05 * uCyberIntensity;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uCyberIntensity" to 0.9f,
            "uCyanGlow" to floatArrayOf(0.3f, 1.4f, 1.6f),
            "uMagentaGlow" to floatArrayOf(1.5f, 0.2f, 1.3f),
            "uGlitchAmount" to 0.3f,
        )

    override fun getFilterType(): FilterType = FilterType.CYBERPUNK
}

/**
 * Cherry Blossom filter shader - Soft pink and white sakura tones
 */
class CherryBlossomFilterShader : FilterShader {
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
uniform float uSakuraIntensity;
uniform vec3 uPinkBloom;
uniform vec3 uWhitePetal;
uniform vec3 uSoftGlow;

out vec4 fragColor;

void main() {
    vec4 color = texture(uTexture, vTextureCoord);

    // Soften the image for dreamy sakura effect
    color.rgb = pow(color.rgb, vec3(0.92));

    // Enhance pink tones naturally
    float pinkAmount = max(color.r - max(color.g, color.b) * 0.8, 0.0);
    color.rgb = mix(color.rgb, color.rgb * uPinkBloom, pinkAmount * uSakuraIntensity);

    // Add soft cherry blossom overlay
    color.rgb = mix(color.rgb, color.rgb * uWhitePetal, 0.25 * uSakuraIntensity);

    // Dreamy center glow like sunlight through petals
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(vTextureCoord, center);
    float glow = 1.0 - smoothstep(0.0, 0.8, dist);
    color.rgb += uSoftGlow * glow * uSakuraIntensity;

    // Increase brightness for ethereal effect
    color.rgb *= (1.0 + uSakuraIntensity * 0.12);

    // Subtle pink vignette
    float vignette = 1.0 - smoothstep(0.4, 1.0, dist);
    color.rgb = mix(color.rgb * 0.92, color.rgb, vignette);
    color.rgb += vec3(0.03, 0.015, 0.02) * (1.0 - vignette) * uSakuraIntensity;

    // Add petal-like texture effect
    float petals = sin(vTextureCoord.x * 25.0) * sin(vTextureCoord.y * 25.0);
    color.rgb += vec3(0.015, 0.008, 0.012) * abs(petals) * uSakuraIntensity;

    // Soft focus effect for romantic atmosphere
    float softness = 1.0 - dist * 0.3;
    color.rgb *= softness;

    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uSakuraIntensity" to 0.8f,
            "uPinkBloom" to floatArrayOf(1.15f, 0.95f, 1.08f),
            "uWhitePetal" to floatArrayOf(1.05f, 0.98f, 1.02f),
            "uSoftGlow" to floatArrayOf(0.06f, 0.03f, 0.04f),
        )

    override fun getFilterType(): FilterType = FilterType.CHERRY_BLOSSOM
}
