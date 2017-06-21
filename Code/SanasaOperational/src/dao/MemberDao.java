/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Memberstatus;
import entity.Smember;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class MemberDao {

    public static ObservableList getAll() {
        return CommonDao.select("Smember.findAll");
    }

    public static void add(Smember member) throws DaoException {
        CommonDao.insert(member);
    }

    public static ObservableList<Smember> getAllByName(String name) {
        HashMap hmap = new HashMap();
        hmap.put("name", "%" + name + "%");

        return CommonDao.select("Smember.findAllByName", hmap);
    }

    public static ObservableList<Smember> getAllByNic(String nic) {

        HashMap hmap = new HashMap();
        hmap.put("nic", "%" + nic + "%");

        return CommonDao.select("Smember.findAllByNic", hmap);
    }

    public static ObservableList<Smember> getAllByStatus(Memberstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);

        return CommonDao.select("Smember.findAllByStatus", hmap);
    }

    public static ObservableList<Smember> getAllByMemberid(String memberid) {
        HashMap hmap = new HashMap();
        hmap.put("memberid", "%" + memberid + "%");
        return CommonDao.select("Smember.findAllByMemberid", hmap);
    }

    public static Smember getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Smember) CommonDao.select("Smember.findById", hmap).get(0);
    }

    public static void delete(Smember member) {
        CommonDao.delete(member);
    }

    public static void update(Smember member) {
        CommonDao.update(member);
    }

    public static String getLastMemberID() {

        String last = "0";

        if (CommonDao.select("Smember.findLastMemberID").size() > 0) {
            last = (String) CommonDao.select("Smember.findLastMemberID").get(0);
        }

        return last;
    }

    public static ObservableList<Smember> getAllByRelationCount() {

        return CommonDao.select("Smember.findAllByRelationCount");

    }

    public static ObservableList<Smember> getAllNotPropertyMembers() {

        return CommonDao.select("Smember.findAllNotPropertyMembers");

    }

}
