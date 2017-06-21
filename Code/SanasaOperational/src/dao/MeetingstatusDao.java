/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Meetingstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class MeetingstatusDao {
    
    public static ObservableList getAll(){ 
        return CommonDao.select("Meetingstatus.findAll"); 
    }

    public static Meetingstatus getById(int id) {

        Meetingstatus status = null;
        
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        
        if(CommonDao.select("Meetingstatus.findById",hmap).size() > 0){
            status = (Meetingstatus) CommonDao.select("Meetingstatus.findById",hmap).get(0);
        }
        
        return status;
        
    }
    
}
