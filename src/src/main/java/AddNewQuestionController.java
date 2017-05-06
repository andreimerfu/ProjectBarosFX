import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import insidefx.undecorator.Undecorator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class AddNewQuestionController {

    @FXML private JFXTextField questionField;
    @FXML private JFXTextField answerA;
    @FXML private JFXTextField answerB;
    @FXML private JFXTextField answerC;
    @FXML private JFXTextField correctAnswer;

    @FXML private JFXButton browseButton;
    @FXML private JFXButton saveButton ;
    @FXML private JFXButton resetButton ;
    @FXML private JFXButton backButton ;


    @FXML private ImageView imageView;

    private Stage stageQues;

    final FileChooser fileChooser = new FileChooser();
    FileInputStream fin;
    File file;


    void start(Stage stage)throws IOException {
        Parent home = FXMLLoader.load(getClass().getResource("/AddNewQuestion.fxml"));
        Undecorator undecorator = new Undecorator(stage,(Region)home);
        undecorator.getStylesheets().add("skin/undecorator.css");
        Scene homeScene = new Scene(undecorator,630,470);
        homeScene.setFill(Color.TRANSPARENT);
        stage.setScene(homeScene);
        stage.show();
        stageQues = stage;
    }

    @FXML private void handleButtonAction(ActionEvent event) throws IOException,InterruptedException {
        if (event.getSource() == backButton) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            AdministrationController administration = new AdministrationController();
            administration.start(stage);
        }

        else if (event.getSource() == resetButton){
            questionField.setText("");
            answerA.setText("");
            answerB.setText("");
            answerC.setText("");
            correctAnswer.setText("");
            imageView.setImage(null);
            file.delete();
            fin.close();

        }
        else if(event.getSource() == saveButton){
            if(!questionField.getText().trim().isEmpty() && !answerA.getText().trim().isEmpty() &&
                    !answerB.getText().trim().isEmpty() && !answerC.getText().trim().isEmpty() &&
                    !correctAnswer.getText().trim().isEmpty() && imageView.getImage() != null){
                DBConnect connect = new DBConnect();
                connect.insertQuestion(questionField.getText(),answerA.getText(),answerB.getText(),answerC.getText(),
                correctAnswer.getText(),fin,file);
            }
        }
        else if(event.getSource() == browseButton){
            try{

                file = fileChooser.showOpenDialog(stageQues);

                if(file.isFile() && (file.getName().contains(".jpg")) || file.getName().contains(".png") ||
                        file.getName().contains(".bmp") || file.getName().contains(".JPG")){

                    final Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);

                    fin = new FileInputStream(file);

                }



            }catch(NullPointerException ex){
                System.out.println("Formatul imaginii nu este corespunzator !");
            }

        }
    }
}
