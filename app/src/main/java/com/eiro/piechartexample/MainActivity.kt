package com.eiro.piechartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random

//TODO
//1. Алгоритм, который убирает рядомстоящие одинковые цвета
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataPoints = FloatArray(15) {Random.nextFloat()}

        val pieChart = findViewById<PieChartView>(R.id.testPieChart)
        //заменить на динамическую обработку цветов
        pieChart.setSliceColor(intArrayOf())

        pieChart.setValues(dataPoints)
        //выставить дефолтное значение для цвета
        pieChart.setCenterColor(R.color.white)
    }
}