/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.page;

import java.io.IOException;
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
public class DeletePageServlet extends HttpServlet{
    protected void doDelete( HttpServletRequest request,
                  HttpServletResponse response ) throws ServletException,
                                                IOException{
        String id = request.getParameter("id");
        deleteTSummaryPage(id);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().println("Deleted!");
    }
    
    private String deleteTSummaryPage(String id){
        TTransport transport;
        String results;
        transport = new TSocket("localhost", 9090); 
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TSummaryPageService.Client client = new TSummaryPageService.Client(protocol);

            results = client.deleteTSummaryPage(id);
            transport.close();
        } catch (TException e) {
            results = null;
            transport.close();
        }
        return results;
    }
}
