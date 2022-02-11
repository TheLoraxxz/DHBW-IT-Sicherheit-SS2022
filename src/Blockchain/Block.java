package Blockchain;

import java.util.Date;

public class Block {
    private final String previousHash;
    private final long time;
    private String hash;
    private int nonce;
    private String merkleRoot;
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.time = new Date().getTime();
        this.hash = calculateHash();
    }
    public String calculateHash() {
        return HashUtilities.SHA256(this.previousHash+time+nonce+merkleRoot);
    }

    public void mineBlock(int difficulty) {

    }

}
