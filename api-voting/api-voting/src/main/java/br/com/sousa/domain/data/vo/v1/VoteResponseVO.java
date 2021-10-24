package br.com.sousa.domain.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "id_schedule", "document", "register_date", "vote", "message"})
public class VoteResponseVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -4011697800461425406L;

    @JsonProperty("id")
    private Long key;

    private String document;

    private String vote;

    @JsonProperty("register_date")
    private Date registerDate;

    @JsonProperty("id_schedule")
    private Long idSchedule;

    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VoteResponseVO that = (VoteResponseVO) o;
        return Objects.equals(key, that.key) && Objects.equals(document, that.document) && Objects.equals(vote, that.vote) && Objects.equals(registerDate, that.registerDate) && Objects.equals(idSchedule, that.idSchedule) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, document, vote, registerDate, idSchedule, message);
    }
}
