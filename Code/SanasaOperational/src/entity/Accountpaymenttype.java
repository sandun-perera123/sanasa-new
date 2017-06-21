/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "accountpaymenttype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accountpaymenttype.findAll", query = "SELECT a FROM Accountpaymenttype a"),
    @NamedQuery(name = "Accountpaymenttype.findById", query = "SELECT a FROM Accountpaymenttype a WHERE a.id = :id"),
    @NamedQuery(name = "Accountpaymenttype.findByName", query = "SELECT a FROM Accountpaymenttype a WHERE a.name = :name"),

    @NamedQuery(name = "Accountpaymenttype.findAllExceptInterest", query = "SELECT a FROM Accountpaymenttype a WHERE a.id <> 3")

})






public class Accountpaymenttype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accountpaymenttypeId", fetch = FetchType.EAGER)
    private List<Accountpayment> accountpaymentList;

    public Accountpaymenttype() {
    }

    public Accountpaymenttype(Integer id) {
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

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Accountpayment> getAccountpaymentList() {
        return accountpaymentList;
    }

    public void setAccountpaymentList(List<Accountpayment> accountpaymentList) {
        this.accountpaymentList = accountpaymentList;
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
        if (!(object instanceof Accountpaymenttype)) {
            return false;
        }
        Accountpaymenttype other = (Accountpaymenttype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
