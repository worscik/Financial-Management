package pl.financemanagement.AppTools;

import org.springframework.security.core.parameters.P;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppTools {

    public static final String UUID_REGEX =
            String.valueOf(Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"));

    public static boolean isBlank(String value){
        return value.isBlank() || value.isEmpty();
    }

    public static boolean isNotBlank(String value){
        return !value.isBlank() || !value.isEmpty();
    }

    public static UUID validUUIDFromString(String uuid) {
            Pattern pattern = Pattern.compile(UUID_REGEX);
        Matcher matcher = pattern.matcher(uuid);
        if(matcher.find()){
            return UUID.fromString(uuid);
        }
        throw new IllegalArgumentException("UUID not valid");
    }
    
    
}
