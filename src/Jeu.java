import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;


/**
 * Classe reprentant le jeu et la logique interne de l’application (modèle)
 */
public class Jeu {

    // Largeur, hauteur du niveau
    public static final int WIDTH = 640, HEIGHT = 480;

    // Entités dans le jeu
    private Bulle[][] bulles;
    private  ArrayList<Balle> balles = new ArrayList<>();
    private ArrayList<Fish> fishes = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Integer> meilleursScores = new ArrayList<>();






    // joueurs dans le jeu
    private Player[] players;



    // niveau du jeu
    private int level;
    private boolean firstChangeLevel;

    // si la partie est terminée
    private boolean gameOver;


    private int palier;

    private boolean afficherLevel;

    public boolean getModeInvicible() {
        return modeInvicible;
    }

    public void setModeInvicible(boolean modeInvicible) {
        this.modeInvicible = modeInvicible;
    }

    private boolean modeInvicible;

 



    private boolean stopNewFish = false;


    private boolean sniperGame;



    private boolean modeSolo;   // Si oui on est en mode solo sinon on est en mode multi

    private boolean itemsEnabled;

    public void setSerieActivated(boolean serieActivated) {
        this.serieActivated = serieActivated;
    }

    private boolean serieActivated;



    public void enableItems() {
        itemsEnabled = true;

    }

    public void saveScore() {
            int score = players[0].getPoints();
            meilleursScores.add(score);
            Collections.reverse(meilleursScores);
        
    }




    public boolean getSniperGame() {
        return sniperGame;
    }

    public void setSniperGame(boolean sniperGame) {
        this.sniperGame = sniperGame;
    }

    public int getBalles() {
        return players[0].getBalles();
    }

    public void setBalles(int balles) {
        if(players[0].getBalles()!=0) {
            players[0].setBalles(balles);
        }
    }


    public int getSerie() {
        return players[0].getSerie();
    }




    /**
     * Permet de savoir qui est le gagnant de la partie
     * @return un string
     */
    public String getWinner() {
            Player winner = players[0];
            for (int i = 0; i < players.length-1; i++) {
                if (players[i].getPoints() < players[i+1].getPoints()) {
                    winner = players[i+1];
                }
            }
            return "Player " + winner.getId() + " wins!";
    }


    public void setGameOver(boolean gameOver) {
       this.gameOver = gameOver;
    }



    /**
     * Savoir si le jeu est terminée
     * @return un boolean
     */
    public boolean getGameOver() {
        return this.gameOver;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return players[0].getPoints();
    }

    public void setScore(int score) {

            players[0].setPoints(score);

    }

    public void setLife(int life) {
        if(getLife()<3) {
            players[0].setNbVies(life);
        }
    }

    public int getLife() {
        return players[0].getNbVies();
    }


    /**
     * Getter du nombre de vies du Player avec l'id correspondant
     * @return le nombre de vie restant
     */
    public int getNbViesPlayer(int id) {
        return players[id].getNbVies();
    }

    /**
     * Mutateur du nombre de vies du player avec l'id correspondant
     * @param life nouveau nombre de vies restant
     */
    public void setNbViesPlayer(int id, int life) {
        players[id].setNbVies(life);
    }


    public boolean isInvicible(){
        return players[0].isInvicible();
    }
    public void setInvicible (boolean isInvicible){
        players[0].setInvicible(isInvicible);
    }


    public void setSerie(int serie) {
        players[0].setSerie(serie);
    }


    /**
     * Constructeur de Jeu
     */
    public Jeu(int nbPlayers) {

        modeSolo = nbPlayers == 1;

        // Test du mode sniper
        //sniperGame = true;
        sniperGame = false;
        itemsEnabled = false;


        modeInvicible = false;

        serieActivated = false;


        level = 0;
        firstChangeLevel=false;
        afficherLevel = false;
        palier = 0;


        // pas de bulles au debut du jeu
        bulles = new Bulle[0][0];

      

        players = new Player[nbPlayers];

        int counter = 1;
        for (int i = 0; i < players.length ; i++) {
            players[i] = new Player(counter);
            counter++;
        }


    }


    /**
     * Initialise un groupe de bulles
     */
    public void groupBulles() {
        bulles = new Bulle[3][5];
        Bulle.groupBulles(bulles, WIDTH, HEIGHT);
    }

    public void newBall(double x, double y) {
        Balle balle = new Balle(x, y, true);
        balles.add(balle);
    }

    public void newBallSniper(double x, double y) {
        Balle balle = new Balle(x, y, false);
        items.add(balle);
    }

    public void newFish(int level) {
        Fish fish = new Fish(level);
        fishes.add(fish);
    }

    public void newSpecialFish(int level) {
        Fish fish;
        Random random = new Random();
        int valeurRandom = random.nextInt(2);
        if (valeurRandom == 0){
            fish = new Crab(level);
            fishes.add(fish);
        } else{
            fish = new Star(level);
            fishes.add(fish);
        }

    }

    public void newBonusFish(int level) {
        Fish fish;
        Random random = new Random();
        int valeurRandom = random.nextInt(3);
        if (valeurRandom == 0){
            fish = new Appat(level);
            fishes.add(fish);
        } else if (valeurRandom == 1){
            fish = new Sailfish(level);
            fishes.add(fish);
        } else {
            fish = new Predator(level);
            fishes.add(fish);
        }



    }




    public void newItem(){
      /*  Heart heart = new Heart();
        items.add(heart);*/

        if(sniperGame){
            Random random = new Random();
            int valeurRandom = random.nextInt(6);
            if(valeurRandom == 0){
                Heart heart = new Heart();
                items.add(heart);
            } else {
                newBallSniper(random.nextDouble()*(Jeu.WIDTH-20), random.nextDouble()*(Jeu.HEIGHT-20));
            }
        } else {
            Heart heart = new Heart();
            items.add(heart);
        }
    }

    public ArrayList<Item> getItem(){
        return this.items;
    }



    /**
     * Met a jour les donnes du jeu
     *
     * @param dt Temps écoulé depuis le dernier update()
     */
    public void update(double dt) {



        // Pour chaque groupe de bulle
        for (Bulle[] value : bulles) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                // mettre a jour la vitesse de la bulle
                Bulle bulle = value[j];
                bulle.update(dt);
            }
        }

        if (balles != null) {
            for (Iterator<Balle> iterator = balles.iterator(); iterator.hasNext(); ) {
                Balle balle = iterator.next();
                balle.update(dt);
                if (balle.getRayon() <= 0) {
                    iterator.remove();
                }
            }
        }


        if ( players[0].getNbVies() == 0 ) {
            saveScore();
            gameOver = true;

        }


        if(itemsEnabled) {
            Random random = new Random();
            if (!sniperGame) {
                int valeurRandom = random.nextInt(1001);
                if (valeurRandom == 0) {
                    newItem();
                }
            } else {
                int valeurRandom = random.nextInt(201);
                if (valeurRandom == 0) {
                    newItem();
                }
            }
        }


        for (Iterator<Item> iterator = items.iterator(); iterator.hasNext(); ) {
            Item item = iterator.next();
            item.update(dt);
            for (Balle balle : balles) {
                item.testCollision(balle);
                if (item.estAttrape() && !item.isUsed()) {
                    if(item instanceof Heart) {
                        if (item.getId().equals("vie bonus")) {
                            if (players[0].getNbVies() < 3) {
                                players[0].setNbVies(players[0].getNbVies() + 1);
                            }
                        } else {
                            if (!players[0].isInvicible()) {
                                players[0].setNbVies(players[0].getNbVies() - 1);
                                players[0].setSerie(0);
                            }
                        }
                    } else {
                        if(sniperGame){
                            if(!players[0].isInvicible()) {
                                players[0].setBalles(players[0].getBalles() + 2);
                            } else {
                                players[0].setBalles(players[0].getBalles() + 1);
                            }
                        }
                    }
                    item.setUsed(true);
                    balle.setAttrape(true);
                    iterator.remove();
                }
            }
            if(item.getLastTimeActivation()){
                iterator.remove();
            }
        }









        if (fishes != null) {
            ArrayList<Fish> poubelle = new ArrayList<>();
            for (Iterator<Fish> iterator = fishes.iterator(); iterator.hasNext(); ) {
                Fish fish = iterator.next();

                fish.update(dt);

                if((fish.getX()<-fish.largeur)||(fish.getX()>Jeu.WIDTH)){
                    iterator.remove();
                    if((fish.isFood())) {
                        if(!players[0].isInvicible()) {
                            players[0].setNbVies(players[0].getNbVies() - 1);
                            players[0].setSerie(0);



                            }
                        }
                    }


                if(!fish.isFood()){
                    if(fish.getY() < -fish.getHauteur()){
                        iterator.remove();
                    }
                }

                if(fish instanceof Predator) {
                    for (Iterator<Fish> iterator2 = fishes.iterator(); iterator2.hasNext(); ) {
                        Fish other = iterator2.next();
                        if(fish != other) {
                            if(!(other instanceof Appat)) {
                                if (((Predator)fish).intersects(other) && other.isLeftOfScreen() != ((Predator)fish).isLeftOfScreen() ) {
                                    poubelle.add(other);
                                    if(!players[0].isInvicible()){
                                        players[0].setNbVies(players[0].getNbVies()-1);
                                    }
                                    players[0].setSerie(0);
                                }
                            }                                             


                        }

                    }
                }






                for (Balle balle : balles) {
                    fish.testCollision(balle);

                    for (Fish f: fishes) {
                        if (f.getX() >= 0 && f.getX() <= Jeu.WIDTH - f.getLargeur()) {
                            if (f.getY() >= 0 && f.getY() <= Jeu.HEIGHT - f.getHauteur()) {
                                if (f instanceof Sailfish) {
                                    if (!((Sailfish) f).isMaxSpeed()) {
                                        f.setVX(2 * f.getVX());
                                        ((Sailfish) f).setMaxSpeed(true);
                                    }
                                }
                            }
                        }
                    }



                    if (fish.estAttrape()) {
                        if((!fish.isFood())) {



                            if(!players[0].isInvicible()) {
                                players[0].setNbVies(players[0].getNbVies() - 1);
                            } else {
                                players[0].setSerie(0);
                            }
                        } else {
                            players[0].setPoints(players[0].getPoints() + 1);
                            players[0].setSerie(players[0].getSerie()+1);
                        }

                          poubelle.add(fish);
                         //iterator.remove();
                         balle.setAttrape(true);

                    }

                }
            }
            fishes.removeAll(poubelle);
        }





        for (Balle balle : balles) {
            if(!balle.aAttrape()){
                players[0].setSerie(0);
            }
        }



        if(players[0].getSerie() != 0) {

            if ( !serieActivated && players[0].getSerie() % 10  == 0 ||players[0].getSerie() % 11  == 0 ||players[0].getSerie() % 12  == 0) {
                players[0].setInvicible(true);
                setSerieActivated(true);
            }

        }








         if(modeSolo) {

             if (players[0].getPoints()  == palier + 5) {
                 firstChangeLevel = false;
           }

             if (players[0].getPoints() % 5 == 0 && !firstChangeLevel) {

                 setStopNewFish(true);
                 afficherLevel=true;

                 firstChangeLevel = true;
                 level +=  1;
                 palier = players[0].getPoints();
             }
                                                
         }


        if(sniperGame){
            if(!players[0].isInvicible()) {
                if (players[0].getBalles() == 0) {
                    saveScore();
                    gameOver = true;
                }
            }
        }




    }


    /**
     * Dessine toutes les formes de l'animation dans le contexte
     * graphique du canvas
     *
     * @param context contexte graphique du canvas
     */
    public void draw(GraphicsContext context) {

        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        // Pour chaque groupe de bulle
        for (Bulle[] value : bulles) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = value[j];
                // dessiner la bulle
                bulle.draw(context);
            }
        }


        if (fishes != null) {
            for (Fish fish : fishes) {
                fish.draw(context);
            }
        }




        if (balles != null) {
            for (Balle balle: balles) {
                balle.draw(context);
            }
        }

        for (Item item: items) {
            item.draw(context);
        }




        // dessine le score
        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(Font.font(25));
        if(!modeInvicible) {
            context.setFill(Color.WHITE);
        } else {
            context.setFill(Color.rgb(126,211,33));
        }
        context.fillText(""+players[0].getPoints(), WIDTH/2.0 + 20, 60);

        if(sniperGame){

            // dessine les balles restantes
            context.setTextAlign(TextAlignment.CENTER);
            context.setFont(Font.font(20));
            context.setFill(Color.WHITE);
            context.fillText("Balles restantes: "+ players[0].getBalles(), 100, 80);
        }

        // dessine les balles restantes
        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(Font.font(20));
        context.setFill(Color.WHITE);
        context.fillText("Série: "+ players[0].getSerie(), 70, 30);





        // dessine les vies restantex
        if (players[0].getNbVies()==3) {
            if(!modeInvicible) {
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0, 80, 30, 30);
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0 + 50, 80, 30, 30);
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            } else {
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0, 80, 30, 30);
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0 + 50, 80, 30, 30);
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            }
        }

        if (players[0].getNbVies()==2) {
            if(!modeInvicible) {
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0, 80, 30, 30);
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            } else {
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0, 80, 30, 30);
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            }
        }

        if (players[0].getNbVies()==1) {
            if(!modeInvicible) {
                context.drawImage(new Image("fish/00.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            } else {
                context.drawImage(new Image("fish/invincible.png"), WIDTH / 2.0 - 50, 80, 30, 30);
            }
        }

       

        


    }

    public int getLevel() {
        return level;
    }


    public boolean getAfficherLevel() {
        return afficherLevel;
    }

    public void setAfficherLevel(boolean afficherLevel) {
        this.afficherLevel = afficherLevel;
    }

    public boolean getStopNewFish() {
        return stopNewFish;
    }

    public void setStopNewFish(boolean stopNewFish) {
        this.stopNewFish = stopNewFish;
    }
}
