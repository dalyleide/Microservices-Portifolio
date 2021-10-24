package br.com.sousa.domain.data.vo.v1;

import br.com.sousa.util.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "title", "description", "status", "open_date", "expiration_date"})
public class ScheduleResponseVO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 600619093845112636L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;

    private String title;

    private String description;

    private StatusEnum status;

    @JsonProperty("open_date")
    private Date openDate;

    @JsonProperty("expiration_date")
    private Date expirationDate;

    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ScheduleResponseVO that = (ScheduleResponseVO) o;
        return Objects.equals(key, that.key) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && status == that.status && Objects.equals(openDate, that.openDate) && Objects.equals(expirationDate, that.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, title, description, status, openDate, expirationDate);
    }
}