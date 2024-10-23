package pl.financemanagement.Expenses.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.AppTools.AppTools;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;
import pl.financemanagement.Expenses.Service.ExpenseService;
import pl.financemanagement.User.UserModel.UserNotFoundException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expense")
public class ExpenseController extends DemoResolver<ExpenseService> {


    public ExpenseController(@Qualifier("expenseServiceImpl") ExpenseService service,
                             @Qualifier("expenseServiceDemo") ExpenseService demoService) {
        super(service, demoService);
    }

    @PostMapping()
    ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request,
                                                  BindingResult result,
                                                  Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(request.isDemo()).createExpense(request, principal.getName()));
    }

    @PutMapping()
    ResponseEntity<ExpenseResponse> updateExpense(@RequestBody @Valid ExpenseRequest request,
                                                  BindingResult result,
                                                  Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(request.isDemo()).updateExpense(request, principal.getName()));
    }

    @GetMapping("/list")
    ResponseEntity<List<ExpenseDto>> findExpenses(@RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                  Principal principal) {
        return ResponseEntity.ok(resolveService(isDemo).findExpenseByUserName(principal.getName()));
    }

    @GetMapping("/{externalId}")
    ResponseEntity<ExpenseResponse> findExpenses(@RequestBody ExpenseRequest request,
                                                 @PathVariable String externalId,
                                                 BindingResult result,
                                                 Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        return ResponseEntity.ok(resolveService(request.isDemo()).findExpenseByIdAndUserId(externalId, principal.getName()));
    }

    static ExpenseResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ExpenseResponse(false, errors);
    }

    //TODO operation on bank account
    @PostMapping("/bankBalance")
    public ResponseEntity<Long> bankAccountBalance(@RequestBody @Valid ExpenseRequest request) {
        return null;
    }

}
