package Blockchain;

public class TransactionInput {
    private final String id;
    private TransactionsOutput output;

    public TransactionInput(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public TransactionsOutput getOutput() {
        return output;
    }

    public void setOutput(TransactionsOutput output) {
        this.output = output;
    }
}
