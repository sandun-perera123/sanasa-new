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
@Table(name = "commonaccountsubcategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commonaccountsubcategory.findAll", query = "SELECT c FROM Commonaccountsubcategory c"),
    @NamedQuery(name = "Commonaccountsubcategory.findById", query = "SELECT c FROM Commonaccountsubcategory c WHERE c.id = :id"),
    @NamedQuery(name = "Commonaccountsubcategory.findByName", query = "SELECT c FROM Commonaccountsubcategory c WHERE c.name = :name"),
    
    @NamedQuery(name = "Commonaccountsubcategory.findAllByCategory", query = "SELECT c FROM Commonaccountsubcategory c WHERE c.commonaccountcategoryId = :category")
    
})
public class Commonaccountsubcategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "commonaccountcategory_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Commonaccountcategory commonaccountcategoryId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commonaccountsubcategoryId", fetch = FetchType.LAZY)
    private List<Commonaccount> commonaccountList;

    public Commonaccountsubcategory() {
    }

    public Commonaccountsubcategory(Integer id) {
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

    public Commonaccountcategory getCommonaccountcategoryId() {
        return commonaccountcategoryId;
    }

    public void setCommonaccountcategoryId(Commonaccountcategory commonaccountcategoryId) {
        this.commonaccountcategoryId = commonaccountcategoryId;
    }

    @XmlTransient
    public List<Commonaccount> getCommonaccountList() {
        return commonaccountList;
    }

    public void setCommonaccountList(List<Commonaccount> commonaccountList) {
        this.commonaccountList = commonaccountList;
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
        if (!(object instanceof Commonaccountsubcategory)) {
            return false;
        }
        Commonaccountsubcategory other = (Commonaccountsubcategory) object;
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
