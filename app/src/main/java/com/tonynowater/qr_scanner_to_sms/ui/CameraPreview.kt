package com.tonynowater.qr_scanner_to_sms.ui

import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

@Composable
fun CameraPreview(analyzer: ImageAnalysis.Analyzer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProvideFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = ContextCompat.getMainExecutor(context)

    AndroidView(
        factory = { ctx ->
            val preview = PreviewView(ctx)
            preview.implementationMode = PreviewView.ImplementationMode.PERFORMANCE
            cameraProvideFuture.addListener({
                val cameraProvider = cameraProvideFuture.get()
                bindPreview(lifecycleOwner, preview, cameraProvider, analyzer, executor)
            }, executor)
            preview
        }
    )
}

private fun bindPreview(
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
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, setupImageAnalysis(executor, analyzer), preview)
}

private fun setupImageAnalysis(
    executor: Executor,
    analyzer: ImageAnalysis.Analyzer
): ImageAnalysis {
    return ImageAnalysis.Builder()
        .setTargetResolution(Size(720, 1280))
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