package com.crazyma.batuanimlab

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader


/**
 * @author Batu
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.apply {
                addPlugin(
                    InspectorFlipperPlugin(
                        this@MyApplication,
                        DescriptorMapping.withDefaults()
                    )
                )
            }.start()
        }
    }
}