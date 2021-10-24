package br.com.sousa.controllers.v1;


import br.com.sousa.mocks.v1.MockVote;
import br.com.sousa.domain.data.vo.v1.VoteResponseVO;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
public class VoteControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteControllerIntegrationTest.class);

    @MockBean
    private VoteService voteService;

    @MockBean
    private PollController pollController;

    @MockBean
    private ScheduleController scheduleController;

    @MockBean
    private ScheduleService scheduleService;

    @InjectMocks
    @Autowired
    VoteController controller;

    private MockMvc mockMvc;
    private MockVote mockVote;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockVote = MockVote.create();
    }

    @Test
    public void whenPostRequestToVotesAndValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPostRequestToVotesAndValidSchedule_thenCorrectResponse");

        when(voteService.register(any())).thenReturn(new VoteResponseVO());

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        String vote = "{\"document\": \"123654789\", \"vote\" : \"YES\", \"id_schedule\" : 1}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/vote/v1")
                .content(vote)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJsonUtf8));
    }

    @Test
    public void whenPostRequestToVotesAndInValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPostRequestToVotesAndInValidSchedule_thenCorrectResponse");
        String vote = "{\"document\": \"\", \"vote\" : \"\", \"id_schedule\" : }";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/vote/v1")
                .content(vote)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenGetRequestToVotes_thenFind() throws Exception {
        LOGGER.info("Executing method whenGetRequestToVotes_thenFind");

        var vo = mockVote.mockVoteResponseVO(1L);
        when(voteService.findById(vo.getKey())).thenReturn(vo);

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vote/v1/".concat(vo.getKey().toString()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(applicationJsonUtf8))
                .andExpect(jsonPath("$.id").value(vo.getKey()))
                .andExpect(jsonPath("$.id_schedule").value(vo.getIdSchedule()))
                .andExpect(jsonPath("$.vote").value(vo.getVote()))
                .andExpect(jsonPath("$.register_date").value(vo.getRegisterDate()))
                .andExpect(jsonPath("$.document").value(vo.getDocument()));
    }

    @Test
    public void whenGetRequestToVotes_thenList() throws Exception {
        LOGGER.info("Executing method whenGetRequestToVotes_thenList");

        var list = mockVote.mockVoteResponseVOList();

        when(voteService.findAllByScheduleId(any())).thenReturn(list);

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/vote/v1?scheduleId=".concat(list.get(0).getIdSchedule().toString()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(applicationJsonUtf8))
                .andExpect(jsonPath("$[0].id").value(list.get(0).getKey()))
                .andExpect(jsonPath("$[0].id_schedule").value(list.get(0).getIdSchedule()))
                .andExpect(jsonPath("$[0].vote").value(list.get(0).getVote()))
                .andExpect(jsonPath("$[0].register_date").value(list.get(0).getRegisterDate()))
                .andExpect(jsonPath("$[0].document").value(list.get(0).getDocument()));
    }

}