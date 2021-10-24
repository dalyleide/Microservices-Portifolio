package br.com.sousa.mocks.v1;

import br.com.sousa.domain.data.model.Vote;
import br.com.sousa.domain.data.vo.v1.VoteRequestVO;
import br.com.sousa.domain.data.vo.v1.VoteResponseVO;
import br.com.sousa.util.VoteEnum;
import br.com.sousa.util.converter.CustomVoteConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MockVote {

    private MockVote(){}

    public static MockVote create(){
        return new MockVote();
    }

    public List<VoteResponseVO> mockVoteResponseVOList() {
        List<VoteResponseVO> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(mockVoteResponseVO(i));
        }
        return list;
    }

    public VoteResponseVO mockVoteResponseVO(long id) {
        VoteResponseVO vo = new VoteResponseVO();
        vo.setKey(id);
        vo.setDocument(StringUtils.repeat(String.valueOf(id), 11));
        vo.setRegisterDate(new Date());
        vo.setIdSchedule(id);
        vo.setVote(id % 2 == 0? "sim" : "não");
        return vo;
    }

    public List<Vote> mockVoteList() {
        var list = mockVoteResponseVOList();
        return list.stream().map( vo -> CustomVoteConverter.parseObject(vo))
                .collect(Collectors.toList());
    }

    public Vote mockVote(long l) {
        return CustomVoteConverter.parseObject(mockVoteResponseVO(l));
    }

    public VoteRequestVO mockVoteRequestVO() {
        var vo  = new VoteRequestVO();
        vo.setDocument("11111111111");
        vo.setIdSchedule(1L);
        vo.setVote(VoteEnum.NO.getText());
        return vo;
    }
}
