import javafx.scene.image.Image;

public class Crab extends Fish {
    private double xInit;
    private double tempsInitial;
    private double nowTime;


    public Crab(int level) {
        super(level);
        this.ay = 0;
        this.vy=0;
        this.xInit = 0;
        this.tempsInitial = 0;
        this.nowTime = 0 ;
        this.vx = 423; //(100*Math.pow(level, 1.0/3) + 200)*1.3;


        setImage(new Image("/crabe.png"));




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

        nowTime+=dt;
        int dividerNum = (int) Math.floor(nowTime/0.25);
        if (dividerNum % 3 == 0){
            this.vx = 423;
        }

        if(dividerNum % 3 == 2){
            this.vx = -423;
        }
        super.update(dt);
    }
}
