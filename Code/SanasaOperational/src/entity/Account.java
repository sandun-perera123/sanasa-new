/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sandun
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByNo", query = "SELECT a FROM Account a WHERE a.no = :no"),
    @NamedQuery(name = "Account.findByDocreate", query = "SELECT a FROM Account a WHERE a.docreate = :docreate"),
    @NamedQuery(name = "Account.findByDoclosing", query = "SELECT a FROM Account a WHERE a.doclosing = :doclosing"),
    
    @NamedQuery(name = "Account.findAllByMember", query = "SELECT a FROM Account a WHERE a.smemberId = :member"),
    @NamedQuery(name = "Account.findAllByAccno", query = "SELECT a FROM Account a WHERE a.no = :no"),
    @NamedQuery(name = "Account.findAllByAccstatus", query = "SELECT a FROM Account a WHERE a.accountstatusId = :status"),
    @NamedQuery(name = "Account.findAllNonUpdatedAccounts", query = "SELECT a FROM Account a WHERE (YEAR(a.lastupdate) = YEAR(:date) AND MONTH(a.lastupdate) <> MONTH(:date)) OR a.lastupdate IS NULL")

})

public class Account implements Serializable {

    @Column(name = "lastupdate")
    @Temporal(TemporalType.DATE)
    private Date lastupdate;

    @Column(name = "availablebalance")
    private BigDecimal availablebalance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountId", fetch = FetchType.EAGER)
    private List<Accountpayment> accountpaymentList;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "balance")
    private BigDecimal balance;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "no")
    private Integer no;
    @Column(name = "docreate")
    @Temporal(TemporalType.DATE)
    private Date docreate;
    @Column(name = "doclosing")
    @Temporal(TemporalType.DATE)
    private Date doclosing;
    @JoinColumn(name = "accountstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Accountstatus accountstatusId;
    @JoinColumn(name = "accounttype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Accounttype accounttypeId;
    @JoinColumn(name = "smember_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Smember smemberId;

    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

   public boolean setNo(Integer no) {
        boolean validity = no != -1;
        if(validity){this.no = no; } else { this.no=null; } 
        return validity; 
    }


    public Date getDocreate() {
        return docreate;
    }

   public boolean setDocreate(Date docreate) {
        this.docreate = docreate;
        return docreate != null; 
    }


    public Date getDoclosing() {
        return doclosing;
    }

   public boolean setDoclosing(Date doclosing) {
        this.doclosing = doclosing;
        return doclosing != null; 
    }


    public Accountstatus getAccountstatusId() {
        return accountstatusId;
    }

    public void setAccountstatusId(Accountstatus accountstatusId) {
        this.accountstatusId = accountstatusId;
    }

    public Accounttype getAccounttypeId() {
        return accounttypeId;
    }

    public void setAccounttypeId(Accounttype accounttypeId) {
        this.accounttypeId = accounttypeId;
    }

    public Smember getSmemberId() {
        return smemberId;
    }

    public void setSmemberId(Smember smemberId) {
        this.smemberId = smemberId;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return accounttypeId.getName();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvailablebalance() {
        return availablebalance;
    }

    public void setAvailablebalance(BigDecimal availablebalance) {
        this.availablebalance = availablebalance;
    }

    @XmlTransient
    public List<Accountpayment> getAccountpaymentList() {
        return accountpaymentList;
    }

    public void setAccountpaymentList(List<Accountpayment> accountpaymentList) {
        this.accountpaymentList = accountpaymentList;
    }

    public Date getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(Date lastupdate) {
        this.lastupdate = lastupdate;
    }
    
}
