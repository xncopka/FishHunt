public class Player {

    private int id;
    private int life;
    private int points;

    public Player(int id) {
        this.id = id;
        this.life = 3;
        this.points = 0;
    }

    /**
     * Getter du nombre de vies de la méduse
     * @return le nombre de vie restant
     */
    public int getNbVies() {
        return this.life;
    }

    /**
     * Mutateur du nombre de vies de la meduse
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
