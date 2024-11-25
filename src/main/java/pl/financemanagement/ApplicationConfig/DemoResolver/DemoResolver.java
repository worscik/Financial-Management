package pl.financemanagement.ApplicationConfig.DemoResolver;

public class DemoResolver<T> {

    private final static String USER_DEMO = "demo@financialapp.com";

    private final T service;
    private final T demoService;

    public DemoResolver(T service, T demoService) {
        this.service = service;
        this.demoService = demoService;
    }

    public T resolveService(String email) {
        return email.equals(USER_DEMO) ? demoService : service;
    }
}
