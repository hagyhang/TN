/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import java.util.Calendar;

/**
 *
 * @author Admin
 */
public class StatPoint {
    public long timestamp;
    public int count;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    public String toChartData(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp*1000);
        String time = "\"" + calendar.get(Calendar.HOUR_OF_DAY) + "h " + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "\"";
        return "{\"y\":" + count + ", \"name\":" +  time + "}";
    }
}
