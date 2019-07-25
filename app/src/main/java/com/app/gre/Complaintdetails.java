package com.app.gre;

import android.support.design.widget.TextInputEditText;

public class Complaintdetails {
    int complaintid;
    String category,problem,mno,descp,datetym;

    public Complaintdetails() {
    }

    public String getDatetym() {
        return datetym;
    }

    public void setDatetym(String datetym) {
        this.datetym = datetym;
    }

    public int getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(int complaintid) {
        this.complaintid = complaintid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }
}
