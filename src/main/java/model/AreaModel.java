///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package model;
//
//import common.Util;
//import database.DBConnector;
//import enities.Area;
//import enities.User;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.JSONObject;
//
///**
// *
// * @author Admin
// */
//public class AreaModel {
//    private static String URL = "https://smartbin-892a5.firebaseio.com/Area/";
//    
//    public static ArrayList<Area> getListArea(){
//        ArrayList<Area> listRet = new ArrayList<>();
//        try {
//            String url = URL + ".json";
//            String raw = DBConnector.Intance.sendGet(url);
//            JSONObject o = new JSONObject(raw);
//            o.keySet().forEach(key -> {
//                Area area = new Area();
//                area.build((JSONObject) o.get(key), Integer.parseInt(key));
//                listRet.add(area);
//            });
//        } catch (Exception ex) {
//            
//        }
//        return listRet;
//    }
//    
//    public static boolean pushArea(Area area) {
//        try {
//            String id = Util.getID();
//            String url = URL + "/" + id + ".json";
//            String ret = DBConnector.Intance.sendPut(url, area.toJSONString());
//            JSONObject o = new JSONObject(ret);
//            if (o.keySet().size() > 0)
//                return true;
//        } catch (IOException ex) {
//            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//    
//    public static boolean updateArea(Area area) {
//        try {
//            String url = URL + "/" + area.id + ".json";
//            String ret = DBConnector.Intance.sendPut(url, area.toJSONString());
//            JSONObject o = new JSONObject(ret);
//            if (o.keySet().size() > 0)
//                return true;
//        } catch (IOException ex) {
//            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
//    
//    public static boolean deleteArea(String id) {
//        try {
//            String url = URL + "/" + id + ".json";
//            String ret = DBConnector.Intance.sendDelete(url);
//        } catch (Exception ex) {
//            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return true;
//    }
//}
