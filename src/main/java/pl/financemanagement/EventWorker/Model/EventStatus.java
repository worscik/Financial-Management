package pl.financemanagement.EventWorker.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum EventStatus {

    NEW("n"),
    WORK("w"),
    DONE("d"),
    FAIL("f");

    private final String status;

}
