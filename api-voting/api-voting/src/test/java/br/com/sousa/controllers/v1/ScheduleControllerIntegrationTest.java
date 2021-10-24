package br.com.sousa.controllers.v1;


import br.com.sousa.mocks.v1.MockSchedule;
import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
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
public class ScheduleControllerIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleControllerIntegrationTest.class);

    @MockBean
    private ScheduleService service;

    @MockBean
    private VoteController voteController;

    @MockBean
    private VoteService voteService;

    @InjectMocks
    @Autowired
    ScheduleController controller;

    private MockMvc mockMvc;
    private MockSchedule mockSchedule;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockSchedule = MockSchedule.create();
    }

    @Test
    public void whenPostRequestToSchedulesAndValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPostRequestToSchedulesAndValidSchedule_thenCorrectResponse");

        when(service.create(any())).thenReturn(new ScheduleResponseVO());

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        String schedule = "{\"title\": \"Schedule title\", \"description\" : \"Schedule description\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/schedule/v1")
                .content(schedule)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(applicationJsonUtf8));
    }

    @Test
    public void whenPostRequestToSchedulesAndInValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPostRequestToSchedulesAndInValidSchedule_thenCorrectResponse");

        String schedule = "{\"title\": \"\", \"description\" : \"\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/schedule/v1")
                .content(schedule)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenPutRequestToSchedulesAndValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPutRequestToSchedulesAndValidSchedule_thenCorrectResponse");

        var vo = mockSchedule.mockScheduleResponseVO(1L);
        when(service.update(any(), any())).thenReturn(vo);

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        String schedule = "{\"title\": \""+vo.getTitle()+"\", \"description\" : \""+vo.getTitle()+"\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/schedule/v1/".concat(vo.getKey().toString()))
                .content(schedule)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(applicationJsonUtf8))
                .andExpect(jsonPath("$.id").value(vo.getKey()))
                .andExpect(jsonPath("$.title").value(vo.getTitle()))
                .andExpect(jsonPath("$.description").value(vo.getDescription()))
                .andExpect(jsonPath("$.open_date").value(vo.getOpenDate()))
                .andExpect(jsonPath("$.expiration_date").value(vo.getExpirationDate()));
    }

    @Test
    public void whenPutRequestToSchedulesAndInValidSchedule_thenCorrectResponse() throws Exception {
        LOGGER.info("Executing method whenPutRequestToSchedulesAndInValidSchedule_thenCorrectResponse");

        String schedule = "{\"title\": \"\", \"description\" : \"\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/api/schedule/v1/1")
                .content(schedule)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void whenGetRequestToSchedules_thenFind() throws Exception {
        LOGGER.info("Executing method whenGetRequestToSchedules_thenFind");

        ScheduleResponseVO vo = mockSchedule.mockScheduleResponseVO(1L);
        when(service.findById(vo.getKey())).thenReturn(vo);

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/schedule/v1/".concat(vo.getKey().toString()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(applicationJsonUtf8))
                .andExpect(jsonPath("$.id").value(vo.getKey()))
                .andExpect(jsonPath("$.title").value(vo.getTitle()))
                .andExpect(jsonPath("$.description").value(vo.getDescription()))
                .andExpect(jsonPath("$.open_date").value(vo.getOpenDate()))
                .andExpect(jsonPath("$.expiration_date").value(vo.getExpirationDate()));
    }

    @Test
    public void whenGetRequestToSchedules_thenList() throws Exception {
        LOGGER.info("Executing method whenGetRequestToSchedules_thenList");

        var list = mockSchedule.mockScheduleResponseVOList();

        when(service.findAll()).thenReturn(list);

        MediaType applicationJsonUtf8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/schedule/v1/")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(applicationJsonUtf8))
                .andExpect(jsonPath("$[0].id").value(list.get(0).getKey()))
                .andExpect(jsonPath("$[0].title").value(list.get(0).getTitle()))
                .andExpect(jsonPath("$[0].description").value(list.get(0).getDescription()))
                .andExpect(jsonPath("$[0].open_date").value(list.get(0).getOpenDate()))
                .andExpect(jsonPath("$[0].expiration_date").value(list.get(0).getExpirationDate()));
    }
}