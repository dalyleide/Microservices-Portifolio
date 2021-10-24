package br.com.sousa.schedule;

import br.com.sousa.domain.data.model.Schedule;
import br.com.sousa.domain.service.ScheduleService;
import br.com.sousa.util.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.context.i18n.LocaleContextHolder.getTimeZone;

@Component
public class CloseSchedules {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseSchedules.class);

    private final ScheduleService scheduleService;

    public CloseSchedules(ScheduleService service){
        this.scheduleService = service;
    }

    @Scheduled(cron = "0 0/15 * * * *")
    public void closeOpenSchedules(){

        LOGGER.info(String.format("Running closeOpenSchedules %s", LocalDateTime.now(getTimeZone().toZoneId())));
        var list = scheduleService.findByStatus(StatusEnum.OPEN);
        for (Schedule schedule : list) {
            scheduleService.verifyExpired(schedule);
        }
    }
}
