/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Accounttypestatus;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class AccounttypestatusDao {

    public static ObservableList<Accounttypestatus> getAll() {
        return CommonDao.select("Accounttypestatus.findAll");
    }
    
}
