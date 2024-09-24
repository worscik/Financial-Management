package pl.financemanagement.BankAccount.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.BankAccount.Model.BankAccountErrorResponse;
import pl.financemanagement.BankAccount.Model.BankAccountRequest;
import pl.financemanagement.BankAccount.Model.BankAccountResponse;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountExistsException;
import pl.financemanagement.BankAccount.Model.Exceptions.BankAccountNotFoundException;
import pl.financemanagement.BankAccount.Service.BankAccountService;
import pl.financemanagement.User.UserModel.UserNotFoundException;

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
                                               Principal principal) throws BankAccountExistsException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(
                resolveService(bankAccountRequest.isDemo()).createAccount(bankAccountRequest, principal.getName()));
    }

    @PutMapping()
    ResponseEntity<BankAccountResponse> update(@RequestBody @Valid BankAccountRequest bankAccountRequest,
                                               BindingResult result,
                                               Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(
                resolveService(bankAccountRequest.isDemo()).updateAccount(bankAccountRequest, principal.getName()));
    }

    @GetMapping()
    ResponseEntity<BankAccountResponse> findAccountByExternalId(@RequestParam(defaultValue = "") String externalId,
                                                                @RequestParam(defaultValue = "false") boolean isDemo,
                                                                Principal principal)
            throws BankAccountNotFoundException, UserNotFoundException {
        if (AppTools.isBlank(externalId)) {
            return ResponseEntity.badRequest().body(new BankAccountErrorResponse(
                    false, "externalId cannot be empty"));
        }
        return ResponseEntity.ok(resolveService(isDemo).findAccountByNumber(externalId, principal.getName()));
    }

    @DeleteMapping()
    ResponseEntity<BankAccountResponse> deleteAccount(@RequestBody @Valid BankAccountRequest bankAccountRequest,
                                                      BindingResult result,
                                                      Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok().body(resolveService
                (bankAccountRequest.isDemo()).deleteAccount(bankAccountRequest.getExternalId(), principal.getName()));
    }

    static BankAccountResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new BankAccountErrorResponse(false, errors);
    }

}
