/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class DirectionModel {
    private static final HttpClient client = HttpClientBuilder.create().build();
    public static String getDirection (JSONObject origin, JSONObject dest, JSONArray markerPoints){
        try {
            String str_origin = "origin="+origin.getDouble("lat")+","+origin.getDouble("lon");
            String str_dest = "destination="+dest.getDouble("lat")+","+dest.getDouble("lon");
            String sensor = "sensor=false";
            String waypoints = "waypoints=";
            for(int i=0;i<markerPoints.length();i++){
                JSONObject point = markerPoints.getJSONObject(i);
                waypoints += point.getDouble("lat") + "," + point.getDouble("lon") + "%7C";
            }
//            String encoded = URLEncoder.encode(waypoints,"UTF-8");
//            waypoints = encoded;
            String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key=AIzaSyBut76aU8KK1puDk_6VhcDdwMb6chJMnAs";
            
            HttpPost get = new HttpPost(url);
            // add header
            get.setHeader("Host", "maps.googleapis.com");
            get.setHeader("User-Agent", USER_AGENT);
            get.setHeader("Accept",
                    "text/html,application/json,application/xml;q=0.9,*/*;q=0.8");
            get.setHeader("Accept-Language", "en-US,en;q=0.5");
            get.setHeader("Connection", "keep-alive");
            get.setHeader("Content-Type", "application/json");
            
            HttpResponse response = client.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
//            Logger.getLogger(DirectionModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    public static String getDirectionV2(JSONObject origin, JSONObject dest, JSONArray markerPoints) {
    HttpURLConnection connection = null;
    try {
        String str_origin = "origin="+origin.getDouble("lat")+","+origin.getDouble("lon");
        String str_dest = "destination="+dest.getDouble("lat")+","+dest.getDouble("lon");
        String sensor = "sensor=false";
        String waypoints = "waypoints=";
        for(int i=0;i<markerPoints.length();i++){
            JSONObject point = markerPoints.getJSONObject(i);
            waypoints += point.getDouble("lat") + "," + point.getDouble("lon") + "%7C";
        }
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;
        String output = "json";
        String targetURL = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key=AIzaSyBut76aU8KK1puDk_6VhcDdwMb6chJMnAs";
        //Create connection
        URL url = new URL(targetURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", 
            "application/x-www-form-urlencoded"); 

        connection.setUseCaches(false);

        //Get Response  
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        while ((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        rd.close();
        return response.toString();
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
      }
    }
}
