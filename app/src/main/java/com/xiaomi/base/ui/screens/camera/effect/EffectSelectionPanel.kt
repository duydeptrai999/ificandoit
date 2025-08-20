package com.xiaomi.base.ui.screens.camera.effect

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaomi.base.R

/**
 * Panel for selecting photo effects
 */
@Composable
fun EffectSelectionPanel(
    selectedEffect: EffectType,
    onEffectSelected: (EffectType) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(EffectCategory.LIGHT_GLOW) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.8f),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(16.dp)
    ) {
        // Category tabs
        EffectCategoryTabs(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Effect list for selected category
        EffectList(
            category = selectedCategory,
            selectedEffect = selectedEffect,
            onEffectSelected = onEffectSelected
        )
    }
}

@Composable
private fun EffectCategoryTabs(
    selectedCategory: EffectCategory,
    onCategorySelected: (EffectCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(EffectCategory.values()) { category ->
            EffectCategoryTab(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun EffectCategoryTab(
    category: EffectCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        Color.White
    }

    val borderColor = if (isSelected) {
        Color.Transparent
    } else {
        Color.White.copy(alpha = 0.3f)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = getCategoryDisplayName(category),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun EffectList(
    category: EffectCategory,
    selectedEffect: EffectType,
    onEffectSelected: (EffectType) -> Unit,
    modifier: Modifier = Modifier
) {
    val effectsInCategory = getEffectsForCategory(category)

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(effectsInCategory) { effect ->
            EffectItem(
                effect = effect,
                isSelected = effect == selectedEffect,
                onClick = { onEffectSelected(effect) }
            )
        }
    }
}

@Composable
private fun EffectItem(
    effect: EffectType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    } else {
        Color.White.copy(alpha = 0.1f)
    }

    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Column(
        modifier = modifier
            .width(80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Effect preview (placeholder)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(getEffectPreviewColor(effect)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getEffectIcon(effect),
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = getEffectDisplayName(effect),
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 2
        )
    }
}

// Helper functions
@Composable
private fun getCategoryDisplayName(category: EffectCategory): String {
    return when (category) {
        EffectCategory.LIGHT_GLOW -> stringResource(R.string.effect_category_light_glow)
        EffectCategory.MOTION_BLUR -> stringResource(R.string.effect_category_motion_blur)
        EffectCategory.GLITCH_VHS -> stringResource(R.string.effect_category_glitch_vhs)
        EffectCategory.TEXTURE_OVERLAY -> stringResource(R.string.effect_category_texture_overlay)
        EffectCategory.ARTISTIC -> stringResource(R.string.effect_category_artistic)
        EffectCategory.COLOR_EFFECTS -> stringResource(R.string.effect_category_special_color)
        EffectCategory.DISTORTION -> stringResource(R.string.effect_category_distortion_3d)
        EffectCategory.FUTURISTIC -> stringResource(R.string.effect_category_futuristic_sci_fi)
        EffectCategory.DYNAMIC_PARTICLES -> stringResource(R.string.effect_category_dynamic_particles)
        EffectCategory.FACE_BEAUTY -> stringResource(R.string.effect_category_face_beauty)
        EffectCategory.INTERACTIVE -> stringResource(R.string.effect_category_interactive)
        EffectCategory.SOCIAL_TRENDING -> stringResource(R.string.effect_category_social_trending)
        EffectCategory.ADVANCED_3D_PARTICLE -> stringResource(R.string.effect_category_advanced_3d_particle)
        EffectCategory.ADVANCED_DISTORTION -> stringResource(R.string.effect_category_advanced_distortion)
        EffectCategory.INTERACTIVE_ADVANCED -> stringResource(R.string.effect_category_interactive_advanced)
    }
}

private fun getEffectsForCategory(category: EffectCategory): List<EffectType> {
    return when (category) {
        EffectCategory.LIGHT_GLOW -> listOf(
            EffectType.ORIGINAL,
            EffectType.BOKEH,
            EffectType.LENS_FLARE,
            EffectType.NEON_GLOW,
            EffectType.GOLDEN_HOUR
        )
        EffectCategory.MOTION_BLUR -> listOf(
            EffectType.MOTION_BLUR,
            EffectType.ZOOM_BLUR,
            EffectType.TILT_SHIFT
        )
        EffectCategory.GLITCH_VHS -> listOf(
            EffectType.GLITCH,
            EffectType.VHS,
            EffectType.RGB_SPLIT
        )
        EffectCategory.TEXTURE_OVERLAY -> listOf(
            EffectType.FILM_GRAIN,
            EffectType.FILM_SCRATCHES,
            EffectType.DUST_SPOTS,
            EffectType.PAPER_TEXTURE
        )
        EffectCategory.ARTISTIC -> listOf(
            EffectType.OIL_PAINTING,
            EffectType.WATERCOLOR,
            EffectType.CARTOON,
            EffectType.PENCIL_SKETCH
        )
        EffectCategory.COLOR_EFFECTS -> listOf(
            EffectType.NEGATIVE,
            EffectType.COLOR_SPLASH,
            EffectType.DUOTONE,
            EffectType.GRADIENT_OVERLAY
        )
        EffectCategory.DISTORTION -> listOf(
            EffectType.FISHEYE,
            EffectType.RIPPLE,
            EffectType.MIRROR,
            EffectType.KALEIDOSCOPE
        )
        EffectCategory.FUTURISTIC -> listOf(
            EffectType.HOLOGRAM,
            EffectType.MATRIX_RAIN,
            EffectType.CYBERPUNK_SCAN,
            EffectType.ENERGY_SHIELD,
            EffectType.PORTAL
        )
        EffectCategory.DYNAMIC_PARTICLES -> listOf(
            EffectType.FIRE_EFFECT,
            EffectType.LIGHTNING,
            EffectType.PARTICLE_EXPLOSION,
            EffectType.DNA_HELIX,
            EffectType.TIME_WARP
        )
        EffectCategory.SOCIAL_TRENDING -> listOf(
            EffectType.RETRO_CAM,
            EffectType.BLING_SPARKLE,
            EffectType.DISNEY_STYLE,
            EffectType.GREEN_SCREEN,
            EffectType.TONAL_CINEMATIC
        )
        EffectCategory.ADVANCED_3D_PARTICLE -> listOf(
            EffectType.PARTICLE_EXPLOSION_3D,
            EffectType.DEPTH_FIELD_BLUR,
            EffectType.CARD_DANCE_3D,
            EffectType.CAUSTICS_WATER,
            EffectType.VOLUMETRIC_LIGHT,
            EffectType.PARTICLE_STORM
        )
        EffectCategory.ADVANCED_DISTORTION -> listOf(
            EffectType.MESH_WARP,
            EffectType.TURBULENT_DISPLACE,
            EffectType.DISPLACEMENT_MAP,
            EffectType.FACE_MORPHING_ADVANCED,
            EffectType.LIQUID_DISTORTION,
            EffectType.GRAVITY_WARP
        )
        EffectCategory.INTERACTIVE_ADVANCED -> listOf(
            EffectType.TIME_WARP_SCAN_ADVANCED,
            EffectType.FLUID_DYNAMICS,
            EffectType.FORCE_FIELD,
            EffectType.DEFLECTOR_BOUNCE,
            EffectType.MOTION_TRACKING,
            EffectType.GESTURE_REACTIVE
        )
        EffectCategory.FACE_BEAUTY -> category.effects
        EffectCategory.INTERACTIVE -> category.effects
    }
}

@Composable
fun getEffectDisplayName(effect: EffectType): String {
    return when (effect) {
        EffectType.ORIGINAL -> stringResource(R.string.effect_original)
        EffectType.BOKEH -> stringResource(R.string.effect_bokeh)
        EffectType.LENS_FLARE -> stringResource(R.string.effect_lens_flare)
        EffectType.NEON_GLOW -> stringResource(R.string.effect_neon_glow)
        EffectType.GOLDEN_HOUR -> stringResource(R.string.effect_golden_hour)
        EffectType.MOTION_BLUR -> stringResource(R.string.effect_motion_blur)
        EffectType.ZOOM_BLUR -> stringResource(R.string.effect_zoom_blur)
        EffectType.TILT_SHIFT -> stringResource(R.string.effect_tilt_shift)
        EffectType.GLITCH -> stringResource(R.string.effect_glitch)
        EffectType.VHS -> stringResource(R.string.effect_vhs)
        EffectType.RGB_SPLIT -> stringResource(R.string.effect_rgb_split)
        EffectType.FILM_GRAIN -> stringResource(R.string.effect_film_grain)
        EffectType.FILM_SCRATCHES -> stringResource(R.string.effect_film_scratches)
        EffectType.DUST_SPOTS -> stringResource(R.string.effect_dust_spots)
        EffectType.PAPER_TEXTURE -> stringResource(R.string.effect_paper_texture)
        EffectType.OIL_PAINTING -> stringResource(R.string.effect_oil_painting)
        EffectType.WATERCOLOR -> stringResource(R.string.effect_watercolor)
        EffectType.CARTOON -> stringResource(R.string.effect_cartoon)
        EffectType.PENCIL_SKETCH -> stringResource(R.string.effect_pencil_sketch)
        EffectType.NEGATIVE -> stringResource(R.string.effect_negative)
        EffectType.COLOR_SPLASH -> stringResource(R.string.effect_color_splash)
        EffectType.DUOTONE -> stringResource(R.string.effect_duotone)
        EffectType.GRADIENT_OVERLAY -> stringResource(R.string.effect_gradient_overlay)
        EffectType.FISHEYE -> stringResource(R.string.effect_fisheye)
        EffectType.RIPPLE -> stringResource(R.string.effect_ripple)
        EffectType.MIRROR -> stringResource(R.string.effect_mirror)
        EffectType.KALEIDOSCOPE -> stringResource(R.string.effect_kaleidoscope)
        // Futuristic & Sci-Fi Effects
        EffectType.HOLOGRAM -> stringResource(R.string.effect_hologram)
        EffectType.MATRIX_RAIN -> stringResource(R.string.effect_matrix_rain)
        EffectType.CYBERPUNK_SCAN -> stringResource(R.string.effect_cyberpunk_scan)
        EffectType.ENERGY_SHIELD -> stringResource(R.string.effect_energy_shield)
        EffectType.PORTAL -> stringResource(R.string.effect_portal)
        // Dynamic & Particle Effects
        EffectType.FIRE_EFFECT -> stringResource(R.string.effect_fire_effect)
        EffectType.LIGHTNING -> stringResource(R.string.effect_lightning)
        EffectType.PARTICLE_EXPLOSION -> stringResource(R.string.effect_particle_explosion)
        EffectType.DNA_HELIX -> stringResource(R.string.effect_dna_helix)
        EffectType.TIME_WARP -> stringResource(R.string.effect_time_warp)
        // Advanced 3D & Particle Effects
        EffectType.PARTICLE_EXPLOSION_3D -> stringResource(R.string.effect_particle_explosion_3d)
        EffectType.DEPTH_FIELD_BLUR -> stringResource(R.string.effect_depth_field_blur)
        EffectType.CARD_DANCE_3D -> stringResource(R.string.effect_card_dance_3d)
        EffectType.CAUSTICS_WATER -> stringResource(R.string.effect_caustics_water)
        EffectType.VOLUMETRIC_LIGHT -> stringResource(R.string.effect_volumetric_light)
        EffectType.PARTICLE_STORM -> stringResource(R.string.effect_particle_storm)
        // Advanced Distortion & Morphing
        EffectType.MESH_WARP -> stringResource(R.string.effect_mesh_warp)
        EffectType.TURBULENT_DISPLACE -> stringResource(R.string.effect_turbulent_displace)
        EffectType.DISPLACEMENT_MAP -> stringResource(R.string.effect_displacement_map)
        EffectType.FACE_MORPHING_ADVANCED -> stringResource(R.string.effect_face_morphing_advanced)
        EffectType.LIQUID_DISTORTION -> stringResource(R.string.effect_liquid_distortion)
        EffectType.GRAVITY_WARP -> stringResource(R.string.effect_gravity_warp)
        // Interactive & Dynamic Advanced
        EffectType.TIME_WARP_SCAN_ADVANCED -> stringResource(R.string.effect_time_warp_scan_advanced)
        EffectType.FLUID_DYNAMICS -> stringResource(R.string.effect_fluid_dynamics)
        EffectType.FORCE_FIELD -> stringResource(R.string.effect_force_field)
        EffectType.DEFLECTOR_BOUNCE -> stringResource(R.string.effect_deflector_bounce)
        EffectType.MOTION_TRACKING -> stringResource(R.string.effect_motion_tracking)
        EffectType.GESTURE_REACTIVE -> stringResource(R.string.effect_gesture_reactive)
        // Face & Beauty
        // Face & Beauty effects are handled by their individual enum values
        // Interactive & Dynamic effects are handled by their individual enum values
        EffectType.TIME_WARP_SCAN -> stringResource(R.string.effect_time_warp_scan)
        EffectType.SLOW_ZOOM -> stringResource(R.string.effect_slow_zoom)
        // Social Media Trending
        EffectType.RETRO_CAM -> stringResource(R.string.effect_retro_cam)
        EffectType.BLING_SPARKLE -> stringResource(R.string.effect_bling_sparkle)
        EffectType.DISNEY_STYLE -> stringResource(R.string.effect_disney_style)
        EffectType.GREEN_SCREEN -> stringResource(R.string.effect_green_screen)
        EffectType.TONAL_CINEMATIC -> stringResource(R.string.effect_tonal_cinematic)
        // Face & Beauty Individual Effects
        EffectType.INVISIBLE_EFFECT -> "Invisible Effect"
        EffectType.TRIO_EFFECT -> "Trio Effect"
        EffectType.EXPRESSIFY -> "Expressify"
        EffectType.FACE_MORPH -> "Face Morph"
        EffectType.BEAUTY_MODE -> "Beauty Mode"
        EffectType.ANIME_FILTER -> "Anime Filter"
        EffectType.BIG_EYES -> "Big Eyes"
        EffectType.SLIM_FACE -> "Slim Face"
    }
}

private fun getEffectIcon(effect: EffectType): String {
    return when (effect) {
        EffectType.ORIGINAL -> "○"
        EffectType.BOKEH -> "◉"
        EffectType.LENS_FLARE -> "☀"
        EffectType.NEON_GLOW -> "◈"
        EffectType.GOLDEN_HOUR -> "☽"
        EffectType.MOTION_BLUR -> "→"
        EffectType.ZOOM_BLUR -> "⊙"
        EffectType.TILT_SHIFT -> "⧨"
        EffectType.GLITCH -> "⚡"
        EffectType.VHS -> "▣"
        EffectType.RGB_SPLIT -> "◐"
        EffectType.FILM_GRAIN -> "⋯"
        EffectType.FILM_SCRATCHES -> "╱"
        EffectType.DUST_SPOTS -> "⋅"
        EffectType.PAPER_TEXTURE -> "▦"
        EffectType.OIL_PAINTING -> "🎨"
        EffectType.WATERCOLOR -> "💧"
        EffectType.CARTOON -> "😊"
        EffectType.PENCIL_SKETCH -> "✏"
        EffectType.NEGATIVE -> "◑"
        EffectType.COLOR_SPLASH -> "🌈"
        EffectType.DUOTONE -> "◒"
        EffectType.GRADIENT_OVERLAY -> "▲"
        EffectType.FISHEYE -> "◎"
        EffectType.RIPPLE -> "〰"
        EffectType.MIRROR -> "⟷"
        EffectType.KALEIDOSCOPE -> "❋"
        // Futuristic & Sci-Fi Effects
        EffectType.HOLOGRAM -> "◇"
        EffectType.MATRIX_RAIN -> "⌘"
        EffectType.CYBERPUNK_SCAN -> "⟐"
        EffectType.ENERGY_SHIELD -> "⬟"
        EffectType.PORTAL -> "◉"
        // Dynamic & Particle Effects
        EffectType.FIRE_EFFECT -> "🔥"
        EffectType.LIGHTNING -> "⚡"
        EffectType.PARTICLE_EXPLOSION -> "💥"
        EffectType.DNA_HELIX -> "🧬"
        EffectType.TIME_WARP -> "⏰"
        // Social Media Trending Effects
        EffectType.RETRO_CAM -> "📷"
        EffectType.BLING_SPARKLE -> "✨"
        EffectType.DISNEY_STYLE -> "🏰"
        EffectType.GREEN_SCREEN -> "🟢"
        EffectType.TONAL_CINEMATIC -> "🎬"
        // Face & Beauty Effects
        EffectType.FACE_MORPH -> "😊"
        EffectType.BEAUTY_MODE -> "💄"
        EffectType.ANIME_FILTER -> "🎌"
        EffectType.BIG_EYES -> "👁"
        EffectType.SLIM_FACE -> "✨"
        // Interactive & Dynamic Effects
        EffectType.TIME_WARP_SCAN -> "⏰"
        EffectType.SLOW_ZOOM -> "🔍"
        EffectType.INVISIBLE_EFFECT -> "👻"
        EffectType.TRIO_EFFECT -> "🎭"
        EffectType.EXPRESSIFY -> "😄"
        // Advanced 3D & Particle Effects
        EffectType.PARTICLE_EXPLOSION_3D -> "💫"
        EffectType.DEPTH_FIELD_BLUR -> "🔍"
        EffectType.CARD_DANCE_3D -> "🃏"
        EffectType.CAUSTICS_WATER -> "🌊"
        EffectType.VOLUMETRIC_LIGHT -> "💡"
        EffectType.PARTICLE_STORM -> "⛈"
        // Advanced Distortion & Morphing Effects
        EffectType.MESH_WARP -> "🕸"
        EffectType.TURBULENT_DISPLACE -> "🌪"
        EffectType.DISPLACEMENT_MAP -> "🗺"
        EffectType.FACE_MORPHING_ADVANCED -> "🎭"
        EffectType.LIQUID_DISTORTION -> "💧"
        EffectType.GRAVITY_WARP -> "🌌"
        // Interactive & Dynamic Advanced Effects
        EffectType.TIME_WARP_SCAN_ADVANCED -> "⏱"
        EffectType.FLUID_DYNAMICS -> "🌀"
        EffectType.FORCE_FIELD -> "⚡"
        EffectType.DEFLECTOR_BOUNCE -> "🏀"
        EffectType.MOTION_TRACKING -> "📍"
        EffectType.GESTURE_REACTIVE -> "👋"
        // Face & Beauty effects are handled by their individual enum values
        // Interactive & Dynamic effects are handled by their individual enum values
    }
}

private fun getEffectPreviewColor(effect: EffectType): Color {
    return when (effect) {
        EffectType.ORIGINAL -> Color.Gray
        EffectType.BOKEH -> Color(0xFF4CAF50)
        EffectType.LENS_FLARE -> Color(0xFFFFEB3B)
        EffectType.NEON_GLOW -> Color(0xFF00BCD4)
        EffectType.GOLDEN_HOUR -> Color(0xFFFF9800)
        EffectType.MOTION_BLUR -> Color(0xFF2196F3)
        EffectType.ZOOM_BLUR -> Color(0xFF3F51B5)
        EffectType.TILT_SHIFT -> Color(0xFF9C27B0)
        EffectType.GLITCH -> Color(0xFFE91E63)
        EffectType.VHS -> Color(0xFF795548)
        EffectType.RGB_SPLIT -> Color(0xFFF44336)
        EffectType.FILM_GRAIN -> Color(0xFF607D8B)
        EffectType.FILM_SCRATCHES -> Color(0xFF9E9E9E)
        EffectType.DUST_SPOTS -> Color(0xFF757575)
        EffectType.PAPER_TEXTURE -> Color(0xFFBCAAA4)
        EffectType.OIL_PAINTING -> Color(0xFF8BC34A)
        EffectType.WATERCOLOR -> Color(0xFF03DAC5)
        EffectType.CARTOON -> Color(0xFFFF5722)
        EffectType.PENCIL_SKETCH -> Color(0xFF424242)
        EffectType.NEGATIVE -> Color(0xFF000000)
        EffectType.COLOR_SPLASH -> Color(0xFFE91E63)
        EffectType.DUOTONE -> Color(0xFF673AB7)
        EffectType.GRADIENT_OVERLAY -> Color(0xFF009688)
        EffectType.FISHEYE -> Color(0xFF00BCD4)
        EffectType.RIPPLE -> Color(0xFF2196F3)
        EffectType.MIRROR -> Color(0xFF9C27B0)
        EffectType.KALEIDOSCOPE -> Color(0xFFFF9800)
        // Futuristic & Sci-Fi Effects
        EffectType.HOLOGRAM -> Color(0xFF00FFFF)
        EffectType.MATRIX_RAIN -> Color(0xFF00FF00)
        EffectType.CYBERPUNK_SCAN -> Color(0xFFFF00FF)
        EffectType.ENERGY_SHIELD -> Color(0xFF0080FF)
        EffectType.PORTAL -> Color(0xFF8000FF)
        // Dynamic & Particle Effects
        EffectType.FIRE_EFFECT -> Color(0xFFFF4500)
        EffectType.LIGHTNING -> Color(0xFFFFFF00)
        EffectType.PARTICLE_EXPLOSION -> Color(0xFFFF6600)
        EffectType.DNA_HELIX -> Color(0xFF00FF80)
        EffectType.TIME_WARP -> Color(0xFF4000FF)
        // Social Media Trending Effects
        EffectType.RETRO_CAM -> Color(0xFFD2691E)
        EffectType.BLING_SPARKLE -> Color(0xFFFFD700)
        EffectType.DISNEY_STYLE -> Color(0xFFFF69B4)
        EffectType.GREEN_SCREEN -> Color(0xFF32CD32)
        EffectType.TONAL_CINEMATIC -> Color(0xFF708090)
        // Face & Beauty Effects
        EffectType.FACE_MORPH -> Color(0xFFFFB6C1)
        EffectType.BEAUTY_MODE -> Color(0xFFFFC0CB)
        EffectType.ANIME_FILTER -> Color(0xFFFF1493)
        EffectType.BIG_EYES -> Color(0xFF87CEEB)
        EffectType.SLIM_FACE -> Color(0xFFDDA0DD)
        // Interactive & Dynamic Effects
        EffectType.TIME_WARP_SCAN -> Color(0xFF9370DB)
        EffectType.SLOW_ZOOM -> Color(0xFF4169E1)
        EffectType.INVISIBLE_EFFECT -> Color(0xFFB0C4DE)
        EffectType.TRIO_EFFECT -> Color(0xFF20B2AA)
        EffectType.EXPRESSIFY -> Color(0xFFFF6347)
        // Advanced 3D & Particle Effects
        EffectType.PARTICLE_EXPLOSION_3D -> Color(0xFFFF6B35)
        EffectType.DEPTH_FIELD_BLUR -> Color(0xFF4A90E2)
        EffectType.CARD_DANCE_3D -> Color(0xFF7B68EE)
        EffectType.CAUSTICS_WATER -> Color(0xFF00CED1)
        EffectType.VOLUMETRIC_LIGHT -> Color(0xFFFFA500)
        EffectType.PARTICLE_STORM -> Color(0xFFDC143C)
        // Advanced Distortion & Morphing Effects
        EffectType.MESH_WARP -> Color(0xFF32CD32)
        EffectType.TURBULENT_DISPLACE -> Color(0xFF8A2BE2)
        EffectType.DISPLACEMENT_MAP -> Color(0xFF20B2AA)
        EffectType.FACE_MORPHING_ADVANCED -> Color(0xFFFF1493)
        EffectType.LIQUID_DISTORTION -> Color(0xFF00BFFF)
        EffectType.GRAVITY_WARP -> Color(0xFF9370DB)
        // Interactive & Dynamic Advanced Effects
        EffectType.TIME_WARP_SCAN_ADVANCED -> Color(0xFFFF4500)
        EffectType.FLUID_DYNAMICS -> Color(0xFF1E90FF)
        EffectType.FORCE_FIELD -> Color(0xFF00FF7F)
        EffectType.DEFLECTOR_BOUNCE -> Color(0xFFFF69B4)
        EffectType.MOTION_TRACKING -> Color(0xFF32CD32)
        EffectType.GESTURE_REACTIVE -> Color(0xFFFFD700)

    }
}