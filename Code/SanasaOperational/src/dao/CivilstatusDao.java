/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Civilstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Suranga
 */
public class CivilstatusDao {
    
    public static ObservableList getAll(){ return CommonDao.select("Civilstatus.findAll"); } 

    public static Civilstatus getById(int id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Civilstatus) CommonDao.select("Civilstatus.findById", hmap).get(0);
        
    }
    
}
