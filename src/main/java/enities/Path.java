/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enities;

import java.util.List;
import model.BinModel;
import model.EndPointModel;

/**
 *
 * @author Admin
 */
public class Path {
    public List<Bin> bins;
    public List<EndPoint> points;

    public List<Bin> getBins() {
        return bins;
    }

    public void setBins(List<Bin> bins) {
        this.bins = bins;
    }

    public List<EndPoint> getPoints() {
        return points;
    }

    public void setPoints(List<EndPoint> points) {
        this.points = points;
    }


    
}
