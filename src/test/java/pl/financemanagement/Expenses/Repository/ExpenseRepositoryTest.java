package pl.financemanagement.Expenses.Repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.financemanagement.Expenses.Model.Expense;
import pl.financemanagement.Expenses.Model.ExpenseType;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExpenseRepositoryTest {

    private static final String EXTERNAL_ID = "2ae2eeba-7980-458c-9677-8bc41abf2945";
    private static final String EXTERNAL_ID_2 = "2ae2eeba-7980-458c-9677-8bc41abf2945";
    private static final String EXTERNAL_ID1_3 = "2ae2eeba-7980-458c-9677-8bc41abf2945";
    private static final long USER_ID = 1L;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Test
    void successFindExpenseByExternalIdAndUser() {
        Expense expected = expenseRepository.save(buildExpense(EXTERNAL_ID));

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
        Expense expense_1 = expenseRepository.save(buildExpense(EXTERNAL_ID_2));
        Expense expense_2 = expenseRepository.save(buildExpense(EXTERNAL_ID1_3));

        assertThat(expenseRepository.findExpensesByUserId(USER_ID))
                .hasSize(2)
                .containsExactlyInAnyOrder(expense_1, expense_2);
    }

    @Test
    void returnEmptyListWhenExpenseIsNotFound() {
        assertThat(expenseRepository.findExpensesByUserId(USER_ID))
                .isEmpty();
    }

    private Expense buildExpense(String externalId) {
        Expense expense = new Expense();
        expense.setExpense(BigDecimal.valueOf(1000));
        expense.setExpenseItem("Test");
        expense.setExternalId(externalId);
        expense.setExpenseType(ExpenseType.EXPENSE);
        expense.setUserId(USER_ID);
        return expense;
    }

}