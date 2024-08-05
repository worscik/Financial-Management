package pl.financemanagement.ApplicationConfig.DemoResolver;

public class DemoResolver<T> {


    private final T service;
    private final T demoService;

    public DemoResolver(T service, T demoService) {
        this.service = service;
        this.demoService = demoService;
    }

    public T resolveService(boolean isSample) {
        return isSample ? demoService : service;
    }
}
