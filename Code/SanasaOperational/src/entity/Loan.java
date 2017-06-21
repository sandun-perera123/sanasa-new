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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Sandun
 */
@Entity
@Table(name = "loan")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loan.findAll", query = "SELECT l FROM Loan l ORDER BY l.dorequest DESC, l.id DESC"),
    @NamedQuery(name = "Loan.findById", query = "SELECT l FROM Loan l WHERE l.id = :id"),
    @NamedQuery(name = "Loan.findByAmount", query = "SELECT l FROM Loan l WHERE l.amount = :amount"),
    @NamedQuery(name = "Loan.findByDorequest", query = "SELECT l FROM Loan l WHERE l.dorequest = :dorequest"),
    @NamedQuery(name = "Loan.findByDoloan", query = "SELECT l FROM Loan l WHERE l.doloan = :doloan"),
    @NamedQuery(name = "Loan.findByDopay", query = "SELECT l FROM Loan l WHERE l.dopay = :dopay"),
    @NamedQuery(name = "Loan.findByDuration", query = "SELECT l FROM Loan l WHERE l.duration = :duration"),
    @NamedQuery(name = "Loan.findByApprovedamount", query = "SELECT l FROM Loan l WHERE l.approvedamount = :approvedamount"),
    @NamedQuery(name = "Loan.findByDoapproved", query = "SELECT l FROM Loan l WHERE l.doapproved = :doapproved"),

    @NamedQuery(name = "Loan.findAllByMember", query = "SELECT l FROM Loan l WHERE l.smemberId = :smember"),
    @NamedQuery(name = "Loan.findAllByMemberLoantype", query = "SELECT l FROM Loan l WHERE l.smemberId = :member and l.loantypeId = :loantype"),
    @NamedQuery(name = "Loan.findAllByMemberLoanstatus", query = "SELECT l FROM Loan l WHERE l.smemberId = :member and l.loanstatusId = :loanstatus"),
    @NamedQuery(name = "Loan.findAllByLoantypeLoanstatus", query = "SELECT l FROM Loan l WHERE l.loantypeId = :loantype and l.loanstatusId = :loanstatus"),
    @NamedQuery(name = "Loan.findAllByMemberLoantypeLoanstatus", query = "SELECT l FROM Loan l WHERE l.smemberId = :member and l.loantypeId = :loantype and l.loanstatusId = :loanstatus"),
    
    @NamedQuery(name = "Loan.findAllByLoantype", query = "SELECT l FROM Loan l WHERE l.loantypeId = :loantype"),
    @NamedQuery(name = "Loan.findAllByLoanstatus", query = "SELECT l FROM Loan l WHERE l.loanstatusId = :loanstatus"),
    @NamedQuery(name = "Loan.findAllByLoanDateRange", query = "SELECT l FROM Loan l WHERE l.doloan BETWEEN :fdate AND :tdate"),
    @NamedQuery(name = "Loan.findAllByNIC", query = "SELECT l FROM Loan l WHERE l.smemberId.nic LIKE :nic"),
    @NamedQuery(name = "Loan.findAllByMemberName", query = "SELECT l FROM Loan l WHERE l.smemberId.name LIKE :name"),
    @NamedQuery(name = "Loan.findAllByMemberID", query = "SELECT l FROM Loan l WHERE l.smemberId.memberid = :memberid")
    
})
public class Loan implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "loanId", fetch = FetchType.EAGER)
    private List<Loanpayment> loanpaymentList;

    @Column(name = "interest")
    private BigDecimal interest;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "dopay")
    private Integer dopay;
   
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "dorequest")
    @Temporal(TemporalType.DATE)
    private Date dorequest;
    @Column(name = "doloan")
    @Temporal(TemporalType.DATE)
    private Date doloan;
    @Column(name = "duration")
    private Integer duration;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "approvedamount")
    private BigDecimal approvedamount;
    @Column(name = "doapproved")
    @Temporal(TemporalType.DATE)
    private Date doapproved;
    @JoinColumn(name = "smember_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Smember smemberId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "employee_approved_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employeeApprovedId;
    @JoinColumn(name = "loanstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Loanstatus loanstatusId;
    @JoinColumn(name = "loantype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Loantype loantypeId;

    public Loan() {
    }

    public Loan(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean setAmount(BigDecimal amount) {
        boolean validity = amount != null;
        if(validity){this.amount = amount; } else { this.amount=null; } 
        return validity; 
    }

    public Date getDorequest() {
        return dorequest;
    }

    public void setDorequest(Date dorequest) {
        this.dorequest = dorequest;
    }

    public Date getDoloan() {
        return doloan;
    }

    public boolean setDoloan(Date doloan) {
        this.doloan = doloan;
        return doloan != null; 
    }


    public Integer getDuration() {
        return duration;
    }

    public boolean setDuration(Integer duration) {
        boolean validity = duration != null;
        if(validity){this.duration = duration; } else { this.duration=null; } 
        return validity; 
    }

    public String getRemarks() {
        return remarks;
    }

    public boolean setRemarks(String remarks) {
        boolean validity = remarks != null;
        if(validity){this.remarks = remarks; } else { this.remarks=null; } 
        return validity; 
    }

    public BigDecimal getApprovedamount() {
        return approvedamount;
    }

    public void setApprovedamount(BigDecimal approvedamount) {
        this.approvedamount = approvedamount;
    }

    public Date getDoapproved() {
        return doapproved;
    }

    public void setDoapproved(Date doapproved) {
        this.doapproved = doapproved;
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

    public Employee getEmployeeApprovedId() {
        return employeeApprovedId;
    }

    public void setEmployeeApprovedId(Employee employeeApprovedId) {
        this.employeeApprovedId = employeeApprovedId;
    }

    public Loanstatus getLoanstatusId() {
        return loanstatusId;
    }

    public boolean setLoanstatusId(Loanstatus loanstatusId) {
       boolean validity = loanstatusId != null;  
       if(validity){this.loanstatusId = loanstatusId;}else {this.loanstatusId=null;} 
       return validity;
    }

    public Loantype getLoantypeId() {
        return loantypeId;
    }

    public boolean setLoantypeId(Loantype loantypeId) {
        boolean validity = loantypeId!=null;  
        if(validity){this.loantypeId = loantypeId;}else {this.loantypeId=null;} 
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
        if (!(object instanceof Loan)) {
            return false;
        }
        Loan other = (Loan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return loantypeId.getName()+ " --> "+this.getApprovedamount();
    }

    public Integer getDopay() {
        return dopay;
    }

    public boolean setDopay(Integer dopay) {
       boolean validity = dopay != null;
        if(validity){this.dopay = dopay; } else { this.dopay=null; } 
        return validity; 
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    @XmlTransient
    public List<Loanpayment> getLoanpaymentList() {
        return loanpaymentList;
    }

    public void setLoanpaymentList(List<Loanpayment> loanpaymentList) {
        this.loanpaymentList = loanpaymentList;
    }

 

    
}
