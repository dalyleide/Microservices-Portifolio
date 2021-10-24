package br.com.sousa.domain.data.vo.v1;

import br.com.sousa.util.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"status", "total_votes_yes", "total_votes_no", "message"})
public class VotingResultResponseVO extends ResourceSupport implements Serializable {

    private StatusEnum status;

    @JsonProperty("total_votes_yes")
    private Integer countYes;

    @JsonProperty("total_votes_no")
    private Integer countNo;

    private String message;

    public static VotingResultResponseVO create(StatusEnum status, Integer countYes, Integer countNo, String message) {
        var vo =  new VotingResultResponseVO();
        vo.setCountNo(countNo);
        vo.setCountYes(countYes);
        vo.setStatus(status);
        vo.setMessage(message);
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VotingResultResponseVO that = (VotingResultResponseVO) o;
        return status == that.status && Objects.equals(countYes, that.countYes) && Objects.equals(countNo, that.countNo) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, countYes, countNo, message);
    }
}
