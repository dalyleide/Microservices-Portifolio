package br.com.sousa.controllers.v1;

import br.com.sousa.domain.data.vo.v1.VoteRequestVO;
import br.com.sousa.domain.data.vo.v1.VoteResponseVO;
import br.com.sousa.domain.service.VoteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"VoteEndpoint"})
@RequestMapping("/api/vote/v1")
@RestController
public class VoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    VoteService service;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @ApiOperation("Find all Votes by Schedule ID")
    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public List<VoteResponseVO> findAll(@RequestParam("scheduleId") Long scheduleId) {
        LOGGER.info("Request findAll");
        List<VoteResponseVO> books =  service.findAllByScheduleId(scheduleId);
        books
                .forEach(p -> p.add(
                        linkTo(methodOn(PollController.class).result(p.getIdSchedule())).withSelfRel()
                        )
                );
        return books;
    }

    @ApiOperation("Find Vote by ID")
    @GetMapping(value= "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public VoteResponseVO findById(@PathVariable("id") Long id) {
        LOGGER.info(String.format("Request findById %d", id));
        VoteResponseVO vo = service.findById(id);
        vo.add(linkTo(methodOn(VoteController.class).findById(id)).withSelfRel());
        return vo;
    }

    @ApiOperation("Register the vote for a Schedule")
    @PostMapping(produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml", "application/x-yaml"})
    public VoteResponseVO register(@Valid @RequestBody VoteRequestVO vote) {
        LOGGER.info(String.format("Request register schedule %d, vote %s", vote.getIdSchedule(), vote.getVote()));
        VoteResponseVO vo = service.register(vote);
        vo.add(linkTo(methodOn(PollController.class).result(vote.getIdSchedule())).withSelfRel());
        return vo;
    }

}
