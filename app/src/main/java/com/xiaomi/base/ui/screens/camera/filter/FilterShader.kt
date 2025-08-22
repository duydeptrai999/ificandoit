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
    // Retro vintage filters (priority at top)
    RETRO_70S,
    RETRO_80S_VINTAGE,
    RETRO_90S,
    VINTAGE_CAMERA,
    
    ORIGINAL,
    SEPIA,
    BLACK_WHITE,
    VINTAGE,
    COOL,
    WARM,

    // New filters
    PINK_DREAM,
    RETRO_80S,
    OLD_FILM,
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER,
    NEON_NIGHTS,
    GOLDEN_HOUR,
    CYBERPUNK,
    CHERRY_BLOSSOM,
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
            "u_filterIntensity" to 1.0f,
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
 * Retro 70s filter shader - Heavy vintage film effect with grain, scratches, and warm 70s color grading
 * Optimized for performance while maintaining authentic vintage look
 */

class Retro70sFilterShader : FilterShader {
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

uniform float uGrainIntensity;
uniform float uScratchIntensity;
uniform float uFadeIntensity;
uniform float uVintageIntensity;
uniform float uTime;   // cần truyền vào thời gian để nhiễu chạy

out vec4 fragColor;

// Random noise
float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898,78.233)) + uTime * 10.0) * 43758.5453);
}

void main() {
    // distort ngang (horizontal wave) -> nhòe nhòe
    float distortion = sin(vTextureCoord.y * 200.0 + uTime * 5.0) * 0.002;
    vec2 uv = vTextureCoord + vec2(distortion, 0.0);

    vec4 original = texture(uTexture, uv);
    vec3 color = original.rgb;

    // Sepia tone nhẹ
    float gray = dot(color, vec3(0.3, 0.59, 0.11));
    vec3 sepia = vec3(
        gray * 0.9 + color.r * 0.1,
        gray * 0.7 + color.g * 0.2,
        gray * 0.4 + color.b * 0.1
    );
    color = mix(color, sepia, uVintageIntensity);

    // Scanlines (xọc ngang TV cũ)
    float scanline = sin(vTextureCoord.y * 800.0) * 0.04;
    color -= scanline;

    // Static noise
    float noise = random(uv * 400.0) - 0.5;
    color += noise * uGrainIntensity * 0.25;

    // Vertical scratches random
    float scratch = step(0.996, random(vec2(floor(uv.x * 200.0), uTime)));
    color -= scratch * uScratchIntensity * 0.3;

    // Fade
    color *= (1.0 - uFadeIntensity * 0.3);

    // Vignette nhẹ
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(uv, center);
    float vignette = smoothstep(0.7, 1.0, dist);
    color *= (1.0 - vignette * 0.4);

    fragColor = vec4(clamp(color, 0.0, 1.0), original.a);
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uGrainIntensity" to 0.7f,
            "uScratchIntensity" to 0.6f,
            "uFadeIntensity" to 0.3f,
            "uVintageIntensity" to 0.8f,
            "uTime" to 0f  // bạn phải update giá trị này theo thời gian
        )

    override fun getFilterType(): FilterType = FilterType.RETRO_70S
}



/**
 * Retro 80s Vintage filter shader - VHS artifacts, color bleeding, scan lines
 */
class Retro80sVintageFilterShader : FilterShader {
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
uniform float uVHSIntensity;
uniform float uScanlineIntensity;
uniform float uColorBleedIntensity;

out vec4 fragColor;

float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

void main() {
    vec2 uv = vTextureCoord;
    
    // VHS horizontal distortion
    float distortion = sin(uv.y * 50.0) * 0.002 * uVHSIntensity;
    uv.x += distortion;
    
    vec4 color = texture(uTexture, uv);
    
    // Color channel separation for VHS effect
    float rOffset = 0.003 * uColorBleedIntensity;
    float bOffset = -0.003 * uColorBleedIntensity;
    
    color.r = texture(uTexture, uv + vec2(rOffset, 0.0)).r;
    color.b = texture(uTexture, uv + vec2(bOffset, 0.0)).b;
    
    // Scan lines
    float scanline = sin(uv.y * 800.0) * 0.5 + 0.5;
    color.rgb *= mix(1.0, scanline, uScanlineIntensity);
    
    // VHS color palette - boost magenta and cyan
    color.r *= 1.2;
    color.b *= 1.1;
    color.g *= 0.9;
    
    // Add VHS noise
    float noise = random(uv * 100.0 + fract(sin(dot(uv, vec2(12.9898, 78.233))) * 43758.5453));
    color.rgb += (noise - 0.5) * 0.1 * uVHSIntensity;
    
    // Horizontal static lines
    float staticLine = step(0.995, random(vec2(0.0, floor(uv.y * 300.0))));
    color.rgb += staticLine * 0.3 * uVHSIntensity;
    
    // Reduce overall brightness for aged look
    color.rgb *= 0.85;
    
    // Add slight vignette
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(uv, center);
    float vignette = 1.0 - smoothstep(0.5, 1.0, dist);
    color.rgb *= mix(0.7, 1.0, vignette);
    
    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uVHSIntensity" to 0.8f,
            "uScanlineIntensity" to 0.4f,
            "uColorBleedIntensity" to 0.6f,
        )

    override fun getFilterType(): FilterType = FilterType.RETRO_80S_VINTAGE
}

/**
 * Retro 90s filter shader - Digital artifacts, pixelation, early digital camera look
 */
class Retro90sFilterShader : FilterShader {
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
uniform float uPixelationIntensity;
uniform float uDigitalNoiseIntensity;
uniform float uCompressionIntensity;

out vec4 fragColor;

float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

void main() {
    vec2 uv = vTextureCoord;
    
    // Pixelation effect for early digital camera look
    float pixelSize = 0.003 * uPixelationIntensity;
    uv = floor(uv / pixelSize) * pixelSize;
    
    vec4 color = texture(uTexture, uv);
    
    // Reduce color depth for 90s digital look
    color.rgb = floor(color.rgb * 32.0) / 32.0;
    
    // Add digital compression artifacts
    float blockX = floor(vTextureCoord.x * 40.0) / 40.0;
    float blockY = floor(vTextureCoord.y * 30.0) / 30.0;
    float blockNoise = random(vec2(blockX, blockY)) * uCompressionIntensity;
    color.rgb += (blockNoise - 0.5) * 0.1;
    
    // Digital noise pattern
    float digitalNoise = random(vTextureCoord * 200.0);
    color.rgb += (digitalNoise - 0.5) * uDigitalNoiseIntensity;
    
    // Color channel quantization
    color.r = floor(color.r * 16.0) / 16.0;
    color.g = floor(color.g * 16.0) / 16.0;
    color.b = floor(color.b * 16.0) / 16.0;
    
    // Slight green tint (common in 90s digital cameras)
    color.g *= 1.1;
    
    // Add CCD sensor artifacts
    float ccdNoise = sin(vTextureCoord.x * 1000.0) * sin(vTextureCoord.y * 1000.0);
    color.rgb += ccdNoise * 0.02 * uDigitalNoiseIntensity;
    
    // Reduce saturation slightly
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
    color.rgb = mix(vec3(gray), color.rgb, 0.8);
    
    // Add slight blue tint for LCD screen look
    color.rgb *= vec3(0.95, 0.98, 1.05);
    
    fragColor = color;
}
"""

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uPixelationIntensity" to 0.5f,
            "uDigitalNoiseIntensity" to 0.08f,
            "uCompressionIntensity" to 0.3f,
        )

    override fun getFilterType(): FilterType = FilterType.RETRO_90S
}

/**
 * Vintage Camera filter shader - Extreme aging, heavy scratches, dust, light leaks
 */
class VintageCameraFilterShader : FilterShader {
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
    uniform float uTime;               // Thời gian, cho animation động
    uniform float uIntensity;          // Độ mạnh filter tổng

    out vec4 fragColor;

    // Random đơn giản, không lỗi biên
    float rand(vec2 co) {
        return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
    }

    void main() {
        vec4 color = texture(uTexture, vTextureCoord);

        // 1. Chuyển về grayscale nhẹ
        float gray = dot(color.rgb, vec3(0.3, 0.59, 0.11));
        color.rgb = mix(color.rgb, vec3(gray), 0.5 * uIntensity);

        // 2. Áp sepia vàng úa
        vec3 sepia = vec3(
            dot(color.rgb, vec3(0.393, 0.769, 0.189)),
            dot(color.rgb, vec3(0.349, 0.686, 0.168)),
            dot(color.rgb, vec3(0.272, 0.534, 0.131))
        );
        color.rgb = mix(color.rgb, sepia, 0.8 * uIntensity);

        // 3. Thêm noise (film grain động)
        float grain = rand(vTextureCoord * uTime * 50.0) - 0.5;
        color.rgb += grain * 0.15 * uIntensity;

        // 4. Light leak đỏ cam từ cạnh trái/phải
        float leakLeft = smoothstep(0.0, 0.4, vTextureCoord.x);
        float leakRight = smoothstep(0.0, 0.4, 1.0 - vTextureCoord.x);
        color.rgb += vec3(0.9, 0.5, 0.3) * (1.0 - leakLeft) * 0.3 * uIntensity;
        color.rgb += vec3(0.9, 0.6, 0.4) * (1.0 - leakRight) * 0.25 * uIntensity;

        // 5. Vignette (tối viền)
        float dist = distance(vTextureCoord, vec2(0.5, 0.5));
        float vignette = smoothstep(0.8, 0.4, dist);
        color.rgb *= vignette;

        // 6. Blur nhẹ mô phỏng ống kính cũ
        float offset = 0.002;
        vec3 blur = (
            texture(uTexture, vTextureCoord + vec2(offset, 0.0)).rgb +
            texture(uTexture, vTextureCoord - vec2(offset, 0.0)).rgb +
            texture(uTexture, vTextureCoord + vec2(0.0, offset)).rgb +
            texture(uTexture, vTextureCoord - vec2(0.0, offset)).rgb
        ) * 0.25;
        color.rgb = mix(color.rgb, blur, 0.3 * uIntensity);

        fragColor = vec4(color.rgb, 1.0);
    }
    """

    override fun getUniformValues(): Map<String, Any> =
        mapOf(
            "uTime" to System.currentTimeMillis() % 10000 / 1000f, // animation theo thời gian
            "uIntensity" to 1.0f                                   // độ mạnh filter
        )

    override fun getFilterType(): FilterType = FilterType.VINTAGE_CAMERA
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

/**
 * Pink Dream filter - Romantic pink tones
 */
class PinkDreamFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.PINK_DREAM

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Add pink romantic tint
            color = mix(color, color * vec3(1.2, 0.9, 1.1), 0.4);

            // Add soft pink overlay
            color = mix(color, vec3(1.0, 0.7, 0.8), 0.2);

            // Soft glow effect
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float glow = 1.0 - smoothstep(0.0, 0.7, dist);
            color += vec3(0.1, 0.05, 0.1) * glow;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Retro 80s filter - Neon and synthwave vibes
 */
class Retro80sFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.RETRO_80S

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // High contrast and saturation
            color = pow(color, vec3(0.8));

            // Boost saturation dramatically
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            color = mix(vec3(gray), color, 1.8);

            // Add neon glow effect
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float neonGlow = 1.0 - smoothstep(0.2, 0.8, dist);
            color += vec3(0.2, 0.0, 0.2) * neonGlow;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Old Film filter - Vintage cinema look
 */
class OldFilmFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.OLD_FILM

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Desaturate slightly
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            color = mix(vec3(gray), color, 0.6);

            // Add sepia tone
            mat3 sepiaMatrix = mat3(
                0.393, 0.769, 0.189,
                0.349, 0.686, 0.168,
                0.272, 0.534, 0.131
            );
            color = mix(color, sepiaMatrix * color, 0.4);

            // Reduce brightness and add grain effect
            color *= 0.85;

            // Film grain simulation
            float grain = fract(sin(dot(v_texCoord * 1000.0, vec2(12.9898, 78.233))) * 43758.5453);
            color += (grain - 0.5) * 0.05;

            // Strong vignette effect
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float vignette = 1.0 - smoothstep(0.2, 1.0, dist);
            color *= mix(0.4, 1.0, vignette);

            // Add slight yellow tint for old film look
            color += vec3(0.05, 0.03, 0.0);

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Spring filter - Fresh green and floral tones
 */
class SpringFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.SPRING

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Brighten the image
            color = pow(color, vec3(0.9));

            // Add fresh green tint
            color = mix(color, color * vec3(0.9, 1.1, 0.95), 0.3);

            // Soft bloom effect
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float bloom = 1.0 - smoothstep(0.0, 0.6, dist);
            color += vec3(0.05, 0.1, 0.05) * bloom;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Summer filter - Bright and sunny
 */
class SummerFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.SUMMER

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Increase overall brightness and warmth
            color = pow(color, vec3(0.9));

            // Add warm golden tint
            color = mix(color, color * vec3(1.1, 1.05, 0.9), 0.4);

            // Sun flare effect
            vec2 sunPos = vec2(0.7, 0.3);
            float sunDist = distance(v_texCoord, sunPos);
            float sunFlare = 1.0 - smoothstep(0.0, 0.4, sunDist);
            color += vec3(0.2, 0.15, 0.05) * sunFlare;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Autumn filter - Warm orange and red tones
 */
class AutumnFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.AUTUMN

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Add warm autumn tint (orange/red)
            color = mix(color, color * vec3(1.2, 0.9, 0.7), 0.5);

            // Add golden overlay
            color = mix(color, vec3(1.0, 0.8, 0.4), 0.15);

            // Slightly reduce brightness for cozy feel
            color *= 0.95;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Winter filter - Cool blue and white tones
 */
class WinterFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.WINTER

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Desaturate slightly for cold feel
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            color = mix(vec3(gray), color, 0.8);

            // Add cool blue tint
            color = mix(color, color * vec3(0.9, 0.95, 1.2), 0.4);

            // Add icy highlights
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float ice = 1.0 - smoothstep(0.3, 0.8, dist);
            color += vec3(0.05, 0.1, 0.15) * ice;

            // Increase brightness slightly (snow reflection)
            color *= 1.1;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Neon Nights filter - Cyberpunk neon effect
 */
class NeonNightsFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.NEON_NIGHTS

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // High contrast
            color = pow(color, vec3(1.2));

            // Extreme saturation boost
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            color = mix(vec3(gray), color, 2.0);

            // Add neon glow
            float glow = sin(v_texCoord.x * 20.0) * sin(v_texCoord.y * 20.0);
            color += vec3(0.1, 0.0, 0.1) * abs(glow) * 0.3;

            // Dark background enhancement
            float luminance = dot(color, vec3(0.299, 0.587, 0.114));
            if (luminance < 0.3) {
                color *= 0.5;
            }

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Golden Hour filter - Warm sunset lighting
 */
class GoldenHourFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.GOLDEN_HOUR

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Warm up the image
            color = pow(color, vec3(0.85));

            // Add golden overlay
            color = mix(color, vec3(1.0, 0.8, 0.4), 0.25);

            // Simulate directional lighting
            float lightAngle = dot(normalize(v_texCoord - vec2(0.2, 0.8)), vec2(1.0, 0.0));
            float lighting = max(0.0, lightAngle) * 0.3 + 0.7;
            color *= lighting;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Cyberpunk filter - Futuristic tech aesthetic
 */
class CyberpunkFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.CYBERPUNK

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // High contrast and saturation
            color = pow(color, vec3(1.3));

            // Extreme saturation
            float gray = dot(color, vec3(0.299, 0.587, 0.114));
            color = mix(vec3(gray), color, 1.8);

            // Add digital glitch effect
            float glitch = step(0.98, fract(sin(dot(v_texCoord * 100.0, vec2(12.9898, 78.233))) * 43758.5453));
            color = mix(color, vec3(0.0, 1.0, 1.0), glitch * 0.5);

            // Scanline effect
            float scanline = sin(v_texCoord.y * 800.0) * 0.1 + 0.9;
            color *= scanline;

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}

/**
 * Cherry Blossom filter - Soft pink and white tones like sakura season
 */
class CherryBlossomFilter : BaseFilterShader() {
    override fun getFilterType(): FilterType = FilterType.CHERRY_BLOSSOM

    override val fragmentShader: String = """
        $BASE_FRAGMENT_SHADER

        void main() {
            vec4 textureColor = texture(u_texture, v_texCoord);
            vec3 color = textureColor.rgb;

            // Soften the image with slight blur effect
            color = pow(color, vec3(0.95));

            // Add cherry blossom pink tint
            color = mix(color, color * vec3(1.1, 0.95, 1.05), 0.3);

            // Add soft pink and white overlay for sakura effect
            vec3 sakuraColor = vec3(1.0, 0.9, 0.95); // Soft pink-white
            color = mix(color, sakuraColor, 0.15);

            // Add subtle warm glow
            vec2 center = vec2(0.5, 0.5);
            float dist = distance(v_texCoord, center);
            float glow = 1.0 - smoothstep(0.0, 0.8, dist);
            color += vec3(0.05, 0.02, 0.03) * glow;

            // Increase brightness slightly for dreamy effect
            color *= 1.05;

            // Add subtle vignette with pink tint
            float vignette = 1.0 - smoothstep(0.4, 1.0, dist);
            color = mix(color * 0.9, color, vignette);
            color += vec3(0.02, 0.01, 0.015) * (1.0 - vignette);

            color = mix(textureColor.rgb, color, u_filterIntensity);

            fragColor = vec4(clamp(color, 0.0, 1.0), textureColor.a);
        }
    """
}
