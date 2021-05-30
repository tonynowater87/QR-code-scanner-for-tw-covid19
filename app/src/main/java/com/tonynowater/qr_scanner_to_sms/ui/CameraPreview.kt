package com.tonynowater.qr_scanner_to_sms.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.tonynowater.qr_scanner_to_sms.utils.TWCovid19SmsFormat
import java.util.concurrent.Executors
import android.os.VibrationEffect

import android.os.Build

import androidx.core.content.ContextCompat.getSystemService

import android.os.Vibrator




var temp = ""
var tempTimeStamp = 0L
var intervalTimeInMilliSeconds = 6000L

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

    AndroidView(
        factory = { ctx ->
            val preview = PreviewView(ctx)
            preview.implementationMode =
                PreviewView.ImplementationMode.COMPATIBLE //why COMPATIBLE can fill screen??
            cameraProvideFuture.addListener({
                val cameraProvider = cameraProvideFuture.get()
                bindPreview(context, lifecycleOwner, preview, cameraProvider)
            }, executor)
            preview
        },
        modifier = Modifier
            .fillMaxSize(1F)
            .shadow(1.dp, RoundedCornerShape(20.dp)),
    )
}

private fun bindPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    cameraProvider: ProcessCameraProvider
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
        setupImageAnalysis(context),
        preview
    )
}

private fun setupImageAnalysis(context: Context): ImageAnalysis {

    // configure our MLKit BarcodeScanning client

    /* passing in our desired barcode formats - MLKit supports additional formats outside of the
    ones listed here, and you may not need to offer support for all of these. You should only
    specify the ones you need */
    val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
        /*Barcode.FORMAT_CODE_128,
        Barcode.FORMAT_CODE_39,
        Barcode.FORMAT_CODE_93,
        Barcode.FORMAT_EAN_8,
        Barcode.FORMAT_EAN_13,*/
        Barcode.FORMAT_QR_CODE,
        /*Barcode.FORMAT_UPC_A,
        Barcode.FORMAT_UPC_E,
        Barcode.FORMAT_PDF417*/
    ).build()

    // getClient() creates a new instance of the MLKit barcode scanner with the specified options
    val scanner = BarcodeScanning.getClient(options)

    return ImageAnalysis.Builder()
        //.setTargetResolution(screenSize)
        //.setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
        .apply {
            setAnalyzer(Executors.newSingleThreadExecutor()) {
                processImageProxy(context, scanner, it)
            }
        }
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    context: Context,
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy
) {
    imageProxy.image?.let { image ->
        val inputImage =
            InputImage.fromMediaImage(
                image,
                imageProxy.imageInfo.rotationDegrees
            )

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)

                // `rawValue` is the decoded value of the barcode
                barcode?.rawValue?.let { value ->
                    val diffTime = System.currentTimeMillis() - tempTimeStamp
                    if (temp == value && diffTime < intervalTimeInMilliSeconds) {
                        return@addOnSuccessListener
                    }

                    if (TWCovid19SmsFormat.isValid(value)) {
                        vibrate(context)
                        temp = value
                        tempTimeStamp = System.currentTimeMillis()
                        context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                            data = (Uri.parse("sms:1922"))
                            putExtra("sms_body", TWCovid19SmsFormat.getBody(value))
                        })
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