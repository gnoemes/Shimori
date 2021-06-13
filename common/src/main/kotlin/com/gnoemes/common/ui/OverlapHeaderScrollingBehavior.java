package com.gnoemes.common.ui;

import android.content.Context;
import android.util.AttributeSet;

public class OverlapHeaderScrollingBehavior extends FixScrollingFooterBehavior {

    public OverlapHeaderScrollingBehavior() {
        super();
    }

    public OverlapHeaderScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean shouldHeaderOverlapScrollingChild() {
        return true;
    }
}
