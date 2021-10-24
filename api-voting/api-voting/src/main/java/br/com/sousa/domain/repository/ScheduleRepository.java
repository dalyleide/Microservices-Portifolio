package br.com.sousa.domain.repository;

import br.com.sousa.domain.data.model.Schedule;
import br.com.sousa.util.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s where s.status = :status")
    List<Schedule> findByStatus(@Param("status") StatusEnum open);
}
