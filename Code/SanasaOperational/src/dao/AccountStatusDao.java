/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Accountstatus;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class AccountStatusDao {

    public static ObservableList getAll() {
        return CommonDao.select("Accountstatus.findAll");
    }
    
}
