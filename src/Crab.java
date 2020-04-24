import javafx.scene.image.Image;

public class Crab extends Fish {
    private double tempsZero;
    private int count;
    public Crab(int level) {
        super(level);
        this.vx *= 1.3;
        this.ay = 0;
        setImage(new Image("/crabe.png"));
        tempsZero = 0;
        count = 1;


        //TODO Oscillations

    }
    /**
     * Met à jour la position du crabe
     * @param dt Temps écoulé depuis le dernier update() en secondes
     * Il avance pendant 0.5s, puis recule pendant 0.25s, puis avance
     * pendant 0.5s, jusqu’à avoir dépassé l’autre côté de l’écran.
     */
    @Override
    public void update(double dt) {

        if ((dt - tempsZero) >= ((long)25e+9)){
            count +=1;
        }
        if ( count %3 == 0){
            y -=  dt * vy;
        } else {
            y +=  dt * vy;
        }
    }



}
