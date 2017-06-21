/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Commonaccountcategory;
import entity.Commonaccountsubcategory;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class CommonaccountsubcategoryDao {
    
    public static ObservableList getAll(){ return CommonDao.select("Commonaccountsubcategory.findAll"); } 

    public static ObservableList<Commonaccountsubcategory> getAllByCategory(Commonaccountcategory category) {

        HashMap hmap = new HashMap();
        hmap.put("category", category);

        return CommonDao.select("Commonaccountsubcategory.findAllByCategory", hmap);

    }
    
}
