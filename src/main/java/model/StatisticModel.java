/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import common.Util;
import enities.StatPoint;
import java.util.List;

/**
 *
 * @author Admin
 */
public class StatisticModel implements Runnable{
    private StatPoint listCount[] = new StatPoint[120];
    private long time = System.currentTimeMillis()/ 1000;
    private int MAX_RECORDS = 120;
    private int currentPos;
    private int count = 0;
    private Thread thread = new Thread(this);
    public static StatisticModel Instance = new StatisticModel();
    
    private StatisticModel(){
    }
    
    @Override
    public void run() {
        while (true) {     
            time++;
            count = 0;
            BinModel.getListBin().forEach(bin -> {
                if (bin.status == false){
                    count++;
                }
            });
            StatPoint stat = new StatPoint();
            stat.timestamp = time;
            stat.count = count;
            currentPos = (int) (time%MAX_RECORDS);
//            System.out.println("pos " + currentPos + ": " + stat.toChartData());
            listCount[currentPos] = stat;
//            System.out.println("Pos:  " + currentPos);
            Util.sleep(1000);
        }
    }
    public void start(){
        thread.start();
    }
    
    public String getStatsData(){
        String ret = "";
        long minTime = 10000000000l;
        int index = 0;
        int count = 0;
        for (int i = 0; i < listCount.length; i++) {
            StatPoint stat = listCount[i];
            if (stat != null) {
                if (minTime > stat.timestamp){
                    minTime = stat.timestamp;
                    index = i;
                }
                count++;
            }  
        }
        ret = "[";
//        System.out.println(count);
        while (count > 0) {        
            StatPoint stat = listCount[index];
            if (stat != null){
                if (count != 1){
                    ret += stat.toChartData() + ",";
                } else {
                    ret += stat.toChartData();
                }
            }
            index = (index + 1) % MAX_RECORDS;
            count--;
        } 
        ret += "]";
//        System.out.println(ret);
        return ret;
    }
    
    public static void main(String[] args) {
//        StatisticModel.Instance.start();
        while (true) { 
            System.out.println(StatisticModel.Instance.getStatsData());
            Util.sleep(1000);
        }
    }
}

