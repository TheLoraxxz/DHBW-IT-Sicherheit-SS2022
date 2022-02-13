package Blockchain;

import java.util.ArrayList;
import java.util.HashMap;

public enum Configuration {
    instance;

    int difficulty = 3;
    int transactionSequence = 0;
    Transaction genesisTransaction;
    float minimumTransaction = 0.1f;
    HashMap<String,TransactionsOutput> outputMap = new HashMap<>();
    ArrayList<Block> blockchain = new ArrayList<>();
}
