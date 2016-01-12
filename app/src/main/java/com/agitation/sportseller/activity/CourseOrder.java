package com.agitation.sportseller.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.agitation.sportseller.BaseActivity;
import com.agitation.sportseller.R;
import com.agitation.sportseller.utils.Mark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fanwl on 2015/11/14.
 */
public class CourseOrder extends BaseActivity {

    public static final int ORDER_STATUS_UNCONFIRM = 0;
    public static final int ORDER_STATUS_CONFIRM = 1;
    public static final int ORDER_STATUS_DONE = 2;

    private TabLayout tab_course_appointment;
    private ViewPager pager_course_appointment;
    private List<Integer> statusList = Arrays.asList(ORDER_STATUS_UNCONFIRM, ORDER_STATUS_CONFIRM, ORDER_STATUS_DONE);
    private List<String> orderTitle = Arrays.asList("待确认", "已确认", "已完成");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_order);
        initToolbar();
        initVarible();
        init();
    }

    private void initVarible() {

    }


    private void initToolbar() {
        if (toolbar!=null){
            title.setText("课程订单");
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        tab_course_appointment = (TabLayout) findViewById(R.id.tab_course_appointment);
        pager_course_appointment = (ViewPager) findViewById(R.id.pager_course_appointment);
        setupViewPager(pager_course_appointment);
        pager_course_appointment.setOffscreenPageLimit(1);
        for (String title : orderTitle){
            tab_course_appointment.addTab(tab_course_appointment.newTab().setText(title));
        }
        tab_course_appointment.setupWithViewPager(pager_course_appointment);

    }

    private void setupViewPager(ViewPager mViewPager) {
        MyPagerAdapter pagerAdapter =
                new MyPagerAdapter(getSupportFragmentManager());
        for (int i=0; i<orderTitle.size(); i++){
            pagerAdapter.addFragment(CourseOrderList.getInstance(statusList.get(i)), orderTitle.get(i));
        }
        mViewPager.setAdapter(pagerAdapter);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
