/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Sharegain;
import entity.Smember;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class SharegainDao {

    public static ObservableList getAll() {
        return CommonDao.select("Sharegain.findAll");
    }

    public static void add(Sharegain sharegain) throws DaoException {
        CommonDao.insert(sharegain);
    }

    public static Sharegain getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Sharegain) CommonDao.select("Sharegain.findById", hmap).get(0);
    }

    public static ObservableList<Sharegain> getAllByDateRange(Date fdate, Date tdate) {
        HashMap hmap = new HashMap();
        hmap.put("fdate", fdate);
        hmap.put("tdate", tdate);
        
        return CommonDao.select("Sharegain.findAllByDateRange", hmap);
    }

    public static ObservableList<Sharegain> getAllByMember(Smember member) {
        HashMap hmap = new HashMap();
        hmap.put("member", member);

        return CommonDao.select("Sharegain.findAllByMember", hmap);
    }

    public static void update(Sharegain sharegain) {
        CommonDao.update(sharegain);
    }

    public static void delete(Sharegain sharegain) {
        CommonDao.delete(sharegain);
    }

    public static ObservableList getMonthlyShareSummery() {

        return CommonDao.select("Sharegain.getMonthlyShareSummery");

    }

}
