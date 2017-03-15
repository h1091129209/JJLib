package com.jj.lib;

import com.jj.util.AndroidBug5497Workaround;
import com.jj.base.BaseActivity;

/**
 * Created by qmsoft05 on 2017/3/13.
 */

public class EActivity extends BaseActivity {
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_e;
    }

    @Override
    protected boolean isToolBarBack() {
        return false;
    }

    @Override
    protected void initView() {
        addViewToolbar(R.layout.title_e);
        AndroidBug5497Workaround.assistActivity(this, getStatusBarHeight());
    }

    @Override
    protected void initData() {

    }
}
