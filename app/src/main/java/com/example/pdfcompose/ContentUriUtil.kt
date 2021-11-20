package com.example.pdfcompose

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

object ContentUriUtil {

    fun getFilePath(context: Context, uri: Uri): String? {
        var copy = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            && DocumentsContract.isDocumentUri(context, copy)) {
            when {
                isDownloadsDocument(copy) -> {
                    val id = DocumentsContract.getDocumentId(copy)
                    copy = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), id.toLong())
                }
                isExternalStorageDocument(copy) -> {
                    val id = DocumentsContract.getDocumentId(copy)
                    val split = id.split(":").toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                isMediaDocument(copy) -> {
                    val id = DocumentsContract.getDocumentId(copy)
                    val split = id.split(":").toTypedArray()
                    when (split[0]) {
                        "image" -> {
                            copy = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            copy = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            copy = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }

                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }

        if ("content".equals(copy.scheme, true)) {
            if (isGooglePhotosUri(copy)) {
                return copy.lastPathSegment
            }

            var cursor: Cursor? = null
            try {
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                cursor = context.contentResolver?.query(
                    copy, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getString(0)
                }

                return null
            } catch (ignore: Exception) {
            } finally {
                cursor?.close()
            }
        } else if ("file".equals(copy.scheme, true)) {
            return copy.path
        }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
}