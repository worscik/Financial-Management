package pl.financemanagement.Revenue.Controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.Revenue.Model.RevenueStatsDto;
import pl.financemanagement.Revenue.Service.RevenueService;

import java.security.Principal;

@RestController
@RequestMapping("/revenue")
public class RevenueController extends DemoResolver<RevenueService> {

    public RevenueController(@Qualifier("revenueServiceImpl") RevenueService service,
                             @Qualifier("revenueDemoService") RevenueService demoService) {
        super(service, demoService);
    }

    @GetMapping("/basicData")
    RevenueStatsDto getRevenueBasicData(Principal principal) {
        return resolveService(principal.getName()).getRevenueByDate(principal.getName());
    }


}
