package com.example.photoeditor.photo_editor_screen

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.photoeditor.R
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.ViewType

@Composable
fun PhotoEditorScreen(modifier: Modifier = Modifier, viewmodel: PhotoEditorViewmodel) {
    val context = LocalContext.current
    val state = viewmodel.state.collectAsState()
    var viewVersion by remember { mutableIntStateOf(0) }
    val openMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewmodel.setImageBitmap(uri)
                viewVersion++
            }
        }
    )

    val photoEditorView = PhotoEditorView(context).apply {
        source.maxWidth
        source.maxHeight
        source.scaleType = ImageView.ScaleType.CENTER_CROP
        if (state.value.imageBitmap != null) {
            source.setImageURI(state.value.imageBitmap)
        }
    }
    val photoEditor = PhotoEditor.Builder(context, photoEditorView).build()
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = modifier.weight(3f)) {
            key(viewVersion) {
                AndroidView(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    factory = {
                        RelativeLayout(context).apply {
                            addView(photoEditorView)
                        }
                    }
                )
            }
            Row(modifier = modifier.align(Alignment.BottomStart)) {
                IconPhotoEdit(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            openMedia.launch("image/*")
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_lib_photo
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                }
                IconPhotoEdit(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            TODO()
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_photo_camera
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                }
            }

            Row(modifier = modifier.align(Alignment.BottomEnd)) {
                IconPhotoEdit(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            photoEditor.undo()
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_undo
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                }
                IconPhotoEdit(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            photoEditor.redo()
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            R.drawable.ic_redo
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                }
            }
        }
        Row(modifier = modifier.weight(1f)) {
            ItemPhotoEdit(
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .clickable {
                        photoEditor.setBrushDrawingMode(true)
                    },
                icon = {
                    Icon(
                        modifier = modifier.size(50.dp),
                        painter = painterResource(
                            R.drawable.baseline_brush_24
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                },
                title = "Brush"
            )

            ItemPhotoEdit(
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .clickable {
                        photoEditor.addText("Text", R.color.purple_200)
                    },
                icon = {
                    Icon(
                        modifier = modifier.size(50.dp),
                        painter = painterResource(
                            R.drawable.icon_text
                        ), contentDescription = "add text",
                        tint = Color.White
                    )
                },
                title = "Text"
            )
            ItemPhotoEdit(
                modifier = Modifier
                    .size(100.dp, 150.dp)
                    .clickable {
                        photoEditor.setBrushDrawingMode(false)
                        photoEditor.brushEraser()
                    },
                icon = {
                    Icon(
                        modifier = modifier.size(50.dp),
                        painter = painterResource(
                            R.drawable.baseline_remove_24
                        ), contentDescription = "photo lib",
                        tint = Color.White
                    )
                },
                title = "Eraser"
            )
        }
    }
}

@Composable
fun IconPhotoEdit(modifier: Modifier = Modifier, icon: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
fun ItemPhotoEdit(modifier: Modifier = Modifier, icon: @Composable () -> Unit, title: String) {
    Column(
        modifier = modifier.background(color = Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(30.dp))
        icon()
        Text(title, color = Color.White)
    }
}
