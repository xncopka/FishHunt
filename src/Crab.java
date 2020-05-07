import javafx.scene.image.Image;

public class Crab extends Fish {

    private double nowTime;
    private boolean oscille;
    private boolean sensInverse;


    public Crab(int level) {
        super(level);
        this.ay = 0;
        this.vy=0;
        this.nowTime = 0 ;
        this.vx = vx*1.3;
        this.oscille = false;
        this.sensInverse = false;



        setImage(new Image("/crabe.png"));





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

     /*   if (((x >= 0) && (isLeftOfScreen())  && !(oscille)   )  || (!(isLeftOfScreen()) && (x<=Jeu.WIDTH)  && !(oscille))   )  {
            this.oscille = true;
            nowTime = 0;
        }*/

        if(oscille) {
            int finAvance = (int) Math.floor(nowTime / 0.5);

            if (finAvance  == 1  && !sensInverse ) {
                this.vx *= -1;
                this.sensInverse = true;
            }
            int finRecule = (int) Math.floor(nowTime / 0.75);
            if (finRecule  == 1 && sensInverse) {
                this.vx *= -1;
                nowTime = 0;
                sensInverse = false;
            }
            

          
        }



        super.update(dt);
    }
}
