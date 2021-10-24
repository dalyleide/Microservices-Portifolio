package br.com.sousa.domain.service;

import br.com.sousa.domain.data.vo.v1.ScheduleRequestVO;
import br.com.sousa.util.converter.DozerConverter;
import br.com.sousa.domain.data.model.Schedule;
import br.com.sousa.domain.data.model.Vote;
import br.com.sousa.domain.data.vo.v1.VotingResultResponseVO;
import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
import br.com.sousa.domain.repository.ScheduleRepository;
import br.com.sousa.util.StatusEnum;
import br.com.sousa.util.VoteEnum;
import br.com.sousa.util.exeption.InvalidOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.sousa.util.Constants.*;
import static br.com.sousa.util.DateUtil.*;

@Service
public class ScheduleService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    ScheduleRepository repository;

    ScheduleService(ScheduleRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new staff with title and description informed by parameter and status NOT_STARTED
     *
     * @param vo Schedule vo
     * @return object Schedule
     */
    public ScheduleResponseVO create(ScheduleRequestVO vo){
        LOGGER.debug("Parsing VoteRequestVO into entity = {}", vo);
        var entity = DozerConverter.parseObject(vo, Schedule.class);
        entity.setStatus(StatusEnum.NOT_STARTED);

        LOGGER.debug("Saving entity = {}",entity);
        var responseVO = DozerConverter.parseObject(repository.save(entity), ScheduleResponseVO.class);
        responseVO.setMessage(getMessage(MESSAGE_SUCCESS_SCHEDULE_REGISTER));

        return responseVO;
    }

    /**
     * Changes the description and title of a Schedule
     *
     * @param id Schedule id
     * @param vo object Schedule
     * @return object Schedule
     */
    public ScheduleResponseVO update(Long id, ScheduleRequestVO vo){
        LOGGER.debug("Find schedule by id {}", id);
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage(MESSAGE_EXCEPTION_NOT_FOUND_SCHEDULE, id)));

        LOGGER.debug("Original entity {}", entity);
        entity.setDescription(vo.getDescription());
        entity.setTitle(vo.getTitle());

        LOGGER.debug("Saving entity {}", entity);
        var responseVO = DozerConverter.parseObject(repository.save(entity), ScheduleResponseVO.class);
        responseVO.setMessage(getMessage(MESSAGE_SUCCESS_SCHEDULE_REGISTER));

        return responseVO;
    }

    @Cacheable("list_schedules")
    public List<ScheduleResponseVO> findAll() {
        LOGGER.debug("Find all Schedules on database");
        return DozerConverter.parseListObject(repository.findAll(), ScheduleResponseVO.class);
    }

    /**
     * Opens the voting on a Schedule with duration informed by parameter.
     *
     * @param id Schedule id
     * @param timeInMinutes duration in minutes
     * @return object Schedule
     */
    public ScheduleResponseVO openPoll(Long id, Long timeInMinutes) {
        var schedule = findEntityById(id);

        if(!schedule.isNew())
            throw new InvalidOperationException(getMessage(MESSAGE_EXCEPTION_ERROR_OPEN_SCHEDULE, id));

        var startDate = LocalDateTime.now();
        schedule.setOpenDate(convertDate(startDate));
        schedule.setExpirationDate(getExpirationDate(startDate, timeInMinutes));
        schedule.setStatus(StatusEnum.OPEN);

        var vo= DozerConverter.parseObject(repository.save(schedule), ScheduleResponseVO.class);
        vo.setMessage(getMessage(MESSAGE_SUCCESS_OPEN_SCHEDULE));
        return vo;
    }

    /**
     * Opens the voting on a Schedule with one minute of duration.
     *
     * @param id Schedule id
     * @return object Schedule
     */
    public ScheduleResponseVO openPoll(Long id) {
        return openPoll(id, DEFAULT_TIME_LENGTH);
    }

    /**
     * Returns the result or partial result of voting for a Schedule.
     * If the schedule is still open, the result it will be partial;
     * if not, it is the final result.
     *
     * @param id Schedule id
     * @return Schedule's result with a correct status, total votes YES and total votes NO
     */
    public VotingResultResponseVO result(Long id) {
        var schedule = verifyExpired(id);

        LOGGER.debug("Grouping Votes for a schedule id {}", id);
        var votes = countingVotes(schedule.getVotes());

        return VotingResultResponseVO.create(schedule.getStatus(),
                votes.get(VoteEnum.YES.getValue()) == null? 0 : votes.get(VoteEnum.YES.getValue()).size(),
                votes.get(VoteEnum.NO.getValue()) == null? 0 : votes.get(VoteEnum.NO.getValue()).size(),
                getMessage(votes.size() == 0 ? MESSAGE_WARNING_VOTES_NOT_FOUND_FOR_SCHEDULE :
                        MESSAGE_SUCCESS_RESULT_LOADED_FOR_SCHEDULE, schedule.getId()));
    }

    public Map<Integer, List<Vote>> countingVotes(List<Vote> list){
        return list.stream()
                .collect(Collectors.groupingBy(Vote::getVote));
    }

    /**
     * Checks if a schedule has expired and corrects its status if necessary.
     * If a schedule has expired then it cannot remain in an open status, its status is changed to CLOSED.
     *
     * @param id - Schedule id
     * @return schedule with a correct status
     */
    public Schedule verifyExpired(Long id) {
        var schedule = findEntityById(id);
        return verifyExpired(schedule);
    }

    /**
     * Checks if a schedule has expired and corrects its status if necessary.
     * If a schedule has expired then it cannot remain in an open status, its status is changed to CLOSED.
     *
     * @param schedule - entity Schedule
     * @return schedule with a correct status
     */
    public Schedule verifyExpired(Schedule schedule) {
        LOGGER.debug("Verify if schedule is open and is expired {}", schedule);
        if (schedule.isOpen() && isExpired(schedule.getExpirationDate())){
            LOGGER.debug("Is expired so prepare to alter the status to {}", StatusEnum.CLOSED);
            schedule.setStatus(StatusEnum.CLOSED);
            LOGGER.debug("Saving schedule {}", schedule);
            repository.save(schedule);
        }
        return schedule;
    }

    public ScheduleResponseVO findById(Long id) {
        return DozerConverter.parseObject(findEntityById(id), ScheduleResponseVO.class);
    }

    @Cacheable("schedule_by_id")
    private Schedule findEntityById(Long id){
        LOGGER.debug("Find schedule by id {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(getMessage(MESSAGE_EXCEPTION_NOT_FOUND_SCHEDULE, id)));
    }

    public List<Schedule> findByStatus(StatusEnum open) {
        return repository.findByStatus(open);
    }

}
