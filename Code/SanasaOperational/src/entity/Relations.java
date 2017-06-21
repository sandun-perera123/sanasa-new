/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandun-PC
 */
@Entity
@Table(name = "relations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Relations.findAll", query = "SELECT r FROM Relations r"),
    @NamedQuery(name = "Relations.findById", query = "SELECT r FROM Relations r WHERE r.id = :id"),
    @NamedQuery(name = "Relations.findByName", query = "SELECT r FROM Relations r WHERE r.name = :name"),
    @NamedQuery(name = "Relations.findByRelationship", query = "SELECT r FROM Relations r WHERE r.relationship = :relationship"),
    @NamedQuery(name = "Relations.findByNic", query = "SELECT r FROM Relations r WHERE r.nic = :nic"),
    @NamedQuery(name = "Relations.findByTel", query = "SELECT r FROM Relations r WHERE r.tel = :tel"),
    @NamedQuery(name = "Relations.findByDate", query = "SELECT r FROM Relations r WHERE r.date = :date"),

    @NamedQuery(name = "Relations.findAllByNic", query = "SELECT r FROM Relations r WHERE r.nic LIKE :nic"),
    @NamedQuery(name = "Relations.findAllByMember", query = "SELECT r FROM Relations r WHERE r.smemberId = :member")
})

public class Relations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "relationship")
    private String relationship;
    @Column(name = "nic")
    private String nic;
    @Column(name = "tel")
    private String tel;
    @Lob
    @Column(name = "address")
    private String address;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Gender genderId;
    @JoinColumn(name = "smember_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Smember smemberId;

    public Relations() {
    }

    public Relations(Integer id) {
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
        boolean validity = name != null && name.matches("[^\\d]+");
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
        }
        return validity;
    }

    public String getRelationship() {
        return relationship;
    }

    public boolean setRelationship(String relationship) {
        boolean validity = relationship != null && relationship.matches("[^\\d]+");
        if (validity) {
            this.relationship = relationship;
        } else {
            this.relationship = null;
        }
        return validity;
    }

    public String getNic() {
        return nic;
    }

    public boolean setNic(String nic) {

        //boolean validity = nic != null && nic.matches("\\d{9}[V|v|x|X]");
        
        boolean validity;
        if (nic == null || nic.isEmpty()) {
            validity = true;
            this.nic = null;
        } else {
            nic = nic.trim();
            if (nic.matches("^\\s*([\\d]{9}[v|V|x|X])|([\\d]{12})\\s*$")) {
                this.nic = nic;
                validity = true;
            } else {
                this.nic = null;
                validity = false;
            }
        }
        return validity;
        

    }

    public String getTel() {
        return tel;
    }

    public boolean setTel(String tel) {

        boolean validity;
        if (tel == null || tel.isEmpty()) {
            validity = true;
            this.tel = null;
        } else {
            tel = tel.trim();
            if (tel.matches("0\\d{9}")) {
                this.tel = tel;
                validity = true;
            } else {
                this.tel = null;
                validity = false;
            }
        }
        return validity;

    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        //boolean validity = address != null && !address.isEmpty();
        boolean validity;
        if (address == null || address.isEmpty()) {
            validity = true;
            this.address = null;
        } else {
            address = address.trim();
            if (address != null && !address.isEmpty()){
                this.address = address;
                validity = true;
            } else {
                this.address = null;
                validity = false;
            }
        }
        return validity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Gender getGenderId() {
        return genderId;
    }

    public boolean setGenderId(Gender genderId) {
        boolean validity = genderId != null;
        if (validity) {
            this.genderId = genderId;
        } else {
            this.genderId = null;
        }
        return validity;
    }

    public Smember getSmemberId() {
        return smemberId;
    }

    public boolean setSmemberId(Smember smemberId) {
        boolean validity = smemberId != null;
        if (validity) {
            this.smemberId = smemberId;
        } else {
            this.smemberId = null;
        }
        return validity;
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
        if (!(object instanceof Relations)) {
            return false;
        }
        Relations other = (Relations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Relations[ id=" + id + " ]";
    }

}
