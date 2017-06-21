/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sandun
 */
@Entity
@Table(name = "accounttype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accounttype.findAll", query = "SELECT a FROM Accounttype a"),
    @NamedQuery(name = "Accounttype.findById", query = "SELECT a FROM Accounttype a WHERE a.id = :id"),
    @NamedQuery(name = "Accounttype.findByName", query = "SELECT a FROM Accounttype a WHERE a.name = :name"),
    
    
    @NamedQuery(name = "Accounttype.findAllByCategory", query = "SELECT a FROM Accounttype a WHERE a.accounttypecategoryId = :acccategory"),
    @NamedQuery(name = "Accounttype.findAllByStatus", query = "SELECT a FROM Accounttype a WHERE a.accounttypestatusId = :accstatus")

})
public class Accounttype implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "min")
    private BigDecimal min;
    @Column(name = "interest")
    private BigDecimal interest;
    @JoinColumn(name = "accounttypecategory_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Accounttypecategory accounttypecategoryId;
    @JoinColumn(name = "accounttypestatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Accounttypestatus accounttypestatusId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accounttypeId", fetch = FetchType.EAGER)
    private List<Account> accountList;

    public Accounttype() {
    }

    public Accounttype(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        boolean validity = name != null && !name.isEmpty() && !name.equals("") && name != "";
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
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
        if (!(object instanceof Accounttype)) {
            return false;
        }
        Accounttype other = (Accounttype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public BigDecimal getMin() {
        return min;
    }

    public boolean setMin(BigDecimal min) {
        boolean validity = min != null;
        if (validity) {
            this.min = min;
        } else {
            this.min = null;
        }
        return validity;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public boolean setInterest(BigDecimal interest) {
        boolean validity = (interest != null) && (interest.doubleValue() <= 100.00);
        if(validity){this.interest = interest; } else { this.interest=null; } 
        return validity;
    }

    public Accounttypecategory getAccounttypecategoryId() {
        return accounttypecategoryId;
    }

    public boolean setAccounttypecategoryId(Accounttypecategory accounttypecategoryId) {
        boolean validity = accounttypecategoryId != null;
        if (validity) {
            this.accounttypecategoryId = accounttypecategoryId;
        } else {
            this.accounttypecategoryId = null;
        }
        return validity;
    }

    public Accounttypestatus getAccounttypestatusId() {
        return accounttypestatusId;
    }

    public boolean setAccounttypestatusId(Accounttypestatus accounttypestatusId) {
        boolean validity = accounttypestatusId!=null;  
        if(validity){this.accounttypestatusId = accounttypestatusId;}else {this.accounttypestatusId=null;} 
        return validity;
    }

}
