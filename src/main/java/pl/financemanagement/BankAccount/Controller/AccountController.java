package pl.financemanagement.BankAccount.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;
import pl.financemanagement.BankAccount.Service.AccountService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController extends DemoResolver<AccountService> {

    public AccountController(@Qualifier("accountServiceImpl") AccountService accountServiceImpl,
                             @Qualifier("accountServiceDemo") AccountService accountServiceDemo) {
        super(accountServiceImpl, accountServiceDemo);
    }

    @PostMapping()
    ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest accountRequest,
                                           Principal principal) {
        AccountService accountService = resolveService(true);
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @PutMapping()
    ResponseEntity<AccountResponse> update(@RequestBody @Valid AccountRequest accountRequest,
                                           Principal principal) {
        AccountService accountService = resolveService(true);
        return ResponseEntity.ok(accountService.updateAccount(accountRequest));
    }

    @GetMapping("/{externalId}")
    ResponseEntity<AccountResponse> findAccountByExternalId(@RequestParam UUID externalId,
                                                            Principal principal) {
        AccountService accountService = resolveService(true);
        return ResponseEntity.ok(accountService.isExistingAccount(externalId));
    }

    @DeleteMapping("/{externalId}")
    boolean deleteAccount(@RequestParam UUID externalId,
                          Principal principal) {
        AccountService accountService = resolveService(true);
        return accountService.deleteAccount(externalId);
    }


}
