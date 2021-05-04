package com.crazyma.batuanimlab.adaptive_tab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crazyma.batuanimlab.databinding.ActivityAdaptiveTabBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 * @author Batu
 */
class AdaptiveTabActivity : AppCompatActivity() {

    private val binding: ActivityAdaptiveTabBinding by lazy {
        ActivityAdaptiveTabBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter()
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Tab ${position + 1}"
        }.attach()

    }
}