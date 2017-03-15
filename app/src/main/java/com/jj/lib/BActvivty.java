package com.jj.lib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.jj.base.BaseActivity;
import com.jj.base.ToolBarActivity;

/**
 * Created by qmsoft05 on 2017/3/9.
 */

public class BActvivty extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.content_main;
    }

    @Override
    protected void initView() {
        ImageView iv = (ImageView) findViewById(R.id.iv);
        loadImage(iv, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489400169461&di=13c5271e9954f7a6285a57bc9e2e0264&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201501%2F25%2F162550z0fcuwan80arhc0w.jpg");
    }

    @Override
    protected void initData() {

    }

}
