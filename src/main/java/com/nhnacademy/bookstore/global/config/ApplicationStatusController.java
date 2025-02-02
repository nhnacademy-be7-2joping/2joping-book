package com.nhnacademy.bookstore.global.config;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import com.nhnacademy.bookstore.global.actuator.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator")
@RequiredArgsConstructor
public class ApplicationStatusController {

    private final ApplicationInfoManager applicationInfoManager;
    private final ApplicationStatus applicationStatus;

    @PostMapping("/shutdown")
    @ResponseStatus(value = HttpStatus.OK)
    public void stopStatus() {
        applicationInfoManager.setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
        applicationStatus.stopStatus();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
