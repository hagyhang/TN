/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Util {
    public static String getID(){
        Long current = System.currentTimeMillis() / 1000;
        return current.toString();
    }
    public static void sleep(long miliseconds){
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
