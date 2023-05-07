package edu.xula.www;

import java.util.ArrayList;

public class User {

    private transient int userIdentification;
    private String major;
    private ArrayList<String> transcript;

    public User(int userIdentification){
        this.userIdentification = userIdentification;
        this.major = null;

    }
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setUserIdentification(int userIdentification) {
        this.userIdentification = userIdentification;
    }

    public int getUserIdentification() {
        return userIdentification;
    }

    public ArrayList<String> getTranscript() {
        return transcript;
    }

    public void setTranscript(ArrayList<String> transcript) {
        this.transcript = transcript;
    }


}
