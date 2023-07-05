package combignerdranch.android.draganddraw

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.max
import kotlin.math.min

@Parcelize
class Box(val start: PointF): Parcelable {

    var end: PointF = start

    val left: Float
        get() = min(start.x, end.x)

    val right: Float
        get() = max(start.x, end.x)

    val top: Float
        get() = min(start.y, end.y)

    val bottom: Float
        get() = max(start.y, end.y)

}