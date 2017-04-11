package com.futhark.android.seevoice.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.futhark.android.seevoice.model.constant.AppConstant;

/**
 * the view of displaying voice
 * Created by liuhr on 08/04/2017.
 */

public class DisplayVoiceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = AppConstant.TAG;
    private SurfaceHolder surfaceHolder;
    private MyThread myThread;
    private DisplayVoiceController displayVoiceController;
    private Paint paint;

    public DisplayVoiceView(Context context) {
        this(context, null);
        Log.d(TAG, "display voice view create1");
    }

    public DisplayVoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.d(TAG, "display voice view create2");
    }

    public DisplayVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        Log.d(TAG, "display voice view create3");
    }

    public DisplayVoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        setWillNotDraw(false);
        Log.d(TAG, "display voice view create4");
    }

    public DisplayVoiceController getDisplayVoiceController() {
        return displayVoiceController;
    }

    public void setDisplayVoiceController(DisplayVoiceController displayVoiceController) {
        this.displayVoiceController = displayVoiceController;
        Log.d(TAG, "set display voice controller");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surface created");
        if(getDisplayVoiceController() == null){
            throw new RuntimeException("display voice controller should be set before the view will display");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surface changed");
        if (myThread == null) {
            myThread = new MyThread(this.surfaceHolder, getDisplayVoiceController());
            myThread.setRunning(true);
            myThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surface destroyed");
        boolean retry = true;
        myThread.setRunning(false);
        while (retry) {
            try {
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(TAG, "Interrupted Exception", e);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("Hell world! See Voice", getWidth() / 2, getHeight() / 2, paint);
        if (displayVoiceController != null) {
            displayVoiceController.draw(canvas);
        }
    }

    public class MyThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private boolean running = false;
        private final DisplayVoiceController displayVoiceController;

        MyThread(SurfaceHolder surfaceHolder, DisplayVoiceController displayVoiceController) {
            super();
            this.surfaceHolder = surfaceHolder;
            this.displayVoiceController = displayVoiceController;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running) {
                try {
                    if (displayVoiceController != null) {
                        displayVoiceController.update();
                    }
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        postInvalidate();
                    }
                    sleep(10);
                } catch (InterruptedException e) {
                    Log.d(TAG, "Interrupted Exception", e);
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void setRunning(boolean running) {
            this.running = running;
        }
    }

    public interface DisplayVoiceController {
        /**
         * 绘制声音图像
         * @param canvas
         */
        void draw(Canvas canvas);

        /**
         * 异步线程更新数据
         */
        void update();

        /**
         * 清空数据，开始重绘
         */
        void clear();

        /**
         * 创建后初始化
         */
        void onCreate();
    }
}
