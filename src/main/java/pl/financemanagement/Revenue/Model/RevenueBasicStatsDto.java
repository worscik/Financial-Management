package pl.financemanagement.Revenue.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
public class RevenueBasicStatsDto {

    private Instant date;
    private long userId;
    private double sumExpenses;

}
