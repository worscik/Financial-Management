package pl.financemanagement.BankAccount.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pl.financemanagement.User.UserModel.UserAccount;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID externalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    private Instant createdOn;
    private Instant modifyOn;

    @Version
    private long accountVersion;

    @Column(name = "account_name", nullable = false, unique = true)
    private String accountName;

    @Column(name = "account_number", nullable = false, unique = true)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID accountNumber;

    @Column(nullable = false)
    private BigDecimal accountBalance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

}
