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
@Table(name = "accounttypestatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accounttypestatus.findAll", query = "SELECT a FROM Accounttypestatus a"),
    @NamedQuery(name = "Accounttypestatus.findById", query = "SELECT a FROM Accounttypestatus a WHERE a.id = :id"),
    @NamedQuery(name = "Accounttypestatus.findByName", query = "SELECT a FROM Accounttypestatus a WHERE a.name = :name")})
public class Accounttypestatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accounttypestatusId", fetch = FetchType.EAGER)
    private List<Accounttype> accounttypeList;

    public Accounttypestatus() {
    }

    public Accounttypestatus(Integer id) {
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
    public List<Accounttype> getAccounttypeList() {
        return accounttypeList;
    }

    public void setAccounttypeList(List<Accounttype> accounttypeList) {
        this.accounttypeList = accounttypeList;
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
        if (!(object instanceof Accounttypestatus)) {
            return false;
        }
        Accounttypestatus other = (Accounttypestatus) object;
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
