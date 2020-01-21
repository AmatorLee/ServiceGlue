package com.me.amator.serviceglue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.didi.virtualapk.PluginManager;
import com.me.amator.glueplugin_api.IGluePluginDepend;
import com.me.amator.library1.ITestInterface;
import com.me.amator.library1.ITestInterface1;
import com.me.amator.library1.ITestInterface2;
import com.me.amator.service.ServiceGlue;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click_plugin)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            clickPlugin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        findViewById(R.id.click_interface)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickInterface();
                    }
                });

        findViewById(R.id.click_interface1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickInterface1();
                    }
                });
    }


    public void clickInterface(){
        ServiceGlue.getService(ITestInterface.class)
                .log();
    }

    public void clickInterface1(){
        ServiceGlue.getService(ITestInterface1.class)
                .log();
    }

    public void clickPlugin() {
        Intent intent = ServiceGlue.getService(IGluePluginDepend.class)
                .getGluePluginActivityIntent(this, "from_host");
        startActivity(intent);
    }

}
