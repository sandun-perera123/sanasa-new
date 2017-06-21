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
import javax.persistence.JoinTable;
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
@Table(name = "meeting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meeting.findAll", query = "SELECT m FROM Meeting m"),
    @NamedQuery(name = "Meeting.findById", query = "SELECT m FROM Meeting m WHERE m.id = :id"),
    @NamedQuery(name = "Meeting.findByDomeeting", query = "SELECT m FROM Meeting m WHERE m.domeeting = :domeeting"),
    @NamedQuery(name = "Meeting.findByStarttime", query = "SELECT m FROM Meeting m WHERE m.starttime = :starttime"),
    @NamedQuery(name = "Meeting.findByEndtime", query = "SELECT m FROM Meeting m WHERE m.endtime = :endtime"),
    @NamedQuery(name = "Meeting.findByExpectedcount", query = "SELECT m FROM Meeting m WHERE m.expectedcount = :expectedcount"),
    @NamedQuery(name = "Meeting.findByCount", query = "SELECT m FROM Meeting m WHERE m.count = :count"),
    @NamedQuery(name = "Meeting.findByDate", query = "SELECT m FROM Meeting m WHERE m.date = :date"),

    @NamedQuery(name = "Meeting.findAllByMeetingtype", query = "SELECT m FROM Meeting m WHERE m.meetingtypeId = :meetingtype"),
    @NamedQuery(name = "Meeting.findAllByMeetingstatus", query = "SELECT m FROM Meeting m WHERE m.meetingstatusId = :meetingstatus"),
    @NamedQuery(name = "Meeting.findAllByMeetingdate", query = "SELECT m FROM Meeting m WHERE m.domeeting BETWEEN :from and :to"),
    @NamedQuery(name = "Meeting.findAllByTypeDate", query = "SELECT m FROM Meeting m WHERE m.meetingtypeId = :meetingtype AND m.domeeting BETWEEN :from and :to"),
    @NamedQuery(name = "Meeting.findAllUnmarkedMeetings", query = "SELECT m FROM Meeting m WHERE m.attendanceList IS EMPTY")

})
public class Meeting implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meetingId", fetch = FetchType.EAGER)
    private List<Attendance> attendanceList;

    @JoinTable(name = "commiteemember", joinColumns = {
        @JoinColumn(name = "meeting_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "smember_id", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Smember> smemberList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "domeeting")
    @Temporal(TemporalType.DATE)
    private Date domeeting;
    @Column(name = "starttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "endtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endtime;
    @Lob
    @Column(name = "purpose")
    private String purpose;
    @Column(name = "expectedcount")
    private Integer expectedcount;
    @Column(name = "count")
    private Integer count;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "meetingstatus_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Meetingstatus meetingstatusId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Employee employeeId;
    @JoinColumn(name = "meetingtype_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Meetingtype meetingtypeId;

    public Meeting() {
    }

    public Meeting(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDomeeting() {
        return domeeting;
    }

    public boolean setDomeeting(Date domeeting) {
        this.domeeting = domeeting;
        return domeeting != null;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getPurpose() {
        return purpose;
    }

    public boolean setPurpose(String purpose) {

        boolean validity;
        if (purpose == null || purpose.isEmpty()) {
            validity = true;
            this.purpose = null;
        } else {
            purpose = purpose.trim();
            if (purpose != null && !purpose.isEmpty()) {
                this.purpose = purpose;
                validity = true;
            } else {
                this.purpose = null;
                validity = false;
            }
        }
        return validity;

    }

    public Integer getExpectedcount() {
        return expectedcount;
    }

    public boolean setExpectedcount(Integer expectedcount) {
        boolean validity = expectedcount != null;
        if (validity) {
            this.expectedcount = expectedcount;
        } else {
            this.expectedcount = null;
        }
        return validity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Meetingstatus getMeetingstatusId() {
        return meetingstatusId;
    }

    public void setMeetingstatusId(Meetingstatus meetingstatusId) {
        this.meetingstatusId = meetingstatusId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Meetingtype getMeetingtypeId() {
        return meetingtypeId;
    }

    public boolean setMeetingtypeId(Meetingtype meetingtypeId) {
        boolean validity = meetingtypeId != null;
        if (validity) {
            this.meetingtypeId = meetingtypeId;
        } else {
            this.meetingtypeId = null;
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
        if (!(object instanceof Meeting)) {
            return false;
        }
        Meeting other = (Meeting) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return domeeting + " දින කාරක සභාව";
    }

    @XmlTransient
    public List<Smember> getSmemberList() {
        return smemberList;
    }

    public boolean setSmemberList(List<Smember> smemberList) {
        boolean validity = smemberList != null && !smemberList.isEmpty();
        if (validity) {
            this.smemberList = smemberList;
        } else {
            this.smemberList = null;
        }
        return validity;
    }

    @XmlTransient
    public List<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

}
