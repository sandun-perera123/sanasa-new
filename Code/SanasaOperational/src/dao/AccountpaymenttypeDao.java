/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Accountpaymenttype;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class AccountpaymenttypeDao {

    public static ObservableList<Accountpaymenttype> getAllExceptInterest() {

        return CommonDao.select("Accountpaymenttype.findAllExceptInterest");

    }

    public static Accountpaymenttype getByID(int id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Accountpaymenttype) CommonDao.select("Accountpaymenttype.findById", hmap).get(0);
        
    }

}
