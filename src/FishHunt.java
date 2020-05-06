import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;


public class FishHunt extends Application {
    // Largeur et hauteur de la fenêtre
    public static final int WIDTH = 640, HEIGHT = 480;

    // Contrôleur de l'application
    private Controleur controleur;

    //  Contexte graphique du canvas
    private GraphicsContext context;

    // Classe anonyme servant à creer des animations
    private AnimationTimer timer;

    // Conteneur générique
    private Pane root;

    // Temps qui s’est écoulé depuis le dernier appel de la fonction handle
    private double deltaTime;

    // nombre de joueurs dans le jeu
    private int nbPlayers = 1; // à changer une fois que le mode multi est implenté

    // Texte du Game Over
    private Text over;

    private Text level;

    private Text invincible;

    private boolean firstTimeLevelActivation = false;

    private boolean gameToHighScore = false;



    private boolean[] firstClics = new boolean[]{false, false, false, false};
    private Stage primaryStage;
    private ArrayList<String> meilleursScores = new ArrayList<>();




    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // icone de la barre de tache
        Image icone = new Image("/crabe.png");


        // racine
        root = new Pane();

        this.primaryStage = primaryStage;

        // titre de la fenetre
        primaryStage.setTitle("Fish Hunt");
        newTimer();
        timer.stop();
        primaryStage.setScene(creerAccueil());
        // fenetre non resizable
        primaryStage.setResizable(false);
        // ajouter l'icone dans la barre des taches
        primaryStage.getIcons().add(icone);

        primaryStage.show();


    }

    private Scene creerAccueil() {

        Pane root = new Pane();

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        //root.getChildren().add(canvas);

        context = canvas.getGraphicsContext2D();
        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        context.drawImage(new Image("/logo.png"), 100, 40, 440, 300);


        Button btnNewGame = new Button("Nouvelle Partie");
        btnNewGame.setLayoutX(280);
        btnNewGame.setLayoutY(350);
        btnNewGame.setPrefWidth(105);

        btnNewGame.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu());
        });

        Button btnBestScores = new Button("Meilleurs scores");
        btnBestScores.setLayoutX(280);
        btnBestScores.setLayoutY(380);
        btnBestScores.setPrefWidth(105);

        btnBestScores.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreScores());
        });

        Button btnModeSpecial = new Button("Mode spécial");
        btnModeSpecial.setLayoutX(280);
        btnModeSpecial.setLayoutY(410);
        btnModeSpecial.setPrefWidth(105);
        btnModeSpecial.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreScores());
        });
        root.getChildren().addAll(canvas, btnNewGame, btnBestScores, btnModeSpecial);


        return new Scene(root);
    }

    private Scene creerFenetreScores() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(50, 50, 50, 50));
        Scene scene = new Scene(mainPane, WIDTH, HEIGHT);

        FileReader fileReader = null;
        try {
            fileReader = new FileReader("src/highScore.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.meilleursScores = getMeilleursScores(fileReader);
        ListView<String> listView = new ListView<>();
        listView.getItems().setAll(getMeilleursScores(fileReader));

        StackPane node = new StackPane();

        node.getChildren().add(listView);


        HBox node2 = new HBox();

        Text titre = new Text("MEILLEURS SCORES");

        titre.setFont(Font.font(32));
        node2.getChildren().add(titre);


        mainPane.setCenter(node);
        mainPane.setTop(node2);
        node2.setAlignment(Pos.CENTER);
        HBox node3 = new HBox();
        Button btn1 = new Button("Menu");
        node3.getChildren().add(btn1);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

          System.out.println(gameToHighScore);

        if(gameToHighScore) {
            if (controleur.checkNewScore(controleur.getScore(), getMeilleursScores(fileReader))) {
                System.out.println("HELLO");
                Label label1 = new Label("Votre nom:");
                Label label2 = new Label("a fait " + controleur.getScore() + " points!");
                Button btn2 = new Button("Ajouter!");
                TextField textField = new TextField();
                btn2.setOnAction((e) -> {
                  
                    if (!(textField.getText().equals(""))) {


                        FileReader filereader = null;
                        try {
                            filereader = new FileReader("src/highScore.txt");
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }

                        this.meilleursScores = getMeilleursScores(filereader);
                        int indexScore = controleur.trierScore(controleur.getScore(), meilleursScores, textField.getText());
                        writeScore(indexScore, meilleursScores);
                    }
                });
                node3.getChildren().addAll(label1, textField, label2, btn2);
                node3.setSpacing(10);
            }
        }


        mainPane.setBottom(node3);
        node3.setAlignment(Pos.CENTER);



        return scene;
    }


    private Scene creerFenetreJeu() {

        this.root = new Pane();


        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        // Contexte graphique du canvas
        context = canvas.getGraphicsContext2D();


        Image img = new Image("/cible.png");
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        root.getChildren().add(imageView);

        startGame();
        timer.start();


        scene.setOnMouseMoved((event) -> {
            double x = event.getX() - 25;
            double y = event.getY() - 25;
            imageView.setX(x);
            imageView.setY(y);

        });

        scene.setOnMouseClicked((event) -> {
            controleur.newBall(event.getX(), event.getY());
            if (controleur.getSniperGame()) {
                if (!controleur.getModeInvicible()) {
                    controleur.setBalles(controleur.getBalles() - 1);
                }
            }
        });


        // Actions sur le clavier en appuyant sur la touche
        scene.setOnKeyPressed((event) -> {

            // Quitter l'application si on appuie sur Echap
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }


            // Restart la partie en appuyant sur R
            if (event.getCode() == KeyCode.R) {
                restart();
            }

            if (event.getCode() == KeyCode.H && !firstClics[0]) {
                controleur.setLevel(controleur.getLevel() + 1);
                controleur.setAfficherLevel(true);
                firstClics[0] = true;

            }

            if (event.getCode() == KeyCode.J && !firstClics[1]) {
                controleur.setScore(controleur.getScore() + 1);
                firstClics[1] = true;
            }

            if (event.getCode() == KeyCode.K && !firstClics[2]) {
                controleur.setLife(controleur.getLife() + 1);
                firstClics[2] = true;
            }


            if (event.getCode() == KeyCode.L && !firstClics[3]) {
                setGameOver(true);
                firstClics[3] = true;
            }


        });


        // Actions sur le clavier en relachant la touche
        // Interdit le spammage de touches
        scene.setOnKeyReleased((event) -> {


            if (event.getCode() == KeyCode.H) {
                firstClics[0] = false;

            }


            if (event.getCode() == KeyCode.J) {
                firstClics[1] = false;
            }


            if (event.getCode() == KeyCode.K) {
                firstClics[2] = false;
            }
            if (event.getCode() == KeyCode.L) {
                firstClics[3] = false;
            }


        });

        return scene;
    }

    /**
     * Reinitialise les valeurs du jeu au debut
     */
    public void startGame() {
        controleur = new Controleur(nbPlayers);
        controleur.draw(context);
    }


    /**
     * Cree un nouveau timer permettant de creer des animations
     */
    public void newTimer() {
        // Création de l'animation
        timer = new AnimationTimer() {


            // Initialiser dernier temps et premier temps
            private long lastTime = 0;
            private long firstTime = 0;
            private long firstTime5Sec = 0;
            private long firstTime10Sec = 0;
            private long firstTime15Sec = 0;
            //private long firstTimeLevel = 0;
            private long firstTimeInvicible = 0;
            private long lastTimeInvicible = 0;
            private long firstTimeGameOver;

            private ArrayList<Long> firstTimeLevels = new ArrayList<Long>();


            // fonction appelée à chaque frame
            @Override
            public void handle(long now) {

                // Si dernier temps = 0, faire apparaitre le groupe de bulles
                if (lastTime == 0) {
                    lastTime = now;
                    firstTime = now;
                    controleur.groupBulles();
                    return;
                }


                // Si 3 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un groupe de bulles et un poisson
                if ((now - firstTime) >= ((long) 3e+9)) {
                    firstTime = now;
                    controleur.groupBulles();
                    controleur.newFish(controleur.getLevel());
                }

                // Si 5 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if (controleur.getLevel() >= 2) {
                    if ((now - firstTime5Sec) >= ((long) 5e+9)) {
                        firstTime5Sec = now;
                        controleur.newSpecialFish(controleur.getLevel());
                    }
                }

                /*// Si 10 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if (controleur.getLevel() >= 3) {
                    if ((now - firstTime10Sec) >= ((long) 10e+9)) {
                        firstTime10Sec = now;
                        controleur.newBadFish(controleur.getLevel());
                    }
                }

                // Si 5 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if (controleur.getLevel() >= 4) {
                    if ((now - firstTime15Sec) >= ((long) 10e+9)) {
                        firstTime15Sec = now;
                        controleur.newFastFish(controleur.getLevel());
                    }
                }
*/

                if (!controleur.getItem().isEmpty()) {
                    for (Item item : controleur.getItem()) {
                        if (!item.getFirstTimeActivation()) {
                            item.setFirstTime(now);
                            item.setFirstTimeActivation(true);
                        }
                        if ((now - item.getFirstTime()) >= ((long) 1.5e+9)) {
                            item.setLastTimeActivation(true);
                        }
                    }
                }


                if (controleur.isInvicible() & !controleur.getModeInvicible()) {
                    firstTimeInvicible = now;
                    controleur.setModeInvicible(true);
                    textDebutInvincible();
                }

                if (firstTimeInvicible > 0) {
                    if ((now - firstTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(invincible);
                    }
                }

                if (lastTimeInvicible > 0) {
                    if ((now - lastTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(invincible);
                        lastTimeInvicible = 0;
                    }
                }


                if (now - firstTimeInvicible >= ((long) 10e+9) && controleur.getModeInvicible()) {
                    controleur.setInvicible(false);
                    controleur.setModeInvicible(false);
                    textFinInvincible();
                    firstTimeInvicible = 0;
                    lastTimeInvicible = now;
                }
                // redemarre une partie si la partie est terminée
                if (getGameOver()) {
                    textOver();
                    firstTimeGameOver = now;
                    setGameOver(false);











                }
                //Si cela fait plus de 3 secondes que la partie est finie, retourner a l'accueil
                if(firstTimeGameOver > 0) {
                    if (now - firstTimeGameOver >= (long) 3e+9) {
                        gameToHighScore = true;
                        timer.stop();
                        deltaTime = 0;
                        firstTimeGameOver = 0;
                        primaryStage.setScene(creerFenetreScores());

                    }
                }


                //players[0].getPoints() % 5 == 0 && firstChangeLevel==false
                if (controleur.getAfficherLevel() & !firstTimeLevelActivation) {
                    firstTimeLevelActivation = true;
                    controleur.setAfficherLevel(false);
                    //firstTimeLevel = now;
                    long firstTimeLevel = now;
                    firstTimeLevels.add(firstTimeLevel);
                    textLevel();
                }

                if (!firstTimeLevels.isEmpty()) {
                    for (Iterator<Long> iterator = firstTimeLevels.iterator(); iterator.hasNext(); ) {
                        long firstTimeLevel = iterator.next();



                /*if ((now - firstTimeLevel) >= ((long)0.5e+9)) {
                    root.getChildren().remove(level);
                    firstTimeLevel = 0;
                }*/

                        if ((now - firstTimeLevel) >= ((long) 0.5e+9)) {
                            root.getChildren().remove(level);
                            iterator.remove();
                            firstTimeLevelActivation = false;

                        }

                    }
                }


                // temps = (temps now - dernier temps) converti en seconde
                deltaTime = (now - lastTime) * 1e-9;


                // mettre a jour les nouvelles positions
                controleur.update(deltaTime);

                // dessiner le nouveau dessin
                controleur.draw(context);


                // mettre a jour le dernier temps
                lastTime = now;
            }
        };
        timer.start();
    }


    /**
     * Permet de restart la partie
     */
    public void restart() {

        timer.stop();
        deltaTime = 0;
        startGame();
        newTimer();

    }

    /**
     * Methode qui renvoit si la partie est terminée
     *
     * @return un boolean
     */
    public boolean getGameOver() {
        return controleur.getGameOver();
    }

    public void setGameOver(boolean gameOver) {
        controleur.setGameOver(gameOver);
    }


    /**
     * Methode qui renvoit si la partie est terminée
     *
     * @return un boolean
     */
    public int getLevel() {
        return controleur.getLevel();
    }


    /**
     * Cree le texte du Game Over et l'ajoute à la racine
     */
    public void textOver() {
        over = new Text("GAME OVER");
        over.setFill(Color.RED);
        over.setFont(Font.font(50));
        over.setTextAlignment(TextAlignment.CENTER);
        over.setX(200);
        over.setY(210);
        root.getChildren().add(over);
    }


    /**
     * Cree le texte du level
     */
    public void textLevel() {
        level = new Text("Level " + getLevel());
        level.setFill(Color.WHITE);
        level.setFont(Font.font(100));
        level.setTextAlignment(TextAlignment.CENTER);
        level.setX(200);
        level.setY(210);
        root.getChildren().add(level);
    }

    /**
     * Cree le texte du level
     */
    public void textDebutInvincible() {
        invincible = new Text("Serie de 10 atteinte:\ndebut invincibilité");
        invincible.setFill(Color.GREEN);
        invincible.setFont(Font.font(25));
        invincible.setTextAlignment(TextAlignment.CENTER);
        invincible.setX(410);
        invincible.setY(50);
        root.getChildren().add(invincible);
    }


    /**
     * Cree le texte du level
     */
    public void textFinInvincible() {
        invincible = new Text("10 sec écoulées:\nfin invincibilité");
        invincible.setFill(Color.RED);
        invincible.setFont(Font.font(25));
        invincible.setTextAlignment(TextAlignment.CENTER);
        invincible.setX(410);
        invincible.setY(50);
        root.getChildren().add(invincible);
    }


    public ArrayList<String> getMeilleursScores(FileReader fileReader)  {
        BufferedReader reader;
        try {
         reader = new BufferedReader(fileReader);
         String ligne;
            this.meilleursScores = new ArrayList<>() ;
        while ((ligne = reader.readLine()) != null) {

            meilleursScores.add(ligne);
        }
        reader.close();
    } catch (IOException ex) {
        System.out.println("Erreur à l’ouverture du fichierr");
    }

     return meilleursScores;
    }




    public void writeScore(int indexNewScore, ArrayList<String> meilleursScores) {
        try {
            FileWriter filewriter = new FileWriter("src/highScore.txt", false);
            BufferedWriter writer = new BufferedWriter(filewriter);
            System.out.println("index = " + indexNewScore);

                for (int i = 0; i < indexNewScore; i++) {
                    System.out.println("hey");
                    writer.append(meilleursScores.get(i) + "\n");
                }
                writer.append(meilleursScores.get(indexNewScore) + "\n");
                if(indexNewScore<meilleursScores.size()-1) {
                for (int i = indexNewScore + 1; i < meilleursScores.size(); i++) {
                    System.out.println("heyy");
                    writer.append(meilleursScores.get(i) + "\n");
                }
                }

            writer.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l’écriture du fichier");
        }
    }
















}
