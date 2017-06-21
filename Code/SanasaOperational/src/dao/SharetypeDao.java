/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Sharetype;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class SharetypeDao {

    public static ObservableList getAll() {
        return CommonDao.select("Sharetype.findAll");
    }

    public static Sharetype getByID(int id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Sharetype) CommonDao.select("Sharetype.findById", hmap).get(0);
    }

}
