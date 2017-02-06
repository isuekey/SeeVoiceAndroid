package com.futhark.android.seevoice;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.*;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    public static final String TAG = "see_tag";
    @BindView(R.id.show_voice) SurfaceView showVoiceView;
    @BindView(R.id.press_when_talking) FloatingActionButton pressWhenTalking;
    @BindView(R.id.running_status)
    TextView runningStatusText;

    private String audioFileName;
    private List<String> fileNameList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SpeechRecognizer speechRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        boolean available = speechRecognizer.isRecognitionAvailable(this);
        runningStatusText.setText(available ? R.string.recognizer_service_available : R.string.recognizer_service_incapable);
        //pressWhenTalking = (FloatingActionButton)findViewById(R.id.press_when_talking);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pressWhenTalking.setOnTouchListener(onTouchPressWhenTalking);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAudioRecord();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        File file;
        for(String fileName : fileNameList){
            if(fileName != null){
                file = new File(fileName);
                if(file.exists()) file.delete();
            }
        }
        if( speechRecognizer != null){
            speechRecognizer.stopListening();
        }
    }
    @Override
    protected void onDestroy(){
        if(speechRecognizer != null){
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        super.onDestroy();
    }

    private long beginTime;
    private View.OnTouchListener onTouchPressWhenTalking = new View.OnTouchListener() {
        long touchTime;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            touchTime = System.currentTimeMillis();
            if(isPlaying) return false;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    beginTime = System.currentTimeMillis();
                    audioFileName = getAudioFileName(beginTime);
                    prepareAudioRecord();
                    startAudioRecord();
                    return true;
                case MotionEvent.ACTION_UP:
                    stopAudioRecord();
                    return touchTime > beginTime;
            }
            return false;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            runningStatusText.setText("complete");
            isPlaying = false;
        }
    };

    private String getAudioFileName(long beginTime){
        String audioFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        audioFileName+= "/audio"+beginTime+".3gp";
        return audioFileName;
    }

    private void prepareAudioRecord(){
    }

    private void startAudioRecord(){
    }

    private void stopAudioRecord(){
        fileNameList.add(audioFileName);
    }

    private void playAudioRecord(){
        mediaPlayer = new MediaPlayer();
        isPlaying = true;
        try {
            if(audioFileName != null){
                runningStatusText.setText("begin");
                mediaPlayer.setOnCompletionListener(onCompletionListener);
                Log.d(TAG, "audio file name : "+ audioFileName);
                mediaPlayer.setDataSource(audioFileName);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
    }
    private void releaseMediaPlayer(){
        if(mediaPlayer == null) return;
        isPlaying = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
