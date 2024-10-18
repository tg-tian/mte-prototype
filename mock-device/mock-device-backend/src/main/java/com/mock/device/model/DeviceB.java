package com.mock.device.model;

import lombok.Data;

public class DeviceB extends Device{
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

    public void on(String coffeeType){
        setCurrentStatus("忙碌");
        setCurrentOperation("做咖啡on("+coffeeType+")");

        // 做咖啡。。
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setCurrentStatus("空闲");
        setCurrentOperation("");
    }

    public void check(){
        setCurrentStatus("忙碌");
        setCurrentOperation("自检check");

        // 做咖啡。。
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setCurrentStatus("空闲");
        setCurrentOperation("");
    }
}
