package br.com.sousa.controllers.v1;

import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
import br.com.sousa.domain.data.vo.v1.VotingResultResponseVO;
import br.com.sousa.domain.service.ScheduleService;
import br.com.sousa.domain.service.VoteService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class PollControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollControllerIntegrationTest.class);

    @MockBean
    VoteController voteController;

    @MockBean
    VoteService voteService;

    @MockBean
    ScheduleService scheduleService;

    @InjectMocks
    @Autowired
    private PollController pollController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(pollController).build();
    }

    @Test
    public void whenGetRequestToPoll_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenGetRequestToPoll_thenCorrectResponse");
        when(scheduleService.openPoll(any())).thenReturn(new ScheduleResponseVO());

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/poll/v1/open/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJsonUtf8));
    }

    @Test
    public void whenGetRequestToPollInSpecificDuration_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenGetRequestToPollInSpecificDuration_thenCorrectResponse");
        when(scheduleService.openPoll(any(), any())).thenReturn(new ScheduleResponseVO());

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/poll/v1/open?id=1&minutes=60")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJsonUtf8));
    }

    @Test
    public void whenGetRequestToResultOfPoll_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenGetRequestToResultOfPoll_thenCorrectResponse");
        when(scheduleService.result(any())).thenReturn(new VotingResultResponseVO());

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/poll/v1/result/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJsonUtf8));
    }
}