package com.agitation.sportseller.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.agitation.sportseller.BaseFragment;
import com.agitation.sportseller.R;
import com.agitation.sportseller.adapter.CourseOrderAdapter;
import com.agitation.sportseller.inter.OrderNotice;
import com.agitation.sportseller.utils.DataHolder;
import com.agitation.sportseller.utils.MapTransformer;
import com.agitation.sportseller.utils.Mark;
import com.agitation.sportseller.utils.ToastUtils;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.util.ArrayList;
import java.util.HashMap;
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
    private DataHolder dataHolder;
    private AQuery aq;
    public static final String STATUS_NAME_KEY = "STATUS_NAME_KEY";
    public static final int COMMENT_SUCCEED = 140;
    private String orderId;

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
            if(dataHolder.isLogin()){
                getCourseOrderList();
            }else {
                ToastUtils.showToast(getContext(), "请登录");
            }
        }
        return rootView;
    }

    private void initVarible() {
        aq = new AQuery(getContext());
        dataHolder = DataHolder.getInstance();
        orderList = new ArrayList<>();
        courseOrderAdapter = new CourseOrderAdapter(getActivity(), status, orderList, R.layout.courseorder_list_item);
        tickey_list_lv.setAdapter(courseOrderAdapter);
        tickey_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), CourseDetail.class);
//                intent.putExtra("courseId", orderList.get(position).get("courseId") + "");
//                startActivity(intent);
            }
        });

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

    //获取订单数据
    public void getCourseOrderList(){
        mActivity.showLoadingDialog();
        String url = Mark.getServerIp() + "/api/v1/order/getCourseOrderList";
        aq.transformer(new MapTransformer()).auth(dataHolder.getBasicHandle())
                .ajax(url, Map.class, new AjaxCallback<Map>() {
                    @Override
                    public void callback(String url, Map info, AjaxStatus status) {
                        mActivity.dismissLoadingDialog();
                        if (info != null) {
                            if (Boolean.parseBoolean(info.get("result") + "")) {
                                Map<String, Object> retData = (Map<String, Object>) info.get("retData");
                                List<Map<String, Object>> courseOrderList = (List<Map<String, Object>>) retData.get("courseOrderList");
                                selectedOrderData(courseOrderList);
                            }
                        }
                    }
                });
    }

    //删除订单
    public void deleteOrder(){
        mActivity.showLoadingDialog();
        String url = Mark.getServerIp() + "/api/v1/order/deleteOrder";
        Map<String, Object> param = new HashMap<>();
        param.put("id", orderId);
        aq.transformer(new MapTransformer()).auth(dataHolder.getBasicHandle())
                .ajax(url, param, Map.class, new AjaxCallback<Map>() {
                    @Override
                    public void callback(String url, Map info, AjaxStatus status) {
                        mActivity.dismissLoadingDialog();
                        if (info != null) {
                            if (Boolean.parseBoolean(info.get("result") + "")) {
                                getCourseOrderList();
                            }
                        }
                    }
                });
    }

    protected void processLogic() {
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(getActivity(), false);
        stickinessRefreshViewHolder.setStickinessColor(R.color.colorPrimary);
//        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness);
        swipe_container.setRefreshViewHolder(stickinessRefreshViewHolder);
        swipe_container.setDelegate(this);
    }

    private void init() {
        swipe_container = (BGARefreshLayout) rootView.findViewById(R.id.swipe_container);
        tickey_list_lv = (ListView)rootView.findViewById(R.id.course_list_lv);
    }

    public void selectedOrderData(List<Map<String, Object>> courseOrderList){
//        orderList = UtilsHelper.selectMapList(courseOrderList, "get(:_currobj,'status') like '" + status + "'");
//        courseOrderAdapter.setData(orderList);
//        refreshHandler.sendEmptyMessageDelayed(Mark.DATA_REFRESH_SUCCEED, 2000);
    }

    @Override
    public void dataChange() {

    }

    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            if (msg.what==Mark.DATA_REFRESH_SUCCEED){
//                swipe_container.endRefreshing();
//            }
        }
    };

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout bgaRefreshLayout) {
        getCourseOrderList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout bgaRefreshLayout) {
        return false;
    }
}
