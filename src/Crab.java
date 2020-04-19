public class Crab extends Fish {

    public Crab(int level) {
        super(level);
        this.vx *= 1.3;
        this.ay = 0;

        //TODO Oscillations

    }

    /**
     * Met à jour la position du crabe
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    @Override
    public void update(double dt) {

        super.update(dt);


    }

}
