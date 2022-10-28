package com.innovaturelabs.training.employee.management.tasks;

import java.time.Duration;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innovaturelabs.training.employee.management.repository.UserRepository;

@Service
public class ScheduledTasks {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(initialDelayString = "PT2S",fixedRateString = "PT5H")
    @Transactional
    public void print() {
        System.out.println("Table Cleared : Time => " + new Date());

        userRepository.deleteByEmailContainingAndCreateDateLessThan("#",
                Date.from(new Date().toInstant().minus(Duration.ofDays(1))));

    }
}
