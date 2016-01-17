package com.agitation.sportseller;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by Fanxl on 2016/1/14.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
//        initSecurity();
    }

    private void initPush() {
        //开启推送服务
        PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.enable();
        mPushAgent.onAppStart();
//        String device_token = UmengRegistrar.getRegistrationId(this);
//        Log.i("device_token", device_token);

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };

        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }
}
