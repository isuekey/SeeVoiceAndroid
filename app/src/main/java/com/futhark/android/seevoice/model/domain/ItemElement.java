package com.futhark.android.seevoice.model.domain;

/**
 * item element model
 **/

public class ItemElement {
    private int src;
    private int text;

    public ItemElement(int src, int text) {

        this.src = src;
        this.text = text;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
