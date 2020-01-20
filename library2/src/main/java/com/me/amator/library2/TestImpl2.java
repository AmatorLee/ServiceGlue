package com.me.amator.library2;

import android.util.Log;

import com.me.amator.library1.ITestInterface2;
import com.me.amator.service.api.PluginServiceImpl;

/**
 * Created by liguangquan on 2020-01-20
 * email: liguangquan@bytedance.com
 * desc:
 **/
@PluginServiceImpl
public class TestImpl2 implements ITestInterface2 {

    @Override
    public void log() {
        Log.e(TestImpl2.class.getSimpleName(),"try Log");
    }
}
