package pl.financemanagement.Revenue.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Revenue.Model.RevenueStatsDto;

@Service
@Qualifier("revenueDemoService")
public class RevenueDemoService implements RevenueService {

    @Override
    public RevenueStatsDto getRevenueByDate(String email) {
        return RevenueStatsDto.builder()
                .build();
    }
    
}
