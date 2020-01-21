package com.me.amator.glueplugin_api;

import android.content.Context;
import android.content.Intent;

import com.me.amator.service.api.ServiceInterface;

/**
 * Created by liguangquan on 2020-01-21
 * email: liguangquan@bytedance.com
 * desc:
 **/
@ServiceInterface
public interface IGluePluginDepend {


    String getPackageName();

    Intent getGluePluginActivityIntent(Context contex,String from);

}
