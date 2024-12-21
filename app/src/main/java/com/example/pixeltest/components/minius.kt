import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val ChromeMinimize: ImageVector
    get() {
        if (_ChromeMinimize != null) {
            return _ChromeMinimize!!
        }
        _ChromeMinimize = ImageVector.Builder(
            name = "ChromeMinimize",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 8f)
                verticalLineToRelative(1f)
                horizontalLineTo(3f)
                verticalLineTo(8f)
                horizontalLineToRelative(11f)
                close()
            }
        }.build()
        return _ChromeMinimize!!
    }

private var _ChromeMinimize: ImageVector? = null
