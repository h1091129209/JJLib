package com.jj.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.jj.ActivityManager;
import com.jj.R;
import com.jj.network.GlideCircleTransform;

/**
 * Created by qmsoft05 on 2017/3/8.
 */

public abstract class BaseActivity extends ToolBarActivity implements OnBaseLister {
    /**
     * Glide图片加载框架
     */
    private RequestManager mImageLoader;

    private ProgressDialog _waitDialog;

    protected abstract int getLayoutResource();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ActivityManager.getInstance().addActivity(this);
        initView();
        initData();
    }

    /**
     * 是否有权限
     * @param strs
     * @return
     */
    protected boolean showPermissionsResult(String strs) {
        if (Build.VERSION.SDK_INT >= 23) {
            //是否授权
            if (ActivityCompat.checkSelfPermission(this, strs) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            //弹出请求权限对话框，100是对应下面成功的请求码
            ActivityCompat.requestPermissions(this, new String[] { strs }, 100);
            return false;
        }
        return true;
    }

    /**
     * 对应权限请求成功
     */
    protected void onPermissionsSuccess(String permission) {
    }

    /**
     * 对应权限请求失败
     */
    protected void onPermissionsError(String permission) {
        showToast(R.string.j_permissions_no);
        finish();
    }

    /**
     * 对应权限请求选择不再提醒
     */
    protected void onPermissionsRationale(final String permission) {
        //选择了不再提醒的处理方法
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            // 当前版本的包名
            final String packageNames = info.packageName;
            //选择了不再提醒的处理方法
            new AlertDialog.Builder(this).setTitle(R.string.j_permissions_no_authorization).setMessage(getResources().getString(R.string.j_permissions_no_authorization_msg)).setCancelable(false).setPositiveButton(R.string.j_permissions_set, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", packageNames, null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }).setNegativeButton(R.string.j_permissions_ext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onPermissionsError(permission);
                }
            }).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("base", permissions.toString()+"***"+grantResults.toString()+requestCode+"");
        if (requestCode == 100 && grantResults.length > 0 && permissions.length > 0) {
            //对应权限0为请求成功
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsSuccess(permissions[0]);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                onPermissionsError(permissions[0]);
            } else {
                onPermissionsRationale(permissions[0]);
            }
        }
    }

    @Override
    public void showToast(int msgId) {
        Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ProgressDialog showWaitDialog(int msgId) {
        return showWaitDialog(getResources().getString(msgId));
    }

    @Override
    public ProgressDialog showWaitDialog(String msg) {
        if (_waitDialog == null) {
            _waitDialog = new ProgressDialog(this);
        }
        _waitDialog.setCancelable(false);
        _waitDialog.setMessage(msg);
        _waitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    try {
                        if (_waitDialog != null) {
                            _waitDialog.dismiss();
                            _waitDialog = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        _waitDialog.show();
        return _waitDialog;
    }

    @Override
    public void hideWaitDialog() {
        if (_waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    /**
     *  只内存缓存图片
     * @param iv
     * @param url
     */
    protected void loadMemoryCacheImage(ImageView iv, String url) {
        getImageLoader().load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .placeholder(R.color.jj_colorAccent)
                .error(R.mipmap.ic_default_image_error).into(iv);
    }

    /**
     *  只磁盘缓存图片
     * @param iv
     * @param url
     */
    protected void loadDiskImage(ImageView iv, String url) {
        getImageLoader().load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)// 缓存策略all:缓存源资源和转换后的资源none:不作任何磁盘缓存 source:缓存源资源 result：缓存转换后的资源
                .skipMemoryCache(true) //true跳过内存缓存
                .centerCrop() // 长的一边撑满
                .placeholder(R.color.jj_colorAccent)
                .error(R.mipmap.ic_default_image_error).into(iv);
    }

    /**
     *  转化圆形图片
     * @param iv
     * @param url
     */
    protected void loadCircleImage(ImageView iv, String url) {
        getImageLoader().load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(this))
                .placeholder(R.color.jj_colorAccent)
                .error(R.mipmap.ic_default_image_error).into(iv);
    }

    @Override
    public void onDestroy() {
        if (mImageLoader != null) {
            mImageLoader.onDestroy();
        }
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    /**
    protected void s(ImageView iv, String url) {
        DrawableRequestBuilder builder = getImageLoader().load(url)
//                .asBitmap().asGif()必须在.load(yourUrl)之后马上调用 .asBitmap()或.asGif()
                .skipMemoryCache(true) //true跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 缓存策略all:缓存源资源和转换后的资源none:不作任何磁盘缓存 source:缓存源资源 result：缓存转换后的资源
                .centerCrop() // 长的一边撑满
                .fitCenter() // 短的一边撑满
                .thumbnail(0.1f) //这样会先加载缩略图 然后在加载全图
//                .transform(new BitmapRoundTransformation(this))
                .animate(R.anim.item_alpha_in) //动画
                .dontAnimate() //移除所有的动画。
                .placeholder(R.color.colorAccent)
                .error(R.mipmap.ic_default_image_error)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //异常
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        //成功
                        return false;
                    }
                });
        builder.into(iv);
        //设置图片解码格式
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
        Glide.get(this).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
        Glide.get(this).clearMemory();//清理内存缓存  可以在UI主线程中进行
    }
     **/

}
