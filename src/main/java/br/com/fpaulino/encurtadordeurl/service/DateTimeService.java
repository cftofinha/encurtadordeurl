package br.com.fpaulino.encurtadordeurl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DateTimeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeService.class);

    public void processDate(LocalDate date) {
        LOGGER.info("Processing date: {}", date);
    }

    public void processDateAndTime(LocalDateTime dateAndTime) {
        LOGGER.info("Processing datetime: {}", dateAndTime);
    }
}