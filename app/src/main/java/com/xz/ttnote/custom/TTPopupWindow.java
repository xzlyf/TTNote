package com.xz.ttnote.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xz.ttnote.R;

public class TTPopupWindow extends PopupWindow implements View.OnClickListener{
    private View view;
    private LinearLayout alarm;
    private LinearLayout share;
    private LinearLayout delete;

    public TTPopupWindow(Context context) {
        super(context);

        view = LayoutInflater.from(context).inflate(R.layout.custom_popup_menu, null);
        setContentView(view);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(null);
        setWidth(300);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        initView();



    }

    private void initView() {
        alarm = view.findViewById(R.id.alarm);
        share = view.findViewById(R.id.share);
        delete = view.findViewById(R.id.delete);

        alarm.setOnClickListener(this);
        share.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }
}
