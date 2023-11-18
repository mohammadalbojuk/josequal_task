package com.example.josequaltask.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import com.squareup.picasso.Transformation

class ImageRoundCorners : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            source.width, source
                .height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, source.width, source.height)
        val rectF = RectF(rect)
        val roundPx = 50f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(source, rect, rect, paint)
        source.recycle();
        return output
    }

    override fun key(): String {
        return "RoundImage"
    }
}