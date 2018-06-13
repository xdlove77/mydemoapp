package com.example.dongao.mydemoapp.factoryDemo;

import com.example.dongao.mydemoapp.factoryDemo.AbsFactory.BenCarFactory;
import com.example.dongao.mydemoapp.factoryDemo.AbsFactory.CarFactory;
import com.example.dongao.mydemoapp.factoryDemo.MethodFactory.CarFatory;
import com.example.dongao.mydemoapp.factoryDemo.SimpleFactory.SimpleFactory;

/**
 * Created by fishzhang on 2018/6/13.
 */

public class Consumer {

    public void aboutSimple(){
        ICar car = SimpleFactory.getCar("");
    }

    public void aboutAbs(){
        CarFactory factory=new BenCarFactory();
        ICar car = factory.createCar();
    }

    public void aboutMethod(){
        CarFatory fatory=new com.example.dongao.mydemoapp.factoryDemo.MethodFactory.BenCarFactory();
        BenCar benCar = fatory.createBenCar();
        BsjCar bsjCar = fatory.createBsjCar();
    }
}
