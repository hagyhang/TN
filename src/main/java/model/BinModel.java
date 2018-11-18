/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.Gson;
import database.DBConnector;
import enities.Bin;
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
public class BinModel {
    private static final String URL = "https://smartbin-892a5.firebaseio.com/Bin";
    public static ArrayList<Bin> getListBin(){
        ArrayList<Bin> listRet = new ArrayList<>();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            o.keySet().forEach(key -> {
                Bin bin = new Bin();
                bin.build((JSONObject) o.get(key), Integer.parseInt(key));
                listRet.add(bin);
            });
        } catch (Exception ex) {
            
        }
        return listRet;
    }
    
    public static boolean pushBin(Bin bin) {
        try {
            String id = getID();
            String url = URL + "/" + id + ".json";
            String ret = DBConnector.Intance.sendPut(url, bin.toJSONString());
            JSONObject o = new JSONObject(ret);
            if (o.keySet().size() > 0)
                return true;
        } catch (IOException ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean updateBin(Bin bin) {
        try {
            String url = URL + "/" + bin.id + ".json";
            String ret = DBConnector.Intance.sendPut(url, bin.toJSONString());
            JSONObject o = new JSONObject(ret);
            if (o.keySet().size() > 0)
                return true;
        } catch (IOException ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean deleteBin(String id) {
        try {
            String url = URL + "/" + id + ".json";
            String ret = DBConnector.Intance.sendDelete(url);
        } catch (Exception ex) {
            Logger.getLogger(BinModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static String getID(){
        Long current = System.currentTimeMillis() / 1000;
        return current.toString();
    }
    
    public static void main(String[] args) {
        List<Bin> list = getListBin();
//        list.forEach(bin -> {
//            System.out.println("lon: " + bin.lon + " - lat: " + bin.lat + " - status: " + bin.status);
//        });
//        System.out.println(new Gson().toJson(list));
        deleteBin("123");
    }
}
