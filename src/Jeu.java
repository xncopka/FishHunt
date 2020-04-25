import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
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
    private  ArrayList<Balle> balles = new ArrayList<Balle>();
    private ArrayList<Fish> fishes = new ArrayList<Fish>();

    // joueurs dans le jeu
    private Player[] players;



    // niveau du jeu
    private int level;
    private boolean firstChangeLevel;

    // si la partie est terminée
    private boolean gameOver;


    private int palier;

    private boolean afficherLevel;




    private boolean modeSolo;   // Si oui on est en mode solo sinon on est en mode multi


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

  




    /**
     * Savoir si le jeu est terminée
     * @return un boolean
     */
    public boolean getGameOver() {
        return this.gameOver;
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





    /**
     * Constructeur de Jeu
     */
    public Jeu(int nbPlayers) {

        if(nbPlayers == 1){
            modeSolo = true;
        } else {
            modeSolo = false;
        }



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
        Balle balle = new Balle(x, y);
        balles.add(balle);
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

    public void newBadFish(int level) {

           Fish fish = new Appat(level);
            fishes.add(fish);
    }

    public void newFastFish(int level) {
        Fish fish = new Sailfish(level);
        fishes.add(fish);
    }



    /**
     * Met a jour les donnes du jeu
     *
     * @param dt Temps écoulé depuis le dernier update()
     */
    public void update(double dt) {

        // Pour chaque groupe de bulle
        for (int i = 0; i < bulles.length; i++) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                // mettre a jour la vitesse de la bulle
                Bulle bulle = bulles[i][j];
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
            gameOver = true;
        }

   



        if (fishes != null) {
            for (Iterator<Fish> iterator = fishes.iterator(); iterator.hasNext(); ) {
                Fish fish = iterator.next();
                fish.update(dt);

                if((fish.getX()<-fish.largeur)||(fish.getX()>Jeu.WIDTH)){
                    iterator.remove();
                    if(!(fish instanceof Appat)) {
                        players[0].setNbVies(players[0].getNbVies() - 1);
                    }
                }

                for (Balle balle : balles) {
                    fish.testCollision(balle);
                    if (fish.estAttrape()) {
                        if(fish instanceof Appat) {
                            gameOver=true;
                        } else {
                            players[0].setPoints(players[0].getPoints() + 1);
                            iterator.remove();
                        }
                    }
                }
            }
        }


    /* // A partir du level 3, les poissons servant d'appât apparaissent
        if (level == 3) {
            Appat appat = new Appat(level);
            fishes.add(appat);
        }
*/






          /* Mode Solo:
                Level 1 : 0-4 poissons capturés
                Level 2 : 5-19 poissons capturés
                Level 3 : 20+ poissons capturés

             Mode Multijoueur:
                Level 1: temps au depart
                Level 2: 2min < temps < 5min
                Level 3: 5min et +

           */
         if(modeSolo) {

             if (players[0].getPoints()  == palier + 5) {
                 firstChangeLevel = false;
           }

             if (players[0].getPoints() % 5 == 0 && firstChangeLevel==false) {

                 
                 afficherLevel=true;

                 firstChangeLevel = true;
                 level +=  1;
                 palier = players[0].getPoints();
             }
                                                
         } else {
             // TODO MODE MULTI
         }


        /**
         * /** BONUS
         *  * Apres avoir manger 10 poissons à la suite sans perdre une seule vie, le requin prend confiance et
         *  * sans perdre une vie et plus vifs dans ses prochaines attaques. Le super- pouvoir disparait apres qu'il
         *  * ait perdu une vie
         *  */




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
        for (int i = 0; i < bulles.length; i++) {
            // Pour chaque bulles dans un groupe
            for (int j = 0; j < bulles[0].length; j++) {
                Bulle bulle = bulles[i][j];
                // dessiner la bulle
                bulle.draw(context);
            }
        }


        if (fishes != null) {
            for (Fish f : fishes) {
                f.draw(context);
            }
        }

        if (balles != null) {
            for (Balle balle: balles) {
                balle.draw(context);
            }
        }


        // dessine le score
        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(Font.font(25));
        context.setFill(Color.WHITE);
        context.fillText(""+players[0].getPoints(), WIDTH/2 + 20, 60);

        // dessine les vies restantex
        if (players[0].getNbVies()==3) {
            context.drawImage(new Image("fish/00.png"), WIDTH / 2 , 80, 30, 30);
            context.drawImage(new Image("fish/00.png"), WIDTH / 2 + 50, 80, 30, 30);
            context.drawImage(new Image("fish/00.png"), WIDTH / 2 - 50, 80, 30, 30);
        }

        if (players[0].getNbVies()==2) {
            context.drawImage(new Image("fish/00.png"), WIDTH / 2, 80, 30, 30);
            context.drawImage(new Image("fish/00.png"), WIDTH / 2 - 50, 80, 30, 30);
        }

        if (players[0].getNbVies()==1) {
            context.drawImage(new Image("fish/00.png"), WIDTH / 2 - 50, 80, 30, 30);
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
}
