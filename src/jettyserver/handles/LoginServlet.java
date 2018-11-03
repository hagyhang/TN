/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jettyserver.handles;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import middleware.TSummaryPageService;
import middleware.TUser;
import middleware.TUserService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.javasimon.Counter;
import org.javasimon.SimonManager;
import org.javasimon.Split;
import org.javasimon.Stopwatch;

/**
 *
 * @author hagyhang
 */
public class LoginServlet extends HttpServlet{
    
    public static final String TOPIC = "login-queue";
    public static final String BROKER_LIST = "localhost:9092";
    
    public void ProduceToKafak(String topicName, String broker_list, List<String> links ){
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);     
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);   
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", 
           LongSerializer.class.getName());  
        props.put("value.serializer", 
           StringSerializer.class.getName());
        Producer<Long, String> producer = new KafkaProducer<Long, String>(props);
        for (int i=0; i<links.size(); i++)
            producer.send(new ProducerRecord<Long, String>(topicName, links.get(i)));
        System.out.println(links.size() + "message are sent");
        producer.close();
    }
    
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        Stopwatch stopwatch = SimonManager.getStopwatch("login-stopwatch");
        Split split = stopwatch.start();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//           
//        }
        RequestDispatcher view = getServletContext().getRequestDispatcher("/layout/login.html");
        view.forward(request, response);
        split.stop(); 
        System.out.println("stopwatch: " + stopwatch.toString());
    }
    protected void doPost( HttpServletRequest request,
                          HttpServletResponse response ) throws ServletException,
                                                        IOException{
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        TUser result = getUser(user, pass);
        Gson gson = new Gson();
        String data = gson.toJson(result);
        
        String jwt = createJWT("id", "", data, 3600);
        Cookie cookie = new Cookie("token", jwt);
        response.addCookie(cookie);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(data);
        response.getWriter().println(data);
        
    }
    private TUser getUser(String user, String pass){
        TTransport transport;
        TUser result;
        transport = new TSocket("localhost", 9091);
        try {     
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            TUserService.Client client = new TUserService.Client(protocol);
 
            result =  client.getTUser(user, pass);
            transport.close();
        } catch (TException e) {
            System.out.println(e.getMessage());
            result = null;
            transport.close();
        }
        return result;
    }
    private String createJWT(String id, String issuer, String subject, long liveTime) {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("thanh");
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                                    .setIssuedAt(now)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (liveTime >= 0) {
        long expMillis = nowMillis + liveTime*1000;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}