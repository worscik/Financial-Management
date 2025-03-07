package pl.financemanagement.Expenses.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.financemanagement.User.UserModel.UserAccount;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID externalId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

}
