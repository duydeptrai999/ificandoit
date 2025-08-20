package com.xiaomi.base.ui.screens.camera.opengl

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Utility class for loading shader source code from assets
 */
object ShaderLoader {
    private const val TAG = "ShaderLoader"

    /**
     * Load shader source code from assets folder
     */
    fun loadShaderFromAssets(
        context: Context,
        fileName: String,
    ): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()

            reader.useLines { lines ->
                lines.forEach { line ->
                    stringBuilder.append(line).append("\n")
                }
            }

            val shaderCode = stringBuilder.toString()
            Log.d(TAG, "Successfully loaded shader: $fileName")
            shaderCode
        } catch (e: IOException) {
            Log.e(TAG, "Failed to load shader from assets: $fileName", e)
            null
        }
    }

    /**
     * Load vertex shader from assets
     */
    fun loadVertexShader(context: Context): String? {
        return loadShaderFromAssets(context, "shaders/vertex_shader.glsl")
    }

    /**
     * Load fragment shader from assets
     */
    fun loadFragmentShader(context: Context): String? {
        return loadShaderFromAssets(context, "shaders/fragment_shader.glsl")
    }
}
