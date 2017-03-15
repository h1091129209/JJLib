package com.jj.network;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

import java.io.File;


/**
 * Glide全局配置，使用okhttp，需要在manifest中加meta-data
 * Created by qmsoft05 on 2016/10/19.
 */
public class MyGlideModule extends OkHttpGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        File file = context.getExternalCacheDir();
        builder.setDiskCache(new DiskLruCacheFactory(file.getAbsolutePath(), "glides", 1024 * 1024 * 100));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        super.registerComponents(context, glide);
    }
}
