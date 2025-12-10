package view;

import controller.RegisterController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterPage {

    private Stage stage;
    private RegisterController controller;

    private TextField txtUser, txtEmail;
    private PasswordField txtPass, txtConfirm;
    private ComboBox<String> cmbGender;
    private DatePicker dpDob;
    private Label lblStatus;

    public RegisterPage(Stage stage) {
        this.stage = stage;
        this.controller = new RegisterController();
        initialize();
    }

    private void initialize() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Label lblTitle = new Label("Customer Registration");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        txtUser = new TextField();
        txtEmail = new TextField();
        txtPass = new PasswordField();
        txtConfirm = new PasswordField();
        
        cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("Male", "Female");
        
        dpDob = new DatePicker();

        grid.add(new Label("Username:"), 0, 0); grid.add(txtUser, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(txtEmail, 1, 1);
        grid.add(new Label("Password:"), 0, 2); grid.add(txtPass, 1, 2);
        grid.add(new Label("Confirm Pass:"), 0, 3); grid.add(txtConfirm, 1, 3);
        grid.add(new Label("Gender:"), 0, 4); grid.add(cmbGender, 1, 4);
        grid.add(new Label("Date of Birth:"), 0, 5); grid.add(dpDob, 1, 5);

        Button btnRegister = new Button("Register");
        Button btnLogin = new Button("Back to Login");
        lblStatus = new Label();

        btnRegister.setOnAction(e -> {
            String result = controller.registerUser(
                txtUser.getText(), txtEmail.getText(), txtPass.getText(), txtConfirm.getText(),
                cmbGender.getValue(), dpDob.getValue()
            );

            if (result.equals("Success")) {
                new Alert(Alert.AlertType.INFORMATION, "Registration Successful! Please Login.").showAndWait();
                new LoginPage(stage); 
            } else {
                lblStatus.setText(result);
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        btnLogin.setOnAction(e -> new LoginPage(stage));

        root.getChildren().addAll(lblTitle, grid, btnRegister, btnLogin, lblStatus);

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("GoVlash - Register");
    }
}