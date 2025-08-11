package com.example.photoeditor.photo_editor_screen

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PhotoEditorViewmodel : ViewModel() {
    val state = MutableStateFlow(PhotoEditorState())

    fun setImageBitmap(uri: Uri) {
        state.value = state.value
            .copy(imageBitmap = uri)
    }
}