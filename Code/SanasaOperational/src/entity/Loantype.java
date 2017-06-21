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
 * @author Sandun
 */
@Entity
@Table(name = "loantype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loantype.findAll", query = "SELECT l FROM Loantype l"),
    @NamedQuery(name = "Loantype.findById", query = "SELECT l FROM Loantype l WHERE l.id = :id"),
    @NamedQuery(name = "Loantype.findByName", query = "SELECT l FROM Loantype l WHERE l.name = :name")})
public class Loantype implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "interest")
    private BigDecimal interest;
    @Column(name = "minduration")
    private Integer minduration;
    @Column(name = "maxduration")
    private Integer maxduration;
    @Column(name = "minamount")
    private BigDecimal minamount;
    @Column(name = "maxamount")
    private BigDecimal maxamount;
    @JoinColumn(name = "loantypestatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Loantypestatus loantypestatusId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loantypeId", fetch = FetchType.LAZY)
    private List<Loan> loanList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Loantype() {
    }

    public Loantype(Integer id) {
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
        boolean validity = name != null && !name.isEmpty() && !name.equals("") && name != "";
        if (validity) {
            this.name = name;
        } else {
            this.name = null;
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
        if (!(object instanceof Loantype)) {
            return false;
        }
        Loantype other = (Loantype) object;
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
    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public boolean setInterest(BigDecimal interest) {
        boolean validity = (interest != null) && (interest.doubleValue() <= 100.00);
        if(validity){this.interest = interest; } else { this.interest=null; } 
        return validity;
    }

    public Integer getMinduration() {
        return minduration;
    }

    public void setMinduration(Integer minduration) {
        this.minduration = minduration;
    }

    public Integer getMaxduration() {
        return maxduration;
    }

    public boolean setMaxduration(Integer maxduration) {
        boolean validity = (maxduration != null);
        if(validity){this.maxduration = maxduration; } else { this.maxduration=null; } 
        return validity;
    }

    public BigDecimal getMinamount() {
        return minamount;
    }

    public void setMinamount(BigDecimal minamount) {
        this.minamount = minamount;
    }

    public BigDecimal getMaxamount() {
        return maxamount;
    }

    public void setMaxamount(BigDecimal maxamount) {
        this.maxamount = maxamount;
    }

    public Loantypestatus getLoantypestatusId() {
        return loantypestatusId;
    }

    public void setLoantypestatusId(Loantypestatus loantypestatusId) {
        this.loantypestatusId = loantypestatusId;
    }
    
}
