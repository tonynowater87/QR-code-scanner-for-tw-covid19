package com.tonynowater.qr_scanner_to_sms.ui

import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

//TODO error handling
@Composable
fun CameraPreview(analyzer: ImageAnalysis.Analyzer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightPixel = with(LocalDensity.current) { screenHeightDp.dp.roundToPx() }
    val screenWidthPixel = with(LocalDensity.current) { screenWidthDp.dp.roundToPx() }
    val cameraProvideFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = ContextCompat.getMainExecutor(context)

    Toast.makeText(
        context,
        "screenHeightDp = $screenHeightDp, screenWidthDp = $screenWidthDp",
        Toast.LENGTH_SHORT
    ).show()
    Toast.makeText(
        context,
        "screenHeightPixel = $screenHeightPixel, screenWidthPixel = $screenWidthPixel",
        Toast.LENGTH_SHORT
    ).show()
    Log.d("DEBUG", "screenHeightDp = $screenHeightDp, screenWidthDp = $screenWidthDp")

    AndroidView(
        factory = { ctx ->
            val preview = PreviewView(ctx)
            preview.implementationMode =
                PreviewView.ImplementationMode.COMPATIBLE //why COMPATIBLE can fill screen??
            cameraProvideFuture.addListener({
                val cameraProvider = cameraProvideFuture.get()
                bindPreview(
                    Size(screenWidthPixel, screenHeightPixel),
                    lifecycleOwner,
                    preview,
                    cameraProvider,
                    analyzer,
                    executor
                )
            }, executor)
            preview
        },
        modifier = Modifier
            .fillMaxSize(1F)
            .shadow(1.dp, RoundedCornerShape(20.dp)),
    )
}

private fun bindPreview(
    screenSize: Size,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider,
    analyzer: ImageAnalysis.Analyzer,
    executor: Executor
) {
    val preview = Preview.Builder()
        .build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        setupImageAnalysis(screenSize, executor, analyzer),
        preview
    )
}

private fun setupImageAnalysis(
    screenSize: Size,
    executor: Executor,
    analyzer: ImageAnalysis.Analyzer
): ImageAnalysis {

    return ImageAnalysis.Builder()
        //.setTargetResolution(screenSize)
        //.setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(executor, analyzer)
        }
}

/** Returns true if the device has an available back camera. False otherwise */
private fun hasBackCamera(cameraProvider: ProcessCameraProvider): Boolean {
    return cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
}

/** Returns true if the device has an available front camera. False otherwise */
private fun hasFrontCamera(cameraProvider: ProcessCameraProvider): Boolean {
    return cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)
}