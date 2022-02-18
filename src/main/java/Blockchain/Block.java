package Blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    private final String previousHash;
    private final long timeStamp;
    private Transaction transaction;
    private String merkleRoot;
    private String hash;
    private int nonce;
    private PublicKey miner;
    private float minerReward;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public Transaction getTransactions() {
        return transaction;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String calculateHash() {
        return StringUtility.applySha256(previousHash + timeStamp + nonce + merkleRoot);
    }

    public void mineBlock(int difficulty,PublicKey key) {
        this.merkleRoot = StringUtility.applySha256(transaction.getId()); //directly add Transaction because we have only one transaction
        String target = StringUtility.getDifficultyString(difficulty);

        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
           hash = calculateHash();
        }

        this.miner = key;
        TransactionOutput minerreward = new TransactionOutput(key,Configuration.instance.reward,"BlockMined:"+merkleRoot+"-"+previousHash);
        Blockchainnetwork.getInstance().getUtx0Map().put(minerreward.getID(),minerreward);


    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            return;
        }

        if (!Objects.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                return;
            }
        }
        this.transaction = transaction;

    }
}