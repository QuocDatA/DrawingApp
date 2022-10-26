package com.quocdat.drawingapp.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import android.util.TypedValue
import android.view.MotionEvent

class DrawingView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    fun setColor(newColor: String){
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        //mBrushSize = 20.toFloat()
    }

    fun setSizeForBrush(newSize: Float){
        mBrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    //Change canvas to Canvas? if failed
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mCanvasBitmap.let {
            canvas?.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        }

        for (path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas?.drawPath(path, mDrawPaint!!)
        }

        if (!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas?.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize
                mDrawPath!!.reset()

                mDrawPath?.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE ->{
                mDrawPath?.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP ->{
                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float): Path()
}


