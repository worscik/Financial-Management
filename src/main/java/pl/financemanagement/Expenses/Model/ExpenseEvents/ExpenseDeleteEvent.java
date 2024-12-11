package pl.financemanagement.Expenses.Model.ExpenseEvents;

public class ExpenseDeleteEvent {

    private long userId;
    private String externalId;

    public ExpenseDeleteEvent(long userId, String externalId) {
        this.userId = userId;
        this.externalId = externalId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
