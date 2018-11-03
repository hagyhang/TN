/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles.user;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middleware.TUser;
import middleware.TUserService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.json.JSONObject;

/**
 *
 * @author hagyhang
 */
public class AddUserServlet extends HttpServlet{
    protected void doPost( HttpServletRequest request,
                  HttpServletResponse response ) throws ServletException,
                                                IOException{
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
        TUser user = new TUser();
        user.fullName = o.getString("fullName");
        user.loginName = o.getString("loginName");
        user.password = o.getString("password");
        user.rule = "user";
        String result = addUser(user);
        System.out.println(result);
    }
    public String addUser(TUser user){
        TTransport transport;
        String result;
        transport = new TSocket("localhost", 9091);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TUserService.Client client = new TUserService.Client(protocol);
 
            result =  client.addTUser(user);
            transport.close();
        } catch (TException e) {
            System.out.println(e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    }
}
