package pl.financemanagement.Expenses.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.financemanagement.ApplicationConfig.DemoResolver.DemoResolver;
import pl.financemanagement.Expenses.Model.ExpenseCategory;
import pl.financemanagement.Expenses.Model.ExpenseDto;
import pl.financemanagement.Expenses.Model.ExpenseRequest;
import pl.financemanagement.Expenses.Model.ExpenseResponse;
import pl.financemanagement.Expenses.Service.ExpenseService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/expense")
public class ExpenseController extends DemoResolver<ExpenseService> {

    @Autowired
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
        return ResponseEntity.ok(resolveService(principal.getName()).createExpense(request, principal.getName()));
    }

    @PutMapping()
    ResponseEntity<ExpenseResponse> updateExpense(@RequestBody @Valid ExpenseRequest request,
                                                  BindingResult result,
                                                  Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(principal.getName()).updateExpense(request, principal.getName()));
    }

    @GetMapping("/list/{externalId}")
    ResponseEntity<List<ExpenseDto>> findExpenses(@PathVariable UUID externalId, Principal principal) {
        return ResponseEntity.ok(resolveService(
                principal.getName()).findExpenseByUserNameAndExternalId(principal.getName(), externalId));
    }

    @GetMapping("externalId/{externalId}")
    ResponseEntity<ExpenseResponse> findExpenses(@PathVariable UUID externalId,
                                                 @RequestParam(required = false, defaultValue = "false") boolean isDemo,
                                                 BindingResult result,
                                                 Principal principal) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok(resolveService(
                principal.getName()).findExpenseByIdAndUserId(principal.getName(), externalId));
    }

    @GetMapping("/categories")
    public List<ExpenseCategory> getExpensesCategories() {
        return service().getExpensesCategories();
    }

    static ExpenseResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ExpenseResponse(false, errors);
    }

}
