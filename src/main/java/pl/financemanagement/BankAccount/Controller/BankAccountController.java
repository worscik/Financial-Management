package pl.financemanagement.BankAccount.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Service.BankAccountService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class BankAccountController extends DemoResolver<BankAccountService> {

    public BankAccountController(@Qualifier("bankAccountServiceImpl") BankAccountService bankAccountServiceImpl,
                                 @Qualifier("bankAccountServiceDemo") BankAccountService bankAccountServiceDemo) {
        super(bankAccountServiceImpl, bankAccountServiceDemo);
    }

    @PostMapping()
    ResponseEntity<BankAccountResponse> create(@RequestBody BankAccountRequest bankAccountRequest,
                                               BindingResult result,
                                               Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(
                resolveService(principal.getName()).createAccount(bankAccountRequest, principal.getName()));
    }

    @PutMapping()
    ResponseEntity<BankAccountResponse> update(@RequestBody @Valid BankAccountRequest bankAccountRequest,
                                               BindingResult result,
                                               Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(
                resolveService(principal.getName()).updateAccount(bankAccountRequest, principal.getName()));
    }

    @GetMapping()
    ResponseEntity<BankAccountResponse> findAccountByExternalId(Principal principal) {
        return ResponseEntity.ok(resolveService(principal.getName()).findAccountByPrincipal(principal.getName()));
    }

    @DeleteMapping()
    ResponseEntity<BankAccountResponse> deleteAccount(Principal principal) {
        return ResponseEntity.ok().body(resolveService(principal.getName()).deleteAccount(principal.getName()));
    }

    @GetMapping("/bankBalance")
    public ResponseEntity<BigDecimal> bankAccountBalance(Principal principal) {
        return ResponseEntity.ok(resolveService(principal.getName()).getBankAccountBalance(principal.getName()));
    }

    static BankAccountResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new BankAccountErrorResponse(false, errors);
    }

}
