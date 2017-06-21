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
 * @author Sandun
 */
@Entity
@Table(name = "property")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Property.findAll", query = "SELECT p FROM Property p"),
    @NamedQuery(name = "Property.findById", query = "SELECT p FROM Property p WHERE p.id = :id"),
    @NamedQuery(name = "Property.findByName", query = "SELECT p FROM Property p WHERE p.name = :name"),
    @NamedQuery(name = "Property.findByValue", query = "SELECT p FROM Property p WHERE p.value = :value")})
public class Property implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "value")
    private BigDecimal value;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @JoinColumn(name = "propertygain_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Propertygain propertygainId;
    @JoinColumn(name = "propertytype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Propertytype propertytypeId;

    public Property() {
    }

    public Property(Integer id) {
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
        boolean validity = name != null && !name.isEmpty();
        if(validity){this.name = name; } else { this.name=null; } 
        return validity; 
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean setValue(BigDecimal value) {
        boolean validity = value != null;
        if(validity){this.value = value; } else { this.value=null; } 
        return validity; 
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Propertygain getPropertygainId() {
        return propertygainId;
    }

    public void setPropertygainId(Propertygain propertygainId) {
        this.propertygainId = propertygainId;
    }

    public Propertytype getPropertytypeId() {
        return propertytypeId;
    }

    public void setPropertytypeId(Propertytype propertytypeId) {
        this.propertytypeId = propertytypeId;
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
        if (!(object instanceof Property)) {
            return false;
        }
        Property other = (Property) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public boolean isEqual(Property property){
        
        boolean result = false;
       
        result =     name.equals(property.getName()) &&
                      value.equals(property.getValue()) &&
                       propertytypeId.equals(property.getPropertytypeId())&&
                           remarks.equals(property.getRemarks());

       
        
        return result;
        
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return name;
    }

}
