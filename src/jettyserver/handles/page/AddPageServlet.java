/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author hagyhang
 */
public class AddPageServlet extends HttpServlet{
    protected void doPost( HttpServletRequest request,
                  HttpServletResponse response ) throws ServletException,
                                                IOException{
        String s = request.getParameter("links");
        String temp[] = s.split(";");
        ArrayList<String> links = new ArrayList<>();
        for (int i=0; i<temp.length; i++){
            links.add(temp[i]);
        }

        addLinks(links);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().println("Added!");
    }
    private void addLinks(ArrayList<String> links){
        TTransport transport;
        transport = new TSocket("localhost", 9090);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);

            client.addLinks(links);
            transport.close();
        } catch (TException e) {
            transport.close();
        }
    }
}
