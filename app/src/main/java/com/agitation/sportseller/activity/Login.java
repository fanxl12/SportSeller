package com.agitation.sportseller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.view.View;

import com.agitation.sportseller.BaseActivity;
import com.agitation.sportseller.R;
import com.agitation.sportseller.utils.MapTransformer;
import com.agitation.sportseller.utils.Mark;
import com.agitation.sportseller.utils.SecurityUtils;
import com.agitation.sportseller.utils.SharePreferenceUtil;
import com.agitation.sportseller.utils.ToastUtils;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by fanwl on 2015/9/18.
 */
public class Login extends BaseActivity implements View.OnClickListener {

    private TextInputLayout login_username,login_password;
    private AppCompatCheckBox re_password;
    private boolean isRemeber = false;
    public static final String IS_RM_PW="IS_RM_PW";
    public static final String LOGIN_UN_NAME="LOGIN_UN_NAME";
    public static final String LOGIN_PW_NAME="LOGIN_PW_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initToolbar();
        init();
    }



    private void initToolbar() {
        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
    }

    private void init() {
        login_username = (TextInputLayout) findViewById(R.id.login_username);
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        re_password = (AppCompatCheckBox) findViewById(R.id.re_password);
        findViewById(R.id.forget_password).setOnClickListener(this);
        findViewById(R.id.bt_login).setOnClickListener(this);

        isRemeber = SharePreferenceUtil.getBoolean(this, IS_RM_PW, false);
        re_password.setChecked(isRemeber);

        if (isRemeber){
            String password = SharePreferenceUtil.getString(Login.this,LOGIN_PW_NAME, "");
            login_password.getEditText().setText(password);
        }
        String name = SharePreferenceUtil.getString(Login.this,LOGIN_UN_NAME, "");
        login_username.getEditText().setText(name);
    }

    private void getToLogin(){
        final String name = login_username.getEditText().getText().toString().trim();
        final String password = login_password.getEditText().getText().toString().trim();

        View focusView = null;
        boolean toLogin = true;
        if (TextUtils.isEmpty(name)){
            login_username.setError("用户名不能为空");
            focusView = login_username;
            toLogin = false;
        }else if (TextUtils.isEmpty(password)){
            login_password.setError("密码不能为空");
            focusView = login_password;
            toLogin = false;
        }
        if (!toLogin){
            focusView.requestFocus();
        }

        String url = Mark.getServerIp()+"/baseApi/login";

        String str = "My name is fxl！";

        Map<String,Object> param = new HashMap<>();
        param.put("userName", name);
        param.put("passWord", password);
        param.put("roles", "seller");
        param.put("signStr", SecurityUtils.signStr(str, this));
        param.put("jia", SecurityUtils.encryptionStr(str, this));
        showLoadingDialog();
        aq.transformer(new MapTransformer())
                .ajax(url, param, Map.class, new AjaxCallback<Map>() {
                    @Override
                    public void callback(String url, Map result, AjaxStatus status) {
                        dismissLoadingDialog();
                        if (result != null) {
                            boolean isRegistered = Boolean.parseBoolean(result.get("result") + "");
                            if (isRegistered) {
                                isRemeber = re_password.isChecked();
                                if (isRemeber) {
                                    SharePreferenceUtil.setValue(Login.this, LOGIN_PW_NAME, password);
                                    dataHolder.setIsLogin(isRemeber);
                                }
                                SharePreferenceUtil.setValue(Login.this, LOGIN_UN_NAME, name);
                                SharePreferenceUtil.setValue(Login.this, IS_RM_PW, isRemeber);
                                dataHolder.setBasicHandle(name, password);
                                dataHolder.setUserData((Map<String, Object>) result.get("retData"));
                                updateDeviceTokens();
                                PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
                                try {
                                    mPushAgent.addAlias(name, "HighSport");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            } else {
                                ToastUtils.showToast(Login.this, "登录失败" + "," + result.get("error"));
                            }
                        } else {
                            ToastUtils.showToast(Login.this, "登录失败" + "," + result.get("error"));
                        }
                    }
                });
    }

    private void updateDeviceTokens() {
        String deviceTokens = UmengRegistrar.getRegistrationId(this);
        if (deviceTokens!=null && !TextUtils.isEmpty(deviceTokens)){
            String url = Mark.getServerIp() + "/baseApi/updateDeviceTokens";
            Map<String, Object> param = new HashMap<>();
            param.put("deviceTokens", deviceTokens);
            aq.transformer(new MapTransformer()).auth(dataHolder.getBasicHandle())
                    .ajax(url, param, Map.class, new AjaxCallback<Map>(){
                        @Override
                        public void callback(String url, Map info, AjaxStatus status) {
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                getToLogin();
                break;
        }
    }




}
