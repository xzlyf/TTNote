package com.xz.ttnote.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.xz.ttnote.MyApplication;
import com.xz.ttnote.custom.LoadingDialog;
import com.xz.ttnote.custom.TipsDialog;
import com.xz.ttnote.utils.ToastUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    private TipsDialog tipDialog;
    private LoadingDialog loadingDialog;
    private Bundle bundle;

    abstract protected int getLayoutResource();

    abstract protected boolean homeAsUpEnabled();

    protected abstract void initData();

    private final int SHOWD = 111;
    private final int STOAST = 112;
    private final int LTOAST = 113;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOWD:
                    String[] ary = (String[]) msg.obj;
                    _showDialog(ary[0], ary[1], msg.arg1);
                    break;
                case STOAST:
                    _shortToast((String) msg.obj);
                    break;
                case LTOAST:
                    _longToast((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        //设置是否开启返回homeAsUp按钮
        if (homeAsUpEnabled()) {
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setHomeButtonEnabled(true);
                bar.setDisplayHomeAsUpEnabled(true);

            }
        }
        this.bundle = savedInstanceState;
        initData();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  //id不要写错，前面要加android
                onBackPressed();
                break;
        }
        return true;
    }

    public Bundle getBundle(){
        return bundle;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从集合中移除
        MyApplication.getInstance().finishActivity(this);
    }

    @Override
    public void sToast(String text) {
        //使用Handler回到主线程操作
        Message message = handler.obtainMessage();
        message.what = STOAST;
        message.obj = text;
        handler.sendMessage(message);
    }

    @Override
    public void lToast(String text) {
        //使用Handler回到主线程操作
        Message message = handler.obtainMessage();
        message.what = LTOAST;
        message.obj = text;
        handler.sendMessage(message);
    }

    @Override
    public void showLoading(String text) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        loadingDialog = new LoadingDialog.Builder(this)
                .setTitle("加载中...")
                .setCancelOnClickListeren(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.dismiss();
                    }
                })
                .create();
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void disLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 显示一个对话框，并指定类型
     *
     * @param msg
     * @param type
     */
    @Override
    public void sDialog(String title, String msg, int type) {
        //使用Handler回到主线程操作
        Message message = handler.obtainMessage();
        message.what = SHOWD;
        message.arg1 = type;
        message.obj = new String[]{title, msg};
        handler.sendMessage(message);
    }

    /**
     * 销毁对话框
     */
    @Override
    public void dDialog() {
        if (tipDialog == null) {
            return;
        }
        if (tipDialog.isShowing())
            tipDialog.dismiss();
        if (tipDialog != null) {
            tipDialog = null;
        }
    }

    /**
     * UI线程操作，显示对话框
     *
     * @param title
     * @param msg
     * @param type
     */
    private void _showDialog(String title, String msg, int type) {

        if (tipDialog != null) {
            tipDialog.dismiss();
        }
        tipDialog = new TipsDialog.Builder(this)
                .setTitle(title)
                .setMeg(msg)
                .setIcon(type)
                .create();
        tipDialog.show();

    }

    /**
     * UI现场操作，显示短Toast
     *
     * @param text
     */
    private void _shortToast(String text) {
        ToastUtil.Shows(this, text);
    }

    /**
     * UI现场操作，显示短Toast
     *
     * @param text
     */
    private void _longToast(String text) {
        ToastUtil.Shows_LONG(this, text);
    }


    /**
     * 自动隐藏软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

            }
            return super.dispatchTouchEvent(ev);

        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);

    }

    /**
     * 判断是否应该隐藏软键盘
     *
     * @param view
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域
                return false;
            } else {
                //清除输入框焦点
                view.clearFocus();
                return true;
            }
        }
        return false;
    }


}
