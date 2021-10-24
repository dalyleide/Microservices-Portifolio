package br.com.sousa.domain.service;

import br.com.sousa.controllers.v1.VoteController;
import br.com.sousa.domain.repository.ScheduleRepository;
import br.com.sousa.mocks.v1.MockSchedule;
import br.com.sousa.mocks.v1.MockVote;
import br.com.sousa.util.DateUtil;
import br.com.sousa.util.StatusEnum;
import br.com.sousa.util.VoteEnum;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static br.com.sousa.util.DateUtil.convertDate;
import static br.com.sousa.util.DateUtil.getExpirationDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ScheduleService.class)
@AutoConfigureMockMvc()
public class ScheduleServiceTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleServiceTest.class);
    private final Long DEFAULT_ID = 1L;

    @Autowired
    @InjectMocks
    ScheduleService scheduleService;

    @MockBean
    ScheduleRepository scheduleRepository;

    @MockBean
    MessageSource messageSource;

    @MockBean
    VoteController voteController;

    @MockBean
    VoteService voteService;

    MockSchedule mockSchedule;
    MockVote mockVote;

    @Before
    public void setUp() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pt"));
        mockSchedule = MockSchedule.create();
        mockVote = MockVote.create();
    }

    @Test
    public void testCreate() {
        LOGGER.info("Executing method testCreate");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        when(scheduleRepository.save(any())).thenReturn(entity);

        var vo = scheduleService.create(mockSchedule.mockScheduleRequestVO(DEFAULT_ID));
        Assert.assertNotNull(vo);
        Assert.assertEquals(entity.getStatus(), vo.getStatus());
        Assert.assertEquals(entity.getDescription(), vo.getDescription());
    }

    @Test
    public void testFindAll() {
        LOGGER.info("Executing method testFindAll");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        when(scheduleRepository.findAll()).thenReturn(mockSchedule.mockScheduleList());
        when(scheduleRepository.save(any())).thenReturn(java.util.Optional.ofNullable(entity));

        var list = scheduleService.findAll();
        Assert.assertNotNull(list);
    }

    @Test
    public void testOpenPoll() {
        LOGGER.info("Executing method testOpenPoll");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        entity.setStatus(StatusEnum.NOT_STARTED);
        when(scheduleRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(entity));

        var startDate = LocalDateTime.now();
        var entitySave = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        entitySave.setOpenDate(convertDate(startDate));
        entitySave.setExpirationDate(getExpirationDate(startDate, DEFAULT_ID));
        entitySave.setStatus(StatusEnum.OPEN);

        when(scheduleRepository.save(any())).thenReturn(entitySave);

        var vo = scheduleService.openPoll(DEFAULT_ID);
        Assert.assertNotNull(vo);
        Assert.assertEquals(StatusEnum.OPEN, vo.getStatus());
        Assert.assertEquals(-1, vo.getOpenDate().compareTo(vo.getExpirationDate()));
    }

    @Test
    public void testOpenPollWithParam() {
        LOGGER.info("Executing method testOpenPollWithParam");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        entity.setStatus(StatusEnum.NOT_STARTED);
        when(scheduleRepository.findById(DEFAULT_ID)).thenReturn(Optional.of(entity));

        var startDate = LocalDateTime.now();
        var entitySave = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        entitySave.setOpenDate(convertDate(startDate));
        entitySave.setExpirationDate(getExpirationDate(startDate, 5L));
        entitySave.setStatus(StatusEnum.OPEN);

        when(scheduleRepository.save(any())).thenReturn(entitySave);

        var vo = scheduleService.openPoll(DEFAULT_ID, 5L);
        Assert.assertNotNull(vo);
        Assert.assertEquals(StatusEnum.OPEN, vo.getStatus());
        Assert.assertEquals(-1, vo.getOpenDate().compareTo(vo.getExpirationDate()));
    }

    @Test
    public void testResult() {
        LOGGER.info("Executing method testResult");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        entity.setVotes(mockVote.mockVoteList());
        entity.setExpirationDate(DateUtil.getExpirationDate( LocalDateTime.now(), 5L));
        when(scheduleRepository.findById(any())).thenReturn(Optional.of(entity));
        when(messageSource.getMessage(any(), any(), any())).thenReturn("%d");

        var vo = scheduleService.result(DEFAULT_ID);
        Assert.assertNotNull(vo);
        Assert.assertEquals(StatusEnum.OPEN, vo.getStatus());
        Assert.assertEquals(Integer.valueOf(7), vo.getCountNo());
        Assert.assertEquals(Integer.valueOf(8), vo.getCountYes());
    }

    @Test
    public void testCountingVotes() {
        LOGGER.info("Executing method testCountingVotes");
        var vo  = scheduleService.countingVotes(mockVote.mockVoteList());
        Assert.assertEquals(7l, vo.get(VoteEnum.NO.getValue()).size());
        Assert.assertEquals(8l, vo.get(VoteEnum.YES.getValue()).size());
    }

    @Test
    public void testVerifyExpired(){
        LOGGER.info("Executing method testVerifyExpired");
        var entity = mockSchedule.mockScheduleEntity(DEFAULT_ID);
        when(scheduleRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(entity));

        entity = scheduleService.verifyExpired(DEFAULT_ID);
        Assert.assertEquals(StatusEnum.OPEN, entity.getStatus());

        entity.setExpirationDate(DateUtil.getExpirationDate(LocalDateTime.now(), -60L));
        entity = scheduleService.verifyExpired(DEFAULT_ID);
        Assert.assertEquals(StatusEnum.CLOSED, entity.getStatus());

        entity.setStatus(StatusEnum.NOT_STARTED);
        entity = scheduleService.verifyExpired(DEFAULT_ID);
        Assert.assertEquals(StatusEnum.NOT_STARTED, entity.getStatus());
    }
}