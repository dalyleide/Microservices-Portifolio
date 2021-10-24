package br.com.sousa.controllers.v1;

import br.com.sousa.domain.data.vo.v1.ScheduleRequestVO;
import br.com.sousa.domain.data.vo.v1.ScheduleResponseVO;
import br.com.sousa.domain.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(tags = {"ScheduleEndpoint"})
@RequestMapping("/api/schedule/v1")
@RestController
public class ScheduleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @ApiOperation("Find Schedule by ID")
    @GetMapping(value= "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ScheduleResponseVO findById(@PathVariable("id") Long id) {
        LOGGER.info(String.format("Request findById %d", id));
        var vo = service.findById(id);
        vo.add(linkTo(methodOn(VoteController.class).findAll(id)).withSelfRel());
        vo.add(linkTo(methodOn(PollController.class).result(id)).withSelfRel());
        return vo;
    }

    @ApiOperation("Find all schedules")
    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public List<ScheduleResponseVO> findAll() {
        LOGGER.info("Request findAll");
        List<ScheduleResponseVO> schedules =  service.findAll();
        schedules
                .forEach(p -> {
                    p.add(linkTo(methodOn(ScheduleController.class).findById(p.getKey())).withSelfRel());
                    p.add(linkTo(methodOn(VoteController.class).findAll(p.getKey())).withSelfRel());
                    p.add(linkTo(methodOn(PollController.class).result(p.getKey())).withSelfRel());
                });
        return schedules;
    }

    @ApiOperation("Create a new Schedule")
    @PostMapping(produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ScheduleResponseVO create(@Valid @RequestBody ScheduleRequestVO schedule) {
        LOGGER.info("Request create schedule ".concat(schedule.getTitle()));
        ScheduleResponseVO vo = service.create(schedule);
        vo.add(linkTo(methodOn(ScheduleController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @ApiOperation("Alter a Schedule record by ID")
    @PutMapping(value= "/{id}",produces = {"application/json", "application/xml"}, consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ScheduleResponseVO update(@PathVariable("id") Long id, @Valid @RequestBody ScheduleRequestVO schedule) {

        ScheduleResponseVO vo =  service.update(id, schedule);
        vo.add(linkTo(methodOn(ScheduleController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
}
