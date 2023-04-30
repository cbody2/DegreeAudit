package edu.xula.www;

public class User {

    private transient int userIdentification;


    private String major;

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
}
