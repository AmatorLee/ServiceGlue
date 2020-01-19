package com.me.amator.library2;

import android.util.Log;

import com.me.amator.library1.ITestInterface;
import com.me.amator.service.api.ServiceImpl;

/**
 * Created by liguangquan on 2020-01-19
 * email: liguangquan@bytedance.com
 * desc:
 **/
@ServiceImpl
public class TestImpl implements ITestInterface {
    @Override
    public void log() {
        Log.e(TestImpl.class.getSimpleName(),"try log");
    }
}
