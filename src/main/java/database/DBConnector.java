package database;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import enities.User;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

public class DBConnector {
    public static DBConnector Intance = new DBConnector();
    private static final HttpClient client = HttpClientBuilder.create().build();
    private static String URL = "https://smartbin-892a5.firebaseio.com/";
    private DBConnector(){
    }
    
    public User getUser(String name, String pass) throws Exception{
//        sendPost(URL, postParams);
        JSONObject result;
        String url = URL + "User/" + name + ".json";
        String res = sendGet(url);
        if (res == null) return null;
        result = new JSONObject(res);
        if (result != null && pass.equals(result.get("pass")))
            return new User(name, res);
        return null;
    }
    
    public String sendGet (String url) throws Exception {

	HttpGet get = new HttpGet(url);

	// add header
	get.setHeader("Host", "smartbin-892a5.firebaseio.com");
	get.setHeader("User-Agent", USER_AGENT);
	get.setHeader("Accept", 
             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	get.setHeader("Accept-Language", "en-US,en;q=0.5");
	get.setHeader("Connection", "keep-alive");
	get.setHeader("Content-Type", "application/json");

	HttpResponse response = client.execute(get);
	int responseCode = response.getStatusLine().getStatusCode();
	BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
        return result.toString();
  }
    
    public String sendPut (String url, String data) throws IOException{
        HttpPut put = new HttpPut(url);

	// add header
	put.setHeader("Host", "smartbin-892a5.firebaseio.com");
	put.setHeader("User-Agent", USER_AGENT);
	put.setHeader("Accept", 
             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	put.setHeader("Accept-Language", "en-US,en;q=0.5");
	put.setHeader("Connection", "keep-alive");
	put.setHeader("Content-Type", "application/json");
        
        put.setEntity(new StringEntity(data));
	HttpResponse response = client.execute(put);

	int responseCode = response.getStatusLine().getStatusCode();

	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Response Code : " + responseCode);

	BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
        return result.toString();
    }
    
    public String sendDelete (String url) throws Exception {
	HttpDelete delete = new HttpDelete(url);
	// add header
	delete.setHeader("Host", "smartbin-892a5.firebaseio.com");
	delete.setHeader("User-Agent", USER_AGENT);
	delete.setHeader("Accept", 
             "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	delete.setHeader("Accept-Language", "en-US,en;q=0.5");
	delete.setHeader("Connection", "keep-alive");
	delete.setHeader("Content-Type", "application/json");
	HttpResponse response = client.execute(delete);
	int responseCode = response.getStatusLine().getStatusCode();
	BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

	StringBuffer result = new StringBuffer();
	String line = "";
	while ((line = rd.readLine()) != null) {
		result.append(line);
	}
        return result.toString();
  }
    
    public static void main(String[] args) throws Exception {
        System.out.println(Intance.sendPut(URL + "Bin.json", ""));
//        System.out.println(Intance.getUser("thanh", "thanh123"));
    }
}
