package com.example.dongao.mydemoapp;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.hardware.camera2.params.Face;
import android.media.FaceDetector;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.util.LongSparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class PluginGetResActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbarLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        toolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLayout.setTitle("hehehe");

    }
    private String[] getUninstallApkInfo(String archiveFilePath,Context context){
        String[] appName=new String[2];
        PackageManager packageManager=getPackageManager();
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (packageArchiveInfo!=null){
            appName[0]=packageArchiveInfo.packageName;
            appName[1]=packageManager.getApplicationLabel(packageArchiveInfo.applicationInfo).toString();
        }
        return appName;
    }

    private Resources getPluginResources(String packageName) throws Exception{

        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager,"apkDir"+packageName);
        Resources superResources = getResources();
        Resources resources=new Resources(assetManager
                , superResources.getDisplayMetrics(),superResources.getConfiguration());


        return resources;

    }

    private void dynamicApk(String apkName,String apkDir,String apkPackageName) throws Exception {
        File dir=getDir("dex",Context.MODE_PRIVATE);
        DexClassLoader classLoader=new DexClassLoader(apkDir,dir.getAbsolutePath(),null,getClassLoader().getParent());
        Class<?> aClass = classLoader.loadClass(apkPackageName + "R$mipmap");
        Field one = aClass.getDeclaredField("one");
        int anInt = one.getInt(R.mipmap.class);
        getPluginResources(apkPackageName).getDrawable(anInt);
    }


    /**
     * 动态加载 已安装的Apk
     * @param packageName 插件包名
     * @param pluginContext 插件上下文
     * @return 资源id
     */
    private int dynamicLoadApk(String packageName, Context pluginContext) throws Exception {
        pluginContext = createPackageContext(packageName, CONTEXT_IGNORE_SECURITY | CONTEXT_INCLUDE_CODE);
        PathClassLoader loader=new PathClassLoader(pluginContext.getPackageResourcePath(),ClassLoader.getSystemClassLoader());
        Class clazz=Class.forName("",true,loader);
        Field one = clazz.getDeclaredField("one");
        int anInt = one.getInt(R.mipmap.class);
        pluginContext.getResources().getDrawable(anInt);
        return anInt;

    }

    private List<PackageBean> findAllPlugin(){
        List<PackageBean> beanList=new ArrayList<>();
        PackageManager manager=getPackageManager();
        List<PackageInfo> installedPackages = manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo info :
                installedPackages) {
            if ( info.packageName!=null && info.sharedUserId!=null && info.sharedUserId.equals("")){
                PackageBean bean=new PackageBean(info.packageName,manager.getApplicationLabel(info.applicationInfo).toString());
                beanList.add(bean);
            }
        }
        return beanList;
    }



    public static class PackageBean{
        private  String packageName;
        private String label;

        public PackageBean(String packageName, String label) {
            this.packageName = packageName;
            this.label = label;
        }
    }
}
