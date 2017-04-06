package com.futhark.android.seevoice.model.message;

/**
 * record status message
 * Created by liuhr on 2/7/17.
 */

public class RecordStatusMessage {

    private int recordCapacity;
    private int recordRate;
    private int channelCount;
    private int hasReadSize;

    public int getRecordCapacity() {
        return recordCapacity;
    }

    public void setRecordCapacity(int recordCapacity) {
        this.recordCapacity = recordCapacity;
    }

    public int getRecordRate() {
        return recordRate;
    }

    public void setRecordRate(int recordRate) {
        this.recordRate = recordRate;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getHasReadSize() {
        return hasReadSize;
    }

    public void setHasReadSize(int hasReadSize) {
        this.hasReadSize = hasReadSize;
    }
}
