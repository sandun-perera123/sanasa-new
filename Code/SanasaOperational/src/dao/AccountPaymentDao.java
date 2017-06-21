/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Accountpayment;
import java.util.Date;

import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class AccountPaymentDao {
    

    public static ObservableList<Accountpayment> getAllExceptInterest() {

        return CommonDao.select("Accountpayment.findAllExceptInterest");

    }

    public static Long getCountByDate(Date date) {

        HashMap hm = new HashMap();
        hm.put("date", date);

        return (Long) CommonDao.select("Accountpayment.findAllCountByDate", hm).get(0);

    }
    
    public static ObservableList getMonthlyDipositSummery() {

        return CommonDao.select("Accountpayment.getMonthlyDipositSummery");

    }

    public static void add(Accountpayment ap) throws DaoException {
        CommonDao.insert(ap);
    }

}
