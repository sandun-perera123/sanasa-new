/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Commonaccounttransaction;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class CommonaccounttransactionDao {

    public static void add(Commonaccounttransaction commonaccounttransaction) throws DaoException {
        CommonDao.insert(commonaccounttransaction);
    }

    public static ObservableList<Commonaccounttransaction> getAll() {
        return CommonDao.select("commonaccounttransaction.findAll");
    }
    
    
}
