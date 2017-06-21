/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Loanpayment;

/**
 *
 * @author Sandun-PC
 */
public class LoanpaymentDao {
    
    public static void add(Loanpayment loanpayment) throws DaoException {
        CommonDao.insert(loanpayment);
    }
    
}
