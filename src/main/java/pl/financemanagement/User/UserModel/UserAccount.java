package pl.financemanagement.User.UserModel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.financemanagement.Expenses.Model.Expense;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "external_id", nullable = false)
    private String externalId;
    @Column(nullable = false, unique = true)
    private String email;
    private String name;
    private Instant createdOn;
    private Instant modifyOn;
    @Version
    private long version;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;
    private String password;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();

    public UserAccount() {
    }

}
