package pl.financemanagement.Expenses.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.financemanagement.Expenses.Model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query(nativeQuery = true, value = """
            select *
            from expense
            where external_id = :externalId and user_id = :userId;
            """)
    Optional<Expense> findExpenseByExternalId(@Param("externalId") String externalId,
                                              @Param("user") long userId);


    @Query(nativeQuery = true, value = """
            select *
            from expense
            where user_id = :userId;
            """)
    List<Expense> findExpensesByUserId(@Param("userId") long userId);

}
