package  controllers;
import entities.User;
import enums.UserType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.UserService;


public class LoginController {
    @FXML
    TextField email ;

    @FXML
    PasswordField password ;
    @FXML
    Label errorLabel ;
    public static User currentUser = null ;

    @FXML
    protected void onLogInClick(ActionEvent event ) {
        System.out.println(email.getText() );
        System.out.println(password);
        UserService userService = new UserService() ;
        User user = userService.authenticate(email.getText() , password.getText() ) ;

        if(user == null){
            errorLabel.setText("Invalid email or password.");
            errorLabel.setVisible(true);
        }
        else {
            errorLabel.setVisible(false);
            currentUser = user ;
            try {
                FXMLLoader loader = null;
                if(user.getType() == UserType.ADMIN ) {
                    loader = new FXMLLoader(getClass().getResource("/main/admin-home-view.fxml"));
                }else if(user.getType() == UserType.TEACHER ){
                    loader = new FXMLLoader(getClass().getResource("/main/teacher-home-view.fxml"));
                }else if(user.getType() == UserType.STUDENT ){
                 //student-home-view
                    loader = new FXMLLoader(getClass().getResource("/main/student-home-view.fxml"));

                }
                if(loader != null ){
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                }



            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load dashboard.");
                errorLabel.setVisible(true);
            }
        }


    }
}

