package Blockchain;

import java.security.PublicKey;

public class TransactionsOutput {
    private final String id;
    private final PublicKey recipient;
    private final float value;

    public TransactionsOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        id = HashUtilities.SHA256(HashUtilities.getStringFromKey(recipient));
    }
    public boolean isBelongingTo(PublicKey publicKey) {
        return publicKey==recipient;
    }

    public String getId() {
        return id;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }
}
