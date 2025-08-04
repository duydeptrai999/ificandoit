package com.xiaomi.base.ui.screens.camera.filter

/**
 * Interface for camera filter shaders
 * Defines the contract for implementing custom OpenGL ES filters
 */
interface FilterShader {
    /**
     * Vertex shader source code
     */
    val vertexShader: String
    
    /**
     * Fragment shader source code
     */
    val fragmentShader: String
    
    /**
     * Get uniform values to be passed to the shader
     * @return Map of uniform name to value
     */
    fun getUniformValues(): Map<String, Any>
    
    /**
     * Get the filter type
     */
    fun getFilterType(): FilterType
}

/**
 * Enum representing different filter types
 */
enum class FilterType {
    ORIGINAL,
    SEPIA,
    BLACK_WHITE,
    VINTAGE,
    COOL,
    WARM
}

/**
 * Base implementation of FilterShader with common functionality
 */
abstract class BaseFilterShader : FilterShader {
    
    companion object {
        /**
         * Default vertex shader for camera filters
         */
        const val DEFAULT_VERTEX_SHADER = """
            #version 300 es
            layout(location = 0) in vec4 a_position;
            layout(location = 1) in vec2 a_texCoord;
            
            uniform mat4 u_mvpMatrix;
            uniform mat4 u_texMatrix;
            
            out vec2 v_texCoord;
            
            void main() {
                gl_Position = u_mvpMatrix * a_position;
                v_texCoord = (u_texMatrix * vec4(a_texCoord, 0.0, 1.0)).xy;
            }
        """
        
        /**
         * Base fragment shader template
         */
        const val BASE_FRAGMENT_SHADER = """
            #version 300 es
            #extension GL_OES_EGL_image_external_essl3 : require
            
            precision mediump float;
            
            in vec2 v_texCoord;
            
            uniform samplerExternalOES u_texture;
            uniform int u_filterType;
            uniform float u_filterIntensity;
            
            out vec4 fragColor;
        """
    }
    
    override val vertexShader: String = DEFAULT_VERTEX_SHADER
    
    override fun getUniformValues(): Map<String, Any> {
        return mapOf(
            "u_filterType" to getFilterType().ordinal,
            "u_filterIntensity" to 1.0f
        )
    }
}

/**
 * Original filter (no effect)
 */
class OriginalFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.ORIGINAL
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        void main() {
            fragColor = texture(u_texture, v_texCoord);
        }
    """
}

/**
 * Sepia filter
 */
class SepiaFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.SEPIA
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;
            
            // Sepia transformation matrix
            mat3 sepiaMatrix = mat3(
                0.393, 0.769, 0.189,
                0.349, 0.686, 0.168,
                0.272, 0.534, 0.131
            );
            
            color = sepiaMatrix * color;
            color = mix(textureColor.rgb, color, u_filterIntensity);
            
            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Black and white filter
 */
class BlackWhiteFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.BLACK_WHITE
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;
            
            // Convert to grayscale using luminance formula
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            vec3 grayColor = vec3(gray);
            
            color = mix(textureColor.rgb, grayColor, u_filterIntensity);
            
            fragColor = vec4(color, textureColor.a);
        }
    """
}

/**
 * Vintage filter
 */
class VintageFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.VINTAGE
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;
            
            // Apply sepia first
            mat3 sepiaMatrix = mat3(
                0.393, 0.769, 0.189,
                0.349, 0.686, 0.168,
                0.272, 0.534, 0.131
            );
            color = sepiaMatrix * color;
            
            // Add vintage characteristics
            color *= 0.9; // Slightly darker
            color += vec3(0.1, 0.05, 0.0); // Warm tint
            
            // Vignette effect
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float vignette = 1.0 - smoothstep(0.3, 0.8, dist);
            color *= mix(0.7, 1.0, vignette);
            
            color = mix(textureColor.rgb, color, u_filterIntensity);
            
            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Cool filter (blue tint)
 */
class CoolFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.COOL
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        // RGB to HSV conversion
        vec3 rgb2hsv(vec3 c) {
            vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
            vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
            vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
            
            float d = q.x - min(q.w, q.y);
            float e = 1.0e-10;
            return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
        }
        
        // HSV to RGB conversion
        vec3 hsv2rgb(vec3 c) {
            vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
            vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
            return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
        }
        
        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;
            
            // Convert to HSV
            vec3 hsv = rgb2hsv(color);
            
            // Shift hue towards blue
            hsv.x = mod(hsv.x + 0.1, 1.0);
            
            // Increase saturation slightly
            hsv.y = min(hsv.y * 1.2, 1.0);
            
            // Convert back to RGB
            vec3 coolColor = hsv2rgb(hsv);
            
            // Add blue tint
            coolColor += vec3(0.0, 0.05, 0.1);
            
            color = mix(textureColor.rgb, coolColor, u_filterIntensity);
            
            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Warm filter (orange/red tint)
 */
class WarmFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.WARM
    
    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER
        
        // RGB to HSV conversion
        vec3 rgb2hsv(vec3 c) {
            vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
            vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
            vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
            
            float d = q.x - min(q.w, q.y);
            float e = 1.0e-10;
            return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
        }
        
        // HSV to RGB conversion
        vec3 hsv2rgb(vec3 c) {
            vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
            vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
            return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
        }
        
        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;
            
            // Convert to HSV
            vec3 hsv = rgb2hsv(color);
            
            // Shift hue towards red/orange
            hsv.x = mod(hsv.x - 0.05, 1.0);
            
            // Increase saturation slightly
            hsv.y = min(hsv.y * 1.15, 1.0);
            
            // Convert back to RGB
            vec3 warmColor = hsv2rgb(hsv);
            
            // Add warm tint
            warmColor += vec3(0.1, 0.05, 0.0);
            
            color = mix(textureColor.rgb, warmColor, u_filterIntensity);
            
            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}