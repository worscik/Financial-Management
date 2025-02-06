package pl.financemanagement.Expenses.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String externalId;
    @Version
    private int version;
    private Instant createdOn;
    private Instant modifyOn;
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;
    private String expenseItem;
    @Enumerated(EnumType.STRING)
    private ExpenseType expenseType;
    private BigDecimal expense;
    private long userId;

}
