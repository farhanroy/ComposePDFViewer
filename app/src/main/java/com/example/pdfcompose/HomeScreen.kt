package com.example.pdfcompose

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toFile
import io.github.farhanroy.composepdf.ComposePDF
import java.io.File

@Composable
fun HomeScreen() {
    var imageList by remember { mutableStateOf<ImageBitmap?>(null) }

    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val launcherPDF = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        pdfUri = uri
    }
    
    Button(onClick = { launcherPDF.launch("application/pdf") }) {
        Text("Open file")
    }

    val context = LocalContext.current

    if (pdfUri != null) {
        val file = File(ContentUriUtil.getFilePath(context = context, uri = pdfUri!!)!!)
        ComposePDF(file = file)
    }
}
