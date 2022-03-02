package Konto;

import Blockchain.Wallet;

public class Bankonto {
    private double balance;


    private Wallet victim;

    public Bankonto() {
        this.balance = 5000;
        this.victim = new Wallet("Clue Less");

    }
    public Wallet getVictim() {
        return victim;
    }
    public void print() {
        System.out.print("Wallet balance: ");
        System.out.print(victim.getBalance());
        System.out.println(" BTC");

        System.out.print("Currebnt Balance: ");
        System.out.print(this.balance);
        System.out.println(" BTC");
    }
}
