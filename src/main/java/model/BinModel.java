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
            String id = Util.getID();
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
    
    public static void main(String[] args) {
        List<Bin> list = getListBin();
//        list.forEach(bin -> {
//            System.out.println("lon: " + bin.lon + " - lat: " + bin.lat + " - status: " + bin.status);
//        });
//        System.out.println(new Gson().toJson(list));
        //BT
        pushBin(new Bin(106.69895270790039, 10.794826567741302, true, 1545146730));
        pushBin(new Bin(106.6988346068847, 10.794844022927013, true, 1542445417));
        pushBin(new Bin(106.69866586709713, 10.79477168491749, true, 1542730955));
        pushBin(new Bin(106.69751512768744, 10.795244503890483, true, 1542435496));
        pushBin(new Bin(106.69715026344238, 10.795174353891849, true, 1542435495));
        pushBin(new Bin(106.69533700632928, 10.795824146444916, true, 1542730970));
        pushBin(new Bin(106.6975353022766, 10.795559726467225, true, 1542435497));
//        //Q9    
//        pushBin(new Bin(106.78761433421926, 10.847519698881536, true, 1545146730));
//        pushBin(new Bin(106.7869586466162, 10.847953803896402, true, 1542445417));
//        pushBin(new Bin(106.78708793887972, 10.846539218387804, true, 1542730955));
//        pushBin(new Bin(106.78758875164772, 10.847331465132465, true, 1542435496));
//        pushBin(new Bin(106.78616074537683, 10.845842962599846, true, 1542435495));
//        pushBin(new Bin(106.78810070168288, 10.848569289568553, true, 1542730970));
//        pushBin(new Bin(106.78715241578163, 10.848710774112432, true, 1542435497));
    }
}
