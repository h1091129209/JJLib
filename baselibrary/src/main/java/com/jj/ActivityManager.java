package com.jj;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * activity管理类
 */
public class ActivityManager extends Application {
	private ArrayList<AppCompatActivity> mList = new ArrayList<AppCompatActivity>();
	private static ActivityManager instance;

	private ActivityManager() {
	}

	public synchronized static ActivityManager getInstance() {
		if (null == instance) {
			instance = new ActivityManager();
		}
		return instance;
	}

	/**
	 * 添加activity
	 * @param activity
     */
	public void addActivity(AppCompatActivity activity) {
		mList.add(activity);
	}

	/**
	 * 删除activity
	 * @param activity
     */
	public void removeActivity(AppCompatActivity activity) {
		mList.remove(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(AppCompatActivity activity) {
		if (activity != null && !activity.isFinishing()) {
			mList.remove(activity);
			activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (AppCompatActivity activity : mList) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
				break;
			}
		}
	}

	/**
	 * 退出APP
	 */
	public void exitApp() {
		try {
			for (AppCompatActivity activity : mList) {
				if (activity != null && !activity.isFinishing())
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

}
