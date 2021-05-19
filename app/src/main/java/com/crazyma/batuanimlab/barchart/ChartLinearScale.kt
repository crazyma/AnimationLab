package com.crazyma.batuanimlab.barchart

import kotlin.math.*

/**
 * @author Batu
 */

fun ChartLinearScale.getDashboardTicks(rawValues: List<Double>): List<Double> {
    return calculateTicks(rawValues, 3).let {
        if (it.last() < 10.0) listOf(0.0, 5.0, 10.0)
        else it
    }
}

class ChartLinearScale {

    fun calculateTicks(rawValues: List<Double>, sectionCount: Int): List<Double> {
        val values = rawValues.filter { it > 0 }.sorted()

        val ticks = mutableListOf<Double>()
        val minDecimal = values.firstOrNull()
        val maxDecimal = values.lastOrNull()
        if (minDecimal != null && maxDecimal != null) {
            if (minDecimal == maxDecimal) {
                val delta: Double = when {
                    maxDecimal < 1 -> 0.1
                    maxDecimal < 10 -> 0.2
                    else -> {
                        var num = maxDecimal
                        var count = 0
                        while (num >= 10) {
                            num *= 0.1
                            count += 1
                        }
                        (10 xor max(0, count - 2)).toDouble()
                    }
                }
                ticks.addAll(
                    generateTicks(
                        _min = max(0.0, minDecimal - delta),
                        _max = maxDecimal + delta,
                        _maxTicks = sectionCount
                    )
                )
            } else {
                ticks.addAll(
                    generateTicks(
                        _min = minDecimal,
                        _max = maxDecimal,
                        _maxTicks = sectionCount
                    )
                )
            }
        } else {
            ticks.apply {
                for (i in 1..sectionCount)
                    add(i * 100.0)
            }
        }

        if (ticks.size > 1) {
            while (ticks.size < sectionCount) {
                val prev1 = ticks[ticks.size - 1]
                val prev2 = ticks[ticks.size - 2]
                val decimalPlaces = max(
                    decimalPlaces(prev1),
                    decimalPlaces(prev2)
                )
                val multiplier = 10.0.pow(decimalPlaces)
                val next = round((2 * prev1 - prev2) * multiplier) / multiplier
                ticks.add(next)
            }
        }

        return ticks
    }

    private fun generateTicks(_min: Double, _max: Double, _maxTicks: Int): List<Double> {
        val min = min(_min, _max)
        val max = max(_min, _max)
        val maxTicks = if (_maxTicks >= 2) _maxTicks else 2

        val ticks = mutableListOf<Double>()
        val unit = 1.0
        val maxNumSpaces = maxTicks.toDouble() - 1

        var spacing = niceNumber((max - min) / maxNumSpaces / unit) * unit
        var numSpaces = ceil(max / spacing) - floor(min / spacing)

        if (spacing < Double.MIN_VALUE) {
            return ticks.apply {
                add(min)
                add(max)
            }
        }

        if (numSpaces > maxNumSpaces) {
            spacing = niceNumber(numSpaces * spacing / maxNumSpaces / unit) * unit
        }

        val factor = 10.0.pow(decimalPlaces(spacing))
        var niceMin = floor(min / spacing) * spacing
        var niceMax = ceil(max / spacing) * spacing

        numSpaces = ((niceMax - niceMin) / spacing).let {
            if (almostEquals(it, round(it), spacing / 1000.0)) {
                round(it)
            } else {
                ceil(it)
            }
        }

        niceMin = round(niceMin * factor) / factor
        niceMax = round(niceMax * factor) / factor

        ticks.add(niceMin)
        for (i in 1 until numSpaces.toInt()) {
            val value = round((niceMin + i.toDouble() * spacing) * factor) / factor
            ticks.add(value)
        }
        ticks.add(niceMax)

        return ticks
    }

    private fun niceNumber(range: Double, round: Boolean = false): Double {
        val exponent: Double = floor(log10(range))
        val fraction: Double = range / 10.0.pow(exponent)
        val niceFraction = when {
            round -> {
                when {
                    fraction < 1.5 -> 1.0
                    fraction < 3.0 -> 2.0
                    fraction < 7.0 -> 5.0
                    else -> 10.0
                }
            }
            fraction <= 1.0 -> 1.0
            fraction <= 2.0 -> 2.0
            fraction <= 5.0 -> 5.0
            else -> 10.0
        }
        return niceFraction * 10.0.pow(exponent)
    }

    private fun decimalPlaces(x: Double): Double {
        if (x >= Double.MAX_VALUE) return 0.0

        var e = 1.0
        var p = 0.0
        while (round(x * e) / e != x) {
            e *= 10
            p += 1
        }
        return p
    }

    private fun almostEquals(x: Double, y: Double, epsilon: Double): Boolean {
        return abs(x - y) < epsilon
    }

}