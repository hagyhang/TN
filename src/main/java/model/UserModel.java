/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.DBConnector;
import enities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class UserModel {
    private static String URL = "https://smartbin-892a5.firebaseio.com/User/";
    public static enum UType{
        EMPLOYEE,
        MANAGER
    } 
    
    public static User getUser(String id, String pass) throws Exception{
        
        String url = URL + id + ".json";
        String res = DBConnector.Intance.sendGet(url);
        if (res == null) return null;
        JSONObject userObj = new JSONObject(res);
        if (userObj != null && pass.equals(userObj.get("pass")) && UType.MANAGER.toString().equals(userObj.get("type")))
            return new User(id, userObj);
        return null;
    }
    
    public static User getUser(String id) throws Exception{
        String url = URL + id + ".json";
        String res = DBConnector.Intance.sendGet(url);
        if (res == null) return null;
        JSONObject userObj = new JSONObject(res);
        return new User(id, userObj);
    }
    
    public static ArrayList<User> getListUser(){
        ArrayList<User> listRet = new ArrayList<>();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            o.keySet().forEach(key -> {
                User user = new User();
                user.build((JSONObject) o.get(key), key);
                listRet.add(user);
            });
        } catch (Exception ex) {
            
        }
        return listRet;
    }
    
    public static Set<String> getListUserId(){
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            return o.keySet();
        } catch (Exception ex) {
        }
        return null;
    }
    
    public static boolean pushUser(User user) {
        try {
            String id = user.id;
            if (id != null){
                String url = URL + "/" + id + ".json";
                String ret = DBConnector.Intance.sendPut(url, user.toJSONString());
                JSONObject location = new JSONObject();
                location.put("lat", 10.7939819);
                location.put("lon", 106.7000699);
                if (user.type.equals(UType.EMPLOYEE.toString())){
                    DBConnector.Intance.sendPut("https://smartbin-892a5.firebaseio.com/UserLocation/" + id +".json",
                            location.toString());
                    DBConnector.Intance.sendPut("https://smartbin-892a5.firebaseio.com/UserScore/" + id + ".json", "0"); 
                }                
                JSONObject o = new JSONObject(ret);
                if (o.keySet().size() > 0)
                    return true;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean updateUser(User user) {
        try {
            String url = URL + "/" + user.id + ".json";
            String ret = DBConnector.Intance.sendPut(url, user.toJSONString());
            JSONObject o = new JSONObject(ret);
            if (o.keySet().size() > 0)
                return true;
        } catch (IOException ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean deleteUser(String id) {
        try {
            String url = URL + "/" + id + ".json";
            String ret = DBConnector.Intance.sendDelete(url);
        } catch (Exception ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static String checkID(){
        Long current = System.currentTimeMillis() / 1000;
        return current.toString();
    }
    public static void main(String[] args) {
        try {
            //        User user = new User("thanhnt", "Thanh", "thanh123", "nguyentanthanh1806@gmail.com", "HCM", UType.EMPLOYEE.toString());
//        pushUser(user);
        getUser("thanh", "thanh123");
        } catch (Exception ex) {
            Logger.getLogger(UserModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
