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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author Sandun
 */
@Entity
@Table(name = "propertygain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Propertygain.findAll", query = "SELECT p FROM Propertygain p"),
    @NamedQuery(name = "Propertygain.findById", query = "SELECT p FROM Propertygain p WHERE p.id = :id"),
    @NamedQuery(name = "Propertygain.findByMovablepropertytotal", query = "SELECT p FROM Propertygain p WHERE p.movablepropertytotal = :movablepropertytotal"),
    @NamedQuery(name = "Propertygain.findByImmovablepropertytotal", query = "SELECT p FROM Propertygain p WHERE p.immovablepropertytotal = :immovablepropertytotal"),
    @NamedQuery(name = "Propertygain.findByTotal", query = "SELECT p FROM Propertygain p WHERE p.total = :total"),
    @NamedQuery(name = "Propertygain.findByDate", query = "SELECT p FROM Propertygain p WHERE p.date = :date"),

    @NamedQuery(name = "Propertygain.findAllByMember", query = "SELECT p FROM Propertygain p WHERE p.smemberId = :member")

})
public class Propertygain implements Serializable {

    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "movablepropertytotal")
    private BigDecimal movablepropertytotal;
    @Column(name = "immovablepropertytotal")
    private BigDecimal immovablepropertytotal;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "smember_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Smember smemberId;
    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL, mappedBy = "propertygainId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Property> propertyList;

    public Propertygain() {
    }

    public Propertygain(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMovablepropertytotal() {
        return movablepropertytotal;
    }

    public void setMovablepropertytotal(BigDecimal movablepropertytotal) {
        this.movablepropertytotal = movablepropertytotal;
    }

    public BigDecimal getImmovablepropertytotal() {
        return immovablepropertytotal;
    }

    public void setImmovablepropertytotal(BigDecimal immovablepropertytotal) {
        this.immovablepropertytotal = immovablepropertytotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Smember getSmemberId() {
        return smemberId;
    }

    public boolean setSmemberId(Smember smemberId) {
        boolean validity = smemberId!=null;  
        if(validity){this.smemberId = smemberId;}else {this.smemberId=null;} 
        return validity;
    }

    @XmlTransient
    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
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
        if (!(object instanceof Propertygain)) {
            return false;
        }
        Propertygain other = (Propertygain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Propertygain[ id=" + id + " ]";
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }
    
}
