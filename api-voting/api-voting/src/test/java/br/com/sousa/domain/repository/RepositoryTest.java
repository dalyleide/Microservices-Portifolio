package br.com.sousa.domain.repository;

import br.com.sousa.mocks.v1.MockSchedule;
import br.com.sousa.mocks.v1.MockVote;
import br.com.sousa.util.VoteEnum;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest extends TestCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTest.class);

    private final Long DEFAULT_ID = 1L;

    @Autowired
    VoteRepository repository;

    @Autowired
    ScheduleRepository scheduleRepository;

    MockVote mockVote;
    MockSchedule mockSchedule;

    @Before
    public void setUp() {
        mockVote = MockVote.create();
        mockSchedule = MockSchedule.create();
    }

    @Test
    public void testSave() {
        repository.deleteAll();
        scheduleRepository.deleteAll();
        LOGGER.info("Saving Schedule");
        var schedule = scheduleRepository.save(mockSchedule.mockScheduleEntity(DEFAULT_ID));

        LOGGER.info("Mocking Vote");
        var vote = mockVote.mockVote(DEFAULT_ID);
        vote.setSchedule(schedule);

        LOGGER.info("Saving Vote ".concat(vote.toString()));
        try{
            vote = repository.save(vote);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            Assert.fail();
        }

        assertEquals("11111111111", vote.getDocument());
        assertEquals(VoteEnum.NO.getValue(), vote.getVote());
        assertSame(schedule.getId(), vote.getSchedule().getId());
        assertEquals("Description test for id: 1", vote.getSchedule().getDescription());
        assertEquals("Title test for id: 1", vote.getSchedule().getTitle());
    }

    @Test
    public void testSave_errorFK() {
        LOGGER.info("Saving Vote without Schedule");
        var vote = mockVote.mockVote(DEFAULT_ID);
        try {
            vote = repository.save(vote);
            assertNull(vote);
        } catch (DataIntegrityViolationException e) {
            assertTrue(Objects.requireNonNull(e.getMessage()).contains("FOREIGN KEY(ID_SCHEDULE)"));
        }
    }

    @Test
    public void testFindAllByScheduleId() {
        LOGGER.info("Saving list of schedules");
        var schedules = scheduleRepository.saveAll(mockSchedule.mockScheduleList());

        LOGGER.info("Creating a list of VOTES");
        var list = mockVote.mockVoteList();

        LOGGER.info("Removing votes without schedules from list");
        list = list.stream().filter(vote -> schedules.stream()
                .anyMatch(schedule -> schedule.getId().equals(vote.getId()))).collect(Collectors.toList());

        LOGGER.info("Saving list of VOTES");
        var votesSaved = repository.saveAll(list);
        assertEquals(votesSaved.size(), list.size());

        var result = repository.findAllByScheduleId(list.get(0).getSchedule().getId());
        assertTrue(result.size() >= 1);
    }

    @Test
    public void testFindByScheduleIdAndDocument() {
        LOGGER.info("Saving Schedule");
        var schedule = scheduleRepository.save(mockSchedule.mockScheduleEntity(DEFAULT_ID));

        LOGGER.info("Mocking Vote");
        var vote = mockVote.mockVote(DEFAULT_ID);
        vote.setSchedule(schedule);

        LOGGER.info("Saving Vote");
        vote = repository.save(vote);

        vote = repository.findByScheduleIdAndDocument(vote.getSchedule().getId(), vote.getDocument()).get();
        assertEquals("11111111111", vote.getDocument());
        assertEquals(VoteEnum.NO.getValue(), vote.getVote());
        assertSame(schedule.getId(), vote.getSchedule().getId());
        assertEquals("Description test for id: 1", vote.getSchedule().getDescription());
        assertEquals("Title test for id: 1", vote.getSchedule().getTitle());
    }
}