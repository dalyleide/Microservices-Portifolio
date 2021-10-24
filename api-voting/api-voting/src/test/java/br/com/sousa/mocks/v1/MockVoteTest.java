package br.com.sousa.mocks.v1;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MockVoteTest {

    MockVote mockVote;

    @Before
    public void setUp() {
        mockVote = MockVote.create();
    }

    @Test
    public void testMockVote() {
        var vo = mockVote.mockVoteResponseVO(1l);
        Assert.assertEquals(Long.valueOf(1l), vo.getKey());
        Assert.assertEquals("não", vo.getVote());
        Assert.assertEquals("11111111111", vo.getDocument());
        Assert.assertNotNull(vo.getRegisterDate());
        Assert.assertEquals(Long.valueOf(1l), vo.getIdSchedule());
    }

    @Test
    public void testListMockVote() {
        var list = mockVote.mockVoteResponseVOList();
        list.forEach( vo -> {
            Assert.assertNotNull(vo.getKey());
            Assert.assertEquals(vo.getKey()  % 2 == 0? "sim" : "não", vo.getVote());
            Assert.assertEquals(StringUtils.repeat(vo.getKey().toString(), 11), vo.getDocument());
            Assert.assertNotNull(vo.getRegisterDate());
            Assert.assertEquals(vo.getKey(), vo.getIdSchedule());
        });

        var index = 5;
        var vo = list.get(index);
        Assert.assertEquals(Long.valueOf(index), vo.getKey());
        Assert.assertEquals("não", vo.getVote());
        Assert.assertEquals(StringUtils.repeat(String.valueOf(index), 11), vo.getDocument());
        Assert.assertNotNull(vo.getRegisterDate());
        Assert.assertEquals(Long.valueOf(index), vo.getIdSchedule());
    }

}
