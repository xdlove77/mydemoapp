package com.example.dongao.mydemoapp.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by fishzhang on 2018/4/24.
 */

public final class PermissionsUtils {

    private PermissionsUtils(){}

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestPermissions(Object object,int requestCode,PermissCallBack callBack,String... permissions){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if (callBack!=null)
                callBack.permissGrand();
            return;
        }

        String[] deniedPermissions = checkNeedRequestPermissions(getActivity(object), permissions);
        if (deniedPermissions.length>0){
            if (object instanceof Activity){
                ((Activity) object).requestPermissions(permissions,requestCode);
            }else if (object instanceof Fragment){
                ((Fragment) object).requestPermissions(permissions,requestCode);
            }else if (object instanceof android.app.Fragment){
                ((android.app.Fragment) object).requestPermissions(permissions,requestCode);
            }else
                throw new InvalidParameterException("object error");
        }else{
            if (callBack!=null)
                callBack.permissGrand();
        }
    }

    private static String[] checkNeedRequestPermissions(Context context, String... permissions) {
        ArrayList<String> deniedPermissions=new ArrayList<>();
        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(context, permisson) != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permisson);
            }
        }
        return deniedPermissions.toArray(new String[deniedPermissions.size()]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void requestPermissionsByRationale(final Object object, final int requestCode, final PermissCallBack callBack, String... permissions){
        final Activity activity = getActivity(object);
        String[] deniedPermissions = checkNeedRequestPermissions(activity, permissions);
        final ArrayList<String> needPermissions=new ArrayList<>();
        for (int i = 0; i < deniedPermissions.length; i++) {
            boolean needAdd=false;
            if (object instanceof Activity){
                needAdd=((Activity) object).shouldShowRequestPermissionRationale(deniedPermissions[i]);
            }else if (object instanceof Fragment){
                needAdd=((Fragment) object).shouldShowRequestPermissionRationale(deniedPermissions[i]);
            }else if (object instanceof android.app.Fragment){
                needAdd=((android.app.Fragment) object).shouldShowRequestPermissionRationale(deniedPermissions[i]);
            }else
                throw new InvalidParameterException("object error");
            if (needAdd){
                needPermissions.add(deniedPermissions[i]);
            }
        }
        if (needPermissions.size()<=0){
            new AlertDialog.Builder(activity)
                    .setMessage("缺少必要权限。\n请点击\"设置\"-\"权限\"-打开所需权限。")
                   .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            callBack.permissDenied();
                        }
                    }).setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                            activity.startActivity(intent);
                        }
                    }).show();
        }else{
             new AlertDialog.Builder(activity).setMessage("需要必要的权限")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            requestPermissions(object,requestCode,callBack,needPermissions.toArray(new String[needPermissions.size()]));
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            callBack.permissDenied();
                        }
                    }).show();
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void onPermissionResult(Object object,PermissCallBack callBack,int requestCode,String[] permissions,int[] grandResult){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
            return;
        boolean hasDenied=false;
        if (grandResult.length>0){
            for (int i = 0; i < grandResult.length; i++) {
                if (grandResult[i]== PackageManager.PERMISSION_DENIED) {
                    hasDenied=true;
                }
            }
        }
        if (hasDenied)
            requestPermissionsByRationale(object,requestCode,callBack,permissions);
        else
            callBack.permissGrand();
    }

    private static Activity getActivity(Object object){
        if (object instanceof Fragment){
            return ((Fragment) object).getActivity();
        }else if (object instanceof android.app.Fragment){
            return ((android.app.Fragment) object).getActivity();
        }else if (object instanceof Activity){
            return (Activity) object;
        }else{
            throw new InvalidParameterException("object error");
        }
    }

    public interface PermissCallBack{
        void permissGrand();
        void permissDenied();
    }
}
