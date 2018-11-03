/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.ArrayList;
import java.util.Date;  
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import middleware.TUser;
import middleware.TUserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.json.JSONObject;

/**
 *
 * @author hagyhang
 */
public class AdminServlet extends HttpServlet{
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        Claims claims = getClaims(request.getCookies());
        if (claims != null){
//                JSONObject o = new JSONObject(claims.getSubject());
//                response.addCookie(new Cookie("id", o.getString("id")));
//                response.addCookie(new Cookie("loginName", o.getString("loginName")));
//                response.addCookie(new Cookie("rule", o.getString("rule")));
            RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/admin.html");
            view.forward(request, response);
        }
        else {
            RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/login.html");
            view.forward(request, response); 
        }
    }
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        Claims claims = getClaims(request.getCookies());
        if (claims != null){
            
        }
        
    }
    private Claims getClaims(Cookie[] cookies){
        String token = null;
        Claims claims = null;
        if (cookies != null && cookies.length > 0){
            for (int i = 0; i<cookies.length; i++){
                if (cookies[i].getValue().compareTo("token") != 0)
                    token = cookies[i].getValue();
            }
        }
            
        if (token != null){
            try{
            claims = Jwts.parser()         
               .setSigningKey(DatatypeConverter.parseBase64Binary("thanh"))
               .parseClaimsJws(token).getBody();
            }catch(Exception e){
                Logger.getLogger(AdminServlet.class.getName()).log(Level.SEVERE, "Bad token!!", "");
            }
        }
        return claims; 
    }
    private TUser getUser(String user, String pass){
        TTransport transport;
        TUser result;
        transport = new TSocket("localhost", 9091);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TUserService.Client client = new TUserService.Client(protocol);
 
            result =  client.getTUser(user, pass);
            transport.close();
        } catch (TException e) {
            System.out.println(e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    }
}