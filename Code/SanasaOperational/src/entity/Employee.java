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
 * @author Suranga
 */
@Entity
@Table(name = "employee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findById", query = "SELECT e FROM Employee e WHERE e.id = :id"),
    @NamedQuery(name = "Employee.findByName", query = "SELECT e FROM Employee e WHERE e.name = :name"),
    @NamedQuery(name = "Employee.findByDob", query = "SELECT e FROM Employee e WHERE e.dob = :dob"),
    @NamedQuery(name = "Employee.findByNic", query = "SELECT e FROM Employee e WHERE e.nic = :nic"),
    @NamedQuery(name = "Employee.findByMobile", query = "SELECT e FROM Employee e WHERE e.mobile = :mobile"),
    @NamedQuery(name = "Employee.findByLand", query = "SELECT e FROM Employee e WHERE e.land = :land"),
    @NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email"),
    @NamedQuery(name = "Employee.findByAssigned", query = "SELECT e FROM Employee e WHERE e.assigned = :assigned"),
    
      @NamedQuery(name = "Employee.findAllExeptUsers", query = "SELECT e FROM Employee e WHERE e NOT IN (SELECT u.employeeId FROM User u)"),
    @NamedQuery(name = "Employee.findAllByDesignation", query = "SELECT e FROM Employee e WHERE e.designationId = :designation"),
    @NamedQuery(name = "Employee.findAllByStatus", query = "SELECT e FROM Employee e WHERE e.employeestatusId = :status"),
    @NamedQuery(name = "Employee.findAllByName", query = "SELECT e FROM Employee e WHERE e.name like :name"),
    @NamedQuery(name = "Employee.findAllByNameStatus", query = "SELECT e FROM Employee e WHERE e.employeestatusId = :status and e.name like :name"),
    @NamedQuery(name = "Employee.findAllByNameDesignation", query = "SELECT e FROM Employee e WHERE e.designationId = :designation and e.name like :name"),
    @NamedQuery(name = "Employee.findAllByStatusDesignation", query = "SELECT e FROM Employee e WHERE e.employeestatusId = :status and e.designationId = :designation"),
    @NamedQuery(name = "Employee.findAllByNameStatusDesignation", query = "SELECT e FROM Employee e WHERE e.employeestatusId = :status and e.designationId = :designation and e.name like :name")
})
public class Employee implements Serializable {

    @Lob
    @Column(name = "image")
    private byte[] image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.EAGER)
    private List<Commonaccounttransaction> commonaccounttransactionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.EAGER)
    private List<Accountpayment> accountpaymentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Relations> relationsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Propertygain> propertygainList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Meeting> meetingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Sharegain> sharegainList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Smember> smemberList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<Loan> loanList;
    @OneToMany(mappedBy = "employeeApprovedId", fetch = FetchType.EAGER)
    private List<Loan> loanList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeeId", fetch = FetchType.LAZY)
    private List<User> userList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;
    @Column(name = "nic")
    private String nic;
    @Lob
    @Column(name = "address")
    private String address;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "land")
    private String land;
    @Column(name = "email")
    private String email;
    @Column(name = "assigned")
    @Temporal(TemporalType.DATE)
    private Date assigned;
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Gender genderId;
    @JoinColumn(name = "designation_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Designation designationId;
    @JoinColumn(name = "employeestatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employeestatus employeestatusId;
    @JoinColumn(name = "civilstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Civilstatus civilstatusId;

    public Employee() {
    }

    public Employee(Integer id) {
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
        boolean validity = name != null;
        if(validity){this.name = name; } else { this.name=null; } 
        return validity; 
    }

    public Date getDob() {
        return dob;
    }

    public boolean setDob(Date dob) {
        this.dob=dob; 
        return dob != null; 
    }

    public String getNic() {
        return nic;
    }

    public boolean setNic(String nic) {
        boolean validity = nic != null && nic.matches("\\d{9}[V|v|x|X]");  
        if(validity){this.nic = nic; } else { this.nic=null; } 
        return validity; 
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
       boolean validity = address != null && address.matches("^[_A-Za-z0-9-/.:,\\s]{5}[_A-Za-z0-9-/.:,\\s]*");  
        if(validity){this.address = address; } else { this.address=null; } 
        return validity; 
    }

    public String getMobile() {
        return mobile;
    }

    public boolean setMobile(String mobile) {
        boolean validity = mobile != null && mobile.matches("0\\d{9}");  
        if(validity){this.mobile = mobile; } else { this.mobile=null; } 
        return validity; 
    }

    public String getLand() {
        return land;
    }

    public boolean setLand(String land) {
       boolean validity; 
       if(land==null || land.isEmpty()){ validity=true; this.land=null;}
       else{
           land = land.trim(); 
           if(land.matches("0\\d{9}"))
           {this.land=land; validity=true;}
           else{ this.land=null; validity=false;}           
       }
      return validity; 
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
       boolean validity; 
       if(email==null || email.isEmpty()){ validity=true; this.email=null;}
       else{
           email = email.trim(); 
           if(email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"))
           {this.email=email; validity=true;}
           else{ this.email=null; validity=false;}           
       }
      return validity; 
    }

    public Date getAssigned() {
        return assigned;
    }

    public boolean  setAssigned(Date assigned) {
        this.assigned = assigned;
        return assigned != null; 
    }


    public Gender getGenderId() {
        return genderId;
    }

    public boolean setGenderId(Gender genderId) {
        boolean validity = genderId!=null;  
        if(validity){this.genderId = genderId;}else {this.genderId=null;} 
        return validity;
    }

    public Designation getDesignationId() {
        return designationId;
    }

    public boolean setDesignationId(Designation designationId) {
        boolean validity = designationId!=null;  
        if(validity){this.designationId = designationId;}else {this.designationId=null;} 
        return validity;
    } 

    public Employeestatus getEmployeestatusId() {
        return employeestatusId;
    }

    public boolean setEmployeestatusId(Employeestatus employeestatusId) {
        boolean validity = employeestatusId!=null;  
        if(validity){this.employeestatusId = employeestatusId;}else {this.employeestatusId=null;} 
        return validity;
    }

    public Civilstatus getCivilstatusId() {
        return civilstatusId;
    }

    public boolean setCivilstatusId(Civilstatus civilstatusId) {
        boolean validity = civilstatusId!=null;  
        if(validity){this.civilstatusId = civilstatusId;}else {this.civilstatusId=null;} 
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
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
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    @XmlTransient
    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    @XmlTransient
    public List<Loan> getLoanList1() {
        return loanList1;
    }

    public void setLoanList1(List<Loan> loanList1) {
        this.loanList1 = loanList1;
    }


    @XmlTransient
    public List<Smember> getSmemberList() {
        return smemberList;
    }

    public void setSmemberList(List<Smember> smemberList) {
        this.smemberList = smemberList;
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
    public List<Accountpayment> getAccountpaymentList() {
        return accountpaymentList;
    }

    public void setAccountpaymentList(List<Accountpayment> accountpaymentList) {
        this.accountpaymentList = accountpaymentList;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @XmlTransient
    public List<Commonaccounttransaction> getCommonaccounttransactionList() {
        return commonaccounttransactionList;
    }

    public void setCommonaccounttransactionList(List<Commonaccounttransaction> commonaccounttransactionList) {
        this.commonaccounttransactionList = commonaccounttransactionList;
    }
    
}
