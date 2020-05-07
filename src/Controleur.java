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
    public Controleur(int nbPlayers, boolean modeSpecial, boolean speakerOn) {
        jeu = new Jeu(nbPlayers, modeSpecial, speakerOn);
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

    public void newBall(double x, double y) {jeu.newBall(x,y);}

    public void newFish(int level) {
        jeu.newFish(level);
    }

    public void newSpecialFish(int level) {
        jeu.newSpecialFish(level);
    }


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

    public int getLevel() {
        return jeu.getLevel();
    }

    public void setLevel(int level) {
        jeu.setLevel(level);
    }

    public int getScore() {
        return jeu.getScore();
    }

    public void setScore(int score) {
        jeu.setScore(score);
    }

    public void setLife(int life) {
        jeu.setLife(life);
    }

    public int getLife() {
        return jeu.getLife();
    }

    public int getSerie() {
        return jeu.getSerie();
    }

    public void setSerie(int serie) {
        jeu.setSerie(serie);
    }




    public boolean isInvicible(){
        return jeu.isInvicible();
    }
    public void setInvicible (boolean isInvicible){
        jeu.setInvicible(isInvicible);
    }

    public boolean getModeInvicible() {
        return jeu.getModeInvicible();
    }

    public void setModeInvicible(boolean modeInvicible) {
        jeu.setModeInvicible(modeInvicible);
    }


    public void setGameOver(boolean gameOver) {
        jeu.setGameOver(gameOver);
    }



    public boolean getAfficherLevel() {
        return jeu.getAfficherLevel();
    }

    public void setAfficherLevel(boolean afficherLevel) {
        jeu.setAfficherLevel(afficherLevel);
    }




    public boolean getSniperGame() {
        return jeu.getSniperGame();
    }



    public int getBalles() {
        return jeu.getBalles();
    }

    public void setBalles(int balles) {
        jeu.setBalles(balles);
    }

    public void enableItems() {
        jeu.enableItems();
    }


    public boolean getStopNewFish() {
        return jeu.getStopNewFish();
    }

    public void setStopNewFish(boolean stopNewFish) {
        jeu.setStopNewFish(stopNewFish);
    }


    public void setSerieActivated(boolean serieActivated){
        jeu.setSerieActivated(serieActivated);
    }

    public boolean checkNewScore (int score, ArrayList<String> meilleursScores) {
        return jeu.checkNewScore(score, meilleursScores);
    }


    public int trierScore (int score, ArrayList<String> meilleursScores, String name) {
        return jeu.trierScore(score, meilleursScores, name);
    }

    public MusicGame getChanson() {
        return jeu.getChanson();
    }






}
