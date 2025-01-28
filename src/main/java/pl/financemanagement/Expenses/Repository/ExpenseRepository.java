package pl.financemanagement.Expenses.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.financemanagement.Expenses.Model.Expense;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(nativeQuery = true, value = """
            select *
            from expense
            where external_id = :externalId and user_id = :user_id;
            """)
    Optional<Expense> findExpenseByExternalIdAndUserId(@Param("externalId") String externalId,
                                                     @Param("user_id") long user_id);


    @Query(nativeQuery = true, value = """
            select *
            from expense
            where user_id = :user_id;
            """)
    List<Expense> findExpensesByUserId(@Param("user_id") long user_id);

}
