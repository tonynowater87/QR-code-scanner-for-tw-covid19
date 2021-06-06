package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCornerView() {

    val duration = 1500

    val infiniteTransition = rememberInfiniteTransition()
    val infinitelyAnimatedFloatStrokeWidth = infiniteTransition.animateFloat(
        initialValue = 12F,
        targetValue = 8F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration),
            repeatMode = RepeatMode.Reverse
        )
    )

    val infinitelyAnimatedFloatPosition = infiniteTransition.animateFloat(
        initialValue = -10F,
        targetValue = 10F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val padding = 50.dp
    val yOffset = 0.dp
    val arrowSideLength = 40.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val rectSideLength = screenWidthDp.dp - padding - padding
    val startPointY = 0.dp

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .offset(
                y = yOffset - infinitelyAnimatedFloatPosition.value.dp,
                x = -infinitelyAnimatedFloatPosition.value.dp
            )
    ) {
        val topLeft1 =
            padding.toPx() to startPointY.toPx() + arrowSideLength.toPx()
        val topLeft2 = padding.toPx() to startPointY.toPx()
        val topLeft3 =
            padding.toPx() + arrowSideLength.toPx() to startPointY.toPx()

        drawPath(
            path = Path().apply {
                // draw topLeft
                moveTo(topLeft1.first, topLeft1.second)
                quadraticBezierTo(
                    topLeft2.first,
                    topLeft2.second,
                    topLeft3.first,
                    topLeft3.second
                )
            },
            color = Color.White,
            style = Stroke(width = infinitelyAnimatedFloatStrokeWidth.value, cap = StrokeCap.Round)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .offset(
                y = yOffset - infinitelyAnimatedFloatPosition.value.dp,
                x = infinitelyAnimatedFloatPosition.value.dp
            )
    ) {
        val topRight1 =
            screenWidthDp.dp.toPx() - padding.toPx() - arrowSideLength.toPx() to startPointY.toPx()
        val topRight2 =
            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx()
        val topRight3 =
            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + arrowSideLength.toPx()

        drawPath(
            path = Path().apply {
                // draw topRight
                moveTo(topRight1.first, topRight1.second)
                quadraticBezierTo(
                    topRight2.first,
                    topRight2.second,
                    topRight3.first,
                    topRight3.second
                )
            },
            color = Color.White,
            style = Stroke(width = infinitelyAnimatedFloatStrokeWidth.value, cap = StrokeCap.Round)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .offset(
                y = yOffset + infinitelyAnimatedFloatPosition.value.dp,
                x = infinitelyAnimatedFloatPosition.value.dp
            )
    ) {
        val bottomRight1 =
            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + rectSideLength.toPx() - arrowSideLength.toPx()
        val bottomRight2 =
            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + rectSideLength.toPx()
        val bottomRight3 =
            screenWidthDp.dp.toPx() - padding.toPx() - arrowSideLength.toPx() to startPointY.toPx() + rectSideLength.toPx()

        drawPath(
            path = Path().apply {
                // draw bottomRight
                moveTo(bottomRight1.first, bottomRight1.second)
                quadraticBezierTo(
                    bottomRight2.first,
                    bottomRight2.second,
                    bottomRight3.first,
                    bottomRight3.second
                )
            },
            color = Color.White,
            style = Stroke(width = infinitelyAnimatedFloatStrokeWidth.value, cap = StrokeCap.Round)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .offset(
                y = yOffset + infinitelyAnimatedFloatPosition.value.dp,
                x = -infinitelyAnimatedFloatPosition.value.dp
            )
    ) {

        val bottomLeft1 =
            padding.toPx() + arrowSideLength.toPx() to startPointY.toPx() + rectSideLength.toPx()
        val bottomLeft2 =
            padding.toPx() to startPointY.toPx() + rectSideLength.toPx()
        val bottomLeft3 =
            padding.toPx() to startPointY.toPx() + rectSideLength.toPx() - arrowSideLength.toPx()

        drawPath(
            path = Path().apply {
                // draw bottomRight
                moveTo(bottomLeft1.first, bottomLeft1.second)
                quadraticBezierTo(
                    bottomLeft2.first,
                    bottomLeft2.second,
                    bottomLeft3.first,
                    bottomLeft3.second
                )
            },
            color = Color.White,
            style = Stroke(width = infinitelyAnimatedFloatStrokeWidth.value, cap = StrokeCap.Round)
        )
    }
}