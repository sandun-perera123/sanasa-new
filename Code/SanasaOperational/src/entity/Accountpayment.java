/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandun-PC
 */
@Entity
@Table(name = "accountpayment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accountpayment.findAll", query = "SELECT a FROM Accountpayment a"),
    @NamedQuery(name = "Accountpayment.findById", query = "SELECT a FROM Accountpayment a WHERE a.id = :id"),
//    @NamedQuery(name = "Accountpayment.findByBillno", query = "SELECT a FROM Accountpayment a WHERE a.billno = :billno"),
    @NamedQuery(name = "Accountpayment.findByAmount", query = "SELECT a FROM Accountpayment a WHERE a.amount = :amount"),
    @NamedQuery(name = "Accountpayment.findByDate", query = "SELECT a FROM Accountpayment a WHERE a.date = :date"),

    @NamedQuery(name = "Accountpayment.findAllExceptInterest", query = "SELECT a FROM Accountpayment a WHERE a.accountpaymenttypeId.id <> 3"),
    @NamedQuery(name = "Accountpayment.findAllCountByDate", query = "SELECT COUNT(*) FROM Accountpayment a WHERE a.date = :date"),
    @NamedQuery(name = "Accountpayment.getMonthlyDipositSummery", query = "SELECT NEW MAP(MONTH(a.date) AS Mth, SUM(a.amount) AS Amount) FROM Accountpayment a GROUP BY MONTH(a.date)")

})

public class Accountpayment implements Serializable {

    @Column(name = "voucher_no")
    private String voucherNo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Account accountId;
    @JoinColumn(name = "accountpaymenttype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Accountpaymenttype accountpaymenttypeId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;

    public Accountpayment() {
    }

    public Accountpayment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    public Accountpaymenttype getAccountpaymenttypeId() {
        return accountpaymenttypeId;
    }

    public void setAccountpaymenttypeId(Accountpaymenttype accountpaymenttypeId) {
        this.accountpaymenttypeId = accountpaymenttypeId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accountpayment)) {
            return false;
        }
        Accountpayment other = (Accountpayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Accountpayment[ id=" + id + " ]";
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

}
