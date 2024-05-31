package com.example.bmicalculator.custom


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import com.example.bmicalculator.R
import kotlin.math.roundToLong

class ArcProgress(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private val outerArc = Paint()
    private val innerArc = Paint()


    private var canterpoint=0
    private val progressRectf = RectF()
    private val DEFAULT_FOREGROUND_PROGRESSBAR_WIDTH = 30f
    private val DEFAULT_BACKGROUND_PROGRESSBAR_WIDTH = 30f
    private val DEFAULT_isRoundCorner = false
    private val DEFAULT_CLOCKWISE = true
    private var startAngle = 180f
    private var swipeAngle = 180f
    private var isClockwise = DEFAULT_CLOCKWISE

    private var isRoundCorner = DEFAULT_isRoundCorner

    private val DEFAULT_ANIMATION_DURATION = 1500f

    @ColorInt
    private var DEFAULT_FOREGROUND_PROGRESS_COLOR = Color.parseColor("#71CC75")
    @ColorInt
    private var DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.parseColor("#F9F9FA")

    private var foregroundProgressbarWidth = DEFAULT_FOREGROUND_PROGRESSBAR_WIDTH

    private var backgroundProgressbarWidth = DEFAULT_BACKGROUND_PROGRESSBAR_WIDTH
    @ColorInt
    private var foregroundProgressColor = DEFAULT_FOREGROUND_PROGRESS_COLOR
    @ColorInt
    private var backgroundProgressColor = DEFAULT_BACKGROUND_PROGRESS_COLOR

    private var DEFAULT_PROGRESS = 0f
    private var DEFAULT_MAXSCALE = 50f
    private var progressMaxscale = DEFAULT_MAXSCALE
    private var DEFAULT_TOUCH_ENABLED = false
    private var progressbarTouchEnable = DEFAULT_TOUCH_ENABLED

    private var progress = DEFAULT_PROGRESS
    private val textPaint = Paint()
    private var width = 300f;
    private var height = 3002f;

    init {

        var typedArray = context.obtainStyledAttributes(attributes, R.styleable.ArcProgress)
        initTypeArray(typedArray)
        progressValidation(progress)
        outerArc.strokeWidth = foregroundProgressbarWidth
        innerArc.strokeWidth = backgroundProgressbarWidth
        outerArc.style = Paint.Style.STROKE
        innerArc.color = backgroundProgressColor
        outerArc.color = foregroundProgressColor
        innerArc.style = Paint.Style.STROKE
        textPaint.color = Color.BLACK
        textPaint.strokeWidth = 5f
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 40f
        setRoundedCorner(isRoundCorner)
    }


    constructor(context: Context, attributes: AttributeSet, defStyle: Int) : this(
        context,
        attributes
    ) {
        var typedArray =
            context.obtainStyledAttributes(attributes, R.styleable.ArcProgress, defStyle, 0)
        initTypeArray(typedArray)
        init()
        progressValidation(progress)
    }

    fun initTypeArray(typedArray: TypedArray){

        foregroundProgressbarWidth = typedArray.getFloat(
            R.styleable.ArcProgress_ap_foreground_progressbar_width,
            DEFAULT_FOREGROUND_PROGRESSBAR_WIDTH
        );
        backgroundProgressbarWidth = typedArray.getFloat(
            R.styleable.ArcProgress_ap_background_progressbar_width,
            DEFAULT_BACKGROUND_PROGRESSBAR_WIDTH
        );
        foregroundProgressColor =
            typedArray.getColor(R.styleable.ArcProgress_ap_progress_color, DEFAULT_FOREGROUND_PROGRESS_COLOR)
        backgroundProgressColor = typedArray.getColor(
            R.styleable.ArcProgress_ap_progress_background_color,
            DEFAULT_BACKGROUND_PROGRESS_COLOR
        )
        progress = typedArray.getFloat(R.styleable.ArcProgress_ap_progress, DEFAULT_PROGRESS)
        isRoundCorner = typedArray.getBoolean(R.styleable.ArcProgress_ap_progress_roundedCorner, DEFAULT_isRoundCorner)

        isClockwise=typedArray.getBoolean(R.styleable.ArcProgress_ap_progress_isClockwise,DEFAULT_CLOCKWISE)


        progressMaxscale=typedArray.getFloat(R.styleable.ArcProgress_ap_progress_maxscale,DEFAULT_MAXSCALE)

        progressbarTouchEnable=typedArray.getBoolean(R.styleable.ArcProgress_ap_progress_touchEnabled,DEFAULT_TOUCH_ENABLED)

        typedArray.recycle()
    }

    fun init() {
        outerArc.strokeWidth = foregroundProgressbarWidth
        outerArc.style = Paint.Style.STROKE
        innerArc.color = backgroundProgressColor
        outerArc.color = foregroundProgressColor
        innerArc.strokeWidth = backgroundProgressbarWidth
        innerArc.style = Paint.Style.STROKE
        textPaint.color = Color.BLACK
        textPaint.strokeWidth = 5f
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 40f
        setRoundedCorner(isRoundCorner)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(MeasureSpec.getMode(widthMeasureSpec)>0 && MeasureSpec.getMode(heightMeasureSpec)>0){
            width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
//        Log.e("Width ",MeasureSpec.getMode(widthMeasureSpec).toString()+"height "+)
            height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        }else{
            width=400f;
            height=400f;
        }

        val maxStockWidth=Math.max(outerArc.strokeWidth,innerArc.strokeWidth)
        progressRectf.left = 0f + maxStockWidth/2f
        progressRectf.right = width.toFloat() - maxStockWidth / 2f
        progressRectf.top = 0f + maxStockWidth / 2f
        progressRectf.bottom = height.toFloat()



        setMeasuredDimension(width.toInt(), height.toInt()/2+(3*maxStockWidth/4).toInt())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        progressValidation(progress)
        canvas.drawArc(progressRectf, startAngle, swipeAngle, false, innerArc)
        canvas.drawArc(progressRectf, startAngle, progress, false, outerArc)
        invalidate()
    }


    fun setForegroundProgressColor(color: Int) {
        foregroundProgressColor = color
        outerArc.color = foregroundProgressColor
        invalidate()
    }

    fun setBackgroundProgressColor(color: Int) {
        backgroundProgressColor = color
        innerArc.color =backgroundProgressColor
        invalidate()
    }

    fun setForeGroundProgressThickness(thickness: Float){
        outerArc.strokeWidth = thickness
        invalidate()
    }
    fun setBackGroundProgressWidth(thickness: Float){
        innerArc.strokeWidth = thickness
        invalidate()
    }
    fun setProgressThickness(thickness: Float){
        innerArc.strokeWidth = thickness
        invalidate()
    }

    fun getprogress():Float{
        return progress
    }


    fun setProgress(progress: Float) {
        progressValidation(progress)
        invalidate()
    }

    fun getProgress():Float{
        val percentage=(progress/180)*100
        val progressValue=(percentage/100)*progressMaxscale
        return progressValue
    }

    fun setIsRound(isRoundCorner: Boolean){
        this.isRoundCorner=isRoundCorner
        setRoundedCorner(isRoundCorner)
    }

    fun setRoundedCorner(isRoundCorner:Boolean){
        this.isRoundCorner=isRoundCorner
        if (isRoundCorner){
            outerArc.strokeCap=Paint.Cap.ROUND
            innerArc.strokeCap=Paint.Cap.ROUND
        }
        else{
            outerArc.strokeCap=Paint.Cap.SQUARE
            innerArc.strokeCap=Paint.Cap.SQUARE
        }
    }
    fun progressValidation(progress: Float){
        if(this.progress>=swipeAngle){
            this.progress=swipeAngle

        }else{
            this.progress= progress
        }
        invalidate()
    }

    fun setProgressbarMaxscale(maxScale:Float){
        progressMaxscale=maxScale
    }
    fun getProgressbarMaxscale():Float{
        return progressMaxscale
    }

    fun setTouchEnable(touchEnable:Boolean){
        progressbarTouchEnable=touchEnable
    }

    fun getTouchEnable():Boolean{
        return progressbarTouchEnable
    }

    fun setProgressWithAnimation(progress: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress",this.progress, progress)
        objectAnimator.duration = DEFAULT_ANIMATION_DURATION.roundToLong()
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
    }

    fun setProgressWithAnimation(progress: Float, duration: Int) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress)
        objectAnimator.duration = duration.toLong()
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
    }
}