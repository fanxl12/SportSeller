package com.agitation.sportseller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.agitation.sportseller.R;
import com.agitation.sportseller.activity.CourseOrder;
import com.agitation.sportseller.utils.ViewHolder;

import java.util.List;
import java.util.Map;

/**
 * Created by fanwl on 2015/11/17.
 */
public class CourseOrderAdapter extends CommonAdapter<Map<String, Object>> {

    private int status;
    private OnBtnClickListener onBtnClickListener;

    public CourseOrderAdapter(Context context, int status, List<Map<String, Object>> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        this.status=status;
    }

    public void setData(List<Map<String, Object>> mDatas) {
        super.setData(mDatas);
    }
    public void setOnBtnClickListener(OnBtnClickListener onBtnClickListener){
        this.onBtnClickListener = onBtnClickListener;

    }

    @Override
    public void convert(ViewHolder helper, Map<String, Object> item) {
        helper.setText(R.id.course_order_match_name,item.get("courseName")+"");
        helper.setText(R.id.course_order_match_time,item.get("createDate")+"");
        helper.setText(R.id.course_order_match_address,item.get("address")+"");

        Button order_bt_confirm = helper.getView(R.id.order_bt_confirm);
        order_bt_confirm.setTag(item);

        if (status == CourseOrder.ORDER_STATUS_UNCONFIRM){
            order_bt_confirm.setVisibility(View.VISIBLE);
        }else{
            order_bt_confirm.setVisibility(View.GONE);
        }

        order_bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> item = (Map<String, Object>) v.getTag();
                if (onBtnClickListener!=null){
                    onBtnClickListener.onBtnClickListener(item);
                }
            }
        });
    }

    public interface OnBtnClickListener{
        void onBtnClickListener(Map<String, Object> item);
    }
}
