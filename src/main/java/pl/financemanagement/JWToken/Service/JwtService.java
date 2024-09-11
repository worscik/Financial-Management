package pl.financemanagement.JWToken.Service;

import com.nimbusds.jose.JOSEException;

public interface JwtService {

    String generateUserToken(String login, String role) throws JOSEException;

}
