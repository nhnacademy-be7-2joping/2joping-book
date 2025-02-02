package com.nhnacademy.bookstore.global.actuator;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApplicationStatus {

    private boolean status = true;

    public void stopStatus() {
        this.status = false;
    }

    @PreDestroy
    public void onShutDown() {
        this.status = false;
    }
}
