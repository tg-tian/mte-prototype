package com.mock.device.model;

import lombok.Data;

public class DeviceA extends Device{
    public void start(){
        setCurrentStatus("忙碌");
        setCurrentOperation("启动start");

        // 启动。。
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setCurrentStatus("空闲");
        setCurrentOperation("");
    }

    public void makeCoffee(String coffeeType){
        setCurrentStatus("忙碌");
        setCurrentOperation("做咖啡makeCoffee("+coffeeType+")");

        // 做咖啡。。
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setCurrentStatus("空闲");
        setCurrentOperation("");
    }
}
