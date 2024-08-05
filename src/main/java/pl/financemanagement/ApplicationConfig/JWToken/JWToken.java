package pl.financemanagement.ApplicationConfig.JWToken;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.financemanagement.User.UserController.UserCredentialsRequest;

import java.util.Date;

@RestController
public class JWToken {

    private static final String USER = "user";
    @Value("${jwt.secret.key}")
    private String secretKey;



    @GetMapping("/auth")
    public String getToken(@RequestBody UserCredentialsRequest userCredentialsRequest) throws JOSEException {
        if(USER.equals(userCredentialsRequest.login())){
            return createToken(userCredentialsRequest.login());
        }
            return "User with login " + userCredentialsRequest.login() + " not found!";
    }

    private String createToken(String login) throws JOSEException {
        JWSAlgorithm algorithm = JWSAlgorithm.HS256;
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 1000);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(login)
                .issueTime(now)
                .expirationTime(expirationTime)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(algorithm), claimsSet);
        JWSSigner signer = new MACSigner(secretKey);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }


}
