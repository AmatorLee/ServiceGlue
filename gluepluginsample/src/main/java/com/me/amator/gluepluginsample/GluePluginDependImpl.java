package com.me.amator.gluepluginsample;

import android.content.Context;
import android.content.Intent;

import com.me.amator.glueplugin_api.IGluePluginDepend;
import com.me.amator.service.api.PluginServiceImpl;

/**
 * Created by liguangquan on 2020-01-21
 * email: liguangquan@bytedance.com
 * desc:
 **/
@PluginServiceImpl
public class GluePluginDependImpl implements IGluePluginDepend {
    @Override
    public String getPackageName() {
        return "com.me.amator.gluepluginsample";
    }

    @Override
    public Intent getGluePluginActivityIntent(Context context, String from) {
        Intent intent = new Intent(context, GluePluginActivity.class);
        intent.putExtra("enter_from", from);
        return intent;
    }
}
