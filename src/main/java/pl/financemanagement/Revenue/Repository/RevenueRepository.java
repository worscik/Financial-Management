package pl.financemanagement.Revenue.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.financemanagement.Revenue.Model.RevenueStats;

@Repository
public interface RevenueRepository  extends JpaRepository<RevenueStats, Long> {



}
