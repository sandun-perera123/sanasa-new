/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandun-PC
 */
@Entity
@Table(name = "commonaccounttransaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commonaccounttransaction.findAll", query = "SELECT c FROM Commonaccounttransaction c"),
    @NamedQuery(name = "Commonaccounttransaction.findById", query = "SELECT c FROM Commonaccounttransaction c WHERE c.id = :id"),
    @NamedQuery(name = "Commonaccounttransaction.findByDate", query = "SELECT c FROM Commonaccounttransaction c WHERE c.date = :date"),
    @NamedQuery(name = "Commonaccounttransaction.findByAmount", query = "SELECT c FROM Commonaccounttransaction c WHERE c.amount = :amount")})
public class Commonaccounttransaction implements Serializable {

    @Column(name = "voucher_no")
    private String voucherNo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    private String date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @JoinColumn(name = "commonaccount_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Commonaccount commonaccountId;
    @JoinColumn(name = "commonaccounttransactiontype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Commonaccounttransactiontype commonaccounttransactiontypeId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;

    public Commonaccounttransaction() {
    }

    public Commonaccounttransaction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public boolean setDate(String date) {
        this.date = date;
        return date != null; 
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean setRemarks(String remarks) {
        boolean validity; 
       if(remarks==null || remarks.isEmpty()){ 
           validity=true; this.remarks=null;
       }
       else{
           remarks = remarks.trim(); 
           this.remarks=remarks; validity=true;        
       }
      return validity; 
    }

    public Commonaccount getCommonaccountId() {
        return commonaccountId;
    }

    public void setCommonaccountId(Commonaccount commonaccountId) {
        this.commonaccountId = commonaccountId;
    }

    public Commonaccounttransactiontype getCommonaccounttransactiontypeId() {
        return commonaccounttransactiontypeId;
    }

    public void setCommonaccounttransactiontypeId(Commonaccounttransactiontype commonaccounttransactiontypeId) {
        this.commonaccounttransactiontypeId = commonaccounttransactiontypeId;
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
        if (!(object instanceof Commonaccounttransaction)) {
            return false;
        }
        Commonaccounttransaction other = (Commonaccounttransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Commonaccounttransaction[ id=" + id + " ]";
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    
}
