package com.futhark.android.seevoice.activity;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.futhark.android.seevoice.R;
import com.futhark.android.seevoice.base.BaseActivity;
import com.futhark.android.seevoice.message.RecordStatusMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.voice_record_status)
    TextView showVoiceStatus;
    @BindView(R.id.voice_recording)
    ImageButton pressWhenTalking;

    private StringBuffer stringBuffer = new StringBuffer();
    private AudioRecord audioRecord = null;
    private boolean consumed = false;
    private int minBufferSize =  AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
    private int minBufferSize8times = minBufferSize * 8;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(minBufferSize8times);
    private int readTotal = 0;
    private int hasRead = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        pressWhenTalking.setOnTouchListener(touchListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecord();
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecordStatusMessage message){
        stringBuffer.append("sample rate:").append(message.getRecordRate()).append("\n");
        stringBuffer.append("record channel:").append(message.getChannelCount()).append("\n");
        stringBuffer.append("read capacity:").append(byteBuffer.capacity()).append("\n");
        stringBuffer.append("has read").append(byteBuffer.position()).append("\n\n\n");
        showVoiceStatus.setText(stringBuffer);
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            consumed = false;
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    consumed = true;
                    beginToRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    consumed = true;
                    stopRecording();
                    break;
            }
            return consumed;
        }
    };
    private AudioRecord.OnRecordPositionUpdateListener onRecordPositionUpdateListener = new AudioRecord.OnRecordPositionUpdateListener() {
        @Override
        public void onMarkerReached(AudioRecord recorder) {
            recorder.read(byteBuffer, minBufferSize);
            showRecordStatus();
        }

        @Override
        public void onPeriodicNotification(AudioRecord recorder) {
            recorder.read(byteBuffer, minBufferSize);
            showRecordStatus();
        }
    };

    private void beginToRecord(){
        releaseRecord();
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize8times);
        audioRecord.setRecordPositionUpdateListener(onRecordPositionUpdateListener);
        if(audioRecord.getSampleRate() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
        }
    }

    private void stopRecording(){
        readRecordData();
        releaseRecord();
    }

    private void readRecordData(){
        audioRecord.read(byteBuffer, minBufferSize8times);
    }

    private void showRecordStatus(){
        if(audioRecord == null || audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) return;
        RecordStatusMessage message = new RecordStatusMessage();
        message.setRecordCapacity(minBufferSize8times);
        message.setChannelCount(audioRecord.getChannelCount());
        message.setRecordRate(audioRecord.getSampleRate());
        EventBus.getDefault().post(message);
    }

    private void releaseRecord(){
        if(audioRecord != null){
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }
}
