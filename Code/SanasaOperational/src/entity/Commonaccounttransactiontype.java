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
@Table(name = "commonaccounttransactiontype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commonaccounttransactiontype.findAll", query = "SELECT c FROM Commonaccounttransactiontype c"),
    @NamedQuery(name = "Commonaccounttransactiontype.findById", query = "SELECT c FROM Commonaccounttransactiontype c WHERE c.id = :id"),
    @NamedQuery(name = "Commonaccounttransactiontype.findByName", query = "SELECT c FROM Commonaccounttransactiontype c WHERE c.name = :name")})
public class Commonaccounttransactiontype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commonaccounttransactiontypeId", fetch = FetchType.EAGER)
    private List<Commonaccounttransaction> commonaccounttransactionList;

    public Commonaccounttransactiontype() {
    }

    public Commonaccounttransactiontype(Integer id) {
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
    public List<Commonaccounttransaction> getCommonaccounttransactionList() {
        return commonaccounttransactionList;
    }

    public void setCommonaccounttransactionList(List<Commonaccounttransaction> commonaccounttransactionList) {
        this.commonaccounttransactionList = commonaccounttransactionList;
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
        if (!(object instanceof Commonaccounttransactiontype)) {
            return false;
        }
        Commonaccounttransactiontype other = (Commonaccounttransactiontype) object;
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
