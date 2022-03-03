package Application;

import Blockchain.Blockchainnetwork;
import Blockchain.StringUtility;
import Blockchain.Transaction;
import Blockchain.Wallet;
import Encryption.Cryptography;
import Konto.Bankonto;
import Runner.Runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.PublicKey;
import java.util.Scanner;

public class MainClass {

    private Blockchainnetwork network;
    private Wallet attacker;
    private Object encryption;
    private Runner runner;
    private Bankonto eigeneskonto;
    private boolean isencrypted;

    public static void main(String[] args) {
        MainClass main = new MainClass();
        main.run();
    }

    public MainClass() {
        this.network = new Blockchainnetwork();
        this.eigeneskonto = new Bankonto();
        this.attacker = new Wallet("Ed");
        this.runner= new Runner(0.02755,0.01,5*60*100,5,"\\data");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("jarsigner", "-verify", "jar/report.jar");
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            boolean isComponentAccepted = false;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("verified")) {
                    isComponentAccepted = true;
                }
            }
            if (!isComponentAccepted) {
                System.out.println("Jar not verified");
                return;
            }
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
                if (isencrypted) {
                    File dir = new File("data\\");
                    File[] fileArr = null;
                    try {
                        fileArr = dir.listFiles();
                    } catch (NullPointerException e) {
                    }
                    if (fileArr == null) {
                        System.out.println("Failed to list Files");
                        return;
                    }
                    for (File f : fileArr) {
                        if (f.getName().endsWith(".mcg")) f.delete();
                    }
                }
                break;
            }

            if (input.equals("help")) {
                System.out.println("'launch http://www.trust-me.mcg/report.jar' - encrypting filed in /data");
                System.out.println("'exchange <amount> BTC' - gets you <amount> of Bitcoin");
                System.out.println("'show balance' - shows current Balance in Bitcoin and your BTC");
                System.out.println("'show recipient' - to get the Bitcoin adress of the attacker");
                System.out.println("'pay <amount> BTC to <adress>' - Pay the required amount to the hacker");
                System.out.println("'check payment' - to get your data back encrypted");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------");
                continue;
            }
            if (input.equals("launch http://www.trust-me.mcg/report.jar")) {
                try {
                    Method method = this.encryption.getClass().getMethod("encrypt",String.class);
                    String filePath = "data\\";
                    method.invoke(this.encryption,filePath);
                    runner.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.isencrypted = true;
                System.out.println("Oops, your files have been encrypted. With a payment of 0.02755 BTC all files will be decrypted");
                System.out.println();
                continue;
            }

            if(input.contains("exchange")) {

                double BTCAmount = getAmount(input);
                if (BTCAmount>0) {
                    double euro = BTCAmount/ConfigurationApplication.instance.getKursEURtoBTC();
                    if (BTCAmount>this.network.getSatoshi().getBalance()||!this.eigeneskonto.decharge(euro)) {
                        System.out.println("Not able to buy more BTC");
                        System.out.println();
                    } else {
                        Transaction getCoin = this.network.getSatoshi().sendFunds(this.eigeneskonto.getVictim().getPublicKey(),(float) BTCAmount);
                        this.network.addTransaction(getCoin);
                    }
                }
                continue;

            }
            if (input.equals("show balance")) {
                this.eigeneskonto.print();
                continue;
            }
            if (input.equals("show recipient")) {
                System.out.print("BTC Wallet: ");
                System.out.println(StringUtility.getStringFromKey(this.attacker.getPublicKey()));
                System.out.println();
                continue;
            }

            if (input.equals("check payment")) {
                if (isencrypted) {
                    this.runner.stop();
                    System.out.println(this.attacker.getBalance());
                    if (this.attacker.getBalance()>=this.runner.getBTC()) {
                        try {
                            Method method = this.encryption.getClass().getMethod("decrypt",String.class);
                            String filePath = "data\\";
                            method.invoke(this.encryption,filePath);
                            System.out.println("Your files are no decrypted!");
                            break;
                        } catch (Exception e) {
                            System.out.println("too bad");
                        }

                    } else {
                        System.out.println("Not enough paid");
                        this.runner.run();
                    }

                } else {
                    System.out.println("No need to check the payment");
                    System.out.println();
                }
                continue;

            }

            if (input.contains("pay ")) {
                double payment = getAmount(input);
                if (input.indexOf("to")==-1||input.indexOf("to")+4>input.length()||payment<0) {
                    System.out.println("Please enter a valid Command");
                    System.out.println("See 'help' for more information");
                } else {
                    if (eigeneskonto.getVictim().getBalance()<payment) {
                        System.out.println("Not enough BTC");
                    } else {
                        String attackerKey = input.substring(input.indexOf("to")+3);
                        PublicKey key = StringUtility.getKeyFromString(attackerKey);
                        System.out.println(key);
                        System.out.println(this.attacker.getPublicKey());
                        if (key!=null) {
                            Transaction transaction;
                            if (this.attacker.getPublicKey().equals(key)) {
                                transaction=this.eigeneskonto.getVictim().sendFunds(this.attacker.getPublicKey(),(float) payment);
                            } else {
                                transaction = this.eigeneskonto.getVictim().sendFunds(key,(float) payment);
                            }
                            this.network.addTransaction(transaction);
                            System.out.println(this.attacker.getBalance());

                        } else {
                            System.out.println("Wrong Key");
                        }
                    }

                }
                continue;
            }


            else {
                System.out.println("Please enter a valid Command");
                System.out.println("See 'help' for more information");
            }

        }
    }
    private double getAmount(String input) {
        try {
            String number = input.substring(input.indexOf(" ")+1,input.lastIndexOf(" BTC"));
            if (number.indexOf(",")>-1) {
                number.replace(",",".");
            }

            return Double.parseDouble(number);
        } catch (Exception e) {
            System.out.println("Please enter a valide command.");
            System.out.println("Type 'help' for more information.");
            System.out.println();
        }
        return -1;

    }

}
