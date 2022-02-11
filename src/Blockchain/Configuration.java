package Blockchain;

import java.util.ArrayList;

public enum Configuration {
    instance;
    int difficulty = 3;
    int transactionSequence = 0;
    Transaction genesisTransaction;
    float minimumTransaction = 0.1f;
    ArrayList<Block> blockchain = new ArrayList<>();
}
