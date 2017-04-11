package com.futhark.android.seevoice.controller.fragment;

import android.graphics.Canvas;
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

    private ExerciseItemModel itemModel;
    @BindView(R.id.touch_pressing_when_talking)
    ImageView pressingWhenTalking;
    @BindView(R.id.display_exercising_voice)
    DisplayVoiceView displayVoiceView;

    private AudioRecord audioRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
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
        displayVoiceView.setDisplayVoiceController(this.displayVoiceController);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(audioRecord == null){
            int minSize = AudioRecord.getMinBufferSize(32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 32000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, 2 * minSize);
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

        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void update() {

        }

        @Override
        public void clear() {

        }
    };
}
