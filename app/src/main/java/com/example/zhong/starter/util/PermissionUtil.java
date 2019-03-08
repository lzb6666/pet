package com.example.zhong.starter.util;

import android.Manifest;
import android.app.Activity;

import pub.devrel.easypermissions.EasyPermissions;

public class PermissionUtil {
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static boolean getPermission(Activity activity) {
        if (activity==null){
            return false;
        }
        if (EasyPermissions.hasPermissions(activity, permissions)) {
            /*//已经打开权限
            Toast.makeText(activity, "已经申请相关权限", Toast.LENGTH_SHORT).show();*/
            return true;
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(activity, "需要获取您的相册、照相使用权限", 1, permissions);
            return false;
        }

    }
}
