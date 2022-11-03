package com.innovaturelabs.training.employee.management.tasks;

import java.time.Duration;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innovaturelabs.training.employee.management.repository.UserRepository;

@Service
public class ScheduledTasks {
    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(initialDelayString = "PT2S", fixedRateString = "PT5H")
    @Transactional
    public void print() {

        LOGGER.info("Table Cleared");

        userRepository.deleteByEmailContainingAndCreateDateLessThan("#",
                Date.from(new Date().toInstant().minus(Duration.ofDays(1))));

    }
}
