package com.example.dongao.mydemoapp.factoryDemo.AbsFactory;

import com.example.dongao.mydemoapp.factoryDemo.BsjCar;
import com.example.dongao.mydemoapp.factoryDemo.ICar;

/**
 * Created by fishzhang on 2018/6/13.
 */

public class BsjCarFactory implements CarFactory {

    @Override
    public ICar createCar() {
        return new BsjCar();
    }
}
