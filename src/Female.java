import java.util.Random;

public class Female extends Student {

    // centimeters
    final static double HEIGHT_MEAN  = 165;
    final static double HEIGHT_VAR = 14;

    //165
    //14

    // kilograms
    final static double WEIGHT_MEAN = 70;
    final static double WEIGHT_VAR = 12;

    //70
    //12

    private double height;
    private double weight;

    public Female() {
        Random random = new Random();
        this.height = random.nextGaussian()*HEIGHT_VAR+HEIGHT_MEAN;
        this.weight = random.nextGaussian()*WEIGHT_VAR+WEIGHT_MEAN;
    }

    public Female(double height, double weight) {
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
