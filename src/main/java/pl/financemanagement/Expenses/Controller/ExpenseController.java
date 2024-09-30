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

    @PostMapping("/")
    ResponseEntity<ExpenseResponse> createExpense(@RequestBody @Valid ExpenseRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok().body(resolveService(request.isDemo()).createExpense(request));
    }

    @PutMapping("/")
    ResponseEntity<ExpenseResponse> updateExpense(@RequestBody @Valid ExpenseRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }

        return ResponseEntity.ok().body(resolveService(request.isDemo()).updateExpense(request));
    }

    //TODO DUPLICATE WITH findExpenses
    @GetMapping("/{userExternalId}")
    ResponseEntity<List<ExpenseDto>> findExpenses(@PathVariable String userExternalId,
                                                  @RequestParam boolean isDemo) {
        if (AppTools.isBlank(userExternalId)) {
            throw new IllegalArgumentException("ExternalId can not be empty");
        }
        return ResponseEntity.ok().body(resolveService(isDemo).findExpenseByUserId(userExternalId));
    }

    @GetMapping("/{externalId}")
    ResponseEntity<ExpenseResponse> findExpenses(@RequestBody @Valid ExpenseRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(buildErrorResponse(result));
        }
        return ResponseEntity.ok().body(
                resolveService(request.isDemo()).findExpenseByIdAndUserId(null, null));
    }

    static ExpenseResponse buildErrorResponse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ExpenseResponse(false, errors);
    }

}
