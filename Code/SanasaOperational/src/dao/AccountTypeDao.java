/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Accounttype;
import entity.Accounttypecategory;
import entity.Accounttypestatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class AccountTypeDao {

    public static ObservableList getAll() {
        return CommonDao.select("Accounttype.findAll");
    }

    public static Accounttype getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Accounttype) CommonDao.select("Accounttype.findById", hmap).get(0);
    }

    public static void add(Accounttype accounttype) throws DaoException {
        CommonDao.insert(accounttype);
    }

    public static ObservableList<Accounttype> getAllByCategory(Accounttypecategory acccategory) {
        HashMap hmap = new HashMap();
        hmap.put("acccategory", acccategory);

        return CommonDao.select("Accounttype.findAllByCategory", hmap);
    }

    public static ObservableList<Accounttype> getAllByStatus(Accounttypestatus accstatus) {
        HashMap hmap = new HashMap();
        hmap.put("accstatus", accstatus);

        return CommonDao.select("Accounttype.findAllByStatus", hmap);
    }

    public static void update(Accounttype accounttype) {
        CommonDao.update(accounttype);
    }

    public static void delete(Accounttype accounttype) {
        CommonDao.delete(accounttype);
    }

}
