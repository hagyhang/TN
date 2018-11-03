/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.zzz;

import com.google.gson.Gson;
import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middleware.TSummaryPage;
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
public class SearchServlet extends HttpServlet{
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{

//        response.setContentType("text/html");
//        response.setStatus(HttpServletResponse.SC_OK);
//        try {
//            response.getWriter().println(getLayout());
//        } catch (TemplateException ex) {
//            Logger.getLogger(SearchServlet.class.getName()).log(Level.SEVERE, null, ex);
//        }

        RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/search.html");
        view.forward(request, response);
//        response.sendRedirect("/layout/search.html");
    }
    @Override
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        ArrayList<String> links = new ArrayList<String>();
        links.add(request.getParameter("link"));
        ArrayList<TSummaryPage> pages = getResults(links);
        String json;
        if (pages == null){
            pages = new ArrayList<TSummaryPage>();
        }
            Gson gson = new Gson();
            json = gson.toJson(pages);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(json);
        response.getWriter().println(json);
    }
    public ArrayList<TSummaryPage> getResults(ArrayList<String> links){
        ArrayList<TSummaryPage> result=null;
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);
 
            result = (ArrayList<TSummaryPage>) client.getTSummaryPagesByIDs(links);
            transport.close();
        } catch (TException e) {
            System.out.println("Error in getResult!! " + e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    }    
    public String getLayout() throws TemplateException{
        int result;
        TemplateLoader templateLoader = TemplateResourceLoader.create("resources/layout/");
        //Template load file
        Template template = templateLoader.getTemplate("search.xtm");
        //Use TemplateDictionary to put to xtm
        TemplateDictionary templeDictionary = new TemplateDictionary();
        String data = template.renderToString(templeDictionary);
        return data;
    }
}
