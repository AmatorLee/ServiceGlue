package com.me.amator.serviceglue;

import android.app.Application;
import android.content.Context;

import com.didi.virtualapk.PluginManager;

/**
 * Created by liguangquan on 2020-01-21
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class GlueApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base)
                .init();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        loadPlugin();
    }

    private void loadPlugin() {
        try {
            GluePluginUtils.getInstance().loadPlugin(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
