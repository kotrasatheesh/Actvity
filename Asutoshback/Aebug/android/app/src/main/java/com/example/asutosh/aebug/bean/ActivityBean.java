package com.example.asutosh.aebug.bean;

/**
 * Created by Asutosh on 13-09-2017.
 */

public class ActivityBean {
    private String Activty_id;
    private String Activty_by;
    private String Description;

    public String getActivty_id() {
        return Activty_id;
    }

    public void setActivty_id(String activty_id) {
        Activty_id = activty_id;
    }

    public String getActivty_by() {
        return Activty_by;
    }

    public void setActivty_by(String activty_by) {
        Activty_by = activty_by;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProj_id() {
        return proj_id;
    }

    public void setProj_id(String proj_id) {
        this.proj_id = proj_id;
    }

    public String getProj_name() {
        return proj_name;
    }

    public void setProj_name(String proj_name) {
        this.proj_name = proj_name;
    }

    private String time;
    private String proj_id;
    private String proj_name;

}
