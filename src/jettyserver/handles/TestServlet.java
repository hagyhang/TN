/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author hagyhang
 */
public class TestServlet extends HttpServlet{
    
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{

        RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/test.html");
        view.forward(request, response);
    }
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
		try {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (!item.isFormField()) {
					String name = item.getFieldName();
					request.setAttribute(name, item);
				} else {
					String name = item.getFieldName();
					if (!name.isEmpty()) {
						request.setAttribute(item.getFieldName(), item.getString("utf-8"));
					}
				}
			};
		} catch (FileUploadException ex) {
			Logger.getLogger(TestServlet.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
}
