package Blockchain;

public class Block {
    private String hash;
    private final String previousHash;

    public Block(String previousHash) {
        this.previousHash = previousHash;
    }

}
