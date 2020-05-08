/**
 * Classe qui représente le joueur
 */

public class Player {


    private int life;
    private int points; // points en nombre de poissons attrapés
    private int serie; // nombre de poisson touches de facon consecutive
    private boolean invicible;
    private int balles;

    /**
     * Constructeur du joueur
     */
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

    /**
     * Getter du nombre des points accumulés par le joueur
     * @return le nombre de points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Mutateur des points accumulés par le joueur
     * @param points points du jeu
     */
    public void setPoints(int points) {
        this.points = points;
    }
/**
 * Getter du nombre de poisson touche par une balle de facon consecutive
 */
    public int getSerie() {
        return serie;
    }

    /**
     * Mutateur du u nombre de poisson touche par une balle de facon consecutive
     * @param serie nombre de poisson touche de facon consecutive
     */
    public void setSerie(int serie) {
        this.serie = serie;
    }

    /**
     * Booleen qui dit si le joueur est invincible ou non
     * @return vrai ou faux
     */
    public boolean isInvicible() {
        return invicible;
    }

    /**
     * Mutateur de linvincibilite du joueur
     * @param invicible
     */
    public void setInvicible(boolean invicible) {
        this.invicible = invicible;
    }

    /**
     * Getter des balles du jeu
     * @return les balles du jeu
     */
    public int getBalles() {
        return balles;
    }

    /**
     * Mutateur des balles
     * @param balles du jeu
     */
    public void setBalles(int balles) {
        this.balles = balles;
    }
}
