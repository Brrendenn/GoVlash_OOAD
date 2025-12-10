package view;

import controller.EmployeeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.User;

import java.sql.Date;

@SuppressWarnings("unused")
public class EmployeePage {

    private Stage stage;
    private User adminUser;
    private EmployeeController controller;
    
    private TableView<User> table;
    private TextField txtUsername, txtEmail;
    private PasswordField txtPass, txtConfirmPass;
    private ComboBox<String> cmbGender, cmbRole;
    private DatePicker dpDob;
    private Label lblStatus;

    public EmployeePage(Stage stage, User adminUser) {
        this.stage = stage;
        this.adminUser = adminUser;
        this.controller = new EmployeeController();
        initialize();
    }

    @SuppressWarnings("unchecked")
	private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Employee Management");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setOnAction(e -> new AdminPage(stage, adminUser)); 
        
        BorderPane topBar = new BorderPane();
        topBar.setLeft(lblTitle);
        topBar.setRight(btnBack);
        root.setTop(topBar);

        table = new TableView<>();

        TableColumn<User, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        
        TableColumn<User, String> colName = new TableColumn<>("Username");
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        TableColumn<User, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<User, String> colGender = new TableColumn<>("Gender");
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<User, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        table.getColumns().addAll(colId, colName, colEmail, colGender, colRole);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        refreshTable(); 
        root.setCenter(table);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 0, 0));

        txtUsername = new TextField();
        txtEmail = new TextField();
        txtPass = new PasswordField();
        txtConfirmPass = new PasswordField();
        
        cmbGender = new ComboBox<>();
        cmbGender.getItems().addAll("Male", "Female");
        
        cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("Admin", "Laundry Staff", "Receptionist");
        
        dpDob = new DatePicker();

        form.add(new Label("Username:"), 0, 0);
        form.add(txtUsername, 1, 0);
        
        form.add(new Label("Email:"), 2, 0);
        form.add(txtEmail, 3, 0);
        
        form.add(new Label("Password:"), 0, 1);
        form.add(txtPass, 1, 1);
        
        form.add(new Label("Confirm Pass:"), 2, 1);
        form.add(txtConfirmPass, 3, 1);
        
        form.add(new Label("Gender:"), 0, 2);
        form.add(cmbGender, 1, 2);
        
        form.add(new Label("Date of Birth:"), 2, 2);
        form.add(dpDob, 3, 2);
        
        form.add(new Label("Role:"), 0, 3);
        form.add(cmbRole, 1, 3);

        Button btnAdd = new Button("Add Employee");
        lblStatus = new Label();

        HBox btnBox = new HBox(10, btnAdd);
        btnBox.setAlignment(Pos.CENTER_LEFT);

        form.add(btnBox, 1, 4);
        form.add(lblStatus, 2, 4, 2, 1);

        root.setBottom(form);

        btnAdd.setOnAction(e -> {
            String result = controller.addEmployee(
                txtUsername.getText(),
                txtEmail.getText(),
                txtPass.getText(),
                txtConfirmPass.getText(),
                cmbGender.getValue(),
                dpDob.getValue(),
                cmbRole.getValue()
            );

            if (result.equals("Success")) {
                lblStatus.setText("Employee Added!");
                lblStatus.setStyle("-fx-text-fill: green;");
                refreshTable();
                clearFields();
            } else {
                lblStatus.setText(result);
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Employee Management");
    }

    private void refreshTable() {
        table.setItems(controller.getAllEmployees());
    }

    private void clearFields() {
        txtUsername.clear();
        txtEmail.clear();
        txtPass.clear();
        txtConfirmPass.clear();
        cmbGender.getSelectionModel().clearSelection();
        cmbRole.getSelectionModel().clearSelection();
        dpDob.setValue(null);
    }
}