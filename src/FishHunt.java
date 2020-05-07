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
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Classe qui sert à définir ce qui doit être affiché (Vue)
 */
public class FishHunt extends Application {

    // Largeur et hauteur de la fenêtre
    public static final int WIDTH = 640, HEIGHT = 480;

    // Contrôleur de l'application
    private Controleur controleur;

    //  Contexte graphique du canvas
    private GraphicsContext context;

    // Classe anonyme servant à creer des animations
    private AnimationTimer timer;

    private StackPane root;


    // Temps qui s’est écoulé depuis le dernier appel de la fonction handle
    private double deltaTime;

    // nombre de joueurs dans le jeu
    private int nbPlayers = 1; // à changer une fois que le mode multi est implenté

    // Texte du Game Over
    private Text over;

    private Text level;

    private boolean firstTimeLevelActivation = false;

    private boolean [] firstClics = new boolean[]{false, false, false, false, false, false};

    private boolean gameToScore = false;

    private boolean prevGameSpecial;

    private Stage primaryStage;
    private ArrayList<String> meilleursScores ;
    private ArrayList<String> meilleursScoresSpecial;

    private MusicGame backgroundMusic;

    private HBox debutInvinc;
    private HBox finInvinc;

    private boolean printErreur;

    private boolean speakerOn;






    /** Méthode main de HighSeaTower
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Méthode start qui est redéfinie et sert à construire l’interface
     * @param primaryStage représente la fenetre de l'application
     * @throws Exception // FXMLLoader.load(...) throws an IOException
     */
    @Override
    public void start(Stage primaryStage) throws Exception {


        // icone de la barre de tache
        Image icone = new Image("/shark-jaws.png");


        // racine
     
        this.primaryStage = primaryStage;

        this.speakerOn = true;

        // titre de la fenetre
        primaryStage.setTitle("Fish Hunt");

        primaryStage.setScene(creerAccueil());

        String filepath = "src/Aqua Road - Shining Sea.mp3";
        backgroundMusic = new MusicGame();
        backgroundMusic.playMusic(filepath);






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


        Button btn1 = new Button("Nouvelle Partie");
        btn1.setLayoutX(280);
        btn1.setLayoutY(350);
        btn1.setPrefWidth(105);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(false));
        });

        Button btn2 = new Button("Meilleurs Scores");
        btn2.setLayoutX(280);
        btn2.setLayoutY(380);
        btn2.setPrefWidth(105);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(creerSceneScores());
        });

        Button btn3 = new Button("Mode Spécial");
        btn3.setLayoutX(280);
        btn3.setLayoutY(410);
        btn3.setPrefWidth(105);
        btn3.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(true));
        });

        Image question = new Image(getClass().getResourceAsStream("question2.png"));
        Button btn4 = new Button();
        btn4.setGraphic(new ImageView(question));
        btn4.setLayoutX(600);
        btn4.setLayoutY(10);

        btn4.setOnAction((e) -> {
            primaryStage.setScene( instructionsJeu());
        });

        Button btn5 = new Button("Crédits");
        btn5.setLayoutX(280);
        btn5.setLayoutY(440);
        btn5.setPrefWidth(105);
        btn5.setOnAction((e) -> {
            primaryStage.setScene(creerCredits());
        });

        root.getChildren().addAll(canvas, btn1, btn2, btn3, btn4, btn5);

        speaker(root);



        gameToScore = false;
        return new Scene(root);
    }

    private Scene creerSceneScores() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(25, 50, 50, 50));
        Scene scene = new Scene(mainPane, WIDTH, HEIGHT);

        FileReader fileReader = null;
        FileReader fileReader2 = null;
        try {
            fileReader = new FileReader("src/highScore.txt");
            fileReader2 = new FileReader("src/highScore2.txt");

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }


        HBox conteneurListView = new HBox();
        conteneurListView.setSpacing(25);
        conteneurListView.setAlignment(Pos.CENTER);

        VBox listAndTitre = new VBox();
        Text titreListView = new Text("Mode Normal");


        meilleursScores=getMeilleursScores(fileReader) ;
        ListView<String> listView = new ListView<>();
        listView.getItems().setAll(meilleursScores);
        listAndTitre.setAlignment(Pos.CENTER);
        listAndTitre.setSpacing(2);

        listAndTitre.getChildren().setAll(titreListView, listView);


        VBox listAndTitre2 = new VBox();
        Text titreListView2 = new Text("Mode Spécial");



        meilleursScoresSpecial=getMeilleursScores(fileReader2) ;
        ListView<String> listView2 = new ListView<>();
        listView2.getItems().setAll(meilleursScoresSpecial);
        listAndTitre2.setAlignment(Pos.CENTER);
        listAndTitre2.setSpacing(2);


        listAndTitre2.getChildren().setAll(titreListView2, listView2);


        conteneurListView.getChildren().addAll(listAndTitre, listAndTitre2);

        //StackPane node = new StackPane();
        conteneurListView.setPadding(new Insets(10, 0, 10, 0));

        //node.getChildren().add(conteneurListView);



        HBox node2 = new HBox();

        Text titre = new Text("Meilleurs Scores");

        titre.setFont(Font.font(32));
        node2.getChildren().add(titre);


        mainPane.setCenter(conteneurListView);
        mainPane.setTop(node2);
        node2.setAlignment(Pos.TOP_CENTER);

        Insets insets = new Insets(15);
        BorderPane.setMargin(node2, insets);


        VBox node3 = new VBox();
        Button btn1 = new Button("Menu");



        if(gameToScore) {

            ArrayList<String> checkArrayList;
            if (!prevGameSpecial) {
                checkArrayList = meilleursScores;
            } else {
                checkArrayList = meilleursScoresSpecial;
            }
            if (controleur.checkNewScore(controleur.getScore(), checkArrayList)) {
                HBox hboxtemp = new HBox();
                Label label1 = new Label("Votre nom:");
                Label label2 = new Label("a fait " + controleur.getScore() + " points!");
                Button btn2 = new Button("Ajouter!");
                TextField textField = new TextField();

                btn2.setOnAction((e) -> {
                            if (!(textField.getText().equals(""))) {

                                if (textField.getText().contains(" - ")) {
                                    printErreur = true;
                                    primaryStage.setScene(creerSceneScores());

                                } else {


                                FileReader filereader = null;
                                try {
                                    if (!prevGameSpecial) {

                                        filereader = new FileReader("src/highScore.txt");
                                    } else {

                                        filereader = new FileReader("src/highScore2.txt");
                                    }
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }

                                if (!prevGameSpecial) {

                                    this.meilleursScores = getMeilleursScores(filereader);
                                    int indexScore = controleur.trierScore(controleur.getScore(), meilleursScores, textField.getText());
                                    writeScore(indexScore, meilleursScores, "src/highScore.txt");
                                } else {

                                    this.meilleursScoresSpecial = getMeilleursScores(filereader);
                                    int indexScore = controleur.trierScore(controleur.getScore(), meilleursScoresSpecial, textField.getText());
                                    writeScore(indexScore, meilleursScoresSpecial, "src/highScore2.txt");
                                }
                                    primaryStage.setScene(creerAccueil());
                            }
                        }


                });

            /*    if(printErreur == true) {
                    Label erreur = new Label("Votre nom ne doit pas contenir la séquence \" - \". Veillez réessayer s'il vous plait.");

                }
*/

                hboxtemp.getChildren().addAll(label1, textField, label2, btn2);
                hboxtemp.setSpacing(10);
                hboxtemp.setAlignment(Pos.CENTER);
                node3.getChildren().addAll(hboxtemp);

                    if(printErreur == true) {
                    Label erreur = new Label("Votre nom ne doit pas contenir le séparateur \" - \". Veillez réessayer s’il vous plaît.");
                    erreur.setTextFill(Color.RED);
                    node3.getChildren().add(erreur);

                }

            }
        }

        node3.getChildren().add(btn1);
        node3.setSpacing(10);
        mainPane.setBottom(node3);
        node3.setAlignment(Pos.CENTER);


        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        return scene;
    }

    private Scene creerFenetreJeu(boolean modeSpecial) {

        backgroundMusic.stopMusic();

         root = new StackPane();


        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        // Contexte graphique du canvas
        context = canvas.getGraphicsContext2D();


        Pane pane = new Pane();
        Image img = new Image("/cible.png");
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        pane.getChildren().add(imageView);
        root.getChildren().add(pane);

        // Debut du jeu

        startGame(modeSpecial, speakerOn);
        newTimer();



  


        scene.setOnMouseMoved((event) -> {
            double x = event.getX() - 25;
            double y = event.getY() - 25;
            imageView.setX(x);
            imageView.setY(y);
            
        });

        scene.setOnMouseClicked((event) -> {
            controleur.newBall(event.getX(), event.getY());
            if(controleur.getSniperGame()) {
                if(!controleur.getModeInvicible()){
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
                controleur.setLevel(controleur.getLevel()+1);
                controleur.setAfficherLevel(true);
                firstClics[0]=true;

            }

            if (event.getCode() == KeyCode.J && !firstClics[1]) {
                controleur.setScore(controleur.getScore()+1);
                firstClics[1]=true;
            }

            if (event.getCode() == KeyCode.K && !firstClics[2]) {
                controleur.setLife(controleur.getLife()+1);
                firstClics[2]=true;
            }


            if (event.getCode() == KeyCode.L && !firstClics[3]) {
                setGameOver(true);
                firstClics[3]=true;
            }

            if (event.getCode() == KeyCode.S && !firstClics[4]) {
                controleur.setSerie(controleur.getSerie()+1);
                firstClics[4]=true;
            }

            if (event.getCode() == KeyCode.O && !firstClics[5]) {
                speakerOn = !speakerOn;
                controleur.enableChanson(speakerOn);
                firstClics[5]=true;
            }




        });


        // Actions sur le clavier en relachant la touche
        // Interdit le spammage de touches
        scene.setOnKeyReleased((event) -> {



            if (event.getCode() == KeyCode.H) {
                firstClics[0]=false;  

            }


            if (event.getCode() == KeyCode.J) {
                firstClics[1]=false;
            }


            if(event.getCode() == KeyCode.K) {
                firstClics[2]=false;
            }
            if (event.getCode() == KeyCode.L){
                firstClics[3]=false;
            }

            if (event.getCode() == KeyCode.S){
                firstClics[4]=false;
            }

            if (event.getCode() == KeyCode.O){
                firstClics[5]=false;
            }
           

        });
        return scene;
    }


    private Scene instructionsJeu() {
        Pane root = new Pane();

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);


        context = canvas.getGraphicsContext2D();
        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        HBox node = new HBox();
        Label label1 = new Label("Vous incarnez un requin qui chasse des poissons " +
                "pour\n"+ "son souper. Étant un requin gourmand, vous ne pouvez\n" +
                "pas vous permettre de laisser trop de poissons " +
                "passer\nAu bout de 3 poissons ratés, la " +
                "partie est perdue." +
                " Le jeu \nse contrôle à la souris et continue à " +
                "l’infini, ou jusqu’à \nce que 3 poissons aient été " +
                "ratés." + "\n\nAppuyer sur la touche H pour faire monter le niveau de\n+1, "
                +"J pour faire monter le score de +1, K pour faire\nmonter le nombre " +
                "de poissons restants de +1 et L pour\nfaire perdre la partie.");
        label1.setFont(Font.font ("Verdana", 18));
        label1.setTextFill(Color.WHITE);
        label1.setTextAlignment(TextAlignment.CENTER);
        node.setLayoutX(50);
        node.setLayoutY(80);
        node.setAlignment(Pos.CENTER);
        node.getChildren().add(label1);

        Image next = new Image(getClass().getResourceAsStream("next.png"));
        Button btn1 = new Button();
        btn1.setGraphic(new ImageView(next));
        btn1.setLayoutX(390);
        btn1.setLayoutY(350);
        btn1.setPrefHeight(20);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(instructions2Jeu());
        });

        Button btn2 = new Button("Menu");
        btn2.setLayoutX(280);
        btn2.setLayoutY(350);
        btn2.setPrefWidth(105);
        btn2.setPrefHeight(20);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        root.getChildren().addAll(canvas, node, btn1, btn2);

        return new Scene(root);
    }

    private Scene instructions2Jeu() {
        Pane root = new Pane();

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);


        context = canvas.getGraphicsContext2D();
        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        HBox node = new HBox();
        Label label2 = new Label(
                "Les appats sont à éviter, ils vous feront perdre 2 vies. " +
                        "\n Ne Laissez pas le requin manger les poissons... \nVous " +
                        "perdrez une vie pour chaque poisson qu'il mange!!"+
                        "\n\n Si vous vous sentez courageux, vous pouvez faire le mode\n spécial."+
                        " Mais attention! Vous aurez un nombre limité de \nballes!" +
                        " Vous pourrez recharger votre quantité de balle en \ncliquant"+
                        " sur les balles noires dans le jeu!" );
        label2.setFont(Font.font ("Verdana", 18));
        label2.setTextFill(Color.WHITE);
        label2.setTextAlignment(TextAlignment.CENTER);
        node.setLayoutX(50);
        node.setLayoutY(80);
        node.setAlignment(Pos.CENTER);
        node.getChildren().add(label2);


        Button btn1 = new Button("Menu");
        btn1.setLayoutX(280);
        btn1.setLayoutY(350);
        btn1.setPrefWidth(105);
        btn1.setPrefHeight(20);
        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        Image next = new Image(getClass().getResourceAsStream("next.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(next));
        btn2.setLayoutX(390);
        btn2.setLayoutY(350);
        btn2.setPrefHeight(20);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(instructions3Jeu());
        });


        Image previous = new Image(getClass().getResourceAsStream("back.png"));
        Button btn3 = new Button();
        btn3.setGraphic(new ImageView(previous));
        btn3.setLayoutX(245);
        btn3.setLayoutY(350);
        btn3.setPrefHeight(20);
        btn3.setOnAction((e) -> {
            primaryStage.setScene( instructionsJeu());
        });
        root.getChildren().addAll(canvas, node, btn1, btn2, btn3);

        return new Scene(root);
    }

    private Scene instructions3Jeu() {
        Pane root = new Pane();

        // Fenêtre de jeu
        Canvas canvas = new Canvas(WIDTH, HEIGHT);


        context = canvas.getGraphicsContext2D();
        // Background bleu du jeu
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);
        HBox node = new HBox();
        Label label2 = new Label(
                "Allo" );
        label2.setFont(Font.font ("Verdana", 18));
        label2.setTextFill(Color.WHITE);
        label2.setTextAlignment(TextAlignment.CENTER);
        node.setLayoutX(50);
        node.setLayoutY(80);
        node.setAlignment(Pos.CENTER);
        node.getChildren().add(label2);

        Button btn1 = new Button("Menu");
        btn1.setLayoutX(280);
        btn1.setLayoutY(350);
        btn1.setPrefWidth(105);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });

        Image previous = new Image(getClass().getResourceAsStream("back.png"));
        Button btn2 = new Button();
        btn2.setGraphic(new ImageView(previous));
        btn2.setLayoutX(245);
        btn2.setLayoutY(350);
        btn2.setPrefHeight(20);
        btn2.setOnAction((e) -> {
            primaryStage.setScene( instructionsJeu());
        });

        root.getChildren().addAll(canvas, node, btn1, btn2);

        return new Scene(root);
    }


    private Scene creerCredits() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        root.setStyle("-fx-background-color: #00008b;");
        root.setPadding(new Insets(50));



        Label titre = new Label("Crédits");
        titre.setTextFill(Color.WHITE);
        titre.setTextAlignment(TextAlignment.CENTER);
        titre.setFont(Font.font ("Verdana", 40));
        root.setTop(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(50));
        vbox.setSpacing(2);


        Label realisation =  new Label("Réalisation :");
        realisation.setFont(Font.font ("Verdana", 18));
        realisation.setTextFill(Color.WHITE);
        realisation.setTextAlignment(TextAlignment.CENTER);
        realisation.setAlignment(Pos.CENTER);


        Label credits = new Label("Thomas Bui\nVanda Lovejoy");
        credits.setFont(Font.font ("Verdana", 14));
        credits.setTextFill(Color.WHITE);
        credits.setTextAlignment(TextAlignment.LEFT);
        credits.setAlignment(Pos.CENTER);

        HBox separateur = new HBox();
        separateur.setPrefHeight(14);

        Label musique = new Label("Musique :");
        musique.setFont(Font.font ("Verdana", 18));
        musique.setTextFill(Color.WHITE);
        musique.setTextAlignment(TextAlignment.CENTER);
        musique.setAlignment(Pos.CENTER);

        Label chansons = new Label("Background : Aqua Road - Shining Sea\nJeu : Noisestorm - Crab Rave\n");
        chansons.setFont(Font.font ("Verdana", 14));
        chansons.setTextFill(Color.WHITE);
        chansons.setTextAlignment(TextAlignment.LEFT);
        chansons.setAlignment(Pos.CENTER);

        HBox separateur2 = new HBox();
        separateur2.setPrefHeight(14);


        Label image = new Label("Image :");
        image.setFont(Font.font ("Verdana", 18));
        image.setTextFill(Color.WHITE);
        image.setTextAlignment(TextAlignment.CENTER);
        image.setAlignment(Pos.CENTER);

        Label site = new Label("Game-icon.net");
        site.setFont(Font.font ("Verdana", 14));
        site.setTextFill(Color.WHITE);
        site.setTextAlignment(TextAlignment.LEFT);
        site.setAlignment(Pos.CENTER);


        vbox.getChildren().addAll(realisation, credits, separateur, musique, chansons, separateur2, image, site);
        
        root.setCenter(vbox);

        Button btn1 = new Button("Menu");
        btn1.setPrefWidth(105);
        btn1.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(btn1, Pos.CENTER);


        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerAccueil());
        });


        root.setBottom(btn1);

        return scene;
    }






    /**
     *  Reinitialise les valeurs du jeu au debut
     */
    public void startGame(boolean modeSpecial, boolean speakerOn) {
        controleur = new Controleur(nbPlayers, modeSpecial, speakerOn);
        controleur.draw(context);
        printErreur = false;
    }



    /**
     *  Cree un nouveau timer permettant de creer des animations
     */
    public void newTimer() {
        // Création de l'animation
        timer = new AnimationTimer() {


            // Initialiser dernier temps et premier temps
            private long lastTime = 0;
            private long firstTime = 0;
            private long firstTime5Sec = 0;
            private long firstTime10Sec = 0;
            private long firstTimeInvicible = 0;
            private long lastTimeInvicible = 0;
            private long firstTimeNewFish = 0;
            private long firstTimeGameOver = 0;

            private ArrayList<Long> firstTimeLevels = new ArrayList<>();




            // fonction appelée à chaque frame
            @Override
            public void handle(long now) {

                // Si dernier temps = 0, faire apparaitre le groupe de bulles
                if (lastTime == 0) {
                    lastTime = now;
                    firstTime = now;
                    firstTimeNewFish= now;
                    controleur.groupBulles();
                    return;
                }



                // Si 3 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un groupe de bulles et un poisson
                if ((now - firstTime) >= ((long)3e+9)) {

                    firstTime = now;
                    controleur.groupBulles();
                    

                }

                if(((now - firstTimeNewFish) >= ((long)3e+9)) && (!controleur.getStopNewFish())) {
                    firstTimeNewFish = now;
                    controleur.newFish(controleur.getLevel());
                    controleur.enableItems();
                }

                // Si 5 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if(controleur.getLevel()>=2 && !controleur.getStopNewFish()) {
                    if ((now - firstTime5Sec) >= ((long) 5e+9)) {
                        firstTime5Sec = now;
                        controleur.newSpecialFish(controleur.getLevel());
                    }
                }

                if(controleur.getLevel()>=3 && controleur.getStopNewFish()) {
                    firstTime10Sec = now;
                }

                // Si 10 secondes se sont écoulés depuis le debut de l'animation,
                // faire apparaitre un poisson spécial
                if(controleur.getLevel()>=3 && !controleur.getStopNewFish()) {
                    if ((now - firstTime10Sec) >= ((long) 2.5e+9)) {
                        firstTime10Sec = now;
                        controleur.newBonusFish(controleur.getLevel());
                    }
                }




                if(!controleur.getItem().isEmpty()){
                    for (Item item: controleur.getItem()) {
                        if(!item.getFirstTimeActivation()) {
                            item.setFirstTime(now);
                            item.setFirstTimeActivation(true);
                        }
                        if ((now - item.getFirstTime()) >= ((long) 1.5e+9)) {
                            item.setLastTimeActivation(true);
                        }                                                      
                    }
                }


                if(controleur.isInvicible() & !controleur.getModeInvicible()){
                    firstTimeInvicible = now;
                    controleur.setModeInvicible(true);
                    textDebutInvincible();
                }

                if(firstTimeInvicible>0) {
                    if ((now - firstTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(debutInvinc);
                    }
                }

                if (lastTimeInvicible>0) {
                    if ((now - lastTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(finInvinc);
                        lastTimeInvicible = 0;
                        controleur.setSerieActivated(false);
                    }
                }



                if(now-firstTimeInvicible >= ((long) 10e+9) && controleur.getModeInvicible()) {
                    controleur.setInvicible(false);
                    controleur.setModeInvicible(false);
                    textFinInvincible();
                    firstTimeInvicible = 0;
                    lastTimeInvicible = now;
                }

             




                // redemarre une partie si la partie est terminée
                if (getGameOver() && !gameToScore) {



                    if(controleur.getSniperGame()) {
                        prevGameSpecial = true;
                    } else {
                        prevGameSpecial = false;
                    }
                    textOver();
                    firstTimeGameOver = now;
                    gameToScore = true;



                }


                //Si cela fait plus de 3 secondes que la partie est finie, retourner a l'accueil
                if(firstTimeGameOver > 0) {
                    if (now - firstTimeGameOver >= (long) 3e+9) {
                        timer.stop();
                        deltaTime = 0;
                        firstTimeGameOver = 0;
                        if(speakerOn) {
                            controleur.getChanson().stopMusic();
                            String filepath = "src/Aqua Road - Shining Sea.mp3";
                            backgroundMusic = new MusicGame();
                            backgroundMusic.playMusic(filepath);
                        }
                        primaryStage.setScene(creerSceneScores());

                    }
                }

                //players[0].getPoints() % 5 == 0 && firstChangeLevel==false
                if (controleur.getAfficherLevel() & !firstTimeLevelActivation){
                    firstTimeLevelActivation = true;
                    controleur.setAfficherLevel(false);
                    //firstTimeLevel = now;
                    long firstTimeLevel = now;
                    firstTimeLevels.add(firstTimeLevel);
                    textLevel();
                }

                if(!firstTimeLevels.isEmpty()){
                for (Iterator<Long> iterator = firstTimeLevels.iterator(); iterator.hasNext(); ) {
                    long firstTimeLevel = iterator.next();



                /*if ((now - firstTimeLevel) >= ((long)0.5e+9)) {
                    root.getChildren().remove(level);
                    firstTimeLevel = 0;
                }*/

                    if ((now - firstTimeLevel) >= ((long)3e+9)) {
                        root.getChildren().remove(level);
                        iterator.remove();
                        firstTimeLevelActivation = false;
                        controleur.setStopNewFish(false);
                        firstTimeNewFish=0;
               
                        
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
     *  Permet de restart la partie
     */
    public void restart(){

        timer.stop();
        deltaTime =0;
        startGame(controleur.getSniperGame(), speakerOn);
        newTimer();

    }

    /**
     * Methode qui renvoit si la partie est terminée
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
     * @return un boolean
     */
    public int getLevel() {
        return controleur.getLevel();
    }






    /**
     * Cree le texte du Game Over et l'ajoute à la racine
     */
    public void textOver(){
        root.getChildren().remove(level);
        over = new Text("GAME OVER");
        over.setFill(Color.RED);
        over.setFont(Font.font(50));
        over.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(over);
    }




    /**
     * Cree le texte du level
     */
    public void textLevel(){
        level = new Text("Level " + getLevel());
        level.setFill(Color.WHITE);
        level.setFont(Font.font(50));
        level.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().add(level);
    }

    /**
     * Cree le texte du level
     */
   public void textDebutInvincible(){
       Text invincible = new Text("Serie de "+ controleur.getSerie() +" atteinte :\ndebut invincibilité");
        invincible.setFill(Color.rgb(126,211,33));
        invincible.setFont(Font.font(20));
        invincible.setTextAlignment(TextAlignment.CENTER);
         debutInvinc = new HBox(invincible);
       debutInvinc.setAlignment(Pos.TOP_RIGHT);
       debutInvinc.setPadding(new Insets(10));
        root.getChildren().add(debutInvinc);
    }


    /**
     * Cree le texte du level
     */
    public void textFinInvincible(){
        Text invincible = new Text("10 sec écoulées :\nfin invincibilité");
        invincible.setFill(Color.RED);
        invincible.setFont(Font.font(20));
        invincible.setTextAlignment(TextAlignment.CENTER);;
         finInvinc = new HBox(invincible);
        finInvinc.setAlignment(Pos.TOP_RIGHT);
        finInvinc.setPadding(new Insets(10));
        root.getChildren().add(finInvinc);
    }


    public ArrayList<String> getMeilleursScores(FileReader fileReader)  {
        BufferedReader reader;
        ArrayList<String> arrayList = new ArrayList<>() ;
        try {
            reader = new BufferedReader(fileReader);
            String ligne;
            while ((ligne = reader.readLine()) != null) {

                arrayList.add(ligne);
            }
            reader.close();

        } catch (IOException ex) {
            System.out.println("Erreur à l’ouverture du fichier");
        }

        return arrayList;
    }



    public void writeScore(int indexNewScore, ArrayList<String> meilleursScores, String adresse) {
        try {
            FileWriter filewriter = new FileWriter(adresse, false);
            BufferedWriter writer = new BufferedWriter(filewriter);
     

            for (int i = 0; i < indexNewScore; i++) {
                writer.append(meilleursScores.get(i) + "\n");
            }
            writer.append(meilleursScores.get(indexNewScore) + "\n");
            if(indexNewScore<meilleursScores.size()-1) {
                for (int i = indexNewScore + 1; i < meilleursScores.size(); i++) {
                    writer.append(meilleursScores.get(i) + "\n");
                }
            }

            writer.close();
        } catch (IOException ex) {
            System.out.println("Erreur à l’écriture du fichier");
        }
    }

    public void speaker(Pane pane) {
        Image img;
        if(speakerOn) {
            img = new Image(getClass().getResourceAsStream("speaker-on.png"));
        } else {
            img = new Image(getClass().getResourceAsStream("speaker-off.png"));
        }

        Button speaker = new Button();
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);

        speaker.setGraphic(imageView);
        speaker.setLayoutX(10);
        speaker.setLayoutY(10);
        pane.getChildren().add(speaker);

        speaker.setOnAction((e) -> {
            if(speakerOn) {
                backgroundMusic.stopMusic();
                speakerOn = false;
                primaryStage.setScene(creerAccueil());
            } else {
                String filepath = "src/Aqua Road - Shining Sea.mp3";
                backgroundMusic = new MusicGame();
                backgroundMusic.playMusic(filepath);
                speakerOn = true;
                primaryStage.setScene(creerAccueil());
            }
        });


    }






}
