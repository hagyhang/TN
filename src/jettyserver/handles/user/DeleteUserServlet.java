/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hagyhang
 */
public class DeleteUserServlet extends HttpServlet{
    protected void doDelete( HttpServletRequest request,
                  HttpServletResponse response ) throws ServletException,
                                                IOException{
        request.getParameter("id");
    }
}