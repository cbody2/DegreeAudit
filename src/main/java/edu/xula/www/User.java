package edu.xula.www;

public class User {

    private transient int userIdentification;

    public User(int userIdentification){
        this.userIdentification = userIdentification;

    }

    public void setUserIdentification(int userIdentification) {
        this.userIdentification = userIdentification;
    }

    public int getUserIdentification() {
        return userIdentification;
    }
}
