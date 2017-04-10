package com.futhark.android.seevoice.view;

import android.content.Context;
import android.graphics.Canvas;
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

public class DisplayVoiceView extends SurfaceView implements SurfaceHolder.Callback{
    private static final String TAG = AppConstant.TAG;
    private SurfaceHolder surfaceHolder;
    private MyThread myThread;
    private DisplayVoiceController displayVoiceController;
    private Paint paint;
    public DisplayVoiceView(Context context) {
        super(context, null);
    }

    public DisplayVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DisplayVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    public DisplayVoiceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        setWillNotDraw(false);
    }

    public DisplayVoiceController getDisplayVoiceController() {
        return displayVoiceController;
    }

    public void setDisplayVoiceController(DisplayVoiceController displayVoiceController) {
        this.displayVoiceController = displayVoiceController;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(myThread == null){
            myThread = new MyThread(this.surfaceHolder);
            myThread.setDisplayVoiceController(getDisplayVoiceController());
            myThread.setRunning(true);
            myThread.start();
        }else if(myThread.getDisplayVoiceController() == null){
            myThread.setDisplayVoiceController(getDisplayVoiceController());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        myThread.setRunning(false);
        while (retry){
            try {
                myThread.join();
                retry = false;
            }catch (InterruptedException e){
                Log.d(TAG, "Interrupted Exception", e);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("Hell world! See Voice", getWidth()/2, getHeight()/2, paint);
        if(displayVoiceController != null){
            displayVoiceController.draw(canvas);
        }
    }

    public class MyThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private boolean running = false;
        private DisplayVoiceController displayVoiceController;

        public MyThread(SurfaceHolder surfaceHolder){
            super();
            this.surfaceHolder = surfaceHolder;
        }

        public void setDisplayVoiceController(DisplayVoiceController displayVoiceController) {
            this.displayVoiceController = displayVoiceController;
        }

        public DisplayVoiceController getDisplayVoiceController() {
            return displayVoiceController;
        }

        @Override
        public void run() {
            Canvas canvas = null;
            while (running){
                try {
                    if(displayVoiceController != null){
                        displayVoiceController.update();
                    }
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder){
                        postInvalidate();
                    }
                    wait(100);
                }catch (InterruptedException e){
                    Log.d(TAG, "Interrupted Exception", e);
                }finally {
                    if(canvas != null){
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }
    public interface DisplayVoiceController {
        void draw(Canvas canvas);
        void update();
    }
}

/**
 *
 public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback
 {
 private SurfaceHolder holder;

 private MyThread myThread;

 private GameController gameController;

 private Paint paint;

 private int width;
 private int height;

 public GameSurfaceView(Context context, GameController gameController, int width, int height)
 {
 super(context);
 holder = getHolder();

 holder.addCallback(this);

 this.gameController = gameController;
 this.width = width;
 this.height = height;
 paint = new Paint();
 //initialize paint object parameters

 setWillNotDraw(false); //this line is very important!
 }

 @Override
 public void surfaceCreated(SurfaceHolder holder)
 {
 }

 @Override
 // This is always called at least once, after surfaceCreated
 public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
 {
 if (myThread == null)
 {
 myThread = new MyThread(holder, gameController);
 myThread.setRunning(true);
 myThread.start();
 }
 }

 @Override
 public void surfaceDestroyed(SurfaceHolder holder)
 {
 boolean retry = true;
 myThread.setRunning(false);
 while (retry)
 {
 try
 {
 myThread.join();
 retry = false;
 }
 catch (InterruptedException e)
 {
 Log.d(getClass().getSimpleName(), "Interrupted Exception", e);
 }
 }
 }

 @Override
 public boolean onTouchEvent(MotionEvent event)
 {
 System.out.println(event.getX() + " " + event.getY());
 gameController.onTouchEvent(event); //handle user interaction
 return super.onTouchEvent(event);
 }

 @Override
 protected void onDraw(Canvas canvas)
 {
 super.onDraw(canvas);
 canvas.drawText("Hello world!", width/20, 20, paint);
 gameController.draw(canvas);
 }

 public Thread getThread()
 {
 return thread;
 }

 public class MyThread extends Thread
 {
 private SurfaceHolder holder;
 private boolean running = false;

 private GameController gameController;

 public MyThread(SurfaceHolder holder, GameController gameController)
 {
 this.holder = holder;
 this.gameController = gameController;
 }

 @Override
 public void run()
 {
 Canvas canvas = null;
 while (running)
 {
 gameController.update(); //update the time between last update() call and now
 try
 {
 canvas = holder.lockCanvas(null);
 synchronized (holder)
 {
 postInvalidate();
 }
 }
 finally
 {
 if (canvas != null)
 {
 holder.unlockCanvasAndPost(canvas);
 }
 }
 }

 }

 public void setRunning(boolean b)
 {
 running = b;
 }
 }
 }
 **/