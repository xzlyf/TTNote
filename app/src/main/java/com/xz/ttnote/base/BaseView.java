package com.xz.ttnote.base;

public interface BaseView {
    void showLoading(String text);
    void disLoading();
    void sToast(String text);
    void lToast(String text);
    void sDialog(String title, String msg, int type);
    void dDialog();
}
