package pl.financemanagement.BankAccount.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Service.BankAccountService;

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
        return ResponseEntity.ok(resolveService(bankAccountRequest.isDemo()).createAccount(bankAccountRequest));
    }

    @PutMapping()
    ResponseEntity<BankAccountResponse> update(@RequestBody @Valid BankAccountRequest bankAccountRequest,
                                               BindingResult result,
                                               Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        return ResponseEntity.ok(resolveService(bankAccountRequest.isDemo()).updateAccount(bankAccountRequest));
    }

    @GetMapping()
    ResponseEntity<BankAccountResponse> findAccountByExternalId(@RequestParam(defaultValue = "") String externalId,
                                                                @RequestParam(defaultValue = "false") boolean isDemo,
                                                                Principal principal) {
        if (AppTools.isBlank(externalId)) {
            return ResponseEntity.badRequest().body(new BankAccountResponse(false,
                    Map.of("externalId", "Cannot be blank")));
        }

        BankAccountService bankAccountService = resolveService(isDemo);
        return ResponseEntity.ok(bankAccountService.isExistingAccount(externalId));
    }

    @DeleteMapping()
    ResponseEntity<BankAccountResponse> deleteAccount(@RequestBody @Valid BankAccountRequest bankAccountRequest,
                                                      BindingResult result,
                                                      Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        BankAccountService bankAccountService = resolveService(bankAccountRequest.isDemo());
        return ResponseEntity.ok().body(bankAccountService.deleteAccount(bankAccountRequest.getExternalId()));
    }

    static BankAccountResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new BankAccountResponse(false, errors);
    }

}
