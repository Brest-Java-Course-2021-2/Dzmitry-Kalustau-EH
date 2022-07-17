package com.epam.brest.service.impl;

import com.epam.brest.service.dto.CalculateSumDtoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@PropertySource("file:service/src/main/resources/scheduler.properties")
public class ScheduleService {

    private final Logger logger = LogManager.getLogger(ScheduleService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private CalculateSumDtoService calculateSumDtoService;

    private int monthCount;

    @Autowired
    public ScheduleService(CalculateSumDtoService calculateSumDtoService) {
        this.calculateSumDtoService = calculateSumDtoService;
        this.monthCount = 3;
    }

    @Scheduled(cron = "${CRON_TIME}")
    public void createScheduledReport() {
        logger.info("Create scheduled report {}", dateFormat.format(new Date()));
        calculateSumDtoService.createReport(monthCount);
    }
}

