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
@Table(name = "loantypestatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loantypestatus.findAll", query = "SELECT l FROM Loantypestatus l"),
    @NamedQuery(name = "Loantypestatus.findById", query = "SELECT l FROM Loantypestatus l WHERE l.id = :id"),
    @NamedQuery(name = "Loantypestatus.findByName", query = "SELECT l FROM Loantypestatus l WHERE l.name = :name")})
public class Loantypestatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loantypestatusId", fetch = FetchType.LAZY)
    private List<Loantype> loantypeList;

    public Loantypestatus() {
    }

    public Loantypestatus(Integer id) {
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
    public List<Loantype> getLoantypeList() {
        return loantypeList;
    }

    public void setLoantypeList(List<Loantype> loantypeList) {
        this.loantypeList = loantypeList;
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
        if (!(object instanceof Loantypestatus)) {
            return false;
        }
        Loantypestatus other = (Loantypestatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Loantypestatus[ id=" + id + " ]";
    }
    
}
