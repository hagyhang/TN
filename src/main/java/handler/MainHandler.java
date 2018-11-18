/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handler;

import com.google.gson.Gson;
import database.DBConnector;
import enities.Bin;
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
import java.util.List;
import model.BinModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    String dashboard() {
        return "dashboard";
    }
    
    @RequestMapping("/statistic")
    String statistic() {
        return "statistic";
    }
    
    @RequestMapping("/management")
    String management() {
        return "management";
    }
    
    @RequestMapping("/management-add")
    String managementAdd() {
        return "management-add";
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    ResponseEntity<Boolean> login(HttpServletRequest request, HttpServletResponse response){
        try {
            String name = request.getParameter("name");
            String pass = request.getParameter("pass");
            String token = getToken(request.getCookies());
            if (token != null && mapToken.containsKey(token)){
                if (token.compareTo(mapToken.get(name)) == 0){          
                    return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
                }
            } 
            User user = DBConnector.Intance.getUser(name, pass);
            if (user != null) {
                mapToken.put(name, token);
                response.addCookie(new Cookie(name, pass));
                response.setStatus(200);
                return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
            }
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.OK);
    }
    
    @CrossOrigin(origins = "http://localhost:5000/*")
    @RequestMapping(value = "/bins", method = {RequestMethod.GET})
    ResponseEntity<String> getBins(HttpServletRequest request, HttpServletResponse response){
        String ret = "";
        try {
//            String token = getToken(request.getCookies());
//            if (token != null && mapToken.containsKey(token)){
//                if (token.compareTo(mapToken.get(name)) == 0){          
//                }
//            }
            List<Bin> list = BinModel.getListBin();
            ret = new Gson().toJson(list);
//            o.put("bins", arr);o.to
        } catch (Exception ex) { 
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
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
            System.out.println("aaa: "+status);
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
                if (cookies[i].getValue().compareTo("token") != 0)
                    token = cookies[i].getValue();
            }
        }
        return token;
    }
}
