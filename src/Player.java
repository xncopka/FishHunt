public class Player {


    private int life;
    private int points; // points en nombre de poissons attrapés
    private int serie;
    private boolean invicible;
    private int balles;





    public Player() {

        this.life = 3;
        this.points = 0;
        this.serie = 0;
        this.invicible = false;
        this.balles = 10;


    }

    /**
     * Getter du nombre de vies du joueur
     * @return le nombre de vie restant
     */
    public int getNbVies() {
        return this.life;
    }

    /**
     * Mutateur du nombre de vies du joueur
     * @param life nouveau nombre de vies restant
     */
    public void setNbVies(int life) { this.life = life;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }


    public boolean isInvicible() {
        return invicible;
    }

    public void setInvicible(boolean invicible) {
        this.invicible = invicible;
    }

    public int getBalles() {
        return balles;
    }

    public void setBalles(int balles) {
        this.balles = balles;
    }


}
