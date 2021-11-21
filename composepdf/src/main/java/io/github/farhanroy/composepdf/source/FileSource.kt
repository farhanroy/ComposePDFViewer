package io.github.farhanroy.composepdf.source

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

class FileSource(private val file: File) {

    fun getDocument(): MutableList<ImageBitmap>{
        val input = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(input)
        val imageList = mutableListOf<ImageBitmap>()

        for (i in 0 until renderer.pageCount) {
            val page = renderer.openPage(i)
            val bitmap =
                Bitmap.createBitmap(500, 650, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            imageList.add(bitmap.asImageBitmap())
            page.close()
        }
        renderer.close()

        return imageList
    }
}