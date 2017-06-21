/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Loan;
import entity.Loanstatus;
import entity.Loantype;
import entity.Smember;
import java.util.Date;
import java.util.HashMap;
import javafx.collections.ObservableList;

/**
 *
 * @author Sandun
 */
public class LoanDao {

    public static ObservableList getAll() {
        return CommonDao.select("Loan.findAll");
    }

    public static void add(Loan loan) throws DaoException {
        CommonDao.insert(loan);
    }

    public static Loan getById(Integer id) {
        HashMap hmap = new HashMap();
        hmap.put("id", id);

        return (Loan) CommonDao.select("Loan.findById", hmap).get(0);
    }

    public static void update(Loan loan) {
        CommonDao.update(loan);
    }

    public static void delete(Loan loan) {
        CommonDao.delete(loan);
    }

    public static ObservableList<Loan> getAllByMember(Smember member) {

        HashMap hmap = new HashMap();
        hmap.put("smember", member);

        return CommonDao.select("Loan.findAllByMember", hmap);

    }

    public static ObservableList<Loan> getAllByLoanstatus(Loanstatus loanStatus) {

        HashMap hmap = new HashMap();
        hmap.put("loanstatus", loanStatus);

        return CommonDao.select("Loan.findAllByLoanstatus", hmap);

    }

    public static ObservableList<Loan> getAllByLoantype(Loantype loanType) {
        HashMap hmap = new HashMap();
        hmap.put("loantype", loanType);

        return CommonDao.select("Loan.findAllByLoantype", hmap);
    }

    public static ObservableList<Loan> getAllByMemberLoantype(Smember member, Loantype loanType) {

        HashMap hmap = new HashMap();
        hmap.put("member", member);
        hmap.put("loantype", loanType);
        return CommonDao.select("Loan.findAllByMemberLoantype", hmap);

    }

    public static ObservableList<Loan> getAllByMemberLoanstatus(Smember member, Loanstatus loanStatus) {

        HashMap hmap = new HashMap();
        hmap.put("member", member);
        hmap.put("loanstatus", loanStatus);
        return CommonDao.select("Loan.findAllByMemberLoanstatus", hmap);

    }

    public static ObservableList<Loan> getAllByLoantypeLoanstatus(Loantype loanType, Loanstatus loanStatus) {

        HashMap hmap = new HashMap();
        hmap.put("loantype", loanType);
        hmap.put("loanstatus", loanStatus);
        return CommonDao.select("Loan.findAllByLoantypeLoanstatus", hmap);

    }

    public static ObservableList<Loan> getAllByMemberLoantypeLoanstatus(Smember member, Loantype loanType, Loanstatus loanStatus) {
        HashMap hmap = new HashMap();
        hmap.put("loantype", member);
        hmap.put("loanstatus", loanType);
        hmap.put("loanstatus", loanStatus);
        return CommonDao.select("Loan.findAllByMemberLoantypeLoanstatus", hmap);
    }

    public static ObservableList<Loan> getAllByStatus(Loanstatus status) {

        HashMap hmap = new HashMap();
        hmap.put("loanstatus", status);
        return CommonDao.select("Loan.findAllByLoanstatus", hmap);

    }

    public static ObservableList<Loan> getAllByNIC(String nic) {

        HashMap hmap = new HashMap();
        hmap.put("nic", nic + "%");
        return CommonDao.select("Loan.findAllByNIC", hmap);

    }

    public static ObservableList<Loan> getAllByMemberName(String name) {

        HashMap hmap = new HashMap();
        hmap.put("name", "%" + name + "%");
        return CommonDao.select("Loan.findAllByMemberName", hmap);

    }

    public static ObservableList<Loan> getAllByMemberID(String memberid) {

        HashMap hmap = new HashMap();
        hmap.put("memberid", Integer.valueOf(memberid));
        return CommonDao.select("Loan.findAllByMemberID", hmap);
        
    }

    public static ObservableList<Loan> getAllByLoanDateRange(Date fdate, Date tdate) {

        HashMap hmap = new HashMap();
        hmap.put("fdate", fdate);
        hmap.put("tdate", tdate);
        return CommonDao.select("Loan.findAllByLoanDateRange", hmap);
        
    }

}
