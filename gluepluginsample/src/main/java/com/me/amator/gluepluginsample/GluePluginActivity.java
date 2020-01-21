package com.me.amator.gluepluginsample;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class GluePluginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plugin_activity_main);
        String from = getIntent().getStringExtra("enter_from");
        if (TextUtils.isEmpty(from)) {
            from = "from_plugin";
        }
        TextView textView = findViewById(R.id.plugin_text);
        textView.setText(from);
    }
}
