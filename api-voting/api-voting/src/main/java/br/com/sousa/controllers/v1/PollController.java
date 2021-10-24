package br.com.sousa.controllers.v1;

import br.com.sousa.domain.data.vo.v1.VotingResultResponseVO;
import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
import br.com.sousa.domain.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"PollEndpoint"})
@RequestMapping("/api/poll/v1")
@RestController
public class PollController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollController.class);

    ScheduleService service;

    public PollController(ScheduleService service) {
        this.service = service;
    }

    @ApiOperation("Open a Schedule for voting and maintains open for one minute")
    @GetMapping(value= "/open/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ScheduleResponseVO openPoll(@PathVariable("id") Long id) {

        LOGGER.debug("Trying to open poll for schedule id {}", id);
        ScheduleResponseVO vo = service.openPoll(id);

        LOGGER.debug("Opened poll successfully schedule {}", vo);
        vo.add(linkTo(methodOn(PollController.class).result(id)).withSelfRel());
        return vo;
    }

    @ApiOperation("Open a Schedule for voting and maintains open for 'minutes' open")
    @GetMapping(value= "/open", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ScheduleResponseVO openPoll(@RequestParam("id") Long id, @RequestParam("minutes") Long minutes) {

        LOGGER.info(String.format("Trying to open poll with duration provided schedule %d, duration %d", id, minutes));
        ScheduleResponseVO vo = service.openPoll(id, minutes);

        LOGGER.debug("Opened poll successfully schedule {}", vo);
        vo.add(linkTo(methodOn(PollController.class).result(id)).withSelfRel());
        return vo;
    }

    @ApiOperation("Return the result of a poll voting about a Schedule")
    @GetMapping(value= "/result/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public VotingResultResponseVO result(@PathVariable("id") Long id) {

        LOGGER.debug("Trying to calculate the result por schedule id {}", id);
        VotingResultResponseVO vo = service.result(id);
        vo.add(linkTo(methodOn(ScheduleController.class).findAll()).withSelfRel());
        vo.add(linkTo(methodOn(ScheduleController.class).findById(id)).withSelfRel());
        return vo;
    }

}
