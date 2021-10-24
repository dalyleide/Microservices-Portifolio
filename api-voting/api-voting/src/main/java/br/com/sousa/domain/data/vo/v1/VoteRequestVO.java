package br.com.sousa.domain.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id_schedule", "document", "vote"})
public class VoteRequestVO implements Serializable {

    private static final long serialVersionUID = -4011697800461425406L;

    @NotBlank(message = "Document is mandatory")
    private String document;

    @NotBlank(message = "Vote is mandatory")
    private String vote;

    @NotNull(message = "Id Schedule is mandatory")
    @JsonProperty("id_schedule")
    private Long idSchedule;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteRequestVO that = (VoteRequestVO) o;
        return Objects.equals(document, that.document) && Objects.equals(vote, that.vote) && Objects.equals(idSchedule, that.idSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, vote, idSchedule);
    }
}
