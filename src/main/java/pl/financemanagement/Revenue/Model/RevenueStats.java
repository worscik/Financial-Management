package pl.financemanagement.Revenue.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "revenue_raw", schema = "stat")
public class RevenueStats {

    @Id
    @JsonIgnore
    private long id;
    private long userId;
    private Instant date;
    private double totalIncomes;
    private double totalExpenses;
    private long numberOfTransactions;
    private double averageTransactionValue;
    private double averageIncomeValue;
    private double averageExpenseValue;

}
