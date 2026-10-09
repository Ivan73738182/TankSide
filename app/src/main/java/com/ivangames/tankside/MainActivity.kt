package com.ivangames.tankside

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this)
        tv.text = "TankSide готов! 🚀"
        tv.setTextColor(Color.WHITE)
        tv.textSize = 40f
        tv.gravity = Gravity.CENTER

        setContentView(tv)
    }
}
