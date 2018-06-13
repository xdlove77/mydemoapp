package com.example.dongao.mydemoapp.factoryDemo.SimpleFactory;

import com.example.dongao.mydemoapp.factoryDemo.BenCar;
import com.example.dongao.mydemoapp.factoryDemo.BsjCar;
import com.example.dongao.mydemoapp.factoryDemo.ICar;

/**
 * Created by fishzhang on 2018/6/13.
 */

public final class SimpleFactory {


    public static <T> T getProduct(Class<T> tClass){
        T t = null;
        try {
            t=tClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static ICar getCar(String type){
        switch (type){
            case "1":
                return new BenCar();
            case "2":
                return new BsjCar();
            default:
                return null;
        }
    }
}
