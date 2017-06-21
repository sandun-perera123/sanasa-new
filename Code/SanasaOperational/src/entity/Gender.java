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
 * @author SahaN
 */
@Entity
@Table(name = "gender")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gender.findAll", query = "SELECT g FROM Gender g"),
    @NamedQuery(name = "Gender.findById", 
            query = "SELECT g FROM Gender g WHERE g.id = :id"),
    @NamedQuery(name = "Gender.findByName", 
            query = "SELECT g FROM Gender g WHERE g.name = :name")})
public class Gender implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genderId", fetch = FetchType.LAZY)
    private List<Relations> relationsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genderId", fetch = FetchType.LAZY)
    private List<Smember> smemberList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genderId", fetch = FetchType.LAZY)
    private List<Employee> employeeList;

    public Gender() {}

    public Gender(Integer id) {
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
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
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
        if (!(object instanceof Gender)) {
            return false;
        }
        Gender other = (Gender) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       return name;
    }

    @XmlTransient
    public List<Smember> getSmemberList() {
        return smemberList;
    }

    public void setSmemberList(List<Smember> smemberList) {
        this.smemberList = smemberList;
    }

    @XmlTransient
    public List<Relations> getRelationsList() {
        return relationsList;
    }

    public void setRelationsList(List<Relations> relationsList) {
        this.relationsList = relationsList;
    }
    
}