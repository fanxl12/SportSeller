package com.agitation.sportseller.activity;

import android.os.Bundle;
import android.view.View;

import com.agitation.sportseller.BaseActivity;
import com.agitation.sportseller.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.main_course_order).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_course_order:

                break;
        }
    }
}
