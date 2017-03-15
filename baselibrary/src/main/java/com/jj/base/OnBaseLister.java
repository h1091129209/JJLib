package com.jj.base;

import android.app.ProgressDialog;

import com.bumptech.glide.RequestManager;

/**
 * Created by qmsoft05 on 2016/8/16.
 */
public interface OnBaseLister {
    void showToast(int msgId);
    void showToast(String msg);
    ProgressDialog showWaitDialog(int msgId);
    ProgressDialog showWaitDialog(String msg);
    void hideWaitDialog();
    RequestManager getGlideImageLoader();
}
