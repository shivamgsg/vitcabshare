package com.techifuzz.team.vitsharecar;

/**
 * Created by Mridul on 09-09-2017.
 */

public class User {

    public String name;
    public String date;
    public String time;
    public String to;
    public String from;
    public String thumb_image;
    public String email;
    public String number;
    public String image;

    public User(){

    }




    public User(String name, String date, String time, String to, String from, String image) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.to = to;
        this.from = from;
        this.thumb_image=thumb_image;
        this.email=email;
        this.number=number;
        this.image=image;


    }



    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
