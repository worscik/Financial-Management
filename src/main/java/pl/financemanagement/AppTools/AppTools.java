package pl.financemanagement.AppTools;

public class AppTools {

    public static boolean isBlank(String value){
        return value.isBlank() || value.isEmpty();
    }

    public static boolean isNotBlank(String value){
        return !value.isBlank() || !value.isEmpty();
    }
    
    
}
