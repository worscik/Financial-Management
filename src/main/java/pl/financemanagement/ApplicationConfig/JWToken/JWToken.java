package pl.financemanagement.ApplicationConfig.JWToken;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JWToken {

    @Value("${jwt.secret.key}")
    private String secretKey;


    @GetMapping("/auth")
    public String getToken(@RequestParam String login, @RequestParam String password) throws JOSEException {
        // TODO USER DAO
        return createToken(login);
    }

    private String createToken(String login) throws JOSEException {
        JWSAlgorithm algorithm = JWSAlgorithm.HS256;

        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + 3600 * 100);

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
