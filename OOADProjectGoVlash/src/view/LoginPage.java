package view;

import controller.UserController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;

public class LoginPage {
    
    private Stage stage;
    private UserController userController;

    public LoginPage(Stage stage) {
        this.stage = stage;
        this.userController = new UserController();
        initialize();
    }

    private void initialize() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label lblTitle = new Label("GoVlash Laundry Login");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        
        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();

        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register as Customer");
        
        Label lblMessage = new Label(); 

        grid.add(lblTitle, 0, 0, 2, 1);
        grid.add(lblUsername, 0, 1);
        grid.add(txtUsername, 1, 1);
        grid.add(lblPassword, 0, 2);
        grid.add(txtPassword, 1, 2);
        
        grid.add(btnLogin, 1, 3);
        grid.add(btnRegister, 1, 4); 
        grid.add(lblMessage, 1, 5);

        btnLogin.setOnAction(e -> {
            String user = txtUsername.getText();
            String pass = txtPassword.getText();

            if(user.isEmpty() || pass.isEmpty()) {
                lblMessage.setText("Fields cannot be empty!");
                lblMessage.setStyle("-fx-text-fill: red;");
                return;
            }

            User loggedInUser = userController.login(user, pass);

            if (loggedInUser != null) {
                String role = loggedInUser.getRole();
                
                if (role.equalsIgnoreCase("Admin")) {
                    new AdminPage(stage, loggedInUser);
                } else if (role.equalsIgnoreCase("Receptionist")) {
                    new ReceptionistPage(stage, loggedInUser);
                } else if (role.equalsIgnoreCase("Laundry Staff")) {
                    new LaundryStaffPage(stage, loggedInUser);
                } else {
                    new CustomerPage(stage, loggedInUser);
                }
            } else {
                lblMessage.setText("Invalid Credentials");
                lblMessage.setStyle("-fx-text-fill: red;");
            }
        });

        btnRegister.setOnAction(e -> {
            new RegisterPage(stage);
        });

        Scene scene = new Scene(grid, 400, 350);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Login");
        stage.show();
    }
}