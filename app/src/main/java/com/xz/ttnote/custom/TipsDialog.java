package com.xz.ttnote.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xz.ttnote.R;

public class TipsDialog extends Dialog {

    private Context mContext;
    private String mTitle, mMsg, submitText, cancelText;
    private int mIcon = -1;
    private View.OnClickListener mSubmit;
    private View.OnClickListener mCancel;

    private TextView tvTitle;
    private TextView tvMsg;
    private ImageView tvIcon;
    private Button tvCancel;
    private Button tvSubmit;

    public TipsDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
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
        tvTitle = findViewById(R.id.tv_title);
        tvMsg = findViewById(R.id.tv_msg);
        tvIcon = findViewById(R.id.tv_icon);
        tvCancel = findViewById(R.id.tv_cancel);
        tvSubmit = findViewById(R.id.tv_submit);
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
    private void show(TipsDialog dialog) {
        //================================
        if (!TextUtils.isEmpty(dialog.mTitle)) {
            dialog.tvTitle.setText(dialog.mTitle);
        } else {
            dialog.tvTitle.setText("提示");
        }
        //=================================
        if (!TextUtils.isEmpty(dialog.mMsg)) {
            dialog.tvMsg.setText(dialog.mMsg);
        } else {
            dialog.tvMsg.setText("（这里什么都没有）");
        }
        //============================
        if (dialog.mIcon != -1) {
            dialog.tvIcon.setImageResource(dialog.mIcon);
        } else {
            dialog.tvIcon.setVisibility(View.GONE);
        }
        //============================
        if (dialog.mSubmit != null) {
            tvSubmit.setText(submitText);
            tvSubmit.setOnClickListener(mSubmit);
        } else {
            tvSubmit.setVisibility(View.GONE);
        }
        //============================
        if (dialog.mCancel != null) {
            tvCancel.setText(dialog.cancelText);
            tvCancel.setOnClickListener(mCancel);
        } else {
            tvCancel.setVisibility(View.GONE);
        }
    }

    public static class Builder {
        private TipsDialog tipsDialog;

        public Builder(Context context) {
            tipsDialog = new TipsDialog(context);
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
         * 内容
         *
         * @param msg
         * @return
         */
        public Builder setMeg(String msg) {
            tipsDialog.mMsg = msg;
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
         * 提交按钮监听
         *
         * @param text
         * @param listener
         * @return
         */
        public Builder setSubmitButtonListener(String text, View.OnClickListener listener) {
            tipsDialog.submitText = text;
            tipsDialog.mSubmit = listener;
            return this;
        }

        /**
         * 取消按钮监听
         *
         * @param text
         * @param listener
         * @return
         */
        public Builder setCancelButtonListener(String text, View.OnClickListener listener) {
            tipsDialog.cancelText = text;
            tipsDialog.mCancel = listener;
            return this;
        }

        /**
         * 通过Builder类设置完属性后构造对话框的方法
         */
        public TipsDialog create() {
            return tipsDialog;
        }

    }
}
