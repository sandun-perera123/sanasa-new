/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Memberstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class MemberstatusDao {
    
     public static ObservableList getAll() {
        return CommonDao.select("Memberstatus.findAll");
    }

    public static Memberstatus getById(int id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Memberstatus) CommonDao.select("Memberstatus.findById", hmap).get(0);
        
    }
    
}
