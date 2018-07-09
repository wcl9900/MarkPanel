package com.wcl.markpanel.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_simple).setOnClickListener(this);
        findViewById(R.id.btn_multi).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_simple:
                startActivity(new Intent(this, SimpleActivity.class));
                break;
            case R.id.btn_multi:
                startActivity(new Intent(this, MultiActivity.class));
                break;
        }
    }
}
