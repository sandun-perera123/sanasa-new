/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;

/**
 *
 * @author Sandun-PC
 */
public class MonthlyDipositSummery {
    
    private String month;
    private BigDecimal total;

    public void setMonth(String month) {
        this.month = month;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getTotal() {
        return total;
    }
    
    
    
}
