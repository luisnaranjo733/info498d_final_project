package info498d.uw.edu.smartalarm;

/**
 * Created by kai on 2/27/16.
 */
public class Alarm {
    public String title;
    public String time;
    public double day;


    public Alarm(String author, String date, double message){
        this.title = author;
        this.time = date;
        this.day = message;
    }

    //default constructor; empty movie
    public Alarm(){}

    public String toString(){
        return this.title + ": " + this.time + " on " + this.day;
    }
}
