/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.zzz;

import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jettyserver.GCDService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author hagyhang
 */
public class EmployeeServlet extends HttpServlet{
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        int a = Integer.parseInt(request.getParameter("a"));
        int b = Integer.parseInt(request.getParameter("b"));
        int r = getResult(a, b);
        
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(getLayout(a, b, r));
        } catch (TemplateException ex) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getResult(int a, int b){
        int result = -1;
        try {
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            GCDService.Client client = new GCDService.Client(protocol);
 
            result = client.getGCD(a, b);
            System.out.println("UCLN la: " + result);
            transport.close();
        } catch (TException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
    
    public String getLayout(int a, int b, int r) throws TemplateException{
        int result;
        TemplateLoader templateLoader = TemplateResourceLoader.create("layout/");
        //Template load file
        Template template = templateLoader.getTemplate("employee.xtm");
        //Use TemplateDictionary to put to xtm
        TemplateDictionary templeDictionary = new TemplateDictionary();
        templeDictionary.setVariable("a", "" + a);
        templeDictionary.setVariable("b", "" + b);
        templeDictionary.setVariable("result", "" + r);
        String data = template.renderToString(templeDictionary);
        return data;
    }
}
