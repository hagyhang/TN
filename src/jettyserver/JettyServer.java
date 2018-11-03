/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.servlet.DispatcherType;
import javax.websocket.server.ServerContainer;
import jettyserver.handles.AdminServlet;
import jettyserver.handles.EventSocket;
import jettyserver.handles.zzz.EmployeeServlet;
import jettyserver.handles.user.GetUsersServlet;
import jettyserver.handles.LoginServlet;
import jettyserver.handles.QuickSearchServlet;
import jettyserver.handles.Test1Servlet;
import jettyserver.handles.TestServlet;
import jettyserver.handles.page.AddPageServlet;
import jettyserver.handles.page.DeletePageServlet;
import jettyserver.handles.page.GetPagesServlet;
import jettyserver.handles.zzz.SearchServlet;
import jettyserver.handles.user.AddUserServlet;
import jettyserver.handles.user.DeleteUserServlet;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler; 
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
/**
 *
 * @author cpu10488
 */
public class JettyServer {

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("config/log4j.properties");
        Server server = new Server(8080);
        
//        ServletHandler servlet_handler = new ServletHandler();
//        servlet_handler.addServletWithMapping(EmployeeServlet.class, "/employee");
//        servlet_handler.addServletWithMapping(SearchServlet.class, "/search");
//        
//        ResourceHandler resource_handler = new ResourceHandler();
//        resource_handler.setDirectoriesListed(true);
//        resource_handler.setWelcomeFiles(new String[]{ "/layout/index.html" });
//        resource_handler.setResourceBase("./src/resources");
        
//        Path webrootPath = new File("./src/resources").toPath().toRealPath();
//        URI webrootUri = webrootPath.toUri();
//        System.err.println("webroot uri: " + webrootUri);
//        Resource webroot = Resource.newResource(webrootUri);
//        if (!webroot.exists())
//        {
//            System.err.println("Resource does not exist: " + webroot);
//            System.exit(-1);
//        }
//        if (!webroot.isDirectory())
//        {
//            System.err.println("Resource is not a directory: " + webroot);
//            System.exit(-1);
//        }
//        Resource theBaseResource = null;
//        theBaseResource = Resource.newResource( "." );
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setWelcomeFiles(new String[] { "index.html" });
        context.setResourceBase("./src/resources");
        
		context.addServlet(TestServlet.class, "/test");
		context.addServlet(Test1Servlet.class, "/test1");
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(QuickSearchServlet.class, "/quick-search");
        context.addServlet(AdminServlet.class, "/admin");
        
        context.addServlet(GetUsersServlet.class, "/user/users");
        context.addServlet(AddUserServlet.class, "/user/add");
        context.addServlet(DeleteUserServlet.class, "/user/delete");
        
        context.addServlet(GetPagesServlet.class, "/page/pages");
        context.addServlet(AddPageServlet.class, "/page/add");
        context.addServlet(DeletePageServlet.class, "/page/delete");
        
        ServletHolder holderPwd = new ServletHolder("default", DefaultServlet.class);
        context.addServlet(holderPwd,"/*");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {  context });
        server.setHandler(handlers);
        
        // Initialize javax.websocket layer
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);

        // Add WebSocket endpoint to javax.websocket layer
        wscontainer.addEndpoint(EventSocket.class);
        
        server.start();
//        server.dump(System.err);
//        Logger.getLogger(JettyServer.class.getName()).info("info!!");
//        Logger.getLogger(JettyServer.class.getName()).warn("warn!!");
        try {
            server.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(JettyServer.class.getName()).log(Level.ALL, null, ex);
        }
    }
}
