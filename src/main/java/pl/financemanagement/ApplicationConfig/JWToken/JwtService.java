package pl.financemanagement.ApplicationConfig.JWToken;

import com.nimbusds.jose.JOSEException;

public interface JwtService {

    String generateUserToken(String login) throws JOSEException;

}
