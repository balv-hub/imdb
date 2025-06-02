package com.balv.imdb.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

 val SentimentVeryDissatisfied: ImageVector
    get() {
        if (_dissatisfied != null) {
            return _dissatisfied!!
        }
        _dissatisfied = ImageVector.Builder(
            name = "Sentiment_very_dissatisfied",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(480f, 540f)
                quadToRelative(-68f, 0f, -123.5f, 38.5f)
                reflectiveQuadTo(276f, 680f)
                horizontalLineToRelative(408f)
                quadToRelative(-25f, -63f, -80.5f, -101.5f)
                reflectiveQuadTo(480f, 540f)
                moveToRelative(-168f, -60f)
                lineToRelative(44f, -42f)
                lineToRelative(42f, 42f)
                lineToRelative(42f, -42f)
                lineToRelative(-42f, -42f)
                lineToRelative(42f, -44f)
                lineToRelative(-42f, -42f)
                lineToRelative(-42f, 42f)
                lineToRelative(-44f, -42f)
                lineToRelative(-42f, 42f)
                lineToRelative(42f, 44f)
                lineToRelative(-42f, 42f)
                close()
                moveToRelative(250f, 0f)
                lineToRelative(42f, -42f)
                lineToRelative(44f, 42f)
                lineToRelative(42f, -42f)
                lineToRelative(-42f, -42f)
                lineToRelative(42f, -44f)
                lineToRelative(-42f, -42f)
                lineToRelative(-44f, 42f)
                lineToRelative(-42f, -42f)
                lineToRelative(-42f, 42f)
                lineToRelative(42f, 44f)
                lineToRelative(-42f, 42f)
                close()
                moveTo(480f, 880f)
                quadToRelative(-83f, 0f, -156f, -31.5f)
                reflectiveQuadTo(197f, 763f)
                reflectiveQuadToRelative(-85.5f, -127f)
                reflectiveQuadTo(80f, 480f)
                reflectiveQuadToRelative(31.5f, -156f)
                reflectiveQuadTo(197f, 197f)
                reflectiveQuadToRelative(127f, -85.5f)
                reflectiveQuadTo(480f, 80f)
                reflectiveQuadToRelative(156f, 31.5f)
                reflectiveQuadTo(763f, 197f)
                reflectiveQuadToRelative(85.5f, 127f)
                reflectiveQuadTo(880f, 480f)
                reflectiveQuadToRelative(-31.5f, 156f)
                reflectiveQuadTo(763f, 763f)
                reflectiveQuadToRelative(-127f, 85.5f)
                reflectiveQuadTo(480f, 880f)
                moveToRelative(0f, -80f)
                quadToRelative(134f, 0f, 227f, -93f)
                reflectiveQuadToRelative(93f, -227f)
                reflectiveQuadToRelative(-93f, -227f)
                reflectiveQuadToRelative(-227f, -93f)
                reflectiveQuadToRelative(-227f, 93f)
                reflectiveQuadToRelative(-93f, 227f)
                reflectiveQuadToRelative(93f, 227f)
                reflectiveQuadToRelative(227f, 93f)
            }
        }.build()
        return _dissatisfied!!
    }

private var _dissatisfied: ImageVector? = null
