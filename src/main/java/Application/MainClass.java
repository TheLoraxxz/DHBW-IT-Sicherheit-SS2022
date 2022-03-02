package Application;

import Blockchain.Blockchainnetwork;
import Blockchain.Transaction;
import Blockchain.Wallet;
import Encryption.Cryptography;
import Konto.Bankonto;
import Runner.Runner;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Scanner;

public class MainClass {

    private Blockchainnetwork network;
    private Wallet attacker;
    private Object encryption;
    private Runner runner;
    private Bankonto eigeneskonto;

    public static void main(String[] args) {
        MainClass main = new MainClass();
        main.run();
    }

    public MainClass() {
        this.network = new Blockchainnetwork();
        this.eigeneskonto = new Bankonto();
        this.attacker = new Wallet("Ed");
        this.runner= new Runner(0.02755,0.01,5*60*100,60*100,"\\data");
        try {
            URL[] urls = {new File("jar\\report.jar").toURI().toURL()};
            URLClassLoader load = new URLClassLoader(urls, Cryptography.class.getClassLoader());
            Class encryption = Class.forName("Cryptography",true,load);
            this.encryption = encryption.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner cmd = new Scanner(System.in);

        while(true) {
            System.out.print("application@user $ ");
            String input = cmd.nextLine();
            if (input.equals("exit")) {
                break;
            }
            if (input.equals("help")) {
                System.out.println("'launch http://www.trust-me.mcg/report.jar' - encrypting filed in /data");
                System.out.println("'exchange <amount> BTC' - gets you <amount> of Bitcoin");
                System.out.println("'show balancy' - shows current Balance in Bitcoin and your BTC");
                System.out.println("'show recipient' - to get the Bitcoin adress of the attacker");
                System.out.println("'pay <amount> BTC to <adress>' - Pay the required amount to the hacker");
                System.out.println("'check payment' - to get your data back encrypted");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------");
            }
            if (input.equals("launch http://ww.trust-me.mcg/report.jar")) {
                try {
                    Method method = this.encryption.getClass().getMethod("encrypt",String.class);
                    String filePath = "data\\";
                    method.invoke(this.encryption,filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted");
            }
            if(input.contains("exchange")) {
                Transaction getCoin = this.network.getSatoshi().sendFunds(this.eigeneskonto.getVictim().getPublicKey(),0.02755f);
                this.network.addTransaction(getCoin);
                break;
            }

        }
    }

}
