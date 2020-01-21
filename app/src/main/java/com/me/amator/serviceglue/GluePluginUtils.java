package com.me.amator.serviceglue;

import android.app.Activity;
import android.content.Context;

import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;
import com.didi.virtualapk.internal.utils.PluginUtil;
import com.me.amator.glueplugin_api.IGluePluginDepend;
import com.me.amator.service.ServiceGlue;

import java.io.File;

/**
 * Created by liguangquan on 2020-01-21
 * email: liguangquan@bytedance.com
 * desc:
 **/
public class GluePluginUtils {

    private static final String GLUE_PLUGIN_APK = "GluePlugin.apk";
    private static final String GLUE_PLUHIN_PACKAGE_NAME = "com.me.amator.gluepluginsample";

    private GluePluginUtils() {

    }

    public static GluePluginUtils getInstance() {
        return GluePluginUtilsHolder.sInstance;
    }

    private static class GluePluginUtilsHolder {
        static GluePluginUtils sInstance = new GluePluginUtils();
    }

    //检测是否已经安装了插件，未安装则通过loadPlugin安装

    public boolean loadPlugin(Context context) throws Exception {
        File apk = new File(context.getExternalCacheDir(), GLUE_PLUGIN_APK);
        if (apk.exists()) {
            //加载插件
            PluginManager.getInstance(context.getApplicationContext())
                    .loadPlugin(apk);
            return true;
        }
        //插件不存在
        return false;
    }
}
