package com.jj.base;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.jj.R;
import com.jj.swipebacklayout.SwipeBackActivityBase;
import com.jj.swipebacklayout.SwipeBackActivityHelper;
import com.jj.swipebacklayout.SwipeBackLayout;
import com.jj.swipebacklayout.SwipeBackUtils;


/**
 *
 */
public abstract class ToolBarActivity extends AppCompatActivity implements SwipeBackActivityBase {
    /**
     *  侧滑返回
     */
    private SwipeBackActivityHelper mHelper;

    protected LayoutInflater mInflater;
    /**
     * 状态栏着色view
     */
    private View mStatusBarView;
    /**
     * 标题栏
     */
    private Toolbar mToolBar;

    /**
     * Android4.4设置状态栏透明
     * @param on true为设置，false为取消
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setStatusBarTranslucent(boolean on) {
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            if ((params.flags & bits) == 0) {
                params.flags |= bits;
                window.setAttributes(params);
            }
        } else {
            if ((params.flags & bits) != 0) {
                params.flags &= ~bits;
                window.setAttributes(params);
            }
        }
    }

    /**
     * Activity全屏显示，但是状态栏不会被覆盖掉
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setActivityFullscreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     *  侧滑返回
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mHelper != null) {
            mHelper.onPostCreate();
        }
    }
    /**
     *  侧滑返回
     */
    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    /**
     *  侧滑返回
     */
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    /**
     *
     * @return 是否初始化侧滑返回
     */
    protected boolean isInitSwipeBack() {
        return true;
    }

    /**
     *
     * @param size 设置滑动边缘的距离
     */
    protected void setEdgeSize(int size) {
        getSwipeBackLayout().setEdgeSize(size);
    }

    /**
     *
     * @param enable 是否启用滑动
     */
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }
    /**
     *  侧滑返回
     */
    @Override
    public void scrollToFinishActivity() {
        SwipeBackUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setStatusBarTranslucent(true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActivityFullscreen();
        }
        if (isInitSwipeBack()) {
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
        }
        mInflater = getLayoutInflater();
    }

    @Override
    public void setContentView(int layoutResID) {
        View mUserView = mInflater.inflate(layoutResID, null);
        /*获取主题中定义的windowActionBarOverlay标志*/
        TypedArray overlyArray = getTheme().obtainStyledAttributes(new int[] { R.attr.windowActionBarOverlay });
        boolean overly = overlyArray.getBoolean(0, false);
        overlyArray.recycle();
        int _topStatusHeight = Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ? 0 : getStatusBarHeight();
        /*获取主题中定义的toolbar的高度actionBarSize*/
        TypedArray barhArray = getTheme().obtainStyledAttributes(new int[] { R.attr.actionBarSize });
        int toolBarHeight = (int) barhArray.getDimension(0, getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        barhArray.recycle();
        /*如果是悬浮状态，则不需要设置间距*/
        int topPadding = overly ? 0 : (toolBarHeight + _topStatusHeight);
        mUserView.setPadding(0, topPadding, 0, 0);
        super.setContentView(mUserView);
        FrameLayout contentLayout = (FrameLayout) findViewById(android.R.id.content);
        if (!overly && _topStatusHeight > 0) {
            mStatusBarView = statusBarView(_topStatusHeight);
            contentLayout.addView(mStatusBarView);
        }
        if (isInitToolBar()) {
            /*通过inflater获取toolbar的布局文件*/
            View toolbar = mInflater.inflate(R.layout.layout_toolbar, null);
            contentLayout.addView(toolbar);
            FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
            fl.height = toolBarHeight;
            fl.topMargin = _topStatusHeight;
            toolbar.setLayoutParams(fl);
            mToolBar = (Toolbar) toolbar.findViewById(R.id.tb_title);
            setSupportActionBar(mToolBar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(isToolBarBack());
                actionBar.setDisplayShowTitleEnabled(isToolBarTitle());
                actionBar.setHomeAsUpIndicator(getToolBarBackIcon());
            }
        }
    }

    /**
     * 获得状态栏高度
     */
    protected int getStatusBarHeight() {
        int result = 75;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     *
     * @return 状态栏view
     */
    protected View statusBarView(int height) {
        View view = new View(this);
        /*获取主题中定义的toolbar的高度*/
        TypedArray barhArray = getTheme().obtainStyledAttributes(new int[] { R.attr.colorPrimaryDark });
        int statusBarColor = barhArray.getColor(0, getResources().getColor(R.color.jj_colorPrimaryDark));
        barhArray.recycle();
        view.setBackgroundColor(statusBarColor);
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(flp);
        return view;
    }

    /**
     *  设置状态栏颜色Color
     * @param resId id
     */
    protected void setStatusBarColor(@ColorRes int resId) {
        if (mStatusBarView != null)
            mStatusBarView.setBackgroundColor(getResources().getColor(resId));
    }

    /**
     *  设置状态栏颜色Drawable
     * @param resId id
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void setStatusBarDrawable(@DrawableRes int resId) {
        if (mStatusBarView != null)
            mStatusBarView.setBackground(getResources().getDrawable(resId));
    }

    /**
     *
     * @return 是否初始化ToolBar
     */
    protected boolean isInitToolBar() {
        return true;
    }

    /**
     * 隐藏标题栏
     */
    protected void hideToolBar() {
        mToolBar.setVisibility(View.GONE);
    }

    /**
     *
     * @return 是否显示返回图标
     */
    protected boolean isToolBarBack() {
        return true;
    }

    /**
     *
     * @return 是否显示标题
     */
    protected boolean isToolBarTitle() {
        return true;
    }

    /**
     *
     * @return 设置返回图标
     */
    protected int getToolBarBackIcon() {
        return R.mipmap.ic_back;
    }

    /**
     *
     * @return ToolBar
     */
    protected Toolbar getToolBar(){
        return mToolBar;
    }

    /**
     *  给ToolBar添加自定义view
     * @param layoutResID 布局ID
     * @return 布局view
     */
    protected View addViewToolbar(@LayoutRes int layoutResID) {
        View viewToolbar = mInflater.inflate(layoutResID, null);
        addViewToolbar(viewToolbar, 0, 0);
        return viewToolbar;
    }

    /**
     *  给ToolBar添加自定义view
     * @param layoutResID 布局ID
     * @param left 布局距离左边的间距
     * @param right 布局距离右边的间距
     * @return 布局view
     */
    protected View addViewToolbar(@LayoutRes int layoutResID, int left, int right) {
        View viewToolbar = mInflater.inflate(layoutResID, null);
        addViewToolbar(viewToolbar, left, right);
        return viewToolbar;
    }

    /**
     * 给ToolBar添加自定义view
     *
     * @param view 布局
     * @param left 布局距离左边的间距
     * @param right 布局距离右边的间距
     *
     */
    protected void addViewToolbar(View view, int left, int right) {
        //设置中间段内容距离左右的间距
        mToolBar.setContentInsetsRelative(left, right);
        mToolBar.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected RelativeLayout addViewToolbar(int left, int right) {
        //设置中间段内容距离左右的间距
        mToolBar.setContentInsetsRelative(left, right);
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams vlps = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rl.setLayoutParams(vlps);
        mToolBar.addView(rl, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true ;
        }
        return super.onOptionsItemSelected(item);
    }
}