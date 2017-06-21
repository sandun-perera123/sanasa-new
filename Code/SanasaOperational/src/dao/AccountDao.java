/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Account;
import entity.Accountstatus;
import entity.Smember;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class AccountDao {
    
    public static ObservableList getAll() {
        return CommonDao.select("Account.findAll");
    }
    
    public static ObservableList getAllNonUpdatedAccounts(Date date) {
        
        HashMap hmap = new HashMap();
        hmap.put("date", date);
        
        return CommonDao.select("Account.findAllNonUpdatedAccounts",hmap);
    }
    
    public static Account getById(Integer id){
        
        Account account = null;
        
        HashMap hmap = new HashMap();
        hmap.put("id", id);
        
        if(CommonDao.select("Account.findById",hmap).size() > 0){
            account = (Account) CommonDao.select("Account.findById",hmap).get(0);
        }
        
        return account;
        
    }

    public static ObservableList<Account> getAllByMember(Smember member) {
        HashMap hmap = new HashMap();
        hmap.put("member", member);

        return CommonDao.select("Account.findAllByMember", hmap);
    }

    public static void add(Account account) throws DaoException {
        CommonDao.insert(account);
    }

    public static void delete(Account account) {
        CommonDao.delete(account);
    }

    public static void update(Account account) {
        CommonDao.update(account);
    }

    public static ObservableList<Account> getAllByAccno(Integer accno) {
        HashMap hmap = new HashMap();
        hmap.put("no", accno);

        return CommonDao.select("Account.findAllByAccno", hmap);
    }

    public static ObservableList<Account> getAllByAccstatus(Accountstatus status) {
        HashMap hmap = new HashMap();
        hmap.put("status", status);

        return CommonDao.select("Account.findAllByAccstatus", hmap);
    }
    
}
