/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Propertygain;
import entity.Sharegain;
import entity.Smember;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class PropertygainDao {
    
    public static ObservableList getAll() {
        return CommonDao.select("Propertygain.findAll");
    }

    public static void add(Propertygain propertygain) throws DaoException {
        CommonDao.insert(propertygain);
    }

    public static Propertygain getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Propertygain) CommonDao.select("Propertygain.findById", hmap).get(0);
    }

    public static void update(Propertygain propertygain) {
        CommonDao.update(propertygain);
    }

    public static void delete(Propertygain propertygain) {
        CommonDao.delete(propertygain);
    }

    public static ObservableList<Propertygain> getAllByMember(Smember member) {
        HashMap hmap = new HashMap();
        hmap.put("member", member);

        return CommonDao.select("Propertygain.findAllByMember", hmap);
    }
    
}
