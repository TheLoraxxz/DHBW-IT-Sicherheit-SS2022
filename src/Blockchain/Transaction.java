package Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    private final PublicKey sender;
    private final PublicKey recipient;
    private final float value;
    private final ArrayList<TransactionsOutput> outputs = new ArrayList<>();
    private final ArrayList<TransactionInput> inputs;
    private String id;
    private byte[] signature;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        Configuration.instance.transactionSequence++;
        return HashUtilities.SHA256(HashUtilities.getStringFromKey(sender) + HashUtilities.getStringFromKey(recipient)
                + value + Configuration.instance.transactionSequence);
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = HashUtilities.getStringFromKey(sender) + HashUtilities.getStringFromKey(recipient) + value;
        signature = HashUtilities.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = HashUtilities.getStringFromKey(sender) + HashUtilities.getStringFromKey(recipient) + value;
        return !HashUtilities.verifyECDSASig(sender, data, signature);

    }

    public boolean processTransaction() {
        if (verifySignature()) {
            System.out.println("#transaction signature failed to verify");
            return false;
        }

        for (TransactionInput i : inputs) {
            i.setOutput(Configuration.instance.outputMap.get(i.getId()));
        }

        if (getInputsValue() < Configuration.instance.minimumTransaction) {
            System.out.println("#transaction input to small | " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        id = calculateHash();
        outputs.add(new TransactionsOutput(recipient, value, id));
        outputs.add(new TransactionsOutput(sender, leftOver, id));

        for (TransactionsOutput o : outputs) {
            Configuration.instance.outputMap.put(o.getId(), o);
        }

        for (TransactionInput i : inputs) {
            if (i.getOutput() == null) {
                continue;
            }
            Configuration.instance.outputMap.remove(i.getOutput().getId());
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;

        for (TransactionInput i : inputs) {
            if (i.getOutput() == null) {
                continue;
            }
            total += i.getOutput().getValue();
        }

        return total;
    }

    public float getOutputsValue() {
        float total = 0;

        for (TransactionsOutput o : outputs) {
            total += o.getValue();
        }

        return total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public ArrayList<TransactionInput> getInputs() {
        return inputs;
    }

    public ArrayList<TransactionsOutput> getOutputs() {
        return outputs;
    }
}
