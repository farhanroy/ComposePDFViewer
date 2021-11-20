package com.example.pdfcompose

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import java.io.File

@Composable
fun HomeScreen() {
    var imageList by remember { mutableStateOf<ImageBitmap?>(null) }

    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val launcherPDF = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        pdfUri = uri
    }
    
    Button(onClick = { launcherPDF.launch("application/pdf") }) {}

    val context = LocalContext.current

    if (pdfUri != null) {
        val file = File(ContentUriUtil.getFilePath(context = context, uri = pdfUri!!)!!)
        val input = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(input)

        for (i in 0 until renderer.pageCount) {

            val page = renderer.openPage(i)
            val bitmap =
                Bitmap.createBitmap(500, 650, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            imageList = bitmap.asImageBitmap()
            page.close()

        }
        renderer.close()
    }

    if (imageList != null) {
        Image(bitmap = imageList!!, contentDescription = null)
    }
}
