/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.AccountDao;
import entity.Account;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Sandun-PC
 */
public class Test {
    
    public static void main(String[] args) {
        
        List<Account> accounts = AccountDao.getAll();
        
        
        
        for (Account acc : accounts) {
            
            System.out.println("Old Balance -> "+ acc.getBalance());
            
            BigDecimal interestRate = acc.getAccounttypeId().getInterest().divide(new BigDecimal(100.00));
            System.out.println(interestRate);
            BigDecimal interestedValue = acc.getBalance().multiply(interestRate);
            System.out.println(interestedValue);
            BigDecimal newBalance = acc.getBalance().add(interestedValue).setScale(2);
            
            acc.setBalance(newBalance);
            
            System.out.println("New Balance -> "+ acc.getBalance());
            System.out.println("##############################");
            
        }

     
        
    }
    
}
