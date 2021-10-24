package br.com.sousa.mocks.v1;

import br.com.sousa.domain.data.model.Schedule;
import br.com.sousa.domain.data.vo.v1.ScheduleRequestVO;
import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
import br.com.sousa.util.DateUtil;
import br.com.sousa.util.StatusEnum;
import br.com.sousa.util.converter.DozerConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MockSchedule {

    private MockSchedule(){};

    public static MockSchedule create(){
        return new MockSchedule();
    }

    public List<ScheduleResponseVO> mockScheduleResponseVOList() {
        List<ScheduleResponseVO> list = new ArrayList<>();
        for (long i = 0; i < 14; i++) {
            list.add(mockScheduleResponseVO(Long.valueOf(i)));
        }
        return list;
    }

    public ScheduleResponseVO mockScheduleResponseVO(Long id) {
        ScheduleResponseVO vo = new ScheduleResponseVO();
        vo.setKey(id);
        vo.setDescription("Description test for id: " + id);
        vo.setTitle("Title test for id: " + id);
        vo.setExpirationDate(DateUtil.getExpirationDate(LocalDateTime.now(), 60L));
        vo.setOpenDate(new Date());
        vo.setStatus(StatusEnum.fromValue(id.intValue() % 3));
        return vo;
    }

    public Schedule mockScheduleEntity(long l) {
        Schedule schedule = DozerConverter.parseObject(mockScheduleResponseVO(1l), Schedule.class);
        schedule.setId(l);
        return schedule;
    }

    public ScheduleRequestVO mockScheduleRequestVO(long id) {
        ScheduleRequestVO vo = new ScheduleRequestVO();
        vo.setDescription("Description test for id: " + id);
        vo.setTitle("Title test for id: " + id);
        return vo;
    }

    public List<Schedule> mockScheduleList() {
        var list = mockScheduleResponseVOList();
        return list.stream().map( vo -> DozerConverter.parseObject(vo, Schedule.class))
                .collect(Collectors.toList());
    }
}
