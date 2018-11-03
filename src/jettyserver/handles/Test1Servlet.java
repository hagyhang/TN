/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hagyhang
 */
public class Test1Servlet extends HttpServlet{
    
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/test1.html");
        view.forward(request, response);
    }
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        
    }
}

