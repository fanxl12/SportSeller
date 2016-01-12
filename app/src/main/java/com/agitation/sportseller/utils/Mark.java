package com.agitation.sportseller.utils;


import android.os.Environment;

import com.agitation.sportseller.BuildConfig;


/**
 * Created by fanwl on 2015/9/21.
 */
public class Mark {

    private static boolean TEST = BuildConfig.TEST;

    //http://192.168.1.200:8088/tickey
    public static String getServerIp(){
        if (TEST){
            return "http://192.168.1.200:8083/sport";
//            return "http://www.highyundong.com:8080/sport";
        }else {
            return "http://www.highyundong.com:8080/sport";
        }
    }

    //APP文件和数据存放位置
    public static String getFilePath(){
        return Environment.getExternalStorageDirectory() + "/HighSport";
    }
}
