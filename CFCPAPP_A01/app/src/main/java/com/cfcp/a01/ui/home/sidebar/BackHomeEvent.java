package com.cfcp.a01.ui.home.sidebar;

public class BackHomeEvent {
    String message;

    public BackHomeEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
