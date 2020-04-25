import javafx.scene.image.Image;

public class Crab extends Fish {
    private double xInit;
    private double dt;
    private int count;

    public Crab(int level) {
        super(level);
        //composante y de la vitesse de la tortue

        this.ay = 0;
        this.vy=0;
        this.xInit =0;
        this.vx = (100*Math.pow(level, 1.0/3) + 200)*1.3;




        setImage(new Image("/crabe.png"));
        this.count = 0;



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
        super.update(dt);
            if ((Math.abs(xInit - this.x) / Math.abs(this.vx)) > 0.25) {
                count++;


                if (count % 3 == 2) {
                    xInit = this.x;
                    this.vx*=-1;
                }
                if (count % 3 == 0 && count != 0) {
                    xInit = this.x;
                    this.vx*=-1;
                }
            }




    }


}
