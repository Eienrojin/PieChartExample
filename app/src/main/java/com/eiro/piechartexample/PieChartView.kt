package com.eiro.piechartexample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.random.Random

class PieChartView : View {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){}

    private var mainCircle: RectF? = null
    private var sliceColors: IntArray = intArrayOf(R.color.purple_200, R.color.black,
        R.color.purple_500, R.color.purple_700, androidx.appcompat.R.color.material_blue_grey_800)
    private val defaultColors = intArrayOf(R.color.purple_200, R.color.black, R.color.purple_500,
        R.color.purple_700, androidx.appcompat.R.color.material_blue_grey_800)
    private var pieData = mutableListOf<PiePiese>()
    private val slicePaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }
    private val centerPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val transparentCenterPaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        alpha = 50
    }
    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color - Color.BLACK
        style = Paint.Style.FILL
        textSize = 74f
        textAlign = Paint.Align.CENTER
    }

    fun setValues(data: FloatArray) {
        var sliceStartPoint = 0F
        var tempSlices = scale(data)
        var pieColors : MutableList<Int> = mutableListOf()

        for (i in data.indices)
            pieColors.add(ContextCompat.getColor(context, sliceColors[Random.nextInt(sliceColors.size)]))

       // avoidSameColors(pieColors)

        for (i in data.indices) {
                var tempData = PiePiese(0f,0f,0,0f,0f)
                tempData.value = data[i]
                tempData.scaledValue = tempSlices[i]
                tempData.color = pieColors[i]
                tempData.sliceStartPoint = sliceStartPoint

                if (sliceStartPoint + tempSlices[i] > 360f || sliceStartPoint + tempSlices[i] < 358f
                    && i == data.size - 1)
                    sliceStartPoint = 360f
                else
                    sliceStartPoint += tempSlices[i]

                tempData.sliceEndPoint = sliceStartPoint
                pieData.add(tempData)
            }

        invalidateAndRequestLayout()
        }
    /*fun addValue(value: Float, color: Int){
        var pieDataValues = floatArrayOf()
        var scaledDataValues = floatArrayOf()

        for (i in pieData.indices)
            pieDataValues[i] = pieData[i].value

        scaledDataValues = scale(pieDataValues)


    }*/
    fun setCenterColor(colorId: Int) {
        centerPaint.color = ContextCompat.getColor(context, colorId)
        invalidateAndRequestLayout()
    }
    fun setSliceColor(colors: IntArray) {
        sliceColors = colors

        if (colors.isEmpty())
            sliceColors = defaultColors
        invalidateAndRequestLayout()
    }

    private fun invalidateAndRequestLayout() {
        invalidate()
        requestLayout()
    }

    private fun scale(data: FloatArray): FloatArray {
        var scaledValues = FloatArray(data.size)
        for (i in data.indices) {
            scaledValues.fill((data[i] / data.sum()) * 360, i, data.size)
        }
        return scaledValues
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val startTop = 0F
        val startLeft = 0F
        val endBottom = width.toFloat()
        val endRight = endBottom

        mainCircle = RectF(startLeft, startTop, endRight, endBottom)

        var sliceStartPoint = 0F

        //отрисовка каждого куска
        for (i in pieData.indices) {
            slicePaint.color = pieData[i].color
            //сама команда отрисовки одного куска
            canvas!!.drawArc(mainCircle!!, pieData[i].sliceStartPoint, pieData[i].scaledValue, true, slicePaint)
            //перемещение точки раздела
            sliceStartPoint += pieData[i].scaledValue
        }

        val centerX = (measuredWidth / 2).toFloat()
        val centerY = (measuredHeight / 2).toFloat()
        val radius = Math.min(centerX, centerY)

        //отрисовка прозрачного круга
        canvas!!.drawCircle(centerX, centerY, radius / 1.6f, transparentCenterPaint)
        //отрисовка центрального круга
        canvas!!.drawCircle(centerX, centerY, radius / 2f, centerPaint)
        //отрисовка текстового поля
        canvas.drawText("test", centerX, centerY, textPaint)
    }
}