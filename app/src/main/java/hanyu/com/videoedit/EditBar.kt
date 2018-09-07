package hanyu.com.videoedit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import hanyu.com.videoedit.utils.DisplayUtil.Companion.dip2px

/**
 * Created by HanYu on 2018/9/5.
 */
class EditBar : View {
    private var leftIntervalBar: Bitmap? = null//左侧滑动条
    private var rightIntervalBar: Bitmap? = null//右侧滑动条
    private var outsideBg: Bitmap? = null//外部黑色蒙层
    private var insideBg: Bitmap? = null//滑动条中间无蒙层区域
    private var maxTimeInterval: Double = 0.0//最大时间
    private var minTimeInterval: Double = 0.0//最小时间一般为0
    private var minTouchScale: Int = 0 //滑动判断临界值
    private var pressedBarBM: Bitmap? = null//被拖动滑条
    private var barWidth: Int = 0//滑动条宽度
    private var paint: Paint? = null //背景画笔
    private var rectPaint: Paint? = null//上下边框画笔
    private var insideBgMatrix: Matrix? = null
    private var outsideBgMatrix: Matrix? = null
    private var outsideBitmap: Bitmap? = null
    private var insideBitmap: Bitmap? = null
    private var normalizedMinValue = 0.0//点坐标占总长度的比例值，范围从0-1
    private var normalizedMaxValue = 1.0//点坐标占总长度的比例值，范围从0-1
    private var minCutTime: Long = 10 * 1000
    private var pointId: Int = INVALID_POINT_ID
    private var motionDownX: Float = 0f // down事件x坐标
    private var pressedBar: BarType = BarType.NULL
    private var trackMotionEvent: Boolean = false
    private var trackListener: IRangeSeekBarChangeListener? = null//监听bar滑动状态

    enum class BarType {
        MAX, MIN, NULL
    }

    companion object {
        const val INVALID_POINT_ID: Int = -1
    }



    constructor(maxTimeInterval: Double, minTimeInterval: Double, context: Context) : this(context) {
        this.maxTimeInterval = maxTimeInterval
        this.minTimeInterval = minTimeInterval
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isFocusable = true
        }
        isFocusableInTouchMode = true
        initView()
    }


    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {

    }

    private fun initView() {
        minTouchScale = ViewConfiguration.get(context).scaledTouchSlop//获取最小滑动临界
        changeBarScale()
        outsideBg = BitmapFactory.decodeResource(resources, R.drawable.outside_bg)
        insideBg = BitmapFactory.decodeResource(resources, R.drawable.inside_bg)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        rectPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        rectPaint?.style = Paint.Style.FILL
        rectPaint?.color = Color.WHITE//上下边框颜色 #ffffff
        outsideBgMatrix = Matrix()
        insideBgMatrix = Matrix()
        outsideBitmap = Bitmap.createBitmap(outsideBitmap, 0, 0, outsideBitmap?.width!!, outsideBitmap?.height!!, null, true)
        insideBitmap = Bitmap.createBitmap(insideBitmap, 0, 0, insideBitmap?.width!!, insideBitmap?.height!!, null, true)
        outsideBgMatrix?.postScale(getAllScale(), 1f)
        outsideBg = Bitmap.createBitmap(outsideBg, 0, 0, outsideBg?.width!!, outsideBg?.height!!, outsideBgMatrix, false)
    }

    /**对原图进行比例缩放**/
    private fun changeBarScale() {
        leftIntervalBar = BitmapFactory.decodeResource(resources, R.drawable.handle_left)
        val setWidth: Int = resources.getDimensionPixelSize(R.dimen.ThumbBarWidth)//真正需要展示的宽度
        val setHeight: Int = resources.getDimensionPixelSize(R.dimen.ThumbnailHeight)//真正需要展示的高度
        val oldWidth: Int? = leftIntervalBar?.width//原始宽度
        val oldHeight = leftIntervalBar?.height//原始高度
        val scaleWidth: Float = (setWidth.toFloat() / oldWidth!!)//宽度缩放比例
        val scaleHeight: Float = (setHeight.toFloat() / oldHeight!!)//高度缩放比例
        val matrix = Matrix()
        matrix.setScale(scaleWidth, scaleHeight)
        leftIntervalBar = Bitmap.createBitmap(leftIntervalBar, 0, 0, oldWidth, oldHeight, matrix, true)
        rightIntervalBar = leftIntervalBar
        pressedBarBM = leftIntervalBar
        barWidth = setWidth
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rangeL = normalizedToScreen(0.0)
        val rangeR = normalizedToScreen(1.0)
        val insideScale: Float = rangeR - rangeL / insideBg?.width!!//播放区域长度
        if (insideScale > 0) {
            try {
                if (canvas != null) {
                    //画出中间展示缩略图部分
                    insideBgMatrix?.postScale(insideScale, 1f)
                    canvas.drawBitmap(insideBitmap, insideBgMatrix, paint)


                    //画左边的半透明遮罩
                    val bgLeft = Bitmap.createBitmap(outsideBg, 0, 0, (rangeL - 0f).toInt() + barWidth / 2, outsideBg!!.height)
                    canvas.drawBitmap(bgLeft, 0f, 0f, paint)

                    //画右边的半透明遮罩
                    val bgRight = Bitmap.createBitmap(outsideBg, (rangeR - barWidth / 2).toInt(), 0, (width - rangeR).toInt() + barWidth / 2, outsideBg!!.height)
                    canvas.drawBitmap(bgRight, (rangeR - barWidth / 2).toInt().toFloat(), 0f, paint)


                    //画上下的矩形
                    canvas.drawRect(rangeL, 0f, rangeR, dip2px(2, context).toFloat(), rectPaint)
                    canvas.drawRect(rangeL, (height - dip2px(2, context).toFloat()), rangeR, height.toFloat(), rectPaint)


                    //画左右滑动bar
                    drawBar(normalizedToScreen(normalizedMinValue), false, canvas, true)
                    drawBar(normalizedToScreen(normalizedMaxValue), false, canvas, false)

                }
            } catch (e: Exception) {
                Log.e("EditBarError", e.printStackTrace().toString())
            }
        }


    }

    /**获取滑动条距离边界距离**/
    private fun normalizedToScreen(normalizedValue: Double): Float {
        return (paddingLeft + normalizedValue * (width - paddingLeft - paddingRight)).toFloat()
    }


    private fun getAllScale(): Float {
        val insideScaleStart: Float = 0.0f
        val insideScaleEnd: Float = (width - paddingEnd).toFloat()
        return (insideScaleEnd - insideScaleStart) / insideBg?.width!!
    }

    private fun drawBar(screenCoord: Float, pressed: Boolean, canvas: Canvas, isLeft: Boolean) {
        canvas.drawBitmap(if (pressed) pressedBarBM else if (isLeft) leftIntervalBar else rightIntervalBar, screenCoord - if (isLeft) 0 else barWidth, 0f, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.pointerCount > 1) {
            return super.onTouchEvent(event)
        }

        if (!isEnabled) {
            return false
        }

        if (maxTimeInterval <= minCutTime) {
            return super.onTouchEvent(event)
        }
        var pointerIndex: Int
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                pointId = event.getPointerId(event.pointerCount - 1)//最后一个点Id
                pointerIndex = event.findPointerIndex(pointId)
                motionDownX = event.getX(pointerIndex)
                pressedBar = findWitchBarPressed(motionDownX)

                if (pressedBar == BarType.NULL) {
                    return super.onTouchEvent(event)
                }
                isPressed = true
                trackMotionEvent = true//开始追踪滑动事件
                trackEvent(event)//追踪启动！
                notifyParent()
                if (trackListener != null) {
                    // todo trackListener!!.onRangeSeekBarValuesChanged()
                }


            }
        }
        // return super.onTouchEvent(event)


    }

    fun setMinCutTime(minCutTime: Long) {
        this.minCutTime = minCutTime
    }

    /**判断是哪个Bar被点击**/
    private fun findWitchBarPressed(pressedX: Float): BarType {
        var result: BarType = BarType.NULL
        val minBarScale: Boolean = isBarScale(pressedX, minTimeInterval, 2.0)
        val maxBarScale: Boolean = isBarScale(pressedX, maxTimeInterval, 2.0)
        if (minBarScale && maxBarScale) {
            result = if (pressedX / width > 0.5f) BarType.MIN else BarType.MAX
        } else if (maxBarScale) {
            result = BarType.MAX
        } else if (minBarScale) {
            result = BarType.MIN
        }
        return result
    }

    /**当前触摸点X坐标-最小值图片中心点在屏幕的X坐标之差<=最小点图片的宽度的一般
    即判断触摸点是否在以最小值图片中心为原点，宽度一半为半径的圆内。**/
    private fun isBarScale(touchX: Float, normalizedThumbValue: Double, scale: Double): Boolean {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= barWidth / 2 * scale
    }


    private fun trackEvent(event: MotionEvent) {
//todo 追踪
    }


    /**阻止父view拦截事件**/
    private fun notifyParent() {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

    interface IRangeSeekBarChangeListener {
        fun onRangeSeekBarValuesChanged(bar: EditBar, minValue: Long, maxValue: Long, action: Int, isMin: Boolean, pressedBar: BarType)
    }

    fun setTrackLisener(trackListener: IRangeSeekBarChangeListener) {
        this.trackListener = trackListener
    }



}

