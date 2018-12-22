/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import org.json.JSONObject;

/**
 *
 * @author Admin
 */
public class User extends BaseEnity{
    public String id;
    public String name;
    public String pass;
    public String address;
    public String email;
    public String type;
    
    public User (String name, String pass){
        this.id = name;
        this.pass = pass;
    }
    public User (String id, String name, String pass, String email, String address, String type){
        this.id = id;
        this.pass = pass;
        this.name = name;
        this.email = email;
        this.address = address;
        this.type = type;
        
    }
    
    public User (String id, JSONObject o){
        this.id = id;
        this.name = o.getString("name");
        this.email = o.getString("email");
        this.address = o.getString("address");
        this.type = o.getString("type");
        
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void build(JSONObject o, String id) {
        this.id = id;
        this.name = o.getString("name");   
        this.pass = o.getString("pass");
        this.address = o.getString("address");
        this.email = o.getString("email");
        this.type = o.getString("type");
    }
    
}
