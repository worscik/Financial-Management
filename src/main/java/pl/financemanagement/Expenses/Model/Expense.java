package pl.financemanagement.Expenses.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private UUID externalId;
    private int version;
    private Instant createdOn;
    private Instant modifyOn;
    private ExpenseCategory expenseCategory;
    private ExpenseType expenseType;
    private long userId;
    private BigDecimal amount;


}
