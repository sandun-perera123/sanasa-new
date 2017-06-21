/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javafx.collections.ObservableList;

/**
 *
 * @author Sandun-PC
 */
public class LoantypestatusDao {
    
    public static ObservableList getAll() {
        return CommonDao.select("Loantypestatus.findAll");
    }
    
}
