package Blockchain;

public class Miner {
    private String name;

    private Wallet wallet;
    public Miner(String name) {
        this.name = name;
        this.wallet = new Wallet();
    }

    public String getName() {
        return name;
    }

    public Wallet getWallet() {
        return wallet;
    }
    public void mine(Block block) {
        block.mineBlock(Configuration.instance.difficulty,this.wallet.getPublicKey());
    }

}
