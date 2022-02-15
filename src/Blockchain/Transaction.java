package Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {
    private final PublicKey sender;
    private final PublicKey reciepient;
    private final float value;
    private byte[] signature;

    public Transaction(PublicKey from,PublicKey to,float value) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
    }
    public void sign(PrivateKey key) {
        this.signature = HashTools.signTransaction(key,HashTools.StringToKey(sender)+HashTools.StringToKey(reciepient)+value);

    }
}
