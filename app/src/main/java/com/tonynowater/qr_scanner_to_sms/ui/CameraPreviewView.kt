package com.tonynowater.qr_scanner_to_sms.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Size
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.model.QRCodeModel
import com.tonynowater.qr_scanner_to_sms.utils.TWCovid19SmsFormat
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.concurrent.Executors


var temp = ""
var tempTimeStamp = 0L
var intervalTimeInMilliSeconds = 6000L

//TODO error handling
@InternalCoroutinesApi
@Composable
fun CameraPreviewView(vm: MainViewModel, modifier: Modifier, enableTorch: Boolean) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProvideFuture = remember { ProcessCameraProvider.getInstance(context) }
    val camera = remember { mutableStateOf<Camera?>(null) }
    val executor = ContextCompat.getMainExecutor(context)

    vm.enableAllBarCodeFormat?.let { enableAllBarCodeFormat ->
        if (enableAllBarCodeFormat) {
            val options =
                BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build()
            AndroidView(
                factory = { ctx ->
                    val preview = PreviewView(ctx)
                    preview.implementationMode =
                        PreviewView.ImplementationMode.COMPATIBLE //why COMPATIBLE can fill screen??
                    cameraProvideFuture.addListener({
                        val cameraProvider = cameraProvideFuture.get()
                        camera.value = bindPreview(context, lifecycleOwner, preview, cameraProvider, vm, options)
                    }, executor)
                    preview
                },
                modifier = modifier
            )
        } else {
            val options =
                BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            AndroidView(
                factory = { ctx ->
                    val preview = PreviewView(ctx)
                    preview.implementationMode =
                        PreviewView.ImplementationMode.COMPATIBLE //why COMPATIBLE can fill screen??
                    cameraProvideFuture.addListener({
                        val cameraProvider = cameraProvideFuture.get()
                        camera.value = bindPreview(context, lifecycleOwner, preview, cameraProvider, vm, options)
                    }, executor)
                    preview
                },
                modifier = modifier
            )
        }
        camera.value?.cameraControl?.enableTorch(enableTorch)
    }
}

@InternalCoroutinesApi
private fun bindPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider,
    vm: MainViewModel,
    options: BarcodeScannerOptions,
): Camera {
    val preview = Preview.Builder()
        .build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    cameraProvider.unbindAll()
    return cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        setupImageAnalysis(context, vm, options),
        preview
    )
}

@InternalCoroutinesApi
private fun setupImageAnalysis(
    context: Context,
    vm: MainViewModel,
    options: BarcodeScannerOptions
): ImageAnalysis {

    // configure our MLKit BarcodeScanning client

    /* passing in our desired barcode formats - MLKit supports additional formats outside of the
    ones listed here, and you may not need to offer support for all of these. You should only
    specify the ones you need */

    // getClient() creates a new instance of the MLKit barcode scanner with the specified options
    val scanner = BarcodeScanning.getClient(options)

    return ImageAnalysis.Builder()
        .setTargetResolution(Size(1280, 720)) // minimize image size when device with small ram
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(Executors.newSingleThreadExecutor()) {
                processImageProxy(context, scanner, it, vm)
            }
        }
}

@InternalCoroutinesApi
@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    context: Context,
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    vm: MainViewModel
) {
    imageProxy.image?.let { image ->
        val inputImage =
            InputImage.fromMediaImage(
                image,
                imageProxy.imageInfo.rotationDegrees
            )

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0) ?: return@addOnSuccessListener
                barcode.rawValue.let { value ->
                    val diffTime = System.currentTimeMillis() - tempTimeStamp
                    if (temp == value && diffTime < intervalTimeInMilliSeconds) {
                        return@addOnSuccessListener
                    }

                    if (barcode.valueType == Barcode.TYPE_SMS && TWCovid19SmsFormat.isValid(value)) {
                        //Log.d("[DEBUG]", "1922 qr code: $value")
                        if (vm.vibration) {
                            vibrate(context)
                        }
                        temp = value
                        tempTimeStamp = System.currentTimeMillis()
                        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            data = Uri.parse("sms:1922")
                            putExtra("sms_body", TWCovid19SmsFormat.getBody(value))
                        })

                        if (vm.finishAfterScanned) {
                            (context as? Activity)?.finishAndRemoveTask()
                        }
                    } else {
                        //Log.d("[DEBUG]", "not 1922 qr code: $value")
                        vm.scannedInvalidQRCode(
                            QRCodeModel(
                                type = barcode.valueType,
                                rawValue = barcode.rawValue,
                                barcode = barcode
                            )
                        )
                    }
                }
            }
            .addOnFailureListener {
                // This failure will happen if the barcode scanning model
                // fails to download from Google Play Services
                Toast.makeText(context, "初始化發生異常(${it.message})，請重開App再試看看。", Toast.LENGTH_SHORT)
                    .show()
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must
                // call image.close() on received images when finished
                // using them. Otherwise, new images may not be received
                // or the camera may stall.
                imageProxy.image?.close()
                imageProxy.close()
            }
    }
}

private fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                250,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        //deprecated in API 26
        vibrator.vibrate(250)
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

/**
 * TODO
 * 2021-06-14 14:43:08.058 2148-2290/com.tonynowater.qr_scanner_to_sms D/Camera2CameraImpl: {Camera@25808f19[id=0]} Posting surface closed
 * java.lang.Throwable
 * at androidx.camera.camera2.internal.Camera2CameraImpl.postSurfaceClosedError(Camera2CameraImpl.java:1028)
 * at androidx.camera.camera2.internal.Camera2CameraImpl$2.onFailure(Camera2CameraImpl.java:988)
 * at androidx.camera.core.impl.utils.futures.Futures$CallbackListener.run(Futures.java:338)
 * at androidx.camera.core.impl.utils.executor.SequentialExecutor$QueueWorker.workOnQueue(SequentialExecutor.java:229)
 * at androidx.camera.core.impl.utils.executor.SequentialExecutor$QueueWorker.run(SequentialExecutor.java:171)
 * at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1112)
 * at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:587)
 * at java.lang.Thread.run(Thread.java:818)
 */