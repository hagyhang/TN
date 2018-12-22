/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.google.gson.Gson;
import database.DBConnector;
import enities.Bin;
import enities.EndPoint;
import enities.Path;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import enities.User;
import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.BinModel;
import model.DirectionModel;
import model.EMailModel;
import model.EndPointModel;
import model.StatisticModel;
import model.TaskModel;
import model.UserLocationModel;
import model.UserModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Admin
 */
@Controller
@SpringBootApplication
public class MainHandler {
    public static HashMap<String, String> mapToken = new HashMap<>();
    @RequestMapping("/")
    String index() {
        return "login";
    }
    
    @RequestMapping("/dashboard")
    String dashboard(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "dashboard";
                }
            } catch (Exception e){
            }
        }
        return "/";
    }
    
    @RequestMapping("/statistic")
    String statistic(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "statistic";
                }
            } catch (Exception e){
            }
        }
        return "/";
    }
    
    @RequestMapping("/management")
    String management(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "management";
                }
            } catch (Exception e){
            }
        }
        return "/";
    }
    
//    @RequestMapping("/management-add")
//    String managementAdd() {
//        return "management-add";
//    }
    
    @RequestMapping("/management_user")
    String userManagement(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "management_user";
                }
            } catch (Exception e){
            }
        }
        return "/";
    }
    
    @RequestMapping("/management_point")
    String pointManagement(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "management_point";
                }
            } catch (Exception e){
            }
        }
        return "/";   
    }
    
    @RequestMapping("/management_task")
    String taskManagement(HttpServletRequest request, HttpServletResponse response) {
        String token = getToken(request.getCookies());
        if (token != null) {
            try{
                Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
                if (claims.getSubject().equals("OKBEDE")){        
                    return "management_task";
                }
            } catch (Exception e){
            }
        }
        return "/";   
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ResponseEntity<Boolean> login(HttpServletRequest request, HttpServletResponse response){
        try {
            String name = request.getParameter("name");
            String pass = request.getParameter("pass");
            User user = UserModel.getUser(name, pass);
            if (user != null) { 
                String data = "OKBEDE";
                String jwt = createJWT("id", "", data, 3600);
                Cookie cookie = new Cookie("token", jwt);
                response.addCookie(cookie);
                response.setStatus(200);
                return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
            }
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/logout")
    String logout(HttpServletRequest request, HttpServletResponse response){
        try {
            Cookie cookie = new Cookie("token", "");
            response.addCookie(cookie);
            response.setStatus(200);
        } catch (Exception ex) { 
        }
//        System.out.println("aaaaaaaaaaaaaaaa");
        return "/login";
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/bins", method = {RequestMethod.GET})
    ResponseEntity<String> getBins(HttpServletRequest request, HttpServletResponse response){
        String ret = "";
        try { 
            String token = getToken(request.getCookies());
            Claims claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
            if (!claims.getSubject().equals("OKBEDE")){        
                return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/404").build();
            } 
            List<Bin> list = BinModel.getListBin();
            ret = new Gson().toJson(list);
//            o.put("bins", arr);o.to
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/path", method = {RequestMethod.GET})
    ResponseEntity<String> getBinWithEndPoint(HttpServletRequest request, HttpServletResponse response){
        String ret = "";
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            JSONObject o = new JSONObject();
            JSONArray binArr = new JSONArray();
            JSONArray pointArr = new JSONArray();
            List<Bin> listBin = BinModel.getListBin();
            List<EndPoint> listPoint = EndPointModel.getList();
            
            Path path = new Path();
            path.bins = listBin;
            path.points = listPoint;
            o.put("bins", listBin);
            o.put("points", listPoint);
            ret = new Gson().toJson(path);
//            System.out.println("ret: " + ret);
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/path", method = {RequestMethod.POST})
    @ResponseBody
    ResponseEntity<Boolean> putPath(@RequestBody String data){
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            Set<String> listBusyUser = TaskModel.getListBusyUser();
            JSONObject obj = new JSONObject(data);
            JSONArray arr = obj.getJSONArray("wayPoints");
            JSONObject endPoint = obj.getJSONObject("endPoint");
            List<JSONObject> listLocation = UserLocationModel.getListLocation();
            int maxDistance = -1, index = -1, i = 0;
            for (JSONObject location : listLocation) {
                String userId = listLocation.get(i).getString("userId");
                String raw = DirectionModel.getDirectionV2(location, endPoint, arr);
                JSONObject o = new JSONObject(raw);
                JSONArray legs = o.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                int total = 0;
                for (int j = 0; j < legs.length(); j++) {
                    int distance = legs.getJSONObject(j).getJSONObject("distance").getInt("value");
                    total += distance;
                }
                if (!listBusyUser.contains(userId) && maxDistance < total){
                    maxDistance = total;
                    index = i;
                }
                i++;
            }
            if (index > -1){
                String userId = listLocation.get(index).getString("userId");
                TaskModel.updateTask(userId, endPoint.get("id").toString(), arr);
                EMailModel.sendMail(userId);
                return new ResponseEntity(true, HttpStatus.OK);
            }          
        } catch (Exception ex) { 
            System.out.println(ex.getMessage());
        }
        return new ResponseEntity(false, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/bins", method = {RequestMethod.POST})
    ResponseEntity<Boolean> addBins(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            double lon = Double.parseDouble(request.getParameter("lon"));
            double lat = Double.parseDouble(request.getParameter("lat"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            ret = BinModel.pushBin(new Bin(lon, lat, status));
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/bins", method = {RequestMethod.PUT})
    ResponseEntity<Boolean> updateBins(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            double lon = Double.parseDouble(request.getParameter("lon"));
            double lat = Double.parseDouble(request.getParameter("lat"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            int id = Integer.parseInt(request.getParameter("id"));
            ret = BinModel.updateBin(new Bin(lon, lat, status, id));
//            System.out.println("aaa: "+status);
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/bins", method = {RequestMethod.DELETE})
    void deleteBins(HttpServletRequest request, HttpServletResponse response){
        
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String id = request.getParameter("id");
            BinModel.deleteBin(id);
        } catch (Exception ex) { 
        }
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/users", method = {RequestMethod.GET})
    ResponseEntity<String> getUser(HttpServletRequest request, HttpServletResponse response){
        String ret = "";
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            ArrayList<User> list = UserModel.getListUser();
            ret = new Gson().toJson(list);
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/users", method = {RequestMethod.POST})
    ResponseEntity<Boolean> addUser(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String pass = request.getParameter("pass");
            String email = request.getParameter("email");
            String type = request.getParameter("type");
            String address = request.getParameter("address");
            Set<String> listUserId = UserModel.getListUserId();
            if (listUserId == null){
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            if (listUserId.contains(id)){
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            ret = UserModel.pushUser(new User(id, name, pass, email, address, type));
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/users", method = {RequestMethod.PUT})
    ResponseEntity<Boolean> updateUser(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String pass = request.getParameter("pass");
            String email = request.getParameter("email");
            String type = request.getParameter("type");
            String address = request.getParameter("address");
            ret = UserModel.updateUser(new User(id, name, pass, email, address, type));
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/users", method = {RequestMethod.DELETE})
    void deleteUser(HttpServletRequest request, HttpServletResponse response){
        
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String id = request.getParameter("id");
            UserModel.deleteUser(id);
        } catch (Exception ex) { 
        }
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/point", method = {RequestMethod.GET})
    ResponseEntity<String> getPoints(HttpServletRequest request, HttpServletResponse response){
        String ret = "";
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            List<EndPoint> list = EndPointModel.getList();
            ret = new Gson().toJson(list);
//            o.put("bins", arr);o.to
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/point", method = {RequestMethod.POST})
    ResponseEntity<Boolean> addPoint(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            double lon = Double.parseDouble(request.getParameter("lon"));
            double lat = Double.parseDouble(request.getParameter("lat"));
            String name = request.getParameter("name");
            ret = EndPointModel.push(new EndPoint(lon, lat, name));
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/point", method = {RequestMethod.PUT})
    ResponseEntity<Boolean> updatePoint(HttpServletRequest request, HttpServletResponse response){
        boolean ret = false;
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            double lon = Double.parseDouble(request.getParameter("lon"));
            double lat = Double.parseDouble(request.getParameter("lat"));
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            ret = EndPointModel.update(new EndPoint(lon, lat, id, name));
        } catch (NumberFormatException ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/point", method = {RequestMethod.DELETE})
    void deletePoint(HttpServletRequest request, HttpServletResponse response){
        
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String id = request.getParameter("id");
            EndPointModel.delete(id);
        } catch (Exception ex) { 
        }
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/chart_points", method = {RequestMethod.GET})
    ResponseEntity<String> getChartPoint(HttpServletRequest request, HttpServletResponse response){
        
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String data = StatisticModel.Instance.getStatsData();
            String ret = "{\"data\": " + data + "}";
            return new ResponseEntity<String>(ret, HttpStatus.OK) ;
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/chart_score", method = {RequestMethod.GET})
    ResponseEntity<String> getChartScore(HttpServletRequest request, HttpServletResponse response){
        
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            String data = StatisticModel.Instance.getChartScoreData();
            return new ResponseEntity<String>(data, HttpStatus.OK) ;
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/task", method = {RequestMethod.GET})
    ResponseEntity<String> getTasks(HttpServletRequest request, HttpServletResponse response){
        
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            return new ResponseEntity<String>(TaskModel.getTasks().toString(), HttpStatus.OK) ;
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/task", method = {RequestMethod.DELETE})
    ResponseEntity<Boolean> deleteTask(@RequestParam("userId") String userId, @RequestParam("isAccept") boolean isAccept){
        
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            try {
                TaskModel.removeTask(userId, isAccept);
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } catch (Exception e){}
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
    }
    
    private String createJWT(String id, String issuer, String subject, long liveTime) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("thanh");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                    .setIssuedAt(now)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (liveTime >= 0) {
        long expMillis = nowMillis + liveTime*1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
    private String getToken(Cookie[] cookies){
        String token = null;
        if (cookies != null && cookies.length > 0){
            for (int i = 0; i<cookies.length; i++){
                if (cookies[i].getName().compareTo("token") == 0)
                    token = cookies[i].getValue();
            }
        }
        return token;
    }
}
