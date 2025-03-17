package pl.financemanagement.Revenue.Service;

import pl.financemanagement.Revenue.Model.RevenueStatsDto;

public interface RevenueService {

    RevenueStatsDto getRevenueByDate(String email);


}
