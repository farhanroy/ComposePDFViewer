package io.github.farhanroy.composepdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import io.github.farhanroy.composepdf.source.FileSource
import java.io.File

@Composable
fun ComposePDF(file: File) {
    val source = FileSource(file)
    val document = source.getDocument()

    LazyColumn {
        items(document.size) {
            Image(bitmap = document[it], contentDescription = null)
        }
    }
}