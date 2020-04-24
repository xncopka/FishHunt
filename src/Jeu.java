import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import java.util.ArrayList;
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

    // quel niveau on est dans le jeu
    private boolean[] levels = new boolean[3];

    // si la partie est terminée
    private boolean gameOver;


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
     * Represente le niveau de difficulté du jeu
     * @return un nombre
     */
    public int getLevelId() {
        if (levels[0]) {
            return 1;
        } else if (levels[1]) {
            return 2;
        } else {
            return 3; // BONUS ajout de poissons a ne pas attraper
        }
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



        levels[0] = true;

        // pas de bulles au debut du jeu
        bulles = new Bulle[0][0];

        balles = null;
        fishes = null;

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
            for (Balle balle : balles) {
                balle.update(dt);
            }
        }

        if (fishes != null) {
            for (Fish fish : fishes) {
                fish.update(dt);
                for (Balle balle : balles) {
                    fish.testCollision(balle);
                    if (fish.estAttrape()) {
                        fishes.remove(fish);
                    }
                }
            }
        }


        // A partir du level 3, les piranhas apparaissent
        if (levels[2]) {
            Appat appat = new Appat(getLevelId());
            fishes.add(appat);
        }







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
             if (players[0].getPoints() < 6) {
                 levels[0] = true;
             } else if (players[0].getPoints() < 20) {
                 levels[0] = false;
                 levels[1] = true;
             } else {
                 levels[1] = false;
                 levels[2] = true;
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


    }
}
