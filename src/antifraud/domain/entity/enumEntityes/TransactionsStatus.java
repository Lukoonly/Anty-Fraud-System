package antifraud.domain.entity.enumEntityes;

public enum TransactionsStatus {

    ALLOWED("ALLOWED"),
    MANUAL_PROCESSING("MANUAL_PROCESSING"),
    PROHIBITED("PROHIBITED");


    private final String status;

    TransactionsStatus(String region) {
        this.status = region;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
