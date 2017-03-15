package com.jj.lib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;

import com.jj.base.BaseActivity;
import com.jj.base.ToolBarActivity;
import com.jj.swipebacklayout.SwipeBackActivity;

/**
 * Created by qmsoft05 on 2017/3/9.
 */

public class CActvivty extends BaseActivity {

    @Override
    protected int getLayoutResource() {
        return R.layout.content_main;
    }

    @Override
    protected void initView() {
        setStatusBarDrawable(R.drawable.drawable_status_bar_bg);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        loadImage(iv, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1489400169461&di=13c5271e9954f7a6285a57bc9e2e0264&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201501%2F25%2F162550z0fcuwan80arhc0w.jpg");
    }

    @Override
    protected void initData() {
        getSwipeBackLayout().setEdgeSizeMax(true);
    }

    @Override
    protected int getToolBarBackIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
