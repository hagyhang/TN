/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import database.DBConnector;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class TaskModel {
    private static final String URL = "https://smartbin-892a5.firebaseio.com/Task";
    private static final String STATUS_PENDING = "\"Pending\"";
    private static final String STATUS_DONE = "\"Done\"";
    public static Set<String> getListBusyUser(){
        Set<String> listRet = new HashSet<>();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject o = new JSONObject(raw);
            return o.keySet();
        } catch (Exception ex) {
        }
        return listRet;
    }
    
    public static JSONArray getTasks(){
        JSONArray tasks = new JSONArray();
        try {
            String url = URL + ".json";
            String raw = DBConnector.Intance.sendGet(url);
            JSONObject tasksObj = new JSONObject(raw);
            tasksObj.keySet().forEach(key -> {
                JSONObject task = tasksObj.getJSONObject(key);
                task.put("userId", key);
                try {
                    task.put("userName", UserModel.getUser(key).name);
                } catch (Exception e){}
                tasks.put(task);
            });
        } catch (Exception ex) {
        }
        return tasks;
    }
    
    public static void updateTask(String userId, String endPointId, JSONArray waypoints){
        try {
            DBConnector.Intance.sendPut(URL + "/" + userId + ".json", "");
            DBConnector.Intance.sendPut(URL + "/" + userId + "/endpoint.json", endPointId);
            DBConnector.Intance.sendPut(URL + "/" + userId + "/status.json", STATUS_PENDING);
            for (int j = 0; j < waypoints.length(); j++) {
                Integer id = waypoints.getJSONObject(j).getInt("id");
                DBConnector.Intance.sendPut(URL + "/" + userId + "/waypoints/" + j + ".json", id.toString());
            }
        } catch (IOException ex) {
        }
    }
    
    public static void updateTaskStatus(String userId){
        try {
            DBConnector.Intance.sendPut(URL + "/" + userId + "/status.json", STATUS_PENDING);
        } catch (IOException ex) {
        }
    }
    
    public static void removeTask(String userId, boolean isAccept){
        try {
            DBConnector.Intance.sendDelete(URL + "/" + userId + ".json");
            if (isAccept){
                String raw = DBConnector.Intance.sendGet("https://smartbin-892a5.firebaseio.com/UserScore" + "/" + userId + ".json");
                if (raw.equals("null")){
                    DBConnector.Intance.sendPut("https://smartbin-892a5.firebaseio.com/UserScore" + "/" + userId + ".json", "1");
                } else {
                    Integer score = Integer.parseInt(raw);
                    score++;
                    DBConnector.Intance.sendPut("https://smartbin-892a5.firebaseio.com/UserScore" + "/" + userId + ".json", score.toString());
                }
            }
        } catch (Exception ex) {
        }
    }
    public static void main(String[] args) {
        removeTask("thanhnt", true);
    }
}
