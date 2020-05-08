import javafx.scene.image.Image;
/**
 * Classe qui représente un crabe dans l'océan
 */

public class Crab extends Fish {

    private double nowTime;
    private boolean sensInverse;

    /**
     * Constructeur du crabe
     * @param level niveau du jeu
     */
    public Crab(int level) {
        super(level);
        this.ay = 0;
        this.vy=0;
        this.nowTime = 0 ;
        this.vx = vx*1.3;
        this.sensInverse = false;
        setImage(new Image("fish/crabe.png"));
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
        int cycle = (int) Math.floor(nowTime / 0.25);

        if (cycle % 3  == 2  && !sensInverse ) {
            this.vx *= -1;
            this.sensInverse = true;
        }
     
        if (cycle % 3  == 0 && sensInverse) {
            this.vx *= -1;
            sensInverse = false;
        }
        super.update(dt);
    }
}
