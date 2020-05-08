import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

/**
 *  Classe qui relie la vue au modèle, HighSeaTower à Jeu
 */
public class Controleur {

    private Jeu jeu;

    /**
     * Constructeur de Controleur
     */
    public Controleur(boolean modeSpecial, boolean speakerOn) {
        jeu = new Jeu(modeSpecial, speakerOn);
    }


    /**
     * Demande au jeu de dessiner les formes du dessin
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {
        jeu.draw(context);
    }


    /**
     * Demande au jeu de mettre à jour les données du jeu
     * @param deltaTime Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double deltaTime) {
        jeu.update(deltaTime);
    }


    /**
     * Demande au modele de mettre à jour l'état des bulles
     * Methode pour grouper les bulles a l'arriere plan
     */
    public void groupBulles() { jeu.groupBulles();}

    /**
     * Demande au modele de mettre à jour l'état des balles
     * Methode pour creer une nouvelle balle
     * @param x position x de la balle
     * @param y position y de la balle
     */
    public void newBall(double x, double y) {jeu.newBall(x,y);}

    /**
     * Demande au modele de mettre à jour l'état des poissons
     * Methode pour creer un nouveau poisson
     * @param level niveau du jeu
     */
    public void newFish(int level) {
        jeu.newFish(level);
    }

    /**
     * Demande au modele de mettre à jour l'état des poissons speciaux
     * Methode pour creer un nouveau poisson speciale
     * @param level niveau du jeu
     */
    public void newSpecialFish(int level) {
        jeu.newSpecialFish(level);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Initialise un appât, un saumon ou un prédateur avec 33% de probabilité
     * @param level niveau du jeu
     */

    public void newBonusFish(int level){
        jeu.newBonusFish(level);
    }

    public ArrayList<Item> getItem(){
        return jeu.getItem();
    }


    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui indique si la partie est terminee ou pas
     */
    public boolean getGameOver() {
        return jeu.getGameOver();
    }
    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui renvoit le niveau atteint par le joueur
     */
    public int getLevel() {
        return jeu.getLevel();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui pour mutter le niveau atteint par le joueur
     * @param level niveau du jeu
     */
    public void setLevel(int level) {
        jeu.setLevel(level);
    }
    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui renvoit le score du joueur
     */
    public int getScore() {
        return jeu.getScore();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode pour mutter le score du joueur
     * @param score score du jeu
     */
    public void setScore(int score) {
        jeu.setScore(score);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui mutte le nombre de vie restante
     * @param life
     */
    public void setLife(int life) {
        jeu.setLife(life);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui renvoit le nombre de vie restante
     * @return vies dans le jeu
     */
    public int getLife() {
        return jeu.getLife();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui renvoit le nombre de poisson consecutif touche par le joueur
     * @return nombre de poisson consecutif touche
     */
    public int getSerie() {
        return jeu.getSerie();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur du nombre de poisson touche par le joueur
     * @param serie nombre de poisson touche de suite
     */
    public void setSerie(int serie) {
        jeu.setSerie(serie);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Booleen qui renvoit si le joueur est invincible ou non
     * @return vrai ou faux
     */
    public boolean isInvicible(){
        return jeu.isInvicible();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur pour linvincibilite du joueur
     * @param isInvicible vrai ou faux
     */
    public void setInvicible (boolean isInvicible){
        jeu.setInvicible(isInvicible);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Retourn si cest le joueur est dans un mode dinvincibilite ou non
     * @return vrai ou faux
     */
    public boolean getModeInvicible() {
        return jeu.getModeInvicible();
    }

    /**
     * Mutateur pour changer le mode dinvincibilite du joeur
     * @param modeInvicible vrai ou faux
     */
    public void setModeInvicible(boolean modeInvicible) {
        jeu.setModeInvicible(modeInvicible);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur qui indique que la partie est finie
     * @param gameOver vrai ou faux
     */
    public void setGameOver(boolean gameOver) {
        jeu.setGameOver(gameOver);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui retourne si le niveau du jeu est affiche sur lecran ou non
     * @return vrai ou faux
     */
    public boolean getAfficherLevel() {
        return jeu.getAfficherLevel();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur pour afficher le niveau du jeu
     * @param afficherLevel vrai ou faux
     */
    public void setAfficherLevel(boolean afficherLevel) {
        jeu.setAfficherLevel(afficherLevel);
    }




    public boolean getSniperGame() {
        return jeu.getSniperGame();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Renvoit le nombre de balle dans le jeu
     * @return nombre de balle
     */

    public int getBalles() {
        return jeu.getBalles();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur du nombre de balle dans le jeu
     * @param balles nombre de balle en jeu
     */
    public void setBalles(int balles) {
        jeu.setBalles(balles);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur qui indique si les objets sont activés
     * @param enable vrai ou faux
     */
    public void setEnableItems(boolean enable) {
        jeu.setItemsEnabled(enable);
    }


    public boolean getStopNewFish() {
        return jeu.getStopNewFish();
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Mutateur qui empeche ou non linstanciation de nouveau poisson dans le jeu
     * @param stopNewFish vrai ou faux
     */
    public void setStopNewFish(boolean stopNewFish) {
        jeu.setStopNewFish(stopNewFish);
    }


    public void setSerieActivated(boolean serieActivated){
        jeu.setSerieActivated(serieActivated);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Vérifie si le score passé en paramètre est supérieur aux 10 meilleurs.
     * @param score du jeu
     * @param meilleursScores array compose des meilleurs scores
     * @return un boolean pour savoir si on doit ou non ajouter le score du joueur à l'Arraylist
     */
    public boolean checkNewScore (int score, ArrayList<String> meilleursScores) {
        return jeu.checkNewScore(score, meilleursScores);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Trie le score du jeu
     * @param score score du joueur
     * @param meilleursScores array list des meilleurs scores a date dans le jeu
     * @param name nom du joueur
     * @return retourne le score du jeu
     */
    public int trierScore (int score, ArrayList<String> meilleursScores, String name) {
        return jeu.trierScore(score, meilleursScores, name);
    }

    /**
     * Cherche la chanson dans le jeu
     * @return musique du jeu
     */
    public MusicGame getChanson() {
        return jeu.getChanson();
    }

    /**
     * Booleen qui laisse ou non le son de la chanson
     * @param on chanson jouer ou non
     */
    public void enableChanson(boolean on) {
        jeu.enableChanson(on);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui instancie un crabe
     * @param level niveau du jeu
     */
    public void newCrab(int level) {
        jeu.newCrab(level);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui instancie une etoile de mer
     * @param level niveau du jeu
     */
    public void newStar(int level) {
        jeu.newStar(level);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui instancie un saulmon
     * @param level niveau du jeu
     */
    public void newSalmon(int level) {
        jeu.newSalmon(level);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui instancie un predateur
     * @param level niveau du jeu
     */
    public void newPredator(int level) {
        jeu.newPredator(level);
    }

    /**
     * Demande au modele de mettre à jour l'état du jeu
     * Methode qui instancie un appat
     * @param level niveau du jeu
     */
    public void newAppat(int level) {
        jeu.newAppat(level);
    }

}
