package pl.financemanagement.EventWorker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.financemanagement.EventWorker.Model.Event;

public interface EventRespository extends JpaRepository<Event, Long> {

}
