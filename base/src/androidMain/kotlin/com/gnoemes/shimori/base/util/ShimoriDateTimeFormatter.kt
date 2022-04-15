//package com.gnoemes.shimori.base.util
//
//
//@Singleton
//class ShimoriDateTimeFormatter @Inject constructor(
//    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
//    @ShortDate private val shortDateFormatter: DateTimeFormatter,
//    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
//    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter
//) {
//
//    fun formatShortTime(temporalAmount: Temporal): String = shortTimeFormatter.format(temporalAmount)
//
//    fun formatShortDate(temporalAmount: Temporal): String = shortDateFormatter.format(temporalAmount)
//
//    fun formatMediumDate(temporalAmount: Temporal): String = mediumDateFormatter.format(temporalAmount)
//
//    fun formatMediumDateTime(temporalAmount: Temporal): String = mediumDateTimeFormatter.format(temporalAmount)
//
//    fun formatRelativeTime(dateTime: OffsetDateTime?): CharSequence? {
//        if (dateTime == null) return null
//
//        val now = OffsetDateTime.now()
//
//        return if (dateTime.isBefore(now)) {
//            if (dateTime.year == now.year || dateTime.isAfter(now.minusDays(1))) {
//                DateUtils.getRelativeTimeSpanString(
//                        dateTime.toInstant().toEpochMilli(),
//                        System.currentTimeMillis(),
//                        DateUtils.MINUTE_IN_MILLIS,
//                        DateUtils.FORMAT_SHOW_DATE
//                ).toString()
//            } else {
//                formatShortDate(dateTime)
//            }
//        } else {
//            if (dateTime.year == now.year || dateTime.isBefore(now.plusDays(7))) {
//                DateUtils.getRelativeTimeSpanString(
//                        dateTime.toInstant().toEpochMilli(),
//                        System.currentTimeMillis(),
//                        DateUtils.MINUTE_IN_MILLIS,
//                        DateUtils.FORMAT_SHOW_DATE
//                ).toString()
//            } else {
//                formatShortDate(dateTime)
//            }
//        }
//    }
//}