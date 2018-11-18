/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import model.BinModel;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class Bin extends BaseEnity{
    public int id = 0;
    public double lon = -1;
    public double lat = -1;
    public boolean status = false;

    public Bin(double lon, double lat, boolean status) {
        this.lat = lat;
        this.lon = lon;
        this.status = status;
    }
    
    public Bin(double lon, double lat, boolean status, int id) {
        this.lat = lat;
        this.lon = lon;
        this.status = status;
        this.id = id;
    }

    public Bin() {
    }

    public void build (JSONObject o, int id){
        this.id = id;
        this.lat = o.getDouble("lat");
        this.lon = o.getDouble("lon");
        this.status = o.getBoolean("status");
    }

    public static void main(String[] args) {
        Bin bin = new Bin(10, 30, true);
        System.out.println(BinModel.pushBin(bin));
    }
}
