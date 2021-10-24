package br.com.sousa.domain.data.model;

import br.com.sousa.util.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "schedule")
public class Schedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "description", nullable = false, length = 50)
    private String description;
    @Column(name = "status", nullable = false, length = 1)
    private StatusEnum status;
    @Column(name = "open_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date openDate;
    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_schedule")
    private List<Vote> votes;

    public Schedule(Long id) {
        this.id = id;
    }

    public boolean isOpen(){
        return StatusEnum.OPEN.equals(status);
    }

    public boolean isNew(){
        return StatusEnum.NOT_STARTED.equals(status);
    }
}