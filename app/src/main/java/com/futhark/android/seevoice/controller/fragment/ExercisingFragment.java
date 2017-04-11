package com.futhark.android.seevoice.controller.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseFragment;
import com.futhark.android.seevoice.model.domain.ExerciseItemModel;
import com.futhark.android.seevoice.view.DisplayVoiceView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * exercising fragment
 * Created by liuhr on 07/04/2017.
 */

public class ExercisingFragment extends BaseFragment {
    public static final String FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL = "fragment_exercising_argument_item_model";
    public static ExercisingFragment newInstance(@NonNull  ExerciseItemModel itemModel){
        ExercisingFragment fragment = new ExercisingFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL, itemModel);
        fragment.setArguments(arguments);
        return fragment;
    }
    private int recordDataSize = 0;

    private ExerciseItemModel itemModel;
    @BindView(R.id.touch_pressing_when_talking)
    ImageView pressingWhenTalking;
    @BindView(R.id.display_exercising_voice)
    DisplayVoiceView displayVoiceView;

    private AudioRecord audioRecord;
    private Paint paint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        if(arguments == null) return;
        if(arguments.containsKey(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL)){
            this.itemModel = (ExerciseItemModel) arguments.getSerializable(FRAGMENT_EXERCISING_ARGUMENT_ITEM_MODEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercising, container, false);
        ButterKnife.bind(this, view);
        pressingWhenTalking.setOnTouchListener(this.onTouchListener);
        recordDataSize = AudioRecord.getMinBufferSize(32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        this.displayVoiceController.onCreate();
        displayVoiceView.setDisplayVoiceController(this.displayVoiceController);
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
                    Log.d(TAG,"start to record");
                    break;
                case MotionEvent.ACTION_UP:
                    audioRecord.stop();
                    Log.d(TAG,"stop recording");
                    break;
            }
            return true;
        }
    };
    private DisplayVoiceView.DisplayVoiceController displayVoiceController = new DisplayVoiceView.DisplayVoiceController() {
        private short[] recordData = null;
        private short[] recordDisplayData = null;
        private short empty = 0;
        private int currentPosition = 0;
        Path wavePath;
        @Override
        public void draw(Canvas canvas) {
            int startAt = currentPosition - recordDataSize < 0 ? 0 : currentPosition - recordDataSize;
            int dataLength = currentPosition - startAt;
            Rect canvasRect = canvas.getClipBounds();
            int paddingTop = canvasRect.height() /20;
            int wavePeak = canvasRect.height() /2 - paddingTop;
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);

            paint.setColor(Color.WHITE);
            wavePath = new Path();
            int verticalCenter = canvasRect.height() / 2;
            float max = 32767f;
            float zoom = 16;
            float dotWidth = canvasRect.width() * 1.0f / recordDataSize;
            for(int index = startAt; index < dataLength; ++index){
                wavePath.lineTo((index - startAt) * dotWidth, recordData[index] * zoom / max + verticalCenter);
            }
            canvas.drawPath(wavePath, paint);

        }

        @Override
        public void update() {
            if(audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) return;
            int read = audioRecord.read(recordDisplayData, currentPosition, recordDataSize);
            switch (read){
                case AudioRecord.ERROR:
                    Log.d(TAG," 不知道什么东西错了");
                    break;
                case AudioRecord.ERROR_BAD_VALUE:
                    Log.d(TAG, "数据格式有问题");
                    break;
                case AudioRecord.ERROR_INVALID_OPERATION:
                    Log.d(TAG, "状态不正确");
                    break;
                default:
                    if(read > 0) {
                        System.arraycopy(recordDisplayData, 0, recordData, currentPosition, read);
                        currentPosition += read;
                    }
                    break;
            }
        }

        @Override
        public void clear() {
            Arrays.fill(recordData, empty);
            Arrays.fill(recordDisplayData, empty);
            currentPosition = 0;
        }

        @Override
        public void onCreate() {
            recordData = new short[recordDataSize * 2];
            recordDisplayData = new short[recordDataSize];
        }
    };
}
