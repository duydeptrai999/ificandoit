#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require

// Fragment shader for camera preview with filter support
// High performance implementation with optimized filter algorithms

precision mediump float;

in vec2 v_texCoord;

uniform samplerExternalOES u_texture;
uniform int u_filterType;
uniform float u_filterIntensity;

out vec4 fragColor;

// Filter type constants
const int FILTER_ORIGINAL = 0;
const int FILTER_SEPIA = 1;
const int FILTER_BLACK_WHITE = 2;
const int FILTER_VINTAGE = 3;
const int FILTER_COOL = 4;
const int FILTER_WARM = 5;

// Utility functions for color manipulation
vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
    
    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

// Sepia filter
vec3 applySepia(vec3 color) {
    mat3 sepiaMatrix = mat3(
        0.393, 0.769, 0.189,
        0.349, 0.686, 0.168,
        0.272, 0.534, 0.131
    );
    return sepiaMatrix * color;
}

// Black and white filter
vec3 applyBlackWhite(vec3 color) {
    float gray = dot(color, vec3(0.299, 0.587, 0.114));
    return vec3(gray);
}

// Vintage filter
vec3 applyVintage(vec3 color) {
    // Apply sepia first
    vec3 sepia = applySepia(color);
    
    // Add vintage characteristics
    sepia *= 0.9; // Slightly darker
    sepia += vec3(0.1, 0.05, 0.0); // Warm tint
    
    // Vignette effect
    vec2 center = vec2(0.5, 0.5);
    float dist = distance(v_texCoord, center);
    float vignette = 1.0 - smoothstep(0.3, 0.8, dist);
    sepia *= mix(0.7, 1.0, vignette);
    
    return sepia;
}

// Cool filter
vec3 applyCool(vec3 color) {
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
    
    return coolColor;
}

// Warm filter
vec3 applyWarm(vec3 color) {
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
    
    return warmColor;
}

void main() {
    vec4 textureColor = texture(u_texture, v_texCoord);
    vec3 color = textureColor.rgb;
    
    // Apply filter based on type
    if (u_filterType == FILTER_SEPIA) {
        color = applySepia(color);
    } else if (u_filterType == FILTER_BLACK_WHITE) {
        color = applyBlackWhite(color);
    } else if (u_filterType == FILTER_VINTAGE) {
        color = applyVintage(color);
    } else if (u_filterType == FILTER_COOL) {
        color = applyCool(color);
    } else if (u_filterType == FILTER_WARM) {
        color = applyWarm(color);
    }
    // FILTER_ORIGINAL: no changes needed
    
    // Apply filter intensity
    color = mix(textureColor.rgb, color, u_filterIntensity);
    
    // Ensure color values are in valid range
    color = clamp(color, 0.0, 1.0);
    
    fragColor = vec4(color, textureColor.a);
}