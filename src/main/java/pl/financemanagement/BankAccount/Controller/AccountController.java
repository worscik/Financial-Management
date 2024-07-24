package pl.financemanagement.BankAccount.Controller;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.UUID;

@RestController
public class AccountController {

    @GetMapping()
    String cos(){
        return "index.html";
    }


    @PostMapping("/account/test")
    public String testEndpoint() {
        return "Token is valid. Message received: ";
    }

}
