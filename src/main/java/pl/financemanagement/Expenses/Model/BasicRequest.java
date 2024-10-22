package pl.financemanagement.Expenses.Model;

public class BasicRequest {

    public boolean demo = false;

    public BasicRequest(boolean demo) {
        this.demo = demo;
    }

    public BasicRequest() {

    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }
}
