package com.example.dongao.mydemoapp.factoryDemo.AbsFactory;

import com.example.dongao.mydemoapp.factoryDemo.BenCar;
import com.example.dongao.mydemoapp.factoryDemo.ICar;

/**
 * Created by fishzhang on 2018/6/13.
 */

public class BenCarFactory implements CarFactory {
    @Override
    public ICar createCar() {
        return new BenCar();
    }
}
