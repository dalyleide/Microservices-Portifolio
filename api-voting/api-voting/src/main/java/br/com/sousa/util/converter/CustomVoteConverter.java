package br.com.sousa.util.converter;

import br.com.sousa.domain.data.model.Schedule;
import br.com.sousa.domain.data.model.Vote;
import br.com.sousa.domain.data.vo.v1.VoteRequestVO;
import br.com.sousa.domain.data.vo.v1.VoteResponseVO;
import br.com.sousa.util.VoteEnum;

import java.util.List;
import java.util.stream.Collectors;

public class CustomVoteConverter {

    private CustomVoteConverter(){}

    public static VoteResponseVO parseObject(Vote entity){
        var vo = new VoteResponseVO();
        vo.setDocument(entity.getDocument());
        vo.setIdSchedule(entity.getSchedule().getId());
        vo.setKey(entity.getId());
        vo.setRegisterDate(entity.getRegisterDate());
        vo.setVote(VoteEnum.fromValue(entity.getVote()).getText());
        return vo;
    }

    public static Vote parseObject(VoteResponseVO vo){
        var entity = new Vote();
        entity.setDocument(vo.getDocument());
        entity.setSchedule(new Schedule(vo.getIdSchedule()));
        entity.setId(vo.getKey());
        entity.setRegisterDate(vo.getRegisterDate());
        entity.setVote(VoteEnum.fromText(vo.getVote()).getValue());
        return entity;
    }

    public static List<VoteResponseVO> parseListVO(List<Vote> listEntity) {
        return listEntity.stream()
                .map( vote ->  {
                    var entity = DozerConverter.parseObject(vote, VoteResponseVO.class);
                    entity.setVote(VoteEnum.fromValue(vote.getVote()).getText());
                    return entity;
                })
                .collect(Collectors.toList());
    }

    public static Vote parseObject(VoteRequestVO vo) {
        var entity = new Vote();
        entity.setDocument(vo.getDocument());
        entity.setSchedule(new Schedule(vo.getIdSchedule()));
        entity.setVote(VoteEnum.fromText(vo.getVote()).getValue());
        return entity;
    }

}
