package Blockchain;

import java.io.File;
import java.nio.file.WatchKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileWriter;
import java.security.Security;
import java.util.concurrent.ThreadLocalRandom;

public class Blockchainnetwork {
    private ArrayList<Miner> miner = new ArrayList<>();
    private ArrayList<Block> chain = new ArrayList<>(1);
    private Transaction genesisTransaction;
    public HashMap<String, TransactionOutput> getUtx0Map() {
        return utx0Map;
    }
    private Wallet satoshi;
    private HashMap<String, TransactionOutput> utx0Map = new HashMap<>();

    public static Blockchainnetwork network;
    public Blockchainnetwork() {
        Security.addProvider(new BouncyCastleProvider());
        network = this;
        miner.ensureCapacity(3);
        miner.add(new Miner("Bob"));
        miner.add(new Miner("Sam"));
        miner.add(new Miner("Eve"));
        this.satoshi = new Wallet("Satoshi");
        this.genesisTransaction = new Transaction(satoshi.getPublicKey(), satoshi.getPublicKey(),1,null);
        this.genesisTransaction.generateSignature(satoshi.getPrivatekey());
        this.genesisTransaction.setId("0");
        this.genesisTransaction.getOutputs().add(
                new TransactionOutput(this.genesisTransaction.getRecipient(),
                        this.genesisTransaction.getValue(),
                        this.genesisTransaction.getId())
        );
        this.utx0Map.put(this.genesisTransaction.getOutputs().get(0).getID(),this.genesisTransaction.getOutputs().get(0));

        Block genesisBlock = new Block("0");
        genesisBlock.addTransaction(this.genesisTransaction);
        this.addBlockToChain(genesisBlock);
    }
    public void addTransaction(Transaction transaction) {
        Block b = new Block(this.chain.get(this.chain.size()-1).getHash());
        b.addTransaction(transaction);
        addBlockToChain(b);
    }
    private void addBlockToChain(Block b) {
        int random = ThreadLocalRandom.current().nextInt(0,2);
        Miner m = this.miner.get(random);
        m.mine(b);
        this.chain.add(b);
        try {
            FileWriter f = new FileWriter("blockchain.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this.chain);
            f.write(json);
            f.close();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }
    public ArrayList<Miner> getMiner() {
        return miner;
    }
    public Wallet getSatoshi() {
        return satoshi;
    }
    public static Blockchainnetwork getInstance() {
        return Objects.requireNonNullElseGet(network, Blockchainnetwork::new);
    }
    public boolean checkChainvalidity() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = StringUtility.getDifficultyString(Configuration.instance.difficulty);
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(this.genesisTransaction.getOutputs().get(0).getID(), this.genesisTransaction.getOutputs().get(0));

        for (int i = 1; i < this.chain.size(); i++) {
            currentBlock = this.chain.get(i);
            previousBlock = this.chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("#current hashes not equal");
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("#trevious hashes not equal");
                return false;
            }

            if (!currentBlock.getHash().substring(0, Configuration.instance.difficulty).equals(hashTarget)) {
                System.out.println("#block not mined");
                return false;
            }

            TransactionOutput tempOutput;
            Transaction currentTransaction =currentBlock.getTransactions();

            if (currentTransaction.verifySignature()) {
                System.out.println("#Signature on Transaction(" + currentTransaction + ") is Invalid");
                return false;
            }

            if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                System.out.println("#Inputs are not equal to oututs on Transaction(" + currentTransaction + ")");
                return false;
            }

            for (TransactionInput input : currentTransaction.getInputs()) {
                tempOutput = tempUTXOs.get(input.getId());

                if (tempOutput == null) {
                    System.out.println("#referenced input on transaction(" + currentTransaction + ") is missing");
                    return false;
                }

                if (input.getUTX0().getValue() != tempOutput.getValue()) {
                    System.out.println("#referenced input on transaction(" + currentTransaction + ") value invalid");
                    return false;
                }

                tempUTXOs.remove(input.getId());
            }

            for (TransactionOutput output : currentTransaction.getOutputs()) {
                tempUTXOs.put(output.getID(), output);
            }

            if (currentTransaction.getOutputs().get(0).getRecipient() != currentTransaction.getRecipient()) {
                System.out.println("#transaction(" + currentTransaction + ") output recipient is invalid");
                return false;
            }

            if (currentTransaction.getOutputs().get(1).getRecipient() != currentTransaction.getSender()) {
                System.out.println("#transaction(" + currentTransaction + ") output 'change' is not sender");
                return false;
            }
        }
        System.out.println("blockchain valid");
        return true;
    }
}
