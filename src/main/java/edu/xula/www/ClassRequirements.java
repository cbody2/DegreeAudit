package edu.xula.www;

import java.util.Set;

public class ClassRequirements {

    private final Set<String> major;

    private final Set<String[]> core;

    public ClassRequirements(Set<String> major, Set<String[]> core){
        this.major = major;
        this.core = core;
    }

    public ClassRequirements(){
        this.major = null;
        this.core = null;
    }

    public Set<String> getMajor() {
        return major;
    }

    public Set<String[]> getCore() {
        return core;
    }
}
