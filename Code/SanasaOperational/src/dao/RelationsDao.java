/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Relations;
import entity.Smember;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class RelationsDao {

    public static ObservableList getAll() {
        return CommonDao.select("Relations.findAll");
    }

    public static Relations getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Relations) CommonDao.select("Relations.findById", hmap).get(0);
    }

    public static void add(Relations relation) throws DaoException {
        CommonDao.insert(relation);
    }

    public static void delete(Relations relation) {
        CommonDao.delete(relation);
    }

    public static void update(Relations relation) {
        CommonDao.update(relation);
    }

    public static ObservableList getAllByMember(Smember member) {
        HashMap hmap = new HashMap();
        hmap.put("member", member);

        return CommonDao.select("Relations.findAllByMember", hmap);
    }

    public static ObservableList<Relations> getAllByNic(String nic) {

        HashMap hmap = new HashMap();
        hmap.put("nic", "%" + nic + "%");

        return CommonDao.select("Relations.findAllByNic", hmap);

    }

}
