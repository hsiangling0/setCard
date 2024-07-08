package tw.edu.ncku.iim.rsliu.setcard

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SetCardView : View {
    enum class Shape {
        OVAL, DIAMOND, WORM
    }

    enum class Shading {
        EMPTY, SOLID, STRIP
    }

    public var number:Int = 1
        set(value) {
            if (value >= 1 && value <= 3) {
                field = value
                invalidate()
            }
        }

    public var shape = Shape.OVAL
        set(value) {
            field = value
            invalidate()
        }

    public var color = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }

    public var shading = Shading.EMPTY
        set(value) {
            field = value
            invalidate()
        }
    public var cardselected :Boolean= false
        set(value) {
            field = value
            invalidate()
        }

    companion object SetCardConstants {
        const val CARD_STANDARD_HEIGHT =  240.0f
        const val CORNER_RADIUS = 12.0f
        const val SYMBOL_WIDTH_SCALE_FACTOR = 0.6f
        const val SYMBOL_HEIGHT_SCALE_FACTOR = 0.125f
        const val STRIP_DISTANCE_SCALE_FACTOR = 0.05f
    }

    private val cornerScaleFactor: Float
        get() {
            return height / CARD_STANDARD_HEIGHT
        }

    private val cornerRadius: Float
        get() {
            return CORNER_RADIUS * cornerScaleFactor
        }

    private val mPaint = Paint() // For drawing border and face images
    private val mTextPaint = TextPaint() // for drawing pips (text)

    private fun init(context: Context, attrs: AttributeSet? = null) {
        mPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);

//        attrs.let {
//            val a =
//                context.obtainStyledAttributes(it, R.styleable.PlayingCardViewAttrs)
//        }
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs,defStyle) {
        init(context, attrs)
    }

    private fun drawShapeWithVerticalOffset(canvas: Canvas, voffset: Float) {
        val path = Path()
        Log.i("height", height.toString())
        val width = width * SYMBOL_WIDTH_SCALE_FACTOR
        val height = height * SYMBOL_HEIGHT_SCALE_FACTOR

        val center = PointF((getWidth() / 2).toFloat(), getHeight() / 2 + voffset)
        if (shape == Shape.OVAL) {
            //OVAL
            path.addOval(center.x-width/2,center.y-height/2,center.x+width/2,center.y+height/2,Path.Direction.CW)
            canvas.drawPath(path, mPaint)
        } else if (shape == Shape.DIAMOND) {
            //DIAMOND
            path.moveTo(center.x,center.y-height/2)
            path.lineTo(center.x-width/2,center.y)
            path.lineTo(center.x,center.y+height/2)
            path.lineTo(center.x+width/2,center.y)
            path.lineTo(center.x,center.y-height/2)
            canvas.drawPath(path, mPaint)
        } else {
            // WORM
            path.moveTo(center.x - width / 2, center.y + height / 2)

            val cp1 = PointF(center.x - width / 4, center.y - height * 1.5f)
            val cp2 = PointF(center.x + width / 4, center.y)
            val dst = PointF(center.x + width / 2, center.y - height / 2)
            path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, dst.x, dst.y)

            cp1.x = center.x + width / 2
            cp1.y = center.y + height * 2
            cp2.x = center.x - width / 2
            cp2.y = center.y

            dst.x = center.x - width / 2
            dst.y = center.y + height / 2

            path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, dst.x, dst.y)
            canvas.drawPath(path, mPaint)
        }

        drawShadingInPath(canvas, path);
    }

    private fun drawShapes(canvas: Canvas) {
        val height=height*SYMBOL_HEIGHT_SCALE_FACTOR
        mPaint.color = color
        when (number) {
            1,3 -> {
                drawShapeWithVerticalOffset(canvas, 1.0f)
                if (number == 3) {
                    drawShapeWithVerticalOffset(canvas, -height*2)
                    drawShapeWithVerticalOffset(canvas, 1.0f)
                    drawShapeWithVerticalOffset(canvas, height*2)
                }
            }
            2 -> {
                drawShapeWithVerticalOffset(canvas, -height/2*3)
                drawShapeWithVerticalOffset(canvas, height/2*3)
            }
        }
    }


    private fun drawShadingInPath(canvas: Canvas, path: Path) {
        canvas.save();

        if (shading == Shading.SOLID) {
            mPaint.style=Paint.Style.FILL
            mPaint.color=color
            canvas.drawPath(path, mPaint);

        } else if (shading == Shading.STRIP) {
            canvas.clipPath(path);
            val path = Path();
            val strip_distance = width * STRIP_DISTANCE_SCALE_FACTOR;
            var x = 0f
            while (x < width) {
                path.moveTo(x, 0f);
                path.lineTo(x, height.toFloat());
                x += strip_distance
            }
            canvas.drawPath(path, mPaint);
        }
        canvas.restore();
    }


    override fun onDraw(canvas: Canvas) {
        Log.i("draw","start draw card")
        super.onDraw(canvas)

        val path = Path()

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        // Intersect the current clip with the specified path
        canvas.clipPath(path)
        // fill
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        canvas.drawPath(path, mPaint)
        // border
        mPaint.style = Paint.Style.STROKE
        if(cardselected){
            mPaint.strokeWidth = 10.0f
            mPaint.color=Color.YELLOW
        }else{
            mPaint.strokeWidth = 6.0f
            mPaint.color = Color.BLACK
        }

        canvas.drawPath(path, mPaint)

        drawShapes(canvas)
    }
}
