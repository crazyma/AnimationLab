package com.crazyma.batuanimlab.barchart

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.databinding.ActivityBarChartBinding

/**
 * @author Batu
 */
class BarChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBarChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.barChartLayout.setupDate()
        Handler().postDelayed({
            val list = getData(6)
            binding.barChartView.barDataList = list
        }, 1000)
    }

    private fun getData(flag : Int): List<BarData>{
        return when(flag){
            1 -> listOf(
                BarData("9/29", 322032),
                BarData("9/30", 92032),
                BarData("10/1", 26422),
                BarData("10/2", 2666662),
                BarData("10/3", 266666),
                BarData("10/4", 599662),
                BarData("10/5", 242)
            )

            2 -> listOf(
                BarData("9/29", 3),
                BarData("9/30", 9),
                BarData("10/1", 2),
                BarData("10/2", 6),
                BarData("10/3", 0),
                BarData("10/4", 5),
                BarData("10/5", 1)
            )

            3 -> listOf(
                BarData("9/29", 3),
                BarData("9/30", 9),
                BarData("10/1", 2),
                BarData("10/2", 6),
                BarData("10/3", 10),
                BarData("10/4", 5),
                BarData("10/5", 1)
            )

            4 -> listOf(
                BarData("9/29", 3),
                BarData("9/30", 9),
                BarData("10/1", 2),
                BarData("10/2", 6),
                BarData("10/3", 11),
                BarData("10/4", 5),
                BarData("10/5", 1)
            )

            5 -> listOf(
                BarData("9/29", 0),
                BarData("9/30", 0),
                BarData("10/1", 0),
                BarData("10/2", 0),
                BarData("10/3", 1),
                BarData("10/4", 3),
                BarData("10/5", 1)
            )

            6 -> listOf(
                BarData("9/29", 0),// 0
                BarData("9/30", 0),
                BarData("10/1", 0),
                BarData("10/3", 1),
                BarData("10/4", 3),
                BarData("9/29", 3),
                BarData("9/30", 9),
                BarData("10/1", 2),// 7
                BarData("10/2", 6),
                BarData("10/3", 10),
                BarData("10/4", 5),
                BarData("9/29", 3),
                BarData("9/30", 9),
                BarData("10/1", 2),
                BarData("10/2", 6),//14
                BarData("10/3", 11),
                BarData("10/4", 5),
                BarData("10/3", 1),
                BarData("10/4", 3),
                BarData("9/29", 0),
                BarData("9/30", 0),
                BarData("10/1", 0),//21
                BarData("10/2", 0),
                BarData("10/3", 1),
                BarData("10/4", 3),
                BarData("9/29", 0),
                BarData("9/30", 0),
                BarData("10/1", 0),
                BarData("10/3", 1),
                BarData("10/4", 3)// 29
            )
            else -> listOf()
        }
    }

}