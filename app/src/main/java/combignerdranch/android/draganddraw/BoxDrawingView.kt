package combignerdranch.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingBox"

class BoxDrawingView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {

    private var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()

    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                // Сбросить состояние объекта
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }

        Log.i(TAG, "$action at x=${current.x}, y=${current.y}")

        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // Заполнение фона
        canvas.drawPaint(backgroundPaint) //используя серовато-белый цвет, мы заполняем
                                          //«холст» задним фоном для вывода прямоугольников

        /* для каждого прямоугольника в списке мы определяем значения left, right,
        * top и bottom по двум точкам. Значения left и top будут минимальными, а bottom
        * и right — максимальными После вычисления параметров вызов функции Canvas.drawRect(...) рисует
        * красный прямоугольник на экране*/
        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }
}

/* При каждом получении события ACTION_DOWN в поле currentBox сохраняется новый
* объект Box с базовой точкой, соответствующей позиции события. Этот объект Box
* добавляется в массив прямоугольников
* В процессе перемещения пальца по экрану приложение обновляет currentBox.end.
* Затем, когда касание отменяется или палец не касается экрана, поле currentBox обнуляется
* для завершения операции. Объект Box завершен; он сохранен в массиве и уже не будет
* обновляться событиями перемещения. вызов invalidate() в функции updateCurrentBox()заставляет
*  BoxDrawingView перерисовать себя, чтобы пользователь видел прямоугольник в процессе перетаскивания */
