/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import entity.Employeestatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Suranga
 */
public class EmployeestatusDao {
    
    public static ObservableList getAll(){ return CommonDao.select("Employeestatus.findAll"); } 

    public static Employeestatus getById(int id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Employeestatus) CommonDao.select("Employeestatus.findById", hmap).get(0);
        
    }
    
}
