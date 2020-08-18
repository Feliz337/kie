package cc.foxa.kie

import android.content.Context
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


fun currentDate(): Date {
    val calendar: Calendar = Calendar.getInstance()
    return calendar.time
}

fun getTimeAgo(date: Date?, ctx: Context): String? {
    if (date == null) {
        return null
    }
    val time: Long = date.time
    val curDate: Date = currentDate()
    val now: Long = curDate.getTime()
    if (time > now || time <= 0) {
        return null
    }
    val dim = getTimeDistanceInMinutes(time)
    var timeAgo: String? = null
    timeAgo = if (dim == 0) {
        ctx.resources.getString(R.string.date_util_term_less)
            .toString() + " " + ctx.resources
            .getString(R.string.date_util_term_a) + " " + ctx.resources
            .getString(R.string.date_util_unit_minute)
    } else if (dim == 1) {
        return "1 " + ctx.resources.getString(R.string.date_util_unit_minute)
    } else if (dim in 2..44) {
        dim.toString() + " " + ctx.resources.getString(R.string.date_util_unit_minutes)
    } else if (dim in 45..89) {
        ctx.resources.getString(R.string.date_util_prefix_about)
            .toString() + " " + ctx.resources
            .getString(R.string.date_util_term_an) + " " + ctx.resources
            .getString(R.string.date_util_unit_hour)
    } else if (dim in 90..1439) {
        ctx.getResources().getString(R.string.date_util_prefix_about)
            .toString() + " " + Math.round(dim / 60.toFloat()) + " " + ctx.getResources()
            .getString(R.string.date_util_unit_hours)
    } else if (dim in 1440..2519) {
        "1 " + ctx.getResources().getString(R.string.date_util_unit_day)
    } else if (dim in 2520..43199) {
        (dim / 1440.toFloat()).roundToInt().toString() + " " + ctx.getResources()
            .getString(R.string.date_util_unit_days)
    } else if (dim in 43200..86399) {
        ctx.getResources().getString(R.string.date_util_prefix_about)
            .toString() + " " + ctx.getResources()
            .getString(R.string.date_util_term_a) + " " + ctx.getResources()
            .getString(R.string.date_util_unit_month)
    } else if (dim in 86400..525599) {
        Math.round(dim / 43200.toFloat()).toString() + " " + ctx.getResources()
            .getString(R.string.date_util_unit_months)
    } else if (dim in 525600..655199) {
        ctx.getResources().getString(R.string.date_util_prefix_about)
            .toString() + " " + ctx.getResources()
            .getString(R.string.date_util_term_a) + " " + ctx.getResources()
            .getString(R.string.date_util_unit_year)
    } else if (dim in 655200..914399) {
        ctx.getResources().getString(R.string.date_util_prefix_over)
            .toString() + " " + ctx.getResources()
            .getString(R.string.date_util_term_a) + " " + ctx.getResources()
            .getString(R.string.date_util_unit_year)
    } else if (dim in 914400..1051199) {
        ctx.getResources().getString(R.string.date_util_prefix_almost)
            .toString() + " 2 " + ctx.getResources().getString(R.string.date_util_unit_years)
    } else {
        ctx.getResources().getString(R.string.date_util_prefix_about)
            .toString() + " " + Math.round(dim / 525600.toFloat()) + " " + ctx.getResources()
            .getString(R.string.date_util_unit_years)
    }
    return timeAgo + " " + ctx.getResources().getString(R.string.date_util_suffix)
}

fun getTimeDistanceInMinutes(time: Long): Int {
    val timeDistance: Long = currentDate().getTime() - time
    return (abs(timeDistance) / 1000 / 60.toFloat()).roundToInt()
}