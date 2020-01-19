package com.me.amator.serviceglue;

import android.util.Log;

import com.me.amator.library1.ITestInterface1;
import com.me.amator.service.api.InstanceProvider;
import com.me.amator.service.api.ServiceImpl;

/**
 * Created by liguangquan on 2020-01-19
 * email: liguangquan@bytedance.com
 * desc:
 **/
@ServiceImpl
public class TestImpl1 implements ITestInterface1 {

    private TestImpl1() {

    }

    @InstanceProvider
    public static TestImpl1 getInstance() {
        return new TestImpl1();
    }

    @Override
    public void log() {
        Log.e(TestImpl1.class.getSimpleName(), "try log");
    }
}
