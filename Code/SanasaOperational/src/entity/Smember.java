/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
import javax.persistence.ManyToMany;
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
@Table(name = "smember")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Smember.findAll", query = "SELECT s FROM Smember s"),
    @NamedQuery(name = "Smember.findById", query = "SELECT s FROM Smember s WHERE s.id = :id"),
    @NamedQuery(name = "Smember.findByName", query = "SELECT s FROM Smember s WHERE s.name = :name"),

    @NamedQuery(name = "Smember.findAllByName", query = "SELECT s FROM Smember s WHERE s.name like :name"),
    @NamedQuery(name = "Smember.findAllByNic", query = "SELECT s FROM Smember s WHERE s.nic like :nic"),
    @NamedQuery(name = "Smember.findAllByStatus", query = "SELECT s FROM Smember s WHERE s.memberstatusId = :status"),
    @NamedQuery(name = "Smember.findAllByMemberid", query = "SELECT s FROM Smember s WHERE s.memberid like :memberid"),
    @NamedQuery(name = "Smember.findLastMemberID", query = "SELECT s.memberid FROM Smember s ORDER BY s.memberid DESC"),
    @NamedQuery(name = "Smember.findAllByRelationCount", query = "SELECT s FROM Smember s WHERE SIZE(s.relationsList) < 2"),

    @NamedQuery(name = "Smember.findAllNotPropertyMembers", query = "SELECT s FROM Smember s WHERE s.propertygainList IS EMPTY")

})
public class Smember implements Serializable {

    @Basic(optional = false)
    @Column(name = "memberid")
    private String memberid;
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Attendance> attendanceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.LAZY)
    private List<Relations> relationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.EAGER)
    private List<Propertygain> propertygainList;
    @ManyToMany(mappedBy = "smemberList", fetch = FetchType.LAZY)
    private List<Meeting> meetingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Sharegain> sharegainList;

    @Column(name = "fullname")
    private String fullname;
    @Lob
    @Column(name = "address")
    private String address;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "nic")
    private String nic;
    @Column(name = "tel")
    private String tel;
    @Column(name = "occupation")
    private String occupation;
    @Column(name = "education")
    private String education;
    @Column(name = "domembership")
    @Temporal(TemporalType.DATE)
    private Date domembership;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Gender genderId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "memberstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Memberstatus memberstatusId;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.LAZY)
    private List<Loan> loanList;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "smemberId", fetch = FetchType.EAGER)
    private List<Account> accountList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Smember() {
    }

    public Smember(Integer id) {
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

//        name.matches("^[a-zA-Z\\s\\.]+$"); 
        boolean validity = !name.isEmpty();
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
        if (!(object instanceof Smember)) {
            return false;
        }
        Smember other = (Smember) object;
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
    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @XmlTransient
    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public String getFullname() {
        return fullname;
    }

    public boolean setFullname(String fullname) {
        //validate sinhala letters
        //^[\\p{L} .'-]+$

        boolean validity = !fullname.isEmpty();
        if (validity) {
            this.fullname = fullname;
        } else {
            this.fullname = null;
        }
        return validity;
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        boolean validity = address != null && !address.isEmpty();// && address.matches("^[_A-Za-z0-9-/.:,\\s]{5}[_A-Za-z0-9-/.:,\\s]*");  
        if (validity) {
            this.address = address;
        } else {
            this.address = null;
        }
        return validity;
    }

    public Date getDob() {
        return dob;
    }

    public boolean setDob(Date dob) {
        this.dob = dob;
        return dob != null;
    }

    public String getNic() {
        return nic;
    }

    public boolean setNic(String nic) {
        //boolean validity = nic != null && nic.matches("^\\s*([\\d]{9}[v|V|x|X])|([\\d]{12})\\s*$");  

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

    public String getOccupation() {
        return occupation;
    }

    public boolean setOccupation(String occupation) {
        boolean validity = occupation != null;// && occupation.matches("^[_A-Za-z]{3}[_A-Za-z]*([_A-Za-z]*\\s[_A-Za-z]*)*"); 
        if (validity) {
            this.occupation = occupation;
        } else {
            this.occupation = null;
        }
        return validity;
    }

    public String getEducation() {
        return education;
    }

    public boolean setEducation(String education) {
        boolean validity = education != null;// && education.matches("^[_A-Za-z]{3}[_A-Za-z]*([_A-Za-z]*\\s[_A-Za-z]*)*"); 
        if (validity) {
            this.education = education;
        } else {
            this.education = null;
        }
        return validity;
    }

    public Date getDomembership() {
        return domembership;
    }

    public boolean setDomembership(Date domembership) {
        this.domembership = domembership;
        return domembership != domembership;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Memberstatus getMemberstatusId() {
        return memberstatusId;
    }

    public boolean setMemberstatusId(Memberstatus memberstatusId) {
        boolean validity = memberstatusId != null;
        if (validity) {
            this.memberstatusId = memberstatusId;
        } else {
            this.memberstatusId = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Sharegain> getSharegainList() {
        return sharegainList;
    }

    public void setSharegainList(List<Sharegain> sharegainList) {
        this.sharegainList = sharegainList;
    }

    @XmlTransient
    public List<Meeting> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    @XmlTransient
    public List<Propertygain> getPropertygainList() {
        return propertygainList;
    }

    public void setPropertygainList(List<Propertygain> propertygainList) {
        this.propertygainList = propertygainList;
    }

    @XmlTransient
    public List<Relations> getRelationsList() {
        return relationsList;
    }

    public void setRelationsList(List<Relations> relationsList) {
        this.relationsList = relationsList;
    }

    @XmlTransient
    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String getMemberid() {
        return memberid;
    }

    public boolean setMemberid(String memberid) {
        boolean validity = memberid != null;
        if (validity) {
            this.memberid = memberid;
        } else {
            this.memberid = null;
        }
        return validity;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}
