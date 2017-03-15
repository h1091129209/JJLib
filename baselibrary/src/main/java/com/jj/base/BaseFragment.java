package com.jj.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.jj.R;

/**
 * Created by qmsoft05 on 2016/10/18.
 */
public abstract class BaseFragment extends Fragment implements OnBaseLister {
    /**
     * Glide图片加载框架
     */
    private RequestManager mImageLoader;

    protected abstract int getLayoutId();
    protected abstract void initView(View view);
    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void showToast(int msgId) {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnBaseLister) {
            ((OnBaseLister) activity).showToast(msgId);
        }
    }

    @Override
    public void showToast(String msg) {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnBaseLister) {
            ((OnBaseLister) activity).showToast(msg);
        }
    }

    @Override
    public ProgressDialog showWaitDialog(int msgId) {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnBaseLister) {
            return ((OnBaseLister) activity).showWaitDialog(msgId);
        }
        return null;
    }

    @Override
    public ProgressDialog showWaitDialog(String msg) {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnBaseLister) {
            return ((OnBaseLister) activity).showWaitDialog(msg);
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof OnBaseLister) {
            ((OnBaseLister) activity).hideWaitDialog();
        }
    }

    @Override
    public RequestManager getGlideImageLoader() {
        return getImageLoader();
    }

    protected synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }

    /**
     *  内存磁盘都缓存
     * @param iv
     * @param url
     */
    protected void loadImage(ImageView iv, String url) {
        loadImage(iv, url, null);
    }

    /**
     *  内存磁盘都缓存
     * @param iv
     * @param url
     */
    protected void loadImage(ImageView iv, String url, RequestListener<String, GlideDrawable> listener) {
        getImageLoader().load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop()
                .placeholder(R.color.jj_colorAccent)
                .error(R.mipmap.ic_default_image_error).listener(listener).into(iv);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mImageLoader != null) {
            mImageLoader.onDestroy();
        }
    }
}
