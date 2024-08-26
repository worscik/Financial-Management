package pl.financemanagement.BankAccount.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.BankAccount.Model.AccountFindRequest;
import pl.financemanagement.BankAccount.Model.AccountRequest;
import pl.financemanagement.BankAccount.Model.AccountResponse;
import pl.financemanagement.BankAccount.Service.AccountService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController extends DemoResolver<AccountService> {

    public AccountController(@Qualifier("accountServiceImpl") AccountService accountServiceImpl,
                             @Qualifier("accountServiceDemo") AccountService accountServiceDemo) {
        super(accountServiceImpl, accountServiceDemo);
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<AccountResponse> create(@RequestBody @Valid AccountRequest accountRequest,
                                           BindingResult result,
                                           Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(accountRequest.isDemo()).createAccount(accountRequest));
    }

    @PutMapping()
    ResponseEntity<AccountResponse> update(@RequestBody @Valid AccountRequest accountRequest,
                                           BindingResult result,
                                           Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        return ResponseEntity.ok(resolveService(accountRequest.isDemo()).updateAccount(accountRequest));
    }

    @GetMapping()
    ResponseEntity<AccountResponse> findAccountByExternalId(@RequestParam(defaultValue = "") String externalId,
                                                            @RequestParam(defaultValue = "false") boolean isDemo,
                                                            Principal principal) {
        if (AppTools.isBlank(externalId)) {
            return ResponseEntity.badRequest().body(new AccountResponse(false,
                    Map.of("externalId", "Cannot be blank")));
        }

        AccountService accountService = resolveService(isDemo);
        return ResponseEntity.ok(accountService.isExistingAccount(externalId));
    }

    @DeleteMapping()
    ResponseEntity<AccountResponse> deleteAccount(@RequestBody @Valid AccountRequest accountRequest,
                                                  BindingResult result,
                                                  Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        AccountService accountService = resolveService(accountRequest.isDemo());
        return ResponseEntity.ok().body(accountService.deleteAccount(accountRequest.getExternalId()));
    }

    static AccountResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new AccountResponse(false, errors);
    }

}
