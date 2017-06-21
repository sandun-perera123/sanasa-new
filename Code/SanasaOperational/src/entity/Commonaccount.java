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
 * @author Sandun-PC
 */
@Entity
@Table(name = "commonaccount")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commonaccount.findAll", query = "SELECT c FROM Commonaccount c ORDER BY c.commonaccountsubcategoryId.commonaccountcategoryId.id, c.commonaccountsubcategoryId.id ASC"),
    @NamedQuery(name = "Commonaccount.findById", query = "SELECT c FROM Commonaccount c WHERE c.id = :id"),
    @NamedQuery(name = "Commonaccount.findByName", query = "SELECT c FROM Commonaccount c WHERE c.name = :name"),

    @NamedQuery(name = "Commonaccount.findAllByMainSub", query = "SELECT c FROM Commonaccount c WHERE c.commonaccountsubcategoryId = :sub AND c.commonaccountsubcategoryId.commonaccountcategoryId = :main")

})
public class Commonaccount implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commonaccountId", fetch = FetchType.EAGER)
    private List<Commonaccounttransaction> commonaccounttransactionList;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "balance")
    private BigDecimal balance;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "commonaccountsubcategory_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Commonaccountsubcategory commonaccountsubcategoryId;

    public Commonaccount() {
    }

    public Commonaccount(Integer id) {
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
        boolean validity = !name.isEmpty();
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
        }
        return validity;
    }

    public Commonaccountsubcategory getCommonaccountsubcategoryId() {
        return commonaccountsubcategoryId;
    }

    public void setCommonaccountsubcategoryId(Commonaccountsubcategory commonaccountsubcategoryId) {
        this.commonaccountsubcategoryId = commonaccountsubcategoryId;
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
        if (!(object instanceof Commonaccount)) {
            return false;
        }
        Commonaccount other = (Commonaccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @XmlTransient
    public List<Commonaccounttransaction> getCommonaccounttransactionList() {
        return commonaccounttransactionList;
    }

    public void setCommonaccounttransactionList(List<Commonaccounttransaction> commonaccounttransactionList) {
        this.commonaccounttransactionList = commonaccounttransactionList;
    }

}
