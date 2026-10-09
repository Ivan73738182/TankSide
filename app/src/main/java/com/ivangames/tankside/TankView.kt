package com.ivangames.tankside

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class TankView(context: Context) : View(context) {

    // Картинки
    private val hull: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hull)
    private val turret: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.turret)

    // Позиция танка на экране
    private var hullX = 0f
    private var hullY = 0f
    private var hullW = 0f
    private var hullH = 0f

    // Размеры башни в тех же пропорциях, что и корпуса
    private var turretW = 0f
    private var turretH = 0f

    // Угол башни (0 = смотрит вправо)
    private var turretAngle = 0f
    private var lastTouchX = 0f

    // Матрицы
    private val matrix = Matrix()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Корпус занимает 70% ширины экрана
        hullW = w * 0.70f
        hullH = hullW * hull.height.toFloat() / hull.width.toFloat()
        hullX = (w - hullW) / 2f
        hullY = h - hullH - h * 0.15f

        // Башня — в том же масштабе, что корпус
        val scale = hullW / hull.width.toFloat()
        turretW = turret.width * scale
        turretH = turret.height * scale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Фон — зимний градиент
        val bg = Paint()
        bg.shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(Color.parseColor("#8FB8D8"), Color.parseColor("#E8F0F8")),
            null, Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bg)

        // Тень под танком
        val shadow = Paint()
        shadow.color = Color.argb(80, 0, 0, 0)
        canvas.drawOval(
            hullX - hullW * 0.05f,
            hullY + hullH * 0.85f,
            hullX + hullW * 1.05f,
            hullY + hullH * 1.05f,
            shadow
        )

        // Рисуем корпус
        val hullRect = RectF(hullX, hullY, hullX + hullW, hullY + hullH)
        canvas.drawBitmap(hull, null, hullRect, paint)

        // Рисуем башню с поворотом
        canvas.save()
        // Точка вращения — центр башни (в мировых координатах)
        val pivotX = hullX + hullW * 0.45f
        val pivotY = hullY + hullH * 0.25f
        canvas.rotate(turretAngle, pivotX, pivotY)

        // Башня рисуется так, чтобы её центр совпал с pivot
        val turretLeft = pivotX - turretW / 2f
        val turretTop = pivotY - turretH / 2f
        val turretRect = RectF(turretLeft, turretTop, turretLeft + turretW, turretTop + turretH)
        canvas.drawBitmap(turret, null, turretRect, paint)

        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastTouchX
                turretAngle += dx * 0.3f
                // Ограничим угол ±60°
                turretAngle = turretAngle.coerceIn(-60f, 60f)
                lastTouchX = event.x
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
