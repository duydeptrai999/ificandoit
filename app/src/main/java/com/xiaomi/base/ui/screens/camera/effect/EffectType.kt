package com.xiaomi.base.ui.screens.camera.effect

/**
 * Enum class defining various photo effects
 * Based on popular photo editing app effects
 */
enum class EffectType(val displayName: String, val description: String) {
    // Original - no effect
    ORIGINAL("Original", "No effect applied"),

    // Light & Glow Effects
    BOKEH("Bokeh", "Add circular light spots, like camera lens bokeh"),
    LENS_FLARE("Lens Flare", "Add bright light effects from edges"),
    NEON_GLOW("Neon Glow", "Add neon light borders and glowing text"),
    GOLDEN_HOUR("Golden Hour", "Warm sunset/sunrise lighting effect"),

    // Motion & Blur Effects
    MOTION_BLUR("Motion Blur", "Add movement blur effect"),
    ZOOM_BLUR("Zoom Blur", "Blur from center outward"),
    TILT_SHIFT("Tilt-shift", "Focus on one area, blur surroundings"),

    // Glitch & VHS Effects
    GLITCH("Glitch", "TV static noise effect"),
    VHS("VHS", "Old VHS tape effect with horizontal lines"),
    RGB_SPLIT("RGB Split", "Color separation glitch effect"),

    // Texture & Overlay Effects
    FILM_GRAIN("Film Grain", "Add film grain noise"),
    FILM_SCRATCHES("Film Scratches", "Add old film scratches"),
    DUST_SPOTS("Dust Spots", "Add dust spots like old film"),
    PAPER_TEXTURE("Paper Texture", "Add paper/canvas texture"),

    // Artistic Effects
    OIL_PAINTING("Oil Painting", "Oil painting artistic effect"),
    WATERCOLOR("Watercolor", "Watercolor painting effect"),
    CARTOON("Cartoon", "Cartoon/anime style effect"),
    PENCIL_SKETCH("Pencil Sketch", "Pencil drawing effect"),

    // Special Color Effects
    NEGATIVE("Negative", "Film negative effect"),
    COLOR_SPLASH("Color Splash", "Keep one color, make rest black & white"),
    DUOTONE("Duotone", "Two-color effect"),
    GRADIENT_OVERLAY("Gradient Overlay", "Add gradient color overlay"),

    // 3D & Distortion Effects
    FISHEYE("Fish-eye", "Fish-eye lens distortion"),
    RIPPLE("Ripple", "Water ripple distortion"),
    MIRROR("Mirror", "Mirror symmetry effect"),
    KALEIDOSCOPE("Kaleidoscope", "Kaleidoscope pattern effect"),

    // Futuristic & Sci-Fi Effects
    HOLOGRAM("Hologram", "3D holographic projection effect with scan lines"),
    MATRIX_RAIN("Matrix Rain", "Digital rain effect like The Matrix movie"),
    CYBERPUNK_SCAN("Cyberpunk Scan", "Futuristic scanning lines with data overlay"),
    ENERGY_SHIELD("Energy Shield", "Protective energy barrier effect"),
    PORTAL("Portal", "Interdimensional portal swirl effect"),

    // Dynamic & Particle Effects
    FIRE_EFFECT("Fire Effect", "Realistic fire flames overlay"),
    LIGHTNING("Lightning", "Electric lightning bolts effect"),
    PARTICLE_EXPLOSION("Particle Explosion", "Explosive particle burst effect"),
    DNA_HELIX("DNA Helix", "Rotating DNA double helix overlay"),
    TIME_WARP("Time Warp", "Time distortion spiral effect"),

    // Social Media Trending
    RETRO_CAM("Retro Cam", "Social Media Trending"),
    BLING_SPARKLE("Bling Sparkle", "Social Media Trending"),
    DISNEY_STYLE("Disney Style", "Social Media Trending"),
    GREEN_SCREEN("Green Screen", "Social Media Trending"),
    TONAL_CINEMATIC("Tonal Cinematic", "Social Media Trending"),

    // Advanced 3D & Particle Effects
    PARTICLE_EXPLOSION_3D("Particle Explosion 3D", "Advanced 3D & Particle"),
    DEPTH_FIELD_BLUR("Depth Field Blur", "Advanced 3D & Particle"),
    CARD_DANCE_3D("3D Card Dance", "Advanced 3D & Particle"),
    CAUSTICS_WATER("Caustics Water Effect", "Advanced 3D & Particle"),
    VOLUMETRIC_LIGHT("Volumetric Light", "Advanced 3D & Particle"),
    PARTICLE_STORM("Particle Storm", "Advanced 3D & Particle"),

    // Advanced Distortion & Morphing
    MESH_WARP("Mesh Warp", "Advanced Distortion & Morphing"),
    TURBULENT_DISPLACE("Turbulent Displace", "Advanced Distortion & Morphing"),
    DISPLACEMENT_MAP("Displacement Map", "Advanced Distortion & Morphing"),
    FACE_MORPHING_ADVANCED("Advanced Face Morphing", "Advanced Distortion & Morphing"),
    LIQUID_DISTORTION("Liquid Distortion", "Advanced Distortion & Morphing"),
    GRAVITY_WARP("Gravity Warp", "Advanced Distortion & Morphing"),

    // Interactive & Dynamic
    TIME_WARP_SCAN("Time Warp Scan", "Interactive & Dynamic"),
    SLOW_ZOOM("Slow Zoom", "Interactive & Dynamic"),
    INVISIBLE_EFFECT("Invisible Effect", "Interactive & Dynamic"),
    TRIO_EFFECT("Trio Effect", "Interactive & Dynamic"),
    EXPRESSIFY("Expressify", "Interactive & Dynamic"),

    // Face & Beauty
    FACE_MORPH("Face Morph", "Face & Beauty"),
    BEAUTY_MODE("Beauty Mode", "Face & Beauty"),
    ANIME_FILTER("Anime Filter", "Face & Beauty"),
    BIG_EYES("Big Eyes", "Face & Beauty"),
    SLIM_FACE("Slim Face", "Face & Beauty"),

    // Interactive & Dynamic Advanced
    TIME_WARP_SCAN_ADVANCED("Time Warp Scan Advanced", "Interactive & Dynamic Advanced"),
    FLUID_DYNAMICS("Fluid Dynamics", "Interactive & Dynamic Advanced"),
    FORCE_FIELD("Force Field", "Interactive & Dynamic Advanced"),
    DEFLECTOR_BOUNCE("Deflector Bounce", "Interactive & Dynamic Advanced"),
    MOTION_TRACKING("Motion Tracking", "Interactive & Dynamic Advanced"),
    GESTURE_REACTIVE("Gesture Reactive", "Interactive & Dynamic Advanced")
}

/**
 * Effect categories for better organization
 */
enum class EffectCategory(val displayName: String, val effects: List<EffectType>) {
    LIGHT_GLOW(
        "Light & Glow",
        listOf(
            EffectType.BOKEH,
            EffectType.LENS_FLARE,
            EffectType.NEON_GLOW,
            EffectType.GOLDEN_HOUR,
        ),
    ),

    MOTION_BLUR(
        "Motion & Blur",
        listOf(
            EffectType.MOTION_BLUR,
            EffectType.ZOOM_BLUR,
            EffectType.TILT_SHIFT,
        ),
    ),

    GLITCH_VHS(
        "Glitch & VHS",
        listOf(
            EffectType.GLITCH,
            EffectType.VHS,
            EffectType.RGB_SPLIT,
        ),
    ),

    TEXTURE_OVERLAY(
        "Texture & Overlay",
        listOf(
            EffectType.FILM_GRAIN,
            EffectType.FILM_SCRATCHES,
            EffectType.DUST_SPOTS,
            EffectType.PAPER_TEXTURE,
        ),
    ),

    ARTISTIC(
        "Artistic",
        listOf(
            EffectType.OIL_PAINTING,
            EffectType.WATERCOLOR,
            EffectType.CARTOON,
            EffectType.PENCIL_SKETCH,
        ),
    ),

    COLOR_EFFECTS(
        "Color Effects",
        listOf(
            EffectType.NEGATIVE,
            EffectType.COLOR_SPLASH,
            EffectType.DUOTONE,
            EffectType.GRADIENT_OVERLAY,
        ),
    ),

    DISTORTION(
        "3D & Distortion",
        listOf(
            EffectType.FISHEYE,
            EffectType.RIPPLE,
            EffectType.MIRROR,
            EffectType.KALEIDOSCOPE,
        ),
    ),

    FUTURISTIC(
        "Futuristic & Sci-Fi",
        listOf(
            EffectType.HOLOGRAM,
            EffectType.MATRIX_RAIN,
            EffectType.CYBERPUNK_SCAN,
            EffectType.ENERGY_SHIELD,
            EffectType.PORTAL,
        ),
    ),

    DYNAMIC_PARTICLES(
        "Dynamic & Particles",
        listOf(
            EffectType.FIRE_EFFECT,
            EffectType.LIGHTNING,
            EffectType.PARTICLE_EXPLOSION,
            EffectType.DNA_HELIX,
            EffectType.TIME_WARP,
        ),
    ),

    FACE_BEAUTY(
        "Face & Beauty",
        listOf(
            EffectType.FACE_MORPH,
            EffectType.BEAUTY_MODE,
            EffectType.ANIME_FILTER,
            EffectType.BIG_EYES,
            EffectType.SLIM_FACE,
        ),
    ),

    INTERACTIVE(
        "Interactive & Dynamic",
        listOf(
            EffectType.TIME_WARP_SCAN,
            EffectType.SLOW_ZOOM,
            EffectType.INVISIBLE_EFFECT,
            EffectType.TRIO_EFFECT,
            EffectType.EXPRESSIFY,
        ),
    ),

    SOCIAL_TRENDING(
        "Social Media Trending",
        listOf(
            EffectType.RETRO_CAM,
            EffectType.BLING_SPARKLE,
            EffectType.DISNEY_STYLE,
            EffectType.GREEN_SCREEN,
            EffectType.TONAL_CINEMATIC,
        ),
    ),

    ADVANCED_3D_PARTICLE(
        "Advanced 3D & Particle",
        listOf(
            EffectType.PARTICLE_EXPLOSION_3D,
            EffectType.DEPTH_FIELD_BLUR,
            EffectType.CARD_DANCE_3D,
            EffectType.CAUSTICS_WATER,
            EffectType.VOLUMETRIC_LIGHT,
            EffectType.PARTICLE_STORM,
        ),
    ),

    ADVANCED_DISTORTION(
        "Advanced Distortion & Morphing",
        listOf(
            EffectType.MESH_WARP,
            EffectType.TURBULENT_DISPLACE,
            EffectType.DISPLACEMENT_MAP,
            EffectType.FACE_MORPHING_ADVANCED,
            EffectType.LIQUID_DISTORTION,
            EffectType.GRAVITY_WARP,
        ),
    ),

    INTERACTIVE_ADVANCED(
        "Interactive & Dynamic Advanced",
        listOf(
            EffectType.TIME_WARP_SCAN_ADVANCED,
            EffectType.FLUID_DYNAMICS,
            EffectType.FORCE_FIELD,
            EffectType.DEFLECTOR_BOUNCE,
            EffectType.MOTION_TRACKING,
            EffectType.GESTURE_REACTIVE,
        ),
    ),
}
