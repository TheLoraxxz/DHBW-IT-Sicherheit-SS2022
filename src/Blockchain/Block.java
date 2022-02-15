package Blockchain;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Date;

public class Block {
    private final String hash;
    private final String previousHash;
    private final long timestamp;
    private PublicKey miner ;
    private long nonce;
    private String merkleRoot;
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.nonce = 0;
        this.hash = calculateHash();

    }


    private String calculateHash() {
        return HashTools.SHA256(previousHash+timestamp);
    }


}
