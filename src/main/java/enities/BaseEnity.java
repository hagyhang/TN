/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class BaseEnity {
    public String toJSONString(){
        String ret = "{";
        try {
            Field[] fields = this.getClass().getFields();
            for(int i=0; i<fields.length - 1; i++){
                Object o = fields[i].get(this);
                ret += "\"" + fields[i].getName() + "\": " + "\"" + o.toString() + "\", ";
            }
            ret += "\"" + fields[fields.length - 1].getName() + "\": " + "\"" + fields[fields.length - 1].get(this).toString() + "\"}";
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Bin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Bin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
}
