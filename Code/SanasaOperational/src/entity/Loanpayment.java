/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sandun-PC
 */
@Entity
@Table(name = "loanpayment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Loanpayment.findAll", query = "SELECT l FROM Loanpayment l"),
    @NamedQuery(name = "Loanpayment.findById", query = "SELECT l FROM Loanpayment l WHERE l.id = :id"),
    @NamedQuery(name = "Loanpayment.findByInsno", query = "SELECT l FROM Loanpayment l WHERE l.insno = :insno"),
    @NamedQuery(name = "Loanpayment.findByDopayment", query = "SELECT l FROM Loanpayment l WHERE l.dopayment = :dopayment"),
    @NamedQuery(name = "Loanpayment.findByInstallment", query = "SELECT l FROM Loanpayment l WHERE l.installment = :installment"),
    @NamedQuery(name = "Loanpayment.findByInterest", query = "SELECT l FROM Loanpayment l WHERE l.interest = :interest"),
    @NamedQuery(name = "Loanpayment.findByTotalAmount", query = "SELECT l FROM Loanpayment l WHERE l.totalAmount = :totalAmount"),
    @NamedQuery(name = "Loanpayment.findByDate", query = "SELECT l FROM Loanpayment l WHERE l.date = :date"),
    @NamedQuery(name = "Loanpayment.findByPaidAmount", query = "SELECT l FROM Loanpayment l WHERE l.paidAmount = :paidAmount")})
public class Loanpayment implements Serializable {

    @Column(name = "voucher_no")
    private String voucherNo;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "insno")
    private Integer insno;
    @Column(name = "dopayment")
    @Temporal(TemporalType.DATE)
    private Date dopayment;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "installment")
    private BigDecimal installment;
    @Column(name = "interest")
    private BigDecimal interest;
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "paidAmount")
    private BigDecimal paidAmount;
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Loan loanId;

    public Loanpayment() {
    }

    public Loanpayment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInsno() {
        return insno;
    }

    public void setInsno(Integer insno) {
        this.insno = insno;
    }

    public Date getDopayment() {
        return dopayment;
    }

    public void setDopayment(Date dopayment) {
        this.dopayment = dopayment;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Loan getLoanId() {
        return loanId;
    }

    public void setLoanId(Loan loanId) {
        this.loanId = loanId;
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
        if (!(object instanceof Loanpayment)) {
            return false;
        }
        Loanpayment other = (Loanpayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Loanpayment[ id=" + id + " ]";
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    
}
