/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.page;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import jettyserver.handles.AdminServlet;
import middleware.TSummaryPageService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author hagyhang
 */
public class GetPagesServlet extends HttpServlet{
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        Claims claims = getClaims(request.getCookies());
        if (claims != null){
            ArrayList<middleware.TSummaryPage> pages = getResults();
            String json;
            if (pages == null){
                pages = new ArrayList<middleware.TSummaryPage>();
            }
            Gson gson = new Gson();
            json = gson.toJson(pages);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(json);
        }
        else {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Session Invalid!");
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
    public ArrayList<middleware.TSummaryPage> getResults(){
        ArrayList<middleware.TSummaryPage> result=null;
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);
 
            result = (ArrayList<middleware.TSummaryPage>) client.getTSummaryPages();
            transport.close();
        } catch (TException e) {
            System.out.println("Error in getResult!! " + e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    }
}
