/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import common.Util;
import database.DBConnector;
import enities.Bin;
import enities.EndPoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class EndPointModel {
    private static final String URL = "https://smartbin-892a5.firebaseio.com/EndPoint";
    public static ArrayList<EndPoint> getList(){
        ArrayList<EndPoint> listRet = new ArrayList<>();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            o.keySet().forEach(key -> {
                EndPoint point = new EndPoint();
                point.build((JSONObject) o.get(key), Integer.parseInt(key));
                listRet.add(point);
            });
        } catch (Exception ex) {
            
        }
        return listRet;
    }
    
    public static boolean push(EndPoint point) {
        try {
            String id = Util.getID();
            String url = URL + "/" + id + ".json";
            String ret = DBConnector.Intance.sendPut(url, point.toJSONString());
            JSONObject o = new JSONObject(ret);
            if (o.keySet().size() > 0)
                return true;
        } catch (IOException ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean update(EndPoint point) {
        try {
            String url = URL + "/" + point.id + ".json";
            String ret = DBConnector.Intance.sendPut(url, point.toJSONString());
            JSONObject o = new JSONObject(ret);
            if (o.keySet().size() > 0)
                return true;
        } catch (IOException ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean delete(String id) {
        try {
            String url = URL + "/" + id + ".json";
            String ret = DBConnector.Intance.sendDelete(url);
        } catch (Exception ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static void main(String[] args) {
        List<EndPoint> list = getList();
        list.forEach(bin -> {
            System.out.println("lon: " + bin.lon + " - lat: " + bin.lat);
        });
        System.out.println(new Gson().toJson(list));
//        push(new EndPoint(Util.getID(), , 0))
//        deleteBin("123");
    }
}
