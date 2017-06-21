/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Commonaccount;
import entity.Commonaccountcategory;
import entity.Commonaccountsubcategory;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class CommonaccountDao {
    
    public static ObservableList getAll(){ return CommonDao.select("Commonaccount.findAll"); } 

    public static Commonaccount getById(Integer id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Commonaccount) CommonDao.select("Commonaccount.findById", hmap).get(0);
        
    }

    public static void add(Commonaccount commonaccount) throws DaoException {

        CommonDao.insert(commonaccount);
        
    }

    public static void delete(Commonaccount commonaccount) {

        CommonDao.delete(commonaccount);
        
    }

    public static void update(Commonaccount commonaccount) {
        CommonDao.update(commonaccount);
    }

    public static ObservableList<Commonaccount> getAllByMainSub(Commonaccountcategory main, Commonaccountsubcategory sub) {

        HashMap hmap = new HashMap();
        hmap.put("main", main);
        hmap.put("sub", sub);
        return CommonDao.select("Commonaccount.findAllByMainSub", hmap);
        
    }
    
}
