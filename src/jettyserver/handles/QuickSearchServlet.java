/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middleware.TSummaryPageService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author hagyhang
 */
public class QuickSearchServlet extends HttpServlet{
        @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        
        RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/quick_search.html");
        view.forward(request, response);
    }
    protected void doPut( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        String key = request.getParameter("key");
        System.out.println("key:" + key);
        //call middle ware
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);
 
            client.addLinksByKey(key);
            transport.close();
        } catch (TException e) {
            System.out.println("Error in getResult!! " + e.getMessage());
            transport.close();
        }
    }
    protected void doPost( HttpServletRequest request,
                      HttpServletResponse response ) throws ServletException,
                                                    IOException{
        ArrayList<String> ids = new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reader.close();
        String data;
        data = buffer.toString();
        
        JSONObject o = new JSONObject(data);
        JSONArray temp = o.getJSONArray("ids");
        System.out.println("ids: " + temp);
        for (int i=0; i<temp.length(); i++){
            ids.add(temp.getString(i));
        }
        
        ArrayList<middleware.TSummaryPage> pages = getResults(ids);
        String json;
        if (pages == null){
            pages = new ArrayList<middleware.TSummaryPage>();
        }
        Gson gson = new Gson();
        json = gson.toJson(pages);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(json);
        response.getWriter().println(json);
        
    } 
    public ArrayList<middleware.TSummaryPage> getResults(ArrayList<String> ids){
        ArrayList<middleware.TSummaryPage> result=null;
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);
 
            result = (ArrayList<middleware.TSummaryPage>) client.getTSummaryPagesByIDs(ids);
            transport.close();
        } catch (TException e) {
            System.out.println("Error in getResult!! " + e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    } 
}
