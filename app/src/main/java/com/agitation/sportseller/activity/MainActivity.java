package com.agitation.sportseller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.agitation.sportseller.BaseActivity;
import com.agitation.sportseller.R;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.update(this);
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
                startActivity(new Intent(this, CourseOrder.class));
                break;
        }
    }
}
