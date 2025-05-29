package controllers;

import javafx.fxml.FXML;
import utils.UIUtils;

public class TeacherController {
    @FXML
    void  onProfile(){
        UIUtils.openWindow("/main/user_views/updateUser.fxml", controller -> {
            UpdateUserController updateController = (UpdateUserController) controller;
            updateController.initData(LoginController.currentUser);
            updateController.setSpecialityInvisible();
        });
    }
}
