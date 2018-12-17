/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class EndPoint extends BaseEnity{
    public int id = 0;
    public double lon = -1;
    public double lat = -1;
    public String name = "";

    public EndPoint(double lon, double lat, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public EndPoint(double lon, double lat, int id, String name) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
        this.name = name;
    }

    public EndPoint() {       
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void build(JSONObject o, int id) {
        this.id = id;
        this.lat = o.getDouble("lat");
        this.lon = o.getDouble("lon");
        this.name = o.getString("name");
    }
    
}
