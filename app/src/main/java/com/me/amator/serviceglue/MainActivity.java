package com.me.amator.serviceglue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.me.amator.library1.ITestInterface;
import com.me.amator.library1.ITestInterface1;
import com.me.amator.library1.ITestInterface2;
import com.me.amator.service.ServiceGlue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void clickInterface(View view){
        ServiceGlue.getService(ITestInterface.class)
                .log();
    }

    public void clickInterface1(View v){
        ServiceGlue.getService(ITestInterface1.class)
                .log();
    }


    public void ClickInterface2(View v){
        ServiceGlue.getService(ITestInterface2.class).log();
    }

}
