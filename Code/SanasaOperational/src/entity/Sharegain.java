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
import javax.persistence.OrderBy;
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
@Table(name = "sharegain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sharegain.findAll", query = "SELECT s FROM Sharegain s"),
    @NamedQuery(name = "Sharegain.findById", query = "SELECT s FROM Sharegain s WHERE s.id = :id"),
    @NamedQuery(name = "Sharegain.findByDate", query = "SELECT s FROM Sharegain s WHERE s.date = :date"),
    @NamedQuery(name = "Sharegain.findByTotalcount", query = "SELECT s FROM Sharegain s WHERE s.totalcount = :totalcount"),
    @NamedQuery(name = "Sharegain.findByTotalvalue", query = "SELECT s FROM Sharegain s WHERE s.totalvalue = :totalvalue"),

    @NamedQuery(name = "Sharegain.findAllByDateRange", query = "SELECT s FROM Sharegain s WHERE s.date BETWEEN :fdate AND :tdate"),
    @NamedQuery(name = "Sharegain.findAllByMember", query = "SELECT s FROM Sharegain s WHERE s.smemberId = :member"),
    @NamedQuery(name = "Sharegain.findTotalCount", query = "SELECT SUM(s.totalcount) FROM Sharegain s"),
    @NamedQuery(name = "Sharegain.getMonthlyShareSummery", query = "SELECT NEW MAP(MONTH(s.date) AS Mth, SUM(s.totalcount) AS Count) FROM Sharegain s GROUP BY MONTH(s.date)")
})
public class Sharegain implements Serializable {

    @Column(name = "voucher_no")
    private String voucherNo;

    @OneToMany(orphanRemoval=true,cascade = CascadeType.ALL, mappedBy = "sharegainId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Share> shareList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "totalcount")
    private Integer totalcount;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "totalvalue")
    private BigDecimal totalvalue;
    @JoinColumn(name = "smember_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Smember smemberId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;

    public Sharegain() {
    }

    public Sharegain(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public BigDecimal getTotalvalue() {
        return totalvalue;
    }

    public void setTotalvalue(BigDecimal totalvalue) {
        this.totalvalue = totalvalue;
    }

    public Smember getSmemberId() {
        return smemberId;
    }

    public boolean setSmemberId(Smember smemberId) {
        boolean validity = smemberId!=null;  
        if(validity){this.smemberId = smemberId;}else {this.smemberId=null;} 
        return validity;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof Sharegain)) {
            return false;
        }
        Sharegain other = (Sharegain) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Sharegain[ id=" + id + " ]";
    }

    @XmlTransient
    public List<Share> getShareList() {
        return shareList;
    }

    public void setShareList(List<Share> shareList) {
        this.shareList = shareList;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    
}
