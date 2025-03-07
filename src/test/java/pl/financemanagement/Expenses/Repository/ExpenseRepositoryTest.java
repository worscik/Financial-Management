package pl.financemanagement.Expenses.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseType;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.UserRole;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExpenseRepositoryTest {

    private static final UUID EXTERNAL_ID = UUID.fromString("2ae2eeba-7980-458c-9677-8bc41abf2945");
    private static final UUID USER_EXTERNAL_ID = UUID.fromString("167b91fc-d8e3-45f1-bae2-4131e8a073d8"));
    private static final UUID EXTERNAL_ID_2 = UUID.fromString("d8853a30-5c3f-4744-9a86-59f5f4ee1d0b");
    private static final UUID EXTERNAL_ID1_3 = UUID.fromString("4e4221eb-d6ee-4c92-9ec5-d595d9897f40");
    private static final String EMAIL = "exampleEmail";
    private static final String NAME = "exampleName";
    private static final long USER_ID = 1L;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void successFindExpenseByExternalIdAndUser() {
        UserAccount userAccount = buildUserAccount(EMAIL, NAME, USER_EXTERNAL_ID);
        Expense expected = expenseRepository.save(buildExpense(EXTERNAL_ID, userAccount));

        Optional<Expense> expenseByExternalIdAndUserId
                = expenseRepository.findExpenseByExternalIdAndUserId(EXTERNAL_ID, USER_ID);

        assertThat(expenseByExternalIdAndUserId)
                .isPresent()
                .contains(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {100, 300, 1000, 2000})
    void NotFoundExpenseByExternalIdAndUser(long id) {
        Optional<Expense> expenseByExternalIdAndUserId
                = expenseRepository.findExpenseByExternalIdAndUserId(EXTERNAL_ID, id);

        assertThat(expenseByExternalIdAndUserId).isEmpty();
    }

    @Test
    void findExpensesByUserId() {
        UserAccount userAccount = buildUserAccount(EMAIL, NAME, EXTERNAL_ID);
        Expense expense_1 = expenseRepository.save(buildExpense(EXTERNAL_ID_2, userAccount));
        Expense expense_2 = expenseRepository.save(buildExpense(EXTERNAL_ID1_3, userAccount));

        assertThat(expenseRepository.findExpensesByUserId(USER_ID))
                .hasSize(2)
                .containsExactlyInAnyOrder(expense_1, expense_2);
    }

    @Test
    void returnEmptyListWhenExpenseIsNotFound() {
        assertThat(expenseRepository.findExpensesByUserId(USER_ID))
                .isEmpty();
    }

    private Expense buildExpense(UUID externalId, UserAccount userAccount) {
        Expense expense = new Expense();
        expense.setExpense(BigDecimal.valueOf(1000));
        expense.setExpenseItem("Test");
        expense.setExternalId(externalId);
        expense.setExpenseType(ExpenseType.EXPENSE);
        expense.setUser(userAccount);
        return expense;
    }

    private UserAccount buildUserAccount(String email, String name, UUID externalId) {
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(email);
        userAccount.setName(name);
        userAccount.setPassword("password");
        userAccount.setUserRole(UserRole.USER);
        userAccount.setExternalId(externalId);
        return userAccount;
    }

}