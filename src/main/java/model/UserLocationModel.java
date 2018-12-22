/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.DBConnector;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class UserLocationModel {
    private static final String URL = "https://smartbin-892a5.firebaseio.com/UserLocation";
    
    public static List<JSONObject> getListLocation(){
        List<JSONObject> listRet = new ArrayList<>();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            o.keySet().forEach(key -> {
                JSONObject location = o.getJSONObject(key);
                location.put("userId", key);
                listRet.add(location);
            });
        } catch (Exception ex) { 
        }
        return listRet;
    }
}
