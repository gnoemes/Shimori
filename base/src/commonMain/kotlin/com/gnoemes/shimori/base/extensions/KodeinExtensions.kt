package com.gnoemes.shimori.base.extensions

/**
 * Auto resolve a class dependencies by using its constructor reference.
 * The resolution is done at compile time by leveraging inline functions, no reflection is required.
 *
 * Example:
 * ```
 * val myModule = DI.module("myModule") {
 *   bindSingleton { new(::Foo) }
 * }
 * ```
*
* Based on a Koin feature by Marcello Galhardo, adapted for Kodein.
*/
inline fun <reified R> DirectDIAware.new(
    constructor: () -> R,
): R = constructor()

/**
 * @see new
 */
inline fun <reified R, reified T1> DirectDIAware.new(
    constructor: (T1) -> R,
): R = constructor(instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2> DirectDIAware.new(
    constructor: (T1, T2) -> R,
): R = constructor(instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3> DirectDIAware.new(
    constructor: (T1, T2, T3) -> R,
): R = constructor(instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4> DirectDIAware.new(
    constructor: (T1, T2, T3, T4) -> R,
): R = constructor(instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5, T6) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5, T6, T7) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5, T6, T7, T8) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance())

/**
 * @see new
 */
inline fun <reified R, reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9, reified T10> DirectDIAware.new(
    constructor: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R,
): R = constructor(instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance(), instance())
