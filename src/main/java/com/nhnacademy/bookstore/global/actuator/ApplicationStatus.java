package com.nhnacademy.bookstore.global.actuator;

import org.springframework.stereotype.Component;

@Component
public class ApplicationStatus {

    private boolean status = true;

    public void stopStatus() {
        this.status = false;
    }

    public boolean isStatus() {
        return status;
    }
}
