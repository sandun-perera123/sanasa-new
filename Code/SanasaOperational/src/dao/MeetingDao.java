/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Meeting;
import entity.Meetingstatus;
import entity.Meetingtype;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class MeetingDao {
    
    public static ObservableList getAll(){
        return CommonDao.select("Meeting.findAll");
    }

    public static void add(Meeting meeting) throws DaoException {
        CommonDao.insert(meeting);
    }

    public static ObservableList<Meeting> getAllByMeetingtype(Meetingtype meetingtype) {
        HashMap hmap = new HashMap();
        hmap.put("meetingtype", meetingtype);

        return CommonDao.select("Meeting.findAllByMeetingtype", hmap);
    }

    public static ObservableList<Meeting> getAllByMeetingstatus(Meetingstatus meetingstatus) {
        HashMap hmap = new HashMap();
        hmap.put("meetingstatus", meetingstatus);

        return CommonDao.select("Meeting.findAllByMeetingstatus", hmap);
    }

    public static ObservableList<Meeting> getAllByMeetingdate(Date from, Date to) {
        HashMap hmap = new HashMap();
        hmap.put("from", from);
        hmap.put("to", to);

        return CommonDao.select("Meeting.findAllByMeetingdate", hmap);
    }

    public static Meeting getById(Integer id) {
       HashMap hmap = new HashMap();
       hmap.put("id", id);

       return (Meeting) CommonDao.select("Meeting.findById", hmap).get(0);
    }

    public static void update(Meeting meeting) {
        CommonDao.update(meeting);
    }
    
    public static void delete(Meeting meeting) {
        CommonDao.delete(meeting);
    }

    public static ObservableList<Meeting> getAllByTypeDate(Meetingtype meetingtype, Date from, Date to) {

        HashMap hm = new HashMap();
        hm.put("meetingtype", meetingtype);
        hm.put("from", from);
        hm.put("to", to);
        
        return CommonDao.select("Meeting.findAllByTypeDate", hm);
        
    }
    
    public static ObservableList<Meeting> getAllUnmarkedMeetings() {
        
        return CommonDao.select("Meeting.findAllUnmarkedMeetings");
        
    }

    
    
}
