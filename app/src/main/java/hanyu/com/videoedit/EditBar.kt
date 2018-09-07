package hanyu.com.videoedit

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewConfiguration

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
    private var pressedBar: Bitmap? = null//被拖动滑条
    private var barWidth: Int = 0//滑动条宽度
    private var paint: Paint? = null //背景画笔
    private var rectPaint: Paint? = null//上下边框画笔
    private var insideBgMatrix: Matrix? = null
    private var outsideBgMatrix: Matrix? = null


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
        pressedBar = leftIntervalBar
        barWidth = setWidth

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val insideScaleStart: Float = 0.0f
        val insideScaleEnd: Float = (width - paddingEnd).toFloat()
        val allScale: Float = (insideScaleEnd - insideScaleStart) / insideBg?.width!!//总范围
        val rangeL = normalizedToScreen(0.0)
        val rangeR = normalizedToScreen(1.0)
        var playScale: Float = rangeR - rangeL / insideBg?.width!!//播放区域长度


    }

    /**获取滑动条距离边界距离**/
    private fun normalizedToScreen(normalizedValue: Double): Float {
        return (paddingLeft + normalizedValue * (width - paddingLeft - paddingRight)).toFloat()
    }

}

