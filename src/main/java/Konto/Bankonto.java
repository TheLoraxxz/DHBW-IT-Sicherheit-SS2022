package Konto;

import Application.ConfigurationApplication;
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

        System.out.print("Current Balance: ");
        System.out.print(this.balance);
        System.out.println(" €");
    }
    public boolean decharge(double amount) {
        if (amount>this.balance) {
            return false;
        } else {
            this.balance= this.balance-amount;
            return true;
        }
    }
}
