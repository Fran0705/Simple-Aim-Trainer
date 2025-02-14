import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

class Target  {
    private static int targetsMissed;
    private static int targetsHit;
    private double radius;
    Circle circle;

    //Default Constructor
    public Target(){
        this(50);
    }

    //Overloaded Constructor
    public Target(double radius){
        this.radius = radius;
        this.circle = new Circle(radius, Color.rgb(180, 0, 0));
    }

    //Getters
    public static int getTargetsHit() {
        return targetsHit;
    }
    public static int getTargetsMissed() {
        return targetsMissed;
    }
    public double getRadius() {
        return radius;
    }

    //Setters
    public void setRadius(double radius) {          //Settings need to implemented in-game in order for this to be useful
        this.radius = radius;
    }

    //Behaviors
    public static void targetHit(){
        targetsHit++;
        targetsMissed--;       //Beforehand a target is considered missed until it's not been hit
    }
    public static void targetMissed(){
        targetsMissed++;
    }
    public static void resetStats(){
        targetsHit = 0;
        targetsMissed = 0;
    }
    public static double getScore(){
        return 100 * getTargetsHit() / (getTargetsHit() + getTargetsMissed());  //This will always return an int type. In order to nicely output a double value I'd need to format output, which is not working fsr
    }
}
