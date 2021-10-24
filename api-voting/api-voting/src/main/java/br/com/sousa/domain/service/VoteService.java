package br.com.sousa.domain.service;

import br.com.sousa.domain.data.vo.v1.VoteRequestVO;
import br.com.sousa.util.VoteEnum;
import br.com.sousa.util.converter.CustomVoteConverter;
import br.com.sousa.domain.data.vo.v1.VoteResponseVO;
import br.com.sousa.domain.repository.VoteRepository;
import br.com.sousa.util.exeption.InvalidOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.List;
import java.util.Optional;

import static br.com.sousa.util.Constants.*;
import static br.com.sousa.util.converter.CustomVoteConverter.parseObject;
import static br.com.sousa.util.converter.CustomVoteConverter.parseListVO;

@Service
public class VoteService extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    VoteRepository repository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ServiceConsumer serviceConsumer;

    public VoteResponseVO create(VoteRequestVO vo){

        LOGGER.debug("Parsing VoteRequestVO into entity = {}", vo);
        var entity = CustomVoteConverter.parseObject(vo);

        entity.setVote(VoteEnum.fromText(vo.getVote()).getValue());
        LOGGER.debug("Preparing to register the vote = {}", entity);
        return parseObject(repository.save(entity));
    }

    public VoteResponseVO update(VoteResponseVO vo){
        LOGGER.error(getMessage(MESSAGE_EXCEPTION_NOT_ALLOWED_UPDATE_VOTE));
        throw new InvalidOperationException(getMessage(MESSAGE_EXCEPTION_NOT_ALLOWED_UPDATE_VOTE));
    }

    @Cacheable("vote_by_id")
    public VoteResponseVO findById(Long id) {

        LOGGER.debug("Find Vote by id = {}", id);
        var entity = Optional.of(repository.findById(id)).get()
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(MESSAGE_EXCEPTION_NOT_FOUND_VOTE, id)));
        return parseObject(entity);
    }

    @Cacheable("list_votes")
    public List<VoteResponseVO> findAll() {
        return parseListVO(repository.findAll());
    }

    public void delete(Long id) {
        throw new InvalidOperationException(getMessage(MESSAGE_EXCEPTION_NOT_ALLOWED_DELETE_VOTE));
    }

    /**
     * Register a vote if:
     *      ->vote.document is valid
     *      ->schedule is open
     *      ->vote.document does not yet have a registered vote for schedule
     * @param vote object to be register
     * @return an object vote if successfully registered or throws an InvalidOperationException
     */
    public VoteResponseVO register(VoteRequestVO vote) {

        var schedule = scheduleService.verifyExpired(vote.getIdSchedule());
        if (schedule.isOpen()) {
            verifyDocument(vote.getDocument());
            verifyUniqueVote(vote.getIdSchedule(), vote.getDocument());
            var vo = create(vote);
            vo.setMessage(getMessage(MESSAGE_SUCCESS_VOTE_REGISTER));
            return vo;

        } else
            throw new InvalidOperationException(getMessage(MESSAGE_EXCEPTION_NOT_OPEN_SCHEDULE, vote.getIdSchedule()));
    }

    /**
     * Checks if there is already a vote registered with the document for the Schedule
     * @param scheduleId Schedule id
     * @param document Associated document
     * @return boolean TRUE if doesn't exists register or throws an InvalidOperationException
     */
    private boolean verifyUniqueVote(Long scheduleId, String document){
        var vote = repository.findByScheduleIdAndDocument(scheduleId, document);

        if (vote.isEmpty())
            return Boolean.TRUE;

        throw new InvalidOperationException(getMessage(MESSAGE_WARNING_MULTIPLE_VOTES, document, scheduleId));
    }

    /**
     * Check if it is a valid document
     * @param document to be validate
     */
    private void verifyDocument(String document){
        //Uncomment when client application is raised
//        if (!serviceConsumer.isValidDocument(document))
//            throw new InvalidOperationException(getMessage(MESSAGE_EXCEPTION_INVALID_DOCUMENT));
    }

    public List<VoteResponseVO> findAllByScheduleId(Long scheduleId) {
        return parseListVO(repository.findAllByScheduleId(scheduleId));
    }
}
