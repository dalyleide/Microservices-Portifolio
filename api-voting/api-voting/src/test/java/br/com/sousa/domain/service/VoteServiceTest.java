package br.com.sousa.domain.service;

import br.com.sousa.controllers.v1.PollController;
import br.com.sousa.controllers.v1.ScheduleController;
import br.com.sousa.controllers.v1.VoteController;
import br.com.sousa.domain.repository.VoteRepository;
import br.com.sousa.mocks.v1.MockSchedule;
import br.com.sousa.mocks.v1.MockVote;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VoteService.class)
@AutoConfigureMockMvc()
public class VoteServiceTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteServiceTest.class);
    private final Long DEFAULT_ID = 1L;

    @MockBean
    VoteRepository voteRepository;

    @MockBean
    PollController pollController;

    @MockBean
    ScheduleController scheduleController;

    @MockBean
    VoteController voteController;

    @MockBean
    ScheduleService scheduleService;

    @InjectMocks
    @Autowired
    VoteService voteService;

    MockVote mockVote;
    MockSchedule mockSchedule;

    @Before
    public void setUp() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pt"));
        mockVote = MockVote.create();
        mockSchedule = MockSchedule.create();
    }

    @Test
    public void testCreate() {
        LOGGER.info("Executing method testCreate");
        var entity = mockVote.mockVote(DEFAULT_ID);
        when(voteRepository.save(any())).thenReturn(entity);

        var vo = voteService.create(mockVote.mockVoteRequestVO());
        Assert.assertNotNull(vo);
        Assert.assertEquals("11111111111", vo.getDocument());
        Assert.assertEquals("NÃO", vo.getVote());
        Assert.assertEquals(1, (long) vo.getKey());
    }

    @Test
    public void testFindById() {
        LOGGER.info("Executing method testFindById");
        var entity = mockVote.mockVote(DEFAULT_ID);
        when(voteRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(entity));

        var vo = voteService.findById(1L);
        Assert.assertNotNull(vo);
        Assert.assertEquals("11111111111", vo.getDocument());
        Assert.assertEquals("NÃO", vo.getVote());
        Assert.assertEquals(1, (long) vo.getKey());
    }

    @Test
    public void testFindAll() {
        LOGGER.info("Executing method testFindAll");
        when(voteRepository.findAll()).thenReturn(mockVote.mockVoteList());

        var vo = voteService.findAll();
        Assert.assertNotNull(vo);
        Assert.assertEquals(15, vo.size());
    }

    @Test
    public void testRegister() {
        LOGGER.info("Executing method testRegister");
        var entity = mockVote.mockVote(DEFAULT_ID);
        when(voteRepository.findById(any())).thenReturn(java.util.Optional.ofNullable(entity));
        when(scheduleService.verifyExpired((Long) any())).thenReturn(mockSchedule.mockScheduleEntity(DEFAULT_ID));
        when(voteRepository.findByScheduleIdAndDocument(any(), any())).thenReturn(Optional.empty());
        when(voteRepository.save(any())).thenReturn(entity);

        var vo = voteService.register(mockVote.mockVoteRequestVO());
        Assert.assertNotNull(vo);
        Assert.assertEquals("11111111111", vo.getDocument());
        Assert.assertEquals("NÃO", vo.getVote());
        Assert.assertEquals(1, (long) vo.getKey());
    }

    @Test
    public void testFindAllByScheduleId() {
        LOGGER.info("Executing method testFindAllByScheduleId");
        when(voteRepository.findAllByScheduleId(any())).thenReturn(mockVote.mockVoteList());

        var vo = voteService.findAllByScheduleId(DEFAULT_ID);
        Assert.assertNotNull(vo);
        Assert.assertEquals(15, vo.size());
    }
}