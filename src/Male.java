import java.util.Random;

public class Male extends Student {

    // centimeters
    final static double HEIGHT_MEAN  = 180;
    final static double HEIGHT_VAR = 15;

    // 180
    // 15

    // kilograms
    final static double WEIGHT_MEAN = 80;
    final static double WEIGHT_VAR = 15;

    // 80
    // 15

    private double height;
    private double weight;

    public Male() {
        Random random = new Random();
        this.height = random.nextGaussian()*HEIGHT_VAR+HEIGHT_MEAN;
        this.weight = random.nextGaussian()*WEIGHT_VAR+WEIGHT_MEAN;
    }

    public Male(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}
