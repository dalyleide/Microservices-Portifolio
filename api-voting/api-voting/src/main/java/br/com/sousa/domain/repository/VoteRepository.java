package br.com.sousa.domain.repository;

import br.com.sousa.domain.data.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.schedule.id = :schedule_id")
    List<Vote> findAllByScheduleId(@Param("schedule_id") Long scheduleId);

    @Query("select v from Vote v where v.schedule.id = :schedule_id and v.document = :document")
    Optional<Vote> findByScheduleIdAndDocument(@Param("schedule_id") Long scheduleId, @Param("document") String document);
}
