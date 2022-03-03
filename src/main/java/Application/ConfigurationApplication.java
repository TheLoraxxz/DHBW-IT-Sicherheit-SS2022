package Application;

public enum ConfigurationApplication {
    instance;

    private double kursEURtoBTC = 0.000019;

    public double getKursEURtoBTC() {
        return kursEURtoBTC;
    }
}
