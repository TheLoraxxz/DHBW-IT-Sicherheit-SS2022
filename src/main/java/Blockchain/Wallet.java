package Blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {


    private PublicKey publicKey;
    private PrivateKey privatekey;
    private String name;
    public HashMap<String,TransactionOutput> ownFunds = new HashMap<>();

    public Wallet() {
        this.generateKeyPair();
    }
    public Wallet(String name) {
        this.name = name;
        this.generateKeyPair();
    }
    private void generateKeyPair() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keygen.initialize(ecSpec,rand);
            KeyPair pair = keygen.generateKeyPair();
            this.privatekey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            System.out.println("#not enough funds to send transaction - transaction discarded");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<>();

        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : ownFunds.entrySet()) {
            TransactionOutput utx0 = item.getValue();
            total += utx0.getValue();
            inputs.add(new TransactionInput(utx0.getID()));
            if (total > value) {
                break;
            }
        }

        Transaction transaction = new Transaction(publicKey, recipient, value, inputs);
        transaction.generateSignature(this.privatekey);

        for (TransactionInput input : inputs) {
            ownFunds.remove(input.getId());
        }

        return transaction;
    }
    public float getBalance() {
        return 0;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }
    public PrivateKey getPrivatekey() {
        return privatekey;
    }
}
