package com.app.gre;

public class SendNotifications {
    String message,comid,category,prblm,status,descp;

    public SendNotifications() {
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComid() {
        return comid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrblm() {
        return prblm;
    }

    public void setPrblm(String prblm) {
        this.prblm = prblm;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
