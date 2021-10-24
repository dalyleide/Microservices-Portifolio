package br.com.sousa.mocks.v1;

import br.com.sousa.util.StatusEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockScheduleTest {

    MockSchedule mockSchedule;

    @Before
    public void setUp() {
        mockSchedule = MockSchedule.create();
    }

    @Test
    public void testMockSchedule() {
        var vo = mockSchedule.mockScheduleResponseVO(1l);
        Assert.assertEquals(Long.valueOf(1l), vo.getKey());
        Assert.assertEquals("Description test for id: 1", vo.getDescription());
        Assert.assertEquals("Title test for id: 1", vo.getTitle());
        Assert.assertNotNull(vo.getExpirationDate());
        Assert.assertNotNull(vo.getOpenDate());
        Assert.assertEquals(StatusEnum.fromValue(1), vo.getStatus());
    }

    @Test
    public void testListMockSchedule() {
        var list = mockSchedule.mockScheduleResponseVOList();
        list.forEach(vo -> {
            Assert.assertNotNull(vo.getKey());
            Assert.assertEquals(StatusEnum.fromValue(vo.getKey().intValue() % 3), vo.getStatus());
            Assert.assertEquals("Description test for id: ".concat(vo.getKey().toString()), vo.getDescription());
            Assert.assertEquals("Title test for id: ".concat(vo.getKey().toString()), vo.getTitle());
            Assert.assertNotNull(vo.getOpenDate());
            Assert.assertNotNull(vo.getExpirationDate());
        });

        var index = 0;
        Assert.assertEquals(Long.valueOf(index), list.get(index).getKey());
        Assert.assertEquals(StatusEnum.fromValue(index % 3), list.get(index).getStatus());
        Assert.assertEquals("Description test for id: "+ index, list.get(index).getDescription());
        Assert.assertEquals("Title test for id: "+ index, list.get(index).getTitle());
        Assert.assertNotNull(list.get(index).getOpenDate());
        Assert.assertNotNull(list.get(index).getExpirationDate());
    }
}
