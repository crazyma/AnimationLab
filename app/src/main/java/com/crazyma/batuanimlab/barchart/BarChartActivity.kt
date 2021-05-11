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
            val list = listOf(
                BarData("9/29", 322032),
                BarData("9/30", 92032),
                BarData("10/1", 26422),
                BarData("10/2", 2666662),
                BarData("10/3", 266666),
                BarData("10/4", 599662),
                BarData("10/5", 242)
            )
            binding.barChartView.barDataList = list
        }, 1000)
    }

}