package com.example.dongao.mydemoapp.factoryDemo.MethodFactory;

import com.example.dongao.mydemoapp.factoryDemo.BenCar;
import com.example.dongao.mydemoapp.factoryDemo.BsjCar;

/**
 * Created by fishzhang on 2018/6/13.
 */

public interface CarFatory {
    BenCar createBenCar();
    BsjCar createBsjCar();

}
