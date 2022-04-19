package com.gnoemes.shimori.ui.utils


import com.gnoemes.shimori.ui.utils.ColorUtils.intFromLstar
import com.gnoemes.shimori.ui.utils.ColorUtils.intFromXyzComponents
import com.gnoemes.shimori.ui.utils.ColorUtils.linearized
import com.gnoemes.shimori.ui.utils.ColorUtils.lstarFromInt
import com.gnoemes.shimori.ui.utils.MathUtils.sanitizeDegrees
import java.util.*


/**
 * A color system built using CAM16 hue and chroma, and L* from L*a*b*.
 *
 *
 * Using L* creates a link between the color system, contrast, and thus accessibility. Contrast
 * ratio depends on relative luminance, or Y in the XYZ color space. L*, or perceptual luminance can
 * be calculated from Y.
 *
 *
 * Unlike Y, L* is linear to human perception, allowing trivial creation of accurate color tones.
 *
 *
 * Unlike contrast ratio, measuring contrast in L* is linear, and simple to calculate. A
 * difference of 40 in HCT tone guarantees a contrast ratio >= 3.0, and a difference of 50
 * guarantees a contrast ratio >= 4.5.
 */
/**
 * HCT, hue, chroma, and tone. A color system that provides a perceptually accurate color
 * measurement system that can also accurately render what colors will appear as in different
 * lighting environments.
 */
internal class Hct private constructor(hue: Float, chroma: Float, tone: Float) {
    private var hue = 0f
    private var chroma = 0f
    private var tone = 0f
    fun getHue(): Float {
        return hue
    }

    fun getChroma(): Float {
        return chroma
    }

    fun getTone(): Float {
        return tone
    }

    fun toInt(): Int {
        return gamutMap(hue, chroma, tone)
    }

    /**
     * Set the hue of this color. Chroma may decrease because chroma has a different maximum for any
     * given hue and tone.
     *
     * @param newHue 0 <= newHue < 360; invalid values are corrected.
     */
    fun setHue(newHue: Float) {
        setInternalState(gamutMap(sanitizeDegrees(newHue), chroma, tone))
    }

    /**
     * Set the chroma of this color. Chroma may decrease because chroma has a different maximum for
     * any given hue and tone.
     *
     * @param newChroma 0 <= newChroma < ?
     */
    fun setChroma(newChroma: Float) {
        setInternalState(gamutMap(hue, newChroma, tone))
    }

    /**
     * Set the tone of this color. Chroma may decrease because chroma has a different maximum for any
     * given hue and tone.
     *
     * @param newTone 0 <= newTone <= 100; invalid valids are corrected.
     */
    fun setTone(newTone: Float) {
        setInternalState(gamutMap(hue, chroma, newTone))
    }

    private fun setInternalState(argb: Int) {
        val cam = Cam16.fromInt(argb)
        val tone = lstarFromInt(argb)
        hue = cam.hue
        chroma = cam.chroma
        this.tone = tone
    }

    companion object {
        /**
         * Create an HCT color from hue, chroma, and tone.
         *
         * @param hue 0 <= hue < 360; invalid values are corrected.
         * @param chroma 0 <= chroma < ?; Informally, colorfulness. The color returned may be lower than
         * the requested chroma. Chroma has a different maximum for any given hue and tone.
         * @param tone 0 <= tone <= 100; invalid values are corrected.
         * @return HCT representation of a color in default viewing conditions.
         */
        fun from(hue: Float, chroma: Float, tone: Float): Hct {
            return Hct(hue, chroma, tone)
        }

        /**
         * Create an HCT color from a color.
         *
         * @param argb ARGB representation of a color.
         * @return HCT representation of a color in default viewing conditions
         */
        fun fromInt(argb: Int): Hct {
            val cam = Cam16.fromInt(argb)
            return Hct(cam.hue, cam.chroma, lstarFromInt(argb))
        }

        /**
         * When the delta between the floor & ceiling of a binary search for maximum chroma at a hue and
         * tone is less than this, the binary search terminates.
         */
        private const val CHROMA_SEARCH_ENDPOINT = 0.4f

        /** The maximum color distance, in CAM16-UCS, between a requested color and the color returned.  */
        private const val DE_MAX = 1.0f

        /** The maximum difference between the requested L* and the L* returned.  */
        private const val DL_MAX = 0.2f

        /**
         * The minimum color distance, in CAM16-UCS, between a requested color and an 'exact' match. This
         * allows the binary search during gamut mapping to terminate much earlier when the error is
         * infinitesimal.
         */
        private const val DE_MAX_ERROR = 0.000000001f

        /**
         * When the delta between the floor & ceiling of a binary search for J, lightness in CAM16, is
         * less than this, the binary search terminates.
         */
        private const val LIGHTNESS_SEARCH_ENDPOINT = 0.01f

        /**
         * @param hue a number, in degrees, representing ex. red, orange, yellow, etc. Ranges from 0 <=
         * hue < 360.
         * @param chroma Informally, colorfulness. Ranges from 0 to roughly 150. Like all perceptually
         * accurate color systems, chroma has a different maximum for any given hue and tone, so the
         * color returned may be lower than the requested chroma.
         * @param tone Lightness. Ranges from 0 to 100.
         * @return ARGB representation of a color in default viewing conditions
         */
        private fun gamutMap(hue: Float, chroma: Float, tone: Float): Int {
            return gamutMapInViewingConditions(hue, chroma, tone, ViewingConditions.DEFAULT)
        }

        /**
         * @param hue CAM16 hue.
         * @param chroma CAM16 chroma.
         * @param tone L*a*b* lightness.
         * @param viewingConditions Information about the environment where the color was observed.
         */
        fun gamutMapInViewingConditions(
            hue: Float, chroma: Float, tone: Float, viewingConditions: ViewingConditions?
        ): Int {
            var hue = hue
            if (chroma < 1.0 || Math.round(tone) <= 0.0 || Math.round(tone) >= 100.0) {
                return intFromLstar(tone)
            }
            hue = sanitizeDegrees(hue)
            var high = chroma
            var mid = chroma
            var low = 0.0f
            var isFirstLoop = true
            var answer: Cam16? = null
            while (Math.abs(low - high) >= CHROMA_SEARCH_ENDPOINT) {
                val possibleAnswer = findCamByJ(hue, mid, tone)
                if (isFirstLoop) {
                    return if (possibleAnswer != null) {
                        possibleAnswer.viewed(viewingConditions!!)
                    } else {
                        isFirstLoop = false
                        mid = low + (high - low) / 2.0f
                        continue
                    }
                }
                if (possibleAnswer == null) {
                    high = mid
                } else {
                    answer = possibleAnswer
                    low = mid
                }
                mid = low + (high - low) / 2.0f
            }
            return answer?.viewed(viewingConditions!!)
                ?: intFromLstar(tone)
        }

        /**
         * @param hue CAM16 hue
         * @param chroma CAM16 chroma
         * @param tone L*a*b* lightness
         * @return CAM16 instance within error tolerance of the provided dimensions, or null.
         */
        private fun findCamByJ(hue: Float, chroma: Float, tone: Float): Cam16? {
            var low = 0.0f
            var high = 100.0f
            var mid = 0.0f
            var bestdL = 1000.0f
            var bestdE = 1000.0f
            var bestCam: Cam16? = null
            while (Math.abs(low - high) > LIGHTNESS_SEARCH_ENDPOINT) {
                mid = low + (high - low) / 2
                val camBeforeClip = Cam16.fromJch(mid, chroma, hue)
                val clipped = camBeforeClip.int
                val clippedLstar = lstarFromInt(clipped)
                val dL = Math.abs(tone - clippedLstar)
                if (dL < DL_MAX) {
                    val camClipped = Cam16.fromInt(clipped)
                    val dE =
                        camClipped.distance(Cam16.fromJch(camClipped.j, camClipped.chroma, hue))
                    if (dE <= DE_MAX && dE <= bestdE) {
                        bestdL = dL
                        bestdE = dE
                        bestCam = camClipped
                    }
                }
                if (bestdL == 0f && bestdE < DE_MAX_ERROR) {
                    break
                }
                if (clippedLstar < tone) {
                    low = mid
                } else {
                    high = mid
                }
            }
            return bestCam
        }
    }

    init {
        setInternalState(gamutMap(hue, chroma, tone))
    }
}

/**
 * CAM16, a color appearance model. Colors are not just defined by their hex code, but rather, a hex
 * code and viewing conditions.
 *
 *
 * CAM16 instances also have coordinates in the CAM16-UCS space, called J*, a*, b*, or jstar,
 * astar, bstar in code. CAM16-UCS is included in the CAM16 specification, and should be used when
 * measuring distances between colors.
 *
 *
 * In traditional color spaces, a color can be identified solely by the observer's measurement of
 * the color. Color appearance models such as CAM16 also use information about the environment where
 * the color was observed, known as the viewing conditions.
 *
 *
 * For example, white under the traditional assumption of a midday sun white point is accurately
 * measured as a slightly chromatic blue by CAM16. (roughly, hue 203, chroma 3, lightness 100)
 */
internal class Cam16 private constructor(
    /** Hue in CAM16  */
    // CAM16 color dimensions, see getters for documentation.
    val hue: Float,
    /** Chroma in CAM16  */
    val chroma: Float,
    j: Float,
    q: Float,
    m: Float,
    s: Float,
    jstar: Float,
    astar: Float,
    bstar: Float
) {

    /** Lightness in CAM16  */
    val j: Float

    /**
     * Brightness in CAM16.
     *
     *
     * Prefer lightness, brightness is an absolute quantity. For example, a sheet of white paper is
     * much brighter viewed in sunlight than in indoor light, but it is the lightest object under any
     * lighting.
     */
    val q: Float

    /**
     * Colorfulness in CAM16.
     *
     *
     * Prefer chroma, colorfulness is an absolute quantity. For example, a yellow toy car is much
     * more colorful outside than inside, but it has the same chroma in both environments.
     */
    val m: Float

    /**
     * Saturation in CAM16.
     *
     *
     * Colorfulness in proportion to brightness. Prefer chroma, saturation measures colorfulness
     * relative to the color's own brightness, where chroma is colorfulness relative to white.
     */
    val s: Float

    /** Lightness coordinate in CAM16-UCS  */
    // Coordinates in UCS space. Used to determine color distance, like delta E equations in L*a*b*.
    val jStar: Float

    /** a* coordinate in CAM16-UCS  */
    val aStar: Float

    /** b* coordinate in CAM16-UCS  */
    val bStar: Float

    /**
     * CAM16 instances also have coordinates in the CAM16-UCS space, called J*, a*, b*, or jstar,
     * astar, bstar in code. CAM16-UCS is included in the CAM16 specification, and is used to measure
     * distances between colors.
     */
    fun distance(other: Cam16): Float {
        val dJ = jStar - other.jStar
        val dA = aStar - other.aStar
        val dB = bStar - other.bStar
        val dEPrime = Math.sqrt((dJ * dJ + dA * dA + dB * dB).toDouble())
        val dE = 1.41 * Math.pow(dEPrime, 0.63)
        return dE.toFloat()
    }

    /**
     * ARGB representation of the color. Assumes the color was viewed in default viewing conditions,
     * which are near-identical to the default viewing conditions for sRGB.
     */
    val int: Int
        get() = viewed(ViewingConditions.DEFAULT)

    /**
     * ARGB representation of the color, in defined viewing conditions.
     *
     * @param viewingConditions Information about the environment where the color will be viewed.
     * @return ARGB representation of color
     */
    fun viewed(viewingConditions: ViewingConditions): Int {
        val alpha =
            if (chroma.toDouble() == 0.0 || j.toDouble() == 0.0) 0.0f else chroma / Math.sqrt(
                j / 100.0
            ).toFloat()
        val t = Math.pow(
            alpha / Math.pow(1.64 - Math.pow(0.29, viewingConditions.n.toDouble()), 0.73), 1.0 / 0.9
        )
            .toFloat()
        val hRad = hue * Math.PI.toFloat() / 180.0f
        val eHue = 0.25f * (Math.cos(hRad + 2.0) + 3.8).toFloat()
        val ac = (viewingConditions.aw
                * Math.pow(j / 100.0, 1.0 / viewingConditions.c / viewingConditions.z).toFloat())
        val p1 = eHue * (50000.0f / 13.0f) * viewingConditions.nc * viewingConditions.ncb
        val p2 = ac / viewingConditions.nbb
        val hSin = Math.sin(hRad.toDouble()).toFloat()
        val hCos = Math.cos(hRad.toDouble()).toFloat()
        val gamma = 23.0f * (p2 + 0.305f) * t / (23.0f * p1 + 11.0f * t * hCos + 108.0f * t * hSin)
        val a = gamma * hCos
        val b = gamma * hSin
        val rA = (460.0f * p2 + 451.0f * a + 288.0f * b) / 1403.0f
        val gA = (460.0f * p2 - 891.0f * a - 261.0f * b) / 1403.0f
        val bA = (460.0f * p2 - 220.0f * a - 6300.0f * b) / 1403.0f
        val rCBase = Math.max(0.0, 27.13 * Math.abs(rA) / (400.0 - Math.abs(rA)))
            .toFloat()
        val rC = (Math.signum(rA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(rCBase.toDouble(), 1.0 / 0.42).toFloat())
        val gCBase = Math.max(0.0, 27.13 * Math.abs(gA) / (400.0 - Math.abs(gA)))
            .toFloat()
        val gC = (Math.signum(gA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(gCBase.toDouble(), 1.0 / 0.42).toFloat())
        val bCBase = Math.max(0.0, 27.13 * Math.abs(bA) / (400.0 - Math.abs(bA)))
            .toFloat()
        val bC = (Math.signum(bA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(bCBase.toDouble(), 1.0 / 0.42).toFloat())
        val rF = rC / viewingConditions.rgbD[0]
        val gF = gC / viewingConditions.rgbD[1]
        val bF = bC / viewingConditions.rgbD[2]
        val matrix = CAM16RGB_TO_XYZ
        val x = rF * matrix[0][0] + gF * matrix[0][1] + bF * matrix[0][2]
        val y = rF * matrix[1][0] + gF * matrix[1][1] + bF * matrix[1][2]
        val z = rF * matrix[2][0] + gF * matrix[2][1] + bF * matrix[2][2]
        return intFromXyzComponents(x, y, z)
    }

    companion object {
        // Transforms XYZ color space coordinates to 'cone'/'RGB' responses in CAM16.
        val XYZ_TO_CAM16RGB = arrayOf(
            floatArrayOf(0.401288f, 0.650173f, -0.051461f),
            floatArrayOf(-0.250268f, 1.204414f, 0.045854f),
            floatArrayOf(-0.002079f, 0.048952f, 0.953127f)
        )

        // Transforms 'cone'/'RGB' responses in CAM16 to XYZ color space coordinates.
        val CAM16RGB_TO_XYZ = arrayOf(
            floatArrayOf(1.8620678f, -1.0112547f, 0.14918678f),
            floatArrayOf(0.38752654f, 0.62144744f, -0.00897398f),
            floatArrayOf(-0.01584150f, -0.03412294f, 1.0499644f)
        )

        /**
         * Create a CAM16 color from a color, assuming the color was viewed in default viewing conditions.
         *
         * @param argb ARGB representation of a color.
         */
        fun fromInt(argb: Int): Cam16 {
            return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT)
        }

        /**
         * Create a CAM16 color from a color in defined viewing conditions.
         *
         * @param argb ARGB representation of a color.
         * @param viewingConditions Information about the environment where the color was observed.
         */
        // The RGB => XYZ conversion matrix elements are derived scientific constants. While the values
        // may differ at runtime due to floating point imprecision, keeping the values the same, and
        // accurate, across implementations takes precedence.
        fun fromIntInViewingConditions(argb: Int, viewingConditions: ViewingConditions): Cam16 {
            // Transform ARGB int to XYZ
            val red = argb and 0x00ff0000 shr 16
            val green = argb and 0x0000ff00 shr 8
            val blue = argb and 0x000000ff
            val redL = linearized(red / 255f) * 100f
            val greenL = linearized(green / 255f) * 100f
            val blueL = linearized(blue / 255f) * 100f
            val x = 0.41233895f * redL + 0.35762064f * greenL + 0.18051042f * blueL
            val y = 0.2126f * redL + 0.7152f * greenL + 0.0722f * blueL
            val z = 0.01932141f * redL + 0.11916382f * greenL + 0.95034478f * blueL

            // Transform XYZ to 'cone'/'rgb' responses
            val matrix = XYZ_TO_CAM16RGB
            val rT = x * matrix[0][0] + y * matrix[0][1] + z * matrix[0][2]
            val gT = x * matrix[1][0] + y * matrix[1][1] + z * matrix[1][2]
            val bT = x * matrix[2][0] + y * matrix[2][1] + z * matrix[2][2]

            // Discount illuminant
            val rD = viewingConditions.rgbD[0] * rT
            val gD = viewingConditions.rgbD[1] * gT
            val bD = viewingConditions.rgbD[2] * bT

            // Chromatic adaptation
            val rAF =
                Math.pow(viewingConditions.fl * Math.abs(rD) / 100.0, 0.42).toFloat()
            val gAF =
                Math.pow(viewingConditions.fl * Math.abs(gD) / 100.0, 0.42).toFloat()
            val bAF =
                Math.pow(viewingConditions.fl * Math.abs(bD) / 100.0, 0.42).toFloat()
            val rA = Math.signum(rD) * 400.0f * rAF / (rAF + 27.13f)
            val gA = Math.signum(gD) * 400.0f * gAF / (gAF + 27.13f)
            val bA = Math.signum(bD) * 400.0f * bAF / (bAF + 27.13f)

            // redness-greenness
            val a = (11.0 * rA + -12.0 * gA + bA).toFloat() / 11.0f
            // yellowness-blueness
            val b = (rA + gA - 2.0 * bA).toFloat() / 9.0f

            // auxiliary components
            val u = (20.0f * rA + 20.0f * gA + 21.0f * bA) / 20.0f
            val p2 = (40.0f * rA + 20.0f * gA + bA) / 20.0f

            // hue
            val atan2 = Math.atan2(b.toDouble(), a.toDouble()).toFloat()
            val atanDegrees = atan2 * 180.0f / Math.PI.toFloat()
            val hue =
                if (atanDegrees < 0) atanDegrees + 360.0f else if (atanDegrees >= 360) atanDegrees - 360.0f else atanDegrees
            val hueRadians = hue * Math.PI.toFloat() / 180.0f

            // achromatic response to color
            val ac = p2 * viewingConditions.nbb

            // CAM16 lightness and brightness
            val j = (100.0f
                    * Math.pow(
                (
                        ac / viewingConditions.aw).toDouble(), (
                        viewingConditions.c * viewingConditions.z).toDouble()
            ).toFloat())
            val q = ((4.0f
                    / viewingConditions.c) * Math.sqrt((j / 100.0f).toDouble()).toFloat()
                    * (viewingConditions.aw + 4.0f)
                    * viewingConditions.flRoot)

            // CAM16 chroma, colorfulness, and saturation.
            val huePrime = if (hue < 20.14) hue + 360 else hue
            val eHue = 0.25f * (Math.cos(Math.toRadians(huePrime.toDouble()) + 2.0) + 3.8).toFloat()
            val p1 = 50000.0f / 13.0f * eHue * viewingConditions.nc * viewingConditions.ncb
            val t = p1 * Math.hypot(a.toDouble(), b.toDouble()).toFloat() / (u + 0.305f)
            val alpha = (Math.pow(1.64 - Math.pow(0.29, viewingConditions.n.toDouble()), 0.73)
                .toFloat()
                    * Math.pow(t.toDouble(), 0.9).toFloat())
            // CAM16 chroma, colorfulness, saturation
            val c = alpha * Math.sqrt(j / 100.0).toFloat()
            val m = c * viewingConditions.flRoot
            val s = (50.0f
                    * Math.sqrt((alpha * viewingConditions.c / (viewingConditions.aw + 4.0f)).toDouble())
                .toFloat())

            // CAM16-UCS components
            val jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j)
            val mstar = 1.0f / 0.0228f * Math.log1p((0.0228f * m).toDouble())
                .toFloat()
            val astar = mstar * Math.cos(hueRadians.toDouble()).toFloat()
            val bstar = mstar * Math.sin(hueRadians.toDouble()).toFloat()
            return Cam16(hue, c, j, q, m, s, jstar, astar, bstar)
        }

        /**
         * @param j CAM16 lightness
         * @param c CAM16 chroma
         * @param h CAM16 hue
         */
        fun fromJch(j: Float, c: Float, h: Float): Cam16 {
            return fromJchInViewingConditions(j, c, h, ViewingConditions.DEFAULT)
        }

        /**
         * @param j CAM16 lightness
         * @param c CAM16 chroma
         * @param h CAM16 hue
         * @param viewingConditions Information about the environment where the color was observed.
         */
        private fun fromJchInViewingConditions(
            j: Float, c: Float, h: Float, viewingConditions: ViewingConditions
        ): Cam16 {
            val q = ((4.0f
                    / viewingConditions.c) * Math.sqrt(j / 100.0).toFloat()
                    * (viewingConditions.aw + 4.0f)
                    * viewingConditions.flRoot)
            val m = c * viewingConditions.flRoot
            val alpha = c / Math.sqrt(j / 100.0).toFloat()
            val s = (50.0f
                    * Math.sqrt((alpha * viewingConditions.c / (viewingConditions.aw + 4.0f)).toDouble())
                .toFloat())
            val hueRadians = h * Math.PI.toFloat() / 180.0f
            val jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j)
            val mstar = 1.0f / 0.0228f * Math.log1p(0.0228 * m).toFloat()
            val astar = mstar * Math.cos(hueRadians.toDouble()).toFloat()
            val bstar = mstar * Math.sin(hueRadians.toDouble()).toFloat()
            return Cam16(h, c, j, q, m, s, jstar, astar, bstar)
        }

        /**
         * Create a CAM16 color from CAM16-UCS coordinates.
         *
         * @param jstar CAM16-UCS lightness.
         * @param astar CAM16-UCS a dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the Y
         * axis.
         * @param bstar CAM16-UCS b dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the X
         * axis.
         */
        fun fromUcs(jstar: Float, astar: Float, bstar: Float): Cam16 {
            return fromUcsInViewingConditions(jstar, astar, bstar, ViewingConditions.DEFAULT)
        }

        /**
         * Create a CAM16 color from CAM16-UCS coordinates in defined viewing conditions.
         *
         * @param jstar CAM16-UCS lightness.
         * @param astar CAM16-UCS a dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the Y
         * axis.
         * @param bstar CAM16-UCS b dimension. Like a* in L*a*b*, it is a Cartesian coordinate on the X
         * axis.
         * @param viewingConditions Information about the environment where the color was observed.
         */
        fun fromUcsInViewingConditions(
            jstar: Float, astar: Float, bstar: Float, viewingConditions: ViewingConditions
        ): Cam16 {
            val m = Math.hypot(astar.toDouble(), bstar.toDouble())
            val m2 = Math.expm1(m * 0.0228f) / 0.0228f
            val c = m2 / viewingConditions.flRoot
            var h = Math.atan2(bstar.toDouble(), astar.toDouble()) * (180.0f / Math.PI)
            if (h < 0.0) {
                h += 360.0
            }
            val j = jstar / (1f - (jstar - 100f) * 0.007f)
            return fromJchInViewingConditions(
                j,
                c.toFloat(), h.toFloat(), viewingConditions
            )
        }
    }

    /**
     * All of the CAM16 dimensions can be calculated from 3 of the dimensions, in the following
     * combinations: - {j or q} and {c, m, or s} and hue - jstar, astar, bstar Prefer using a static
     * method that constructs from 3 of those dimensions. This constructor is intended for those
     * methods to use to return all possible dimensions.
     *
     * @param hue for example, red, orange, yellow, green, etc.
     * @param chroma informally, colorfulness / color intensity. like saturation in HSL, except
     * perceptually accurate.
     * @param j lightness
     * @param q brightness; ratio of lightness to white point's lightness
     * @param m colorfulness
     * @param s saturation; ratio of chroma to white point's chroma
     * @param jstar CAM16-UCS J coordinate
     * @param astar CAM16-UCS a coordinate
     * @param bstar CAM16-UCS b coordinate
     */
    init {
        this.j = j
        this.q = q
        this.m = m
        this.s = s
        jStar = jstar
        aStar = astar
        bStar = bstar
    }
}

/**
 * Utility methods for color science constants and color space conversions that aren't HCT or CAM16.
 */
internal object ColorUtils {
    private val WHITE_POINT_D65 = floatArrayOf(95.047f, 100.0f, 108.883f)

    /** Standard white point; white on a sunny day.  */
    fun whitePointD65(): FloatArray {
        return Arrays.copyOf(WHITE_POINT_D65, 3)
    }

    /**
     * The red channel of the color, from 0 to 255.
     *
     * @param argb ARGB representation of a color.
     */
    fun redFromInt(argb: Int): Int {
        return argb and 0x00ff0000 shr 16
    }

    /**
     * The green channel of the color, from 0 to 255.
     *
     * @param argb ARGB representation of a color.
     */
    fun greenFromInt(argb: Int): Int {
        return argb and 0x0000ff00 shr 8
    }

    /**
     * The blue channel of the color, from 0 to 255.
     *
     * @param argb ARGB representation of a color.
     */
    fun blueFromInt(argb: Int): Int {
        return argb and 0x000000ff
    }

    /**
     * L*, from L*a*b*, coordinate of the color.
     *
     * @param argb ARGB representation of a color.
     */
    fun lstarFromInt(argb: Int): Float {
        return labFromInt(argb)[0].toFloat()
    }

    /**
     * Hex string representing color, ex. #ff0000 for red.
     *
     * @param argb ARGB representation of a color.
     */
    fun hexFromInt(argb: Int): String {
        val red = redFromInt(argb)
        val blue = blueFromInt(argb)
        val green = greenFromInt(argb)
        return String.format("#%02x%02x%02x", red, green, blue)
    }

    /**
     * Color's coordinates in the XYZ color space.
     *
     * @param argb ARGB representation of a color.
     */
    // The RGB => XYZ conversion matrix elements are derived scientific constants. While the values
    // may differ at runtime due to floating point imprecision, keeping the values the same, and
    // accurate, across implementations takes precedence.
    fun xyzFromInt(argb: Int): FloatArray {
        val r = linearized(redFromInt(argb) / 255f) * 100f
        val g = linearized(greenFromInt(argb) / 255f) * 100f
        val b = linearized(blueFromInt(argb) / 255f) * 100f
        val x = 0.41233894f * r + 0.35762064f * g + 0.18051042f * b
        val y = 0.2126f * r + 0.7152f * g + 0.0722f * b
        val z = 0.01932141f * r + 0.11916382f * g + 0.95034478f * b
        return floatArrayOf(x, y, z)
    }

    /**
     * Create an ARGB color from R, G, and B coordinates. Coordinates are expected to be >= 0 and <=
     * 255.
     */
    fun intFromRgb(r: Int, g: Int, b: Int): Int {
        return 255 shl 24 or (r and 0x0ff shl 16) or (g and 0x0ff shl 8) or (b and 0x0ff) ushr 0
    }

    /**
     * Color's coordinates in the L*a*b* color space.
     *
     * @param argb ARGB representation of a color.
     */
    fun labFromInt(argb: Int): DoubleArray {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        val xyz = xyzFromInt(argb)
        val yNormalized = (xyz[1] / WHITE_POINT_D65[1]).toDouble()
        val fy: Double
        fy = if (yNormalized > e) {
            Math.cbrt(yNormalized)
        } else {
            (kappa * yNormalized + 16) / 116
        }
        val xNormalized = (xyz[0] / WHITE_POINT_D65[0]).toDouble()
        val fx: Double
        fx = if (xNormalized > e) {
            Math.cbrt(xNormalized)
        } else {
            (kappa * xNormalized + 16) / 116
        }
        val zNormalized = (xyz[2] / WHITE_POINT_D65[2]).toDouble()
        val fz: Double
        fz = if (zNormalized > e) {
            Math.cbrt(zNormalized)
        } else {
            (kappa * zNormalized + 16) / 116
        }
        val l = 116.0 * fy - 16
        val a = 500.0 * (fx - fy)
        val b = 200.0 * (fy - fz)
        return doubleArrayOf(l, a, b)
    }

    /** ARGB representation of color in the L*a*b* color space.  */
    fun intFromLab(l: Double, a: Double, b: Double): Int {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        val ke = 8.0
        val fy = (l + 16.0) / 116.0
        val fx = a / 500.0 + fy
        val fz = fy - b / 200.0
        val fx3 = fx * fx * fx
        val xNormalized = if (fx3 > e) fx3 else (116.0 * fx - 16.0) / kappa
        val yNormalized = if (l > ke) fy * fy * fy else l / kappa
        val fz3 = fz * fz * fz
        val zNormalized = if (fz3 > e) fz3 else (116.0 * fz - 16.0) / kappa
        val x = xNormalized * WHITE_POINT_D65[0]
        val y = yNormalized * WHITE_POINT_D65[1]
        val z = zNormalized * WHITE_POINT_D65[2]
        return intFromXyzComponents(
            x.toFloat(),
            y.toFloat(), z.toFloat()
        )
    }

    /** ARGB representation of color in the XYZ color space.  */
    fun intFromXyzComponents(x: Float, y: Float, z: Float): Int {
        var x = x
        var y = y
        var z = z
        x = x / 100f
        y = y / 100f
        z = z / 100f
        val rL = x * 3.2406f + y * -1.5372f + z * -0.4986f
        val gL = x * -0.9689f + y * 1.8758f + z * 0.0415f
        val bL = x * 0.0557f + y * -0.204f + z * 1.057f
        val r = delinearized(rL)
        val g = delinearized(gL)
        val b = delinearized(bL)
        val rInt = Math.max(Math.min(255, Math.round(r * 255)), 0)
        val gInt = Math.max(Math.min(255, Math.round(g * 255)), 0)
        val bInt = Math.max(Math.min(255, Math.round(b * 255)), 0)
        return intFromRgb(rInt, gInt, bInt)
    }

    /** ARGB representation of color in the XYZ color space.  */
    fun intFromXyz(xyz: FloatArray): Int {
        return intFromXyzComponents(
            xyz[0], xyz[1],
            xyz[2]
        )
    }

    /**
     * ARGB representation of grayscale (0 chroma) color with lightness matching L*
     *
     * @param lstar L* in L*a*b*
     */
    fun intFromLstar(lstar: Float): Int {
        val fy = (lstar + 16.0f) / 116.0f
        val kappa = 24389f / 27f
        val epsilon = 216f / 24389f
        val cubeExceedEpsilon = fy * fy * fy > epsilon
        val lExceedsEpsilonKappa = lstar > 8.0f
        val y = if (lExceedsEpsilonKappa) fy * fy * fy else lstar / kappa
        val x = if (cubeExceedEpsilon) fy * fy * fy else (116f * fy - 16f) / kappa
        val z = if (cubeExceedEpsilon) fy * fy * fy else (116f * fy - 16f) / kappa
        val xyz = floatArrayOf(
            x * WHITE_POINT_D65[0],
            y * WHITE_POINT_D65[1],
            z * WHITE_POINT_D65[2]
        )
        return intFromXyz(xyz)
    }

    /**
     * L* in L*a*b* and Y in XYZ measure the same quantity, luminance. L* measures perceptual
     * luminance, a linear scale. Y in XYZ measures relative luminance, a logarithmic scale.
     *
     * @param lstar L* in L*a*b*
     * @return Y in XYZ
     */
    fun yFromLstar(lstar: Float): Float {
        val ke = 8.0f
        return if (lstar > ke) {
            Math.pow((lstar + 16.0) / 116.0, 3.0).toFloat() * 100f
        } else {
            lstar / (24389f / 27f) * 100f
        }
    }

    /**
     * Convert a normalized RGB channel to a normalized linear RGB channel.
     *
     * @param rgb 0.0 <= rgb <= 1.0, represents R/G/B channel
     */
    fun linearized(rgb: Float): Float {
        return if (rgb <= 0.04045f) {
            rgb / 12.92f
        } else {
            Math.pow(((rgb + 0.055f) / 1.055f).toDouble(), 2.4).toFloat()
        }
    }

    /**
     * Convert a normalized linear RGB channel to a normalized RGB channel.
     *
     * @param rgb 0.0 <= rgb <= 1.0, represents linear R/G/B channel
     */
    fun delinearized(rgb: Float): Float {
        return if (rgb <= 0.0031308f) {
            rgb * 12.92f
        } else {
            1.055f * Math.pow(rgb.toDouble(), (1.0f / 2.4f).toDouble()).toFloat() - 0.055f
        }
    }
}

/** Utility methods for mathematical operations.  */
internal object MathUtils {
    /** Ensure min <= input <= max  */
    fun clamp(min: Float, max: Float, input: Float): Float {
        return Math.min(Math.max(input, min), max)
    }

    /** Linearly interpolate from start to stop, by amount (0.0 <= amount <= 1.0)  */
    fun lerp(start: Float, stop: Float, amount: Float): Float {
        return (1.0f - amount) * start + amount * stop
    }

    /** Determine the shortest angle between two angles, measured in degrees.  */
    fun differenceDegrees(a: Float, b: Float): Float {
        return 180f - Math.abs(Math.abs(a - b) - 180f)
    }

    /** Ensure 0 <= degrees < 360  */
    fun sanitizeDegrees(degrees: Float): Float {
        return if (degrees < 0f) {
            degrees % 360.0f + 360f
        } else if (degrees >= 360.0f) {
            degrees % 360.0f
        } else {
            degrees
        }
    }

    /** Ensure 0 <= degrees < 360  */
    fun sanitizeDegrees(degrees: Int): Int {
        return if (degrees < 0) {
            degrees % 360 + 360
        } else if (degrees >= 360) {
            degrees % 360
        } else {
            degrees
        }
    }

    /** Convert radians to degrees.  */
    fun toDegrees(radians: Float): Float {
        return radians * 180.0f / Math.PI.toFloat()
    }

    /** Convert degrees to radians.  */
    fun toRadians(degrees: Float): Float {
        return degrees / 180.0f * Math.PI.toFloat()
    }
}

/**
 * In traditional color spaces, a color can be identified solely by the observer's measurement of
 * the color. Color appearance models such as CAM16 also use information about the environment where
 * the color was observed, known as the viewing conditions.
 *
 *
 * For example, white under the traditional assumption of a midday sun white point is accurately
 * measured as a slightly chromatic blue by CAM16. (roughly, hue 203, chroma 3, lightness 100)
 *
 *
 * This class caches intermediate values of the CAM16 conversion process that depend only on
 * viewing conditions, enabling speed ups.
 */
internal class ViewingConditions
/**
 * Parameters are intermediate values of the CAM16 conversion process. Their names are shorthand
 * for technical color science terminology, this class would not benefit from documenting them
 * individually. A brief overview is available in the CAM16 specification, and a complete overview
 * requires a color science textbook, such as Fairchild's Color Appearance Models.
 */ private constructor(
    val n: Float,
    val aw: Float,
    val nbb: Float,
    val ncb: Float,
    val c: Float,
    val nc: Float,
    val rgbD: FloatArray,
    val fl: Float,
    val flRoot: Float,
    val z: Float
) {
    companion object {
        /** sRGB-like viewing conditions.  */
        val DEFAULT = make(
            ColorUtils.whitePointD65(),
            (200.0f / Math.PI * ColorUtils.yFromLstar(50.0f) / 100f).toFloat(),
            50.0f,
            2.0f,
            false
        )

        /**
         * Create ViewingConditions from a simple, physically relevant, set of parameters.
         *
         * @param whitePoint White point, measured in the XYZ color space. default = D65, or sunny day
         * afternoon
         * @param adaptingLuminance The luminance of the adapting field. Informally, how bright it is in
         * the room where the color is viewed. Can be calculated from lux by multiplying lux by
         * 0.0586. default = 11.72, or 200 lux.
         * @param backgroundLstar The lightness of the area surrounding the color. measured by L* in
         * L*a*b*. default = 50.0
         * @param surround A general description of the lighting surrounding the color. 0 is pitch dark,
         * like watching a movie in a theater. 1.0 is a dimly light room, like watching TV at home at
         * night. 2.0 means there is no difference between the lighting on the color and around it.
         * default = 2.0
         * @param discountingIlluminant Whether the eye accounts for the tint of the ambient lighting,
         * such as knowing an apple is still red in green light. default = false, the eye does not
         * perform this process on self-luminous objects like displays.
         */
        fun make(
            whitePoint: FloatArray,
            adaptingLuminance: Float,
            backgroundLstar: Float,
            surround: Float,
            discountingIlluminant: Boolean
        ): ViewingConditions {
            // Transform white point XYZ to 'cone'/'rgb' responses
            val matrix = Cam16.XYZ_TO_CAM16RGB
            val rW =
                whitePoint[0] * matrix[0][0] + whitePoint[1] * matrix[0][1] + whitePoint[2] * matrix[0][2]
            val gW =
                whitePoint[0] * matrix[1][0] + whitePoint[1] * matrix[1][1] + whitePoint[2] * matrix[1][2]
            val bW =
                whitePoint[0] * matrix[2][0] + whitePoint[1] * matrix[2][1] + whitePoint[2] * matrix[2][2]
            val f = 0.8f + surround / 10.0f
            val c = if (f >= 0.9) MathUtils.lerp(
                0.59f, 0.69f,
                (f - 0.9f) * 10.0f
            ) else MathUtils.lerp(
                0.525f, 0.59f,
                (f - 0.8f) * 10.0f
            )
            var d =
                if (discountingIlluminant) 1.0f else f * (1.0f - 1.0f / 3.6f * Math.exp(((-adaptingLuminance - 42.0f) / 92.0f).toDouble())
                    .toFloat())
            d = if (d > 1.0) 1.0f else if (d < 0.0) 0.0f else d
            val rgbD = floatArrayOf(
                d * (100.0f / rW) + 1.0f - d,
                d * (100.0f / gW) + 1.0f - d,
                d * (100.0f / bW) + 1.0f - d
            )
            val k = 1.0f / (5.0f * adaptingLuminance + 1.0f)
            val k4 = k * k * k * k
            val k4F = 1.0f - k4
            val fl = k4 * adaptingLuminance + 0.1f * k4F * k4F * Math.cbrt(5.0 * adaptingLuminance)
                .toFloat()
            val n = ColorUtils.yFromLstar(backgroundLstar) / whitePoint[1]
            val z = 1.48f + Math.sqrt(n.toDouble()).toFloat()
            val nbb = 0.725f / Math.pow(n.toDouble(), 0.2).toFloat()
            val rgbAFactors = floatArrayOf(
                Math.pow(fl * rgbD[0] * rW / 100.0, 0.42).toFloat(),
                Math.pow(fl * rgbD[1] * gW / 100.0, 0.42).toFloat(),
                Math.pow(fl * rgbD[2] * bW / 100.0, 0.42).toFloat()
            )
            val rgbA = floatArrayOf(
                400.0f * rgbAFactors[0] / (rgbAFactors[0] + 27.13f),
                400.0f * rgbAFactors[1] / (rgbAFactors[1] + 27.13f),
                400.0f * rgbAFactors[2] / (rgbAFactors[2] + 27.13f)
            )
            val aw = (2.0f * rgbA[0] + rgbA[1] + 0.05f * rgbA[2]) * nbb
            return ViewingConditions(
                n, aw, nbb, nbb, c, f, rgbD, fl,
                Math.pow(fl.toDouble(), 0.25).toFloat(), z
            )
        }
    }
}