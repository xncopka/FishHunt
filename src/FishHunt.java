import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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

    private boolean [] firstClics = new boolean[]{false, false, false, false, false};

    private boolean gameToScore = false;

    private boolean prevGameSpecial;

    private Stage primaryStage;
    private ArrayList<String> meilleursScores ;
    private ArrayList<String> meilleursScoresSpecial;





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
        root = new Pane();
        this.primaryStage = primaryStage;

        // titre de la fenetre
        primaryStage.setTitle("Fish Hunt");

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


        Button btn1 = new Button("Nouvelle Partie");
        btn1.setLayoutX(280);
        btn1.setLayoutY(350);
        btn1.setPrefWidth(105);

        btn1.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(false));
        });

        Button btn2 = new Button("Meilleurs scores");
        btn2.setLayoutX(280);
        btn2.setLayoutY(380);
        btn2.setPrefWidth(105);

        btn2.setOnAction((e) -> {
            primaryStage.setScene(creerSceneScores());
        });

        Button btn3 = new Button("Mode spécial");
        btn3.setLayoutX(280);
        btn3.setLayoutY(410);
        btn3.setPrefWidth(105);
        btn3.setOnAction((e) -> {
            primaryStage.setScene(creerFenetreJeu(true));
        });
        root.getChildren().addAll(canvas, btn1, btn2, btn3);

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

        StackPane node = new StackPane();
        node.setPadding(new Insets(10, 0, 10, 0));

        node.getChildren().add(conteneurListView);



        HBox node2 = new HBox();

        Text titre = new Text("Meilleurs scores");

        titre.setFont(Font.font(32));
        node2.getChildren().add(titre);


        mainPane.setCenter(node);
        mainPane.setTop(node2);
        node2.setAlignment(Pos.TOP_CENTER);

        Insets insets = new Insets(20);
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


                        FileReader filereader = null;
                        try {
                            if(!prevGameSpecial) {
                                System.out.println("mode normal");
                                filereader = new FileReader("src/highScore.txt");
                            } else {
                                System.out.println("mode special");
                                filereader = new FileReader("src/highScore2.txt");
                            }
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }

                        if(!prevGameSpecial) {
                            System.out.println("mode normal");
                            this.meilleursScores = getMeilleursScores(filereader);
                            int indexScore = controleur.trierScore(controleur.getScore(), meilleursScores, textField.getText());
                            writeScore(indexScore, meilleursScores, "src/highScore.txt");
                        } else {
                            System.out.println("mode special");
                            this.meilleursScoresSpecial = getMeilleursScores(filereader);
                            int indexScore = controleur.trierScore(controleur.getScore(), meilleursScoresSpecial, textField.getText());
                            writeScore(indexScore, meilleursScoresSpecial, "src/highScore2.txt");
                        }
                    }
                    primaryStage.setScene(creerAccueil());

                });
                hboxtemp.getChildren().addAll(label1, textField, label2, btn2);
                hboxtemp.setSpacing(10);
                hboxtemp.setAlignment(Pos.CENTER);
                node3.getChildren().addAll(hboxtemp);
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

        // Debut du jeu

        startGame(modeSpecial);
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
           

        });
        return scene;
    }



    


    /**
     *  Reinitialise les valeurs du jeu au debut
     */
    public void startGame() {
        controleur = new Controleur(nbPlayers, modeSpecial);
        controleur.draw(context);
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
            private long firstTimeGameOver;

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
                        root.getChildren().remove(invincible);
                    }
                }

                if (lastTimeInvicible>0) {
                    if ((now - lastTimeInvicible) >= ((long) 3e+9)) {
                        root.getChildren().remove(invincible);
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
                if (getGameOver()) {

                    if(controleur.getSniperGame()) {
                        prevGameSpecial = true;
                    } else {
                        prevGameSpecial = false;
                    }
                    textOver();
                    firstTimeGameOver = now;
                    setGameOver(false);



                }


                //Si cela fait plus de 3 secondes que la partie est finie, retourner a l'accueil
                if(firstTimeGameOver > 0) {
                    if (now - firstTimeGameOver >= (long) 3e+9) {
                        timer.stop();
                        deltaTime = 0;
                        firstTimeGameOver = 0;
                        gameToScore = true;
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
        startGame();
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
    public void textLevel(){
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
   public void textDebutInvincible(){
        invincible = new Text("Serie de "+ controleur.getSerie() +" atteinte:\ndebut invincibilité");
        invincible.setFill(Color.rgb(126,211,33));
        invincible.setFont(Font.font(25));
        invincible.setTextAlignment(TextAlignment.CENTER);
        invincible.setX(410);
        invincible.setY(50);
        root.getChildren().add(invincible);
    }


    /**
     * Cree le texte du level
     */
    public void textFinInvincible(){
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
        ArrayList<String> arrayList = new ArrayList<>() ;
        try {
            System.out.println("PLEASE");
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
