#version 300 es

// Vertex shader for camera preview with filter support
// Optimized for high performance real-time rendering

layout(location = 0) in vec4 a_position;
layout(location = 1) in vec2 a_texCoord;

uniform mat4 u_mvpMatrix;
uniform mat4 u_texMatrix;

out vec2 v_texCoord;

void main() {
    gl_Position = u_mvpMatrix * a_position;
    v_texCoord = (u_texMatrix * vec4(a_texCoord, 0.0, 1.0)).xy;
}