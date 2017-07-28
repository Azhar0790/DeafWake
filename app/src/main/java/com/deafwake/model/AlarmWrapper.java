package com.deafwake.model;

import java.io.Serializable;

/**
 * Created by wel on 14-04-2017.
 */

public class AlarmWrapper implements Serializable {
    static final long serialVersionUID = 42L;
    String time="",description="",sunday="0",monday="0",tuesday="0",wednesday="0",thursday="0",friday="0",saturday="0";

    public String getIsSelected() {
        return isSelected;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    String isSelected;
    public AlarmWrapper(){

    }

    public AlarmWrapper(String time,String description,String isChecked){
        this.time=time;
        this.description=description;
        this.isSelected=isChecked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
