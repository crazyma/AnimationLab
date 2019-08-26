package com.crazyma.batuanimlab.crop_row

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crazyma.batuanimlab.R
import kotlinx.android.synthetic.main.activity_crop_row.*

class CropRowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_row)

        Glide.with(imageView1)
            .load("https://gcs.dcard.tw/reaction/aa0d425f-d530-4478-9a77-fe3aedc79eea1539599257655.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView1)

        Glide.with(imageView2)
            .load("https://gcs.dcard.tw/reaction/514c2569-fd53-4d9d-a415-bf0f88e7329f1539599270972.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView2)

        Glide.with(imageView3)
            .load("https://gcs.dcard.tw/reaction/011ead16-9b83-4729-9fde-c588920c6c2d1539599284385.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView3)

        Glide.with(imageView4)
            .load("https://gcs.dcard.tw/reaction/4b018f48-e184-445f-adf1-fc8e04ba09b91539675779141.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView4)

        Glide.with(imageView5)
            .load("https://gcs.dcard.tw/reaction/e8e6bc5d-41b0-4129-b134-97507523d7ff1539599242687.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView5)


        Glide.with(imageView6)
            .load("https://gcs.dcard.tw/reaction/286f599c-f86a-4932-82f0-f5a06f1eca031539599210825.png")
            .apply(RequestOptions.bitmapTransform(PartialLeftRoundCrop()))
            .into(imageView6)
    }

}