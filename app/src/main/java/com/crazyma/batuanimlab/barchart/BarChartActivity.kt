package com.crazyma.batuanimlab.barchart

import android.os.Bundle
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

        binding.barChartView.setupDate()
    }

}