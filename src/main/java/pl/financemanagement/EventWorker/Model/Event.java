package pl.financemanagement.EventWorker.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.Instant;

@AllArgsConstructor(onConstructor_ = @JsonCreator)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    private Instant createdOn;
    private Instant startOn;
    private Instant endOn;
    private String data;

}
