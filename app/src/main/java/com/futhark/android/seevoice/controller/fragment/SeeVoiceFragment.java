package com.futhark.android.seevoice.controller.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.view.DisplayVoiceView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * exercising fragment
 * Created by liuhr on 07/04/2017.
 */

public class SeeVoiceFragment extends BaseFragment {
    public static final String FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL = "fragment_exercising_argument_item_model";
    public static SeeVoiceFragment newInstance(short[] initData){
        SeeVoiceFragment fragment = new SeeVoiceFragment();
        if(initData != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, initData);
            fragment.setArguments(arguments);
        }
        return fragment;
    }
    private int recordDataSize = 0;

    private short[] initData;
    @BindView(R.id.touch_pressing_when_talking)
    ImageView pressingWhenTalking;
    @BindView(R.id.display_exercising_voice)
    DisplayVoiceView displayVoiceView;

    private AudioRecord audioRecord;
    private Paint paint;
    private boolean isRecording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        if(arguments == null) return;
        if(arguments.containsKey(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL)){
            this.initData = (short[]) arguments.getSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_see_voice, container, false);
        ButterKnife.bind(this, view);
        pressingWhenTalking.setOnTouchListener(this.onTouchListener);
        recordDataSize = AudioRecord.getMinBufferSize(32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT) * 2;
        this.displayVoiceController.onCreate();
        displayVoiceView.setDisplayVoiceController(this.displayVoiceController);
        if(initData != null){
            this.displayVoiceController.setVoiceRecord(initData);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(audioRecord == null){
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 2 * recordDataSize);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(audioRecord != null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    displayVoiceController.clear();
                    audioRecord.startRecording();
                    isRecording = true;
                    Log.d(Companion.getTAG(),"start to record");
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if(audioRecord != null) audioRecord.stop();
                    isRecording = false;
                    Log.d(Companion.getTAG(),"stop recording");
                    break;
            }
            return true;
        }
    };

    interface SeeVoiceController extends DisplayVoiceView.DisplayVoiceController{
        short[] getLastVoiceRecord();
        void setVoiceRecord(short[] voiceData);
    }

    private SeeVoiceController displayVoiceController = new SeeVoiceController() {
        private short[] recordData = null;
        private short[] recordDisplayData = null;
        private float[] displayData = null;
        private short empty = 0;
        private int currentPosition = 0;

        @Override
        public void draw(Canvas canvas) {
//            int startAt = currentPosition - recordDataSize < 0 ? 0 : currentPosition - recordDataSize;
            int startAt = 0;
//            int dataLength = recordDataSize;
            int dataLength = currentPosition;
            Rect canvasRect = canvas.getClipBounds();
            int paddingTop = canvasRect.height() /20;
            int wavePeak = canvasRect.height() /2 - paddingTop;

            paint.setColor(Color.WHITE);
            int verticalCenter = canvasRect.height() / 2;
            float max = 32767f;
            float zoom = 4;
            float dotWidth = canvasRect.width() * 1.0f / (dataLength + 1);
            float dotHeight = wavePeak * 1.f / max;
            float startX = 0f;
            float startY = canvasRect.height() / 2;
            float endX, endY;
            int displayIndex;
            for(int index = startAt; index < dataLength; ++index){
                endX = index * dotWidth;
                endY = recordData[index] * dotHeight * zoom + verticalCenter;
                displayIndex = index * 4;
                displayData[displayIndex] = startX;
                displayData[displayIndex + 1] = startY;
                displayData[displayIndex + 2] = endX;
                displayData[displayIndex + 3] = endY;
                startX = endX;
                startY = endY;
            }
            canvas.drawLines(displayData, paint);
        }

        @Override
        public void update() {
            if(!isRecording) return;
            int read = audioRecord.read(recordDisplayData, 0, recordDataSize);
            switch (read){
                case AudioRecord.ERROR:
                    Log.d(Companion.getTAG()," 不知道什么东西错了");
                    break;
                case AudioRecord.ERROR_BAD_VALUE:
                    Log.d(Companion.getTAG(), "数据格式有问题");
                    break;
                case AudioRecord.ERROR_INVALID_OPERATION:
                    Log.d(Companion.getTAG(), "操作不正确");
                    break;
                default:
                    if(read > 0) {
                        System.arraycopy(recordDisplayData, 0, recordData, 0, read);
                        currentPosition = recordData.length;
                    }
                    break;
            }
        }

        @Override
        public void clear() {
            Arrays.fill(recordData, empty);
            Arrays.fill(recordDisplayData, empty);
            Arrays.fill(displayData, empty);
            currentPosition = 0;
        }

        @Override
        public void onCreate() {
            recordData = new short[recordDataSize ];
            recordDisplayData = new short[recordDataSize];
            displayData = new float[recordDataSize * 4];
        }

        @Override
        public short[] getLastVoiceRecord() {
            return recordData;
        }

        @Override
        public void setVoiceRecord(short[] voiceData) {
            this.recordData = voiceData;
            currentPosition = recordData.length;
        }
    };

    public final short[] getLastRecordData(){
        if(displayVoiceController != null){
            return displayVoiceController.getLastVoiceRecord();
        }
        return null;
    }
}
