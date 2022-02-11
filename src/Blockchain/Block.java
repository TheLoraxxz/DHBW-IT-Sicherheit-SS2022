package Blockchain;

import org.w3c.dom.CDATASection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Block {
    private static final Logger logger = Logger.getLogger(Block.class.getName());
    private final String previousHash;
    private final long time;
    private String hash;
    private int nonce;
    private String merkleRoot;
    private String data;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.time = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return HashUtilities.SHA256(this.previousHash+time+nonce+merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = HashUtilities
    }

    public String calculateHashBlock() {
        return HashUtilities.SHA256(previousHash+time+nonce+merkleRoot);
    }

    public String getPreviousHash() {
        return previousHash;
    }


}
