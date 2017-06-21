/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Loanstatus;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class LoanStatusDao {
    
    public static ObservableList getAll() {
        return CommonDao.select("Loanstatus.findAll");
    }

    public static Loanstatus getById(int id) {

        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Loanstatus) CommonDao.select("Loanstatus.findById", hmap).get(0);

    }
    
}
