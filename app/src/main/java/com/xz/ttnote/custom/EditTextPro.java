package com.xz.ttnote.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class EditTextPro extends EditText {
    private Context mContext;
    private View mView;

    public EditTextPro(Context context) {
        super(context);
        init(context);
    }

    public EditTextPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
