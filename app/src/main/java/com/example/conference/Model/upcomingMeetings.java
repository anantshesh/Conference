package com.example.conference.Model;

public class upcomingMeetings {
    String Endtime, Host, Id, MeetingPassword, Startdatetime, uid;

    public upcomingMeetings() {
    }

    public upcomingMeetings(String endtime, String host, String id, String meetingPassword, String startdatetime, String uid) {
        Endtime = endtime;
        Host = host;
        Id = id;
        MeetingPassword = meetingPassword;
        Startdatetime = startdatetime;
        this.uid = uid;
    }

    public String getEndtime() {
        return Endtime;
    }

    public void setEndtime(String endtime) {
        Endtime = endtime;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMeetingPassword() {
        return MeetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
        MeetingPassword = meetingPassword;
    }

    public String getStartdatetime() {
        return Startdatetime;
    }

    public void setStartdatetime(String startdatetime) {
        Startdatetime = startdatetime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
