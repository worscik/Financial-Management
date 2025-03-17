package pl.financemanagement.Revenue.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.financemanagement.Revenue.Model.RevenueStatsDto;
import pl.financemanagement.Revenue.Repository.RevenueRepository;
import pl.financemanagement.User.UserModel.UserAccount;
import pl.financemanagement.User.UserModel.exceptions.UserNotFoundException;
import pl.financemanagement.User.UserRepository.UserAccountRepository;

@Service
@Qualifier("revenueServiceImpl")
public class RevenueServiceImpl implements RevenueService {

    private UserAccountRepository userAccountRepository;
    private RevenueRepository revenueRepository;

    @Autowired
    public RevenueServiceImpl(UserAccountRepository userAccountRepository, RevenueRepository revenueRepository) {
        this.userAccountRepository = userAccountRepository;
        this.revenueRepository = revenueRepository;
    }

    @Override
    public RevenueStatsDto getRevenueByDate(String email) {
        UserAccount userAccount = userAccountRepository.findUserByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email));

        return null;
    }

}
