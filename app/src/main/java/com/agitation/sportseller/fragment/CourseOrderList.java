package com.agitation.sportseller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agitation.sportseller.BaseFragment;
import com.agitation.sportseller.R;
import com.agitation.sportseller.adapter.CourseOrderAdapter;
import com.agitation.sportseller.inter.OrderNotice;
import com.agitation.sportseller.utils.DataHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder;

/**
 * Created by fanwl on 2015/11/15.
 */
public class CourseOrderList extends BaseFragment implements OrderNotice, BGARefreshLayout.BGARefreshLayoutDelegate {

    private View rootView;
    private List<Map<String, Object>> orderList;
    private CourseOrderAdapter courseOrderAdapter;
    private BGARefreshLayout swipe_container;
    private ListView tickey_list_lv;
    private int status;
    public static final String STATUS_NAME_KEY = "STATUS_NAME_KEY";
    private String orderId;
    private DataHolder dataHolder;

    public static CourseOrderList getInstance(int status){
        Bundle bundle = new Bundle();
        bundle.putInt(STATUS_NAME_KEY, status);
        CourseOrderList courseOrderList = new CourseOrderList();
        courseOrderList.setArguments(bundle);
        return courseOrderList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView!=null){
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent!=null)parent.removeView(rootView);
        }else {
            rootView = inflater.inflate(R.layout.course_order_list, container, false);
            status = getArguments().getInt(STATUS_NAME_KEY);
            init();
            initVarible();
            processLogic();
        }
        return rootView;
    }

    private void initVarible() {
        orderList = new ArrayList<>();
        dataHolder = DataHolder.getInstance();
        courseOrderAdapter = new CourseOrderAdapter(getActivity(), status, orderList, R.layout.courseorder_list_item);
        tickey_list_lv.setAdapter(courseOrderAdapter);

        courseOrderAdapter.setOnBtnClickListener(new CourseOrderAdapter.OnBtnClickListener() {
            @Override
            public void onBtnClickListener(Map<String, Object> item, int action) {

                orderId = item.get("id") + "";
//                if (action == CourseOrderAdapter.ACTION_ORDER_DELETE) {
//                    deleteOrder();
//                } else {
//                    int status = Integer.parseInt(item.get("status") + "");
//                    if (status == Mark.ORDER_STATUS_UNPAY) {
//                        double totalMoney = Double.parseDouble(item.get("totalMoney") + "");
//                        pay(totalMoney, orderId, item.get("name") + "", item.get("payWay") + "");
//                    } else if (status == Mark.ORDER_STATUS_UNADVICES) {
//                        Intent intent = new Intent(getContext(), Comment.class);
//                        intent.putExtra("courseId", item.get("courseId") + "");
//                        intent.putExtra("name", item.get("name") + "");
//                        intent.putExtra("time", item.get("createDate") + "");
//                        intent.putExtra("address", item.get("address") + "");
//                        intent.putExtra("orderId", orderId);
//                        startActivityForResult(intent, 140);
//                    }
//                }
            }
        });
    }



    protected void processLogic() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        swipe_container.setRefreshViewHolder(stickinessRefreshViewHolder);
        swipe_container.setDelegate(this);
    }

    private void init() {
        swipe_container = (BGARefreshLayout) rootView.findViewById(R.id.swipe_container);
        tickey_list_lv = (ListView)rootView.findViewById(R.id.course_list_lv);
    }

    @Override
    public void dataChange() {
        List<Map<String, Object>> allData = DataHolder.getInstance().getOrderList();
        for (Map<String, Object> item : allData){
            int orderStatus = Integer.parseInt(item.get("status")+"");
            if (status==orderStatus || (status==3 && orderStatus==4)){
                orderList.add(item);
            }
        }
        Log.d("status", status+"");
        courseOrderAdapter.setData(orderList);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }


}
