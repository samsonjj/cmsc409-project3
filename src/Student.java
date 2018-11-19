public abstract class Student {

    public abstract double getHeight();
    public abstract double getWeight();

    public String toString() {
        return "(" + this.getHeight() + ", " + this.getWeight() + ")";
    }
}
