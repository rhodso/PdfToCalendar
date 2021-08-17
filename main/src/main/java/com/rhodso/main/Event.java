package com.rhodso.main;

public class Event {
    
    private String name;
    private String date;
    private String desc;
    
    public Event(){}
    public Event(String _name){
        name = _name;
        date = "";
        desc = "";
    }
    public Event(String _name, String _date){
        name = _name;
        date = _date;
        desc = "";
    }
    public Event(String _name, String _date, String _desc){
        name = _name;
        date = _date;
        desc = _desc;
    }

    public String getName(){ return name; }
    public String getDate(){ return date; }
    public String getDesc(){ return desc; }

    public void setName(String _name){ name = _name; }
    public void setDate(String _date){ date = _date; }
    public void setDesc(String _desc){ desc = _desc; }

    public String toString(){
        return name + " | " + date;
    }
}
