import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXProgressBar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;




public class MediuInvatareController{

    @FXML private JFXButton sendAnswerButton;
    @FXML private JFXButton exitButton;
    @FXML private JFXButton jumpAnswerButton;
    @FXML private JFXProgressBar progressBar;
    @FXML private Label questionLabel;
    @FXML private JFXCheckBox answerA;
    @FXML private JFXCheckBox answerB;
    @FXML private JFXCheckBox answerC;
    @FXML private ImageView imageView;

    private Integer idQuiz ;

    private boolean isGenerated = false;
    private ArrayList<Integer> questionsList = new ArrayList<Integer>();
    private Queue<Integer> questionsQueue = new LinkedList<>();

    //Daca isGenerated = false , coada nu este generata, o generam si o vom serializa. corespunzator userului
    public void generateQueueForLearningMode(){

        if(!isGenerated) {
            DBConnect connect = new DBConnect();

            //Adaugam in colectia questionsList elemtentele de la 1 la nuarul de intrebari
            for (int i = 1; i <= connect.getCountFromSQL("questions"); ++i) {
                questionsList.add(i);
            }

            //Amestecam numerele pentru a genera o coada cu id-uri random
            Collections.shuffle(questionsList);

            //Copiem id-urile din colectia questionsList in coada
            for (int i = 0; i < questionsList.size(); ++i) {
                questionsQueue.add(questionsList.get(i));
            }
            isGenerated = true;
        }
    }


    public void start(Stage stage)throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/MediuInvatare.fxml"));
        Scene homeScene = new Scene(home, 1000, 600);
        stage.setScene(homeScene);
        stage.show();
    }

    @FXML private void handleButtonAction(ActionEvent event) throws IOException ,InterruptedException{
        if(event.getSource() == exitButton){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirma");
            alert.setHeaderText("Chestionare Auto categoria B");
            alert.setContentText("Esti sigur ca vrei sa iesi din mediul de invatare?");

            alert.showAndWait().ifPresent(response -> {
                if(response == ButtonType.OK){
                    try {
                        Stage stage = (Stage) exitButton.getScene().getWindow();
                        stage.setTitle("Chestionare Auto categoria B");
                        HomeController home = new HomeController();
                        home.start(stage);
                    }catch (IOException ex){ ex.printStackTrace(); }
                }
                else{
                    alert.close();
                }
            });
        }
    }

    @FXML private void initialize() throws InterruptedException {
        DBConnect connect = new DBConnect();
        Connection conn = null;
        try{
           /* conn = DBConnect.getConnection();
            conn.setAutoCommit(false);
            long objectID = DBConnect.writeJavaObject(conn,qflm);
            conn.commit();
            System.out.println("Serialized objectID => " + objectID);*/


        }catch (Exception ex){
            ex.printStackTrace();
        }



        /* Setam progressBarul prin raportul dintre atributul progresMediu caracteristic fiecarui account din baza de date si numarul total de intrebari
        Atunci cand progresMediu = numaru total de intrebari * 2 atunci progressBar se va seta cu 1 si o sa fie complet mediul de invatare. */

        progressBar.setProgress(Double.parseDouble(connect.getInfoFromColumn("progresMediu", LoginController.idAccount_Current)) / (connect.getCountFromSQL("questions") * 2));
        answerA.setSelected(false);
        answerB.setSelected(false);
        answerC.setSelected(false);
        imageView.setImage(null);



    }

}