package com.futhark.android.seevoice.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.futhark.android.seevoice.model.constant.AppConstant

/**
 * the view of displaying voice
 * Created by liuhr on 08/04/2017.
 */

class DisplayVoiceView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : SurfaceView(context, attrs, defStyleAttr, defStyleRes), SurfaceHolder.Callback {
    private val surfaceHolder: SurfaceHolder
    private var myThread: MyThread? = null
    var displayVoiceController: DisplayVoiceController? = null
        set(displayVoiceController) {
            field = displayVoiceController
            Log.d(TAG, "set display voice controller")
        }
    private val paint: Paint

    constructor(context: Context) : this(context, null) {
        Log.d(TAG, "display voice view create1")
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        Log.d(TAG, "display voice view create2")
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0) {
        Log.d(TAG, "display voice view create3")
    }

    init {
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
        paint = Paint()
        paint.color = Color.WHITE
        setWillNotDraw(false)
        Log.d(TAG, "display voice view create4")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surface created")
        if (displayVoiceController == null) {
            throw RuntimeException("display voice controller should be set before the view will display")
        }
        if (myThread == null) {
            myThread = MyThread(this.surfaceHolder, displayVoiceController)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(TAG, "surface changed")
        if (!myThread!!.isRunning) {
            myThread!!.isRunning = true
            myThread!!.start()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surface destroyed")
        var retry = true
        myThread!!.isRunning = false
        while (retry) {
            try {
                myThread!!.join()
                retry = false
            } catch (e: InterruptedException) {
                Log.d(TAG, "Interrupted Exception", e)
            }

        }
        myThread = null
    }

    override fun onDraw(canvas: Canvas) {
        if (this.displayVoiceController != null) {
            this.displayVoiceController!!.draw(canvas)
        }
    }

    inner class MyThread internal constructor(private val surfaceHolder: SurfaceHolder, private val displayVoiceController: DisplayVoiceController?) : Thread() {
        var isRunning = false
            internal set

        override fun run() {
            var canvas: Canvas? = null
            while (isRunning) {
                try {
                    displayVoiceController?.update()
                    canvas = surfaceHolder.lockCanvas()
                    synchronized(surfaceHolder) {
                        postInvalidate()
                    }
                    Thread.sleep(1)
                } catch (e: InterruptedException) {
                    Log.d(TAG, "Interrupted Exception", e)
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }
        }
    }

    interface DisplayVoiceController {
        /**
         * 绘制声音图像
         * @param canvas
         */
        fun draw(canvas: Canvas)

        /**
         * 异步线程更新数据
         */
        fun update()

        /**
         * 清空数据，开始重绘
         */
        fun clear()

        /**
         * 创建后初始化
         */
        fun onCreate()

    }

    companion object {
        private val TAG = AppConstant.TAG
    }
}
