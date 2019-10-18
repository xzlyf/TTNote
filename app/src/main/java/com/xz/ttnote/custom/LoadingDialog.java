package com.xz.ttnote.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xz.ttnote.R;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private String mTitle;
    private int mIcon = -1;
    private ImageView tvIcon;
    private TextView tvTitle;
    private View.OnClickListener mCancel;
    private TextView tvCancel;


    public LoadingDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        Window dialogWindow = getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);//背景透明
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8);
        lp.dimAmount = 0.2f;//背景不变暗
        dialogWindow.setAttributes(lp);
        initView();
    }

    private void initView() {
        tvIcon = findViewById(R.id.tv_icon);
        tvTitle = findViewById(R.id.tv_title);
        tvCancel = findViewById(R.id.tv_cancel);
    }

    @Override
    public void show() {
        super.show();
        show(this);
    }


    /**
     * 设置自定义的属性
     *
     * @param dialog
     */
    private void show(LoadingDialog dialog) {
        //================================
        if (!TextUtils.isEmpty(dialog.mTitle)) {
            dialog.tvTitle.setText(dialog.mTitle);
        } else {
            dialog.tvTitle.setText("加载中");
        }
        //============================
        if (dialog.mIcon != -1) {
            Glide.with(mContext).load(dialog.mIcon).into(dialog.tvIcon);
        } else {
            dialog.tvIcon.setVisibility(View.INVISIBLE);
        }
        //============================
        if (dialog.mCancel != null) {
            tvCancel.setOnClickListener(mCancel);
        }else{
            tvCancel.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private LoadingDialog tipsDialog;

        public Builder(Context context) {
            tipsDialog = new LoadingDialog(context);
        }

        /**
         * 标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            tipsDialog.mTitle = title;
            return this;
        }

        /**
         * 图标
         *
         * @param i
         * @return
         */
        public Builder setIcon(int i) {
            tipsDialog.mIcon = i;
            return this;
        }

        /**
         * 取消监听
         *
         * @return
         */
        public Builder setCancelOnClickListeren(View.OnClickListener listeren) {
            tipsDialog.mCancel = listeren;
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public LoadingDialog create() {
            return tipsDialog;
        }

    }
}
