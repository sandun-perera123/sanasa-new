/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Attendancetype;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class AttendancetypeDao {

    public static ObservableList getAll() {
        return CommonDao.select("Attendancetype.findAll");
    }

    public static Attendancetype getById(Integer id) {
        
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Attendancetype) CommonDao.select("Attendancetype.findById", hmap).get(0);
        
    }

}
