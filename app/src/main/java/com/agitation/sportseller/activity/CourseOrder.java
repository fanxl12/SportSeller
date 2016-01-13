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
import com.agitation.sportseller.fragment.CourseOrderList;
import com.agitation.sportseller.inter.OrderNotice;
import com.agitation.sportseller.utils.MapTransformer;
import com.agitation.sportseller.utils.Mark;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by fanwl on 2015/11/14.
 */
public class CourseOrder extends BaseActivity {

    public static final int ORDER_STATUS_UNCONFIRM = 1;
    public static final int ORDER_STATUS_CONFIRM = 2;
    public static final int ORDER_STATUS_DONE = 3;

    private TabLayout tab_course_appointment;
    private ViewPager pager_course_appointment;
    private List<Integer> statusList = Arrays.asList(ORDER_STATUS_UNCONFIRM, ORDER_STATUS_CONFIRM, ORDER_STATUS_DONE);
    private List<String> orderTitle = Arrays.asList("待确认", "已确认", "已完成");
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_order);
        initToolbar();
        init();
        getCourseOrderList();
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
        fragments = new ArrayList<>();
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        for (int i=0; i<orderTitle.size(); i++){
            Fragment fragment = CourseOrderList.getInstance(statusList.get(i));
            fragments.add(fragment);
            pagerAdapter.addFragment(fragment, orderTitle.get(i));
        }
        mViewPager.setAdapter(pagerAdapter);
    }

    //获取订单数据
    public void getCourseOrderList(){
        showLoadingDialog();
        String url = Mark.getServerIp() + "/api/v1/order/getCourseOrder4Seller";
        aq.transformer(new MapTransformer()).auth(dataHolder.getBasicHandle())
                .ajax(url, Map.class, new AjaxCallback<Map>() {
                    @Override
                    public void callback(String url, Map info, AjaxStatus status) {
                        dismissLoadingDialog();
                        if (info != null) {
                            if (Boolean.parseBoolean(info.get("result") + "")) {
                                Map<String, Object> retData = (Map<String, Object>) info.get("retData");
                                List<Map<String, Object>> courseOrderList = (List<Map<String, Object>>) retData.get("courseOrderList");
                                dataHolder.setOrderList(courseOrderList);
                                updateData();
                            }
                        }
                    }
                });
    }

    private void updateData() {
        for (Fragment fragment : fragments){
            OrderNotice oN = (OrderNotice) fragment;
            oN.dataChange();
        }
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
