package com.crazyma.batuanimlab.crop_row

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class PartialLeftRoundCrop : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        return createCroppedBitmap(toTransform)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }


    override fun equals(other: Any?): Boolean {
        return other != null && other is PartialLeftRoundCrop
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    companion object {
        private const val ID = "com.bumptech.glide.transformations.FillSpace"
        private val ID_BYTES = ID.toByteArray(charset(STRING_CHARSET_NAME))
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.YELLOW
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private fun createCroppedBitmap(originBitmap: Bitmap): Bitmap {
        val finalBitmap = Bitmap.createBitmap(originBitmap.width, originBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        canvas.drawBitmap(originBitmap, 0f, 0f, null)
        canvas.drawCircle(originBitmap.width * -0.37f, originBitmap.height * 0.5f, originBitmap.width * 0.5f, paint)

        return finalBitmap
    }

}