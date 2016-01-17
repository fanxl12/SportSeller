package com.agitation.sportseller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.agitation.sportseller.BaseFragment;
import com.agitation.sportseller.R;
import com.agitation.sportseller.activity.CourseOrder;
import com.agitation.sportseller.adapter.CourseOrderAdapter;
import com.agitation.sportseller.inter.OrderNotice;
import com.agitation.sportseller.utils.DataHolder;
import com.agitation.sportseller.utils.MapTransformer;
import com.agitation.sportseller.utils.Mark;
import com.agitation.sportseller.utils.ToastUtils;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

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
    private DataHolder dataHolder;
    private boolean isRefreshing = false;

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
            dataChange();
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
            public void onBtnClickListener(Map<String, Object> item) {
                confirmOrder(item);
            }
        });
    }

    private void confirmOrder(Map<String, Object> item){
        mActivity.showLoadingDialog();
        String url = Mark.getServerIp() + "/api/v1/order/confirmOrder";
        mActivity.aq.transformer(new MapTransformer()).auth(dataHolder.getBasicHandle())
                .ajax(url, item, Map.class, new AjaxCallback<Map>(){

                    @Override
                    public void callback(String url, Map object, AjaxStatus status) {
                        mActivity.dismissLoadingDialog();
                        if (object!=null){
                            if (Boolean.parseBoolean(object.get("result")+"")){
                                ToastUtils.showToast(mActivity, "订单确认成功");
                                ((CourseOrder)mActivity).getCourseOrderList();
                            }else{
                                ToastUtils.showToast(mActivity, object.get("error")+"");
                            }
                        }
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
        if (courseOrderAdapter==null)return;
        List<Map<String, Object>> allData = dataHolder.getOrderList();
        orderList.clear();
        for (Map<String, Object> item : allData){
            int orderStatus = Integer.parseInt(item.get("status")+"");
            if (status==orderStatus || (status==3 && orderStatus==4)){
                orderList.add(item);
            }
        }
        courseOrderAdapter.setData(orderList);
        if (isRefreshing){
            isRefreshing = false;
            swipe_container.endRefreshing();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        ((CourseOrder)mActivity).updateData();
        isRefreshing = true;
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }


}
