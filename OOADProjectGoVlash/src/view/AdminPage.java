package view;

import controller.ServiceController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Service;
import model.User;

public class AdminPage {

    private Stage stage;
    private User adminUser; 
    private ServiceController controller;
    
    private TableView<Service> table;
    private TextField txtId, txtName, txtDesc, txtPrice, txtDuration;
    private Label lblStatus;

    public AdminPage(Stage stage, User adminUser) {
        this.stage = stage;
        this.adminUser = adminUser;
        this.controller = new ServiceController();
        initialize();
    }

	@SuppressWarnings("unchecked")
	private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Service Management (Admin: " + adminUser.getUsername() + ")");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new LoginPage(stage)); 
        
        Button btnManageEmployees = new Button("Manage Employees");
        btnManageEmployees.setOnAction(e -> new EmployeePage(stage, adminUser));
        
        Button btnManageTransactions = new Button("Manage Transactions");
        btnManageTransactions.setOnAction(e -> new TransactionPage(stage, adminUser));
        
        HBox topButtons = new HBox(10, btnManageTransactions, btnManageEmployees, btnLogout);
        BorderPane topBar = new BorderPane();
        topBar.setLeft(lblTitle);
        topBar.setRight(topButtons);
        root.setTop(topBar);

        table = new TableView<>();
        
        TableColumn<Service, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        
        TableColumn<Service, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        
        TableColumn<Service, String> colDesc = new TableColumn<>("Description");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("serviceDesc"));
        
        TableColumn<Service, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        
        TableColumn<Service, Integer> colDuration = new TableColumn<>("Duration (Days)");
        colDuration.setCellValueFactory(new PropertyValueFactory<>("serviceDuration"));

        table.getColumns().addAll(colId, colName, colDesc, colPrice, colDuration);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        
        refreshTable();
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtId.setText(newVal.getServiceId());
                txtName.setText(newVal.getServiceName());
                txtDesc.setText(newVal.getServiceDesc());
                txtPrice.setText(String.valueOf(newVal.getServicePrice()));
                txtDuration.setText(String.valueOf(newVal.getServiceDuration()));
                txtId.setDisable(true); // ID cannot be changed on update
            }
        });

        root.setCenter(table);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10, 0, 0, 0));

        txtId = new TextField(); 
        txtName = new TextField();
        txtDesc = new TextField();
        txtPrice = new TextField();
        txtDuration = new TextField();

        form.add(new Label("Service ID:"), 0, 0);
        form.add(txtId, 1, 0);
        form.add(new Label("Name:"), 2, 0);
        form.add(txtName, 3, 0);
        
        form.add(new Label("Description:"), 0, 1);
        form.add(txtDesc, 1, 1);
        form.add(new Label("Price:"), 2, 1);
        form.add(txtPrice, 3, 1);
        
        form.add(new Label("Duration:"), 0, 2);
        form.add(txtDuration, 1, 2);

        // Buttons
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnClear = new Button("Clear Form");

        lblStatus = new Label();
        
        HBox buttonBox = new HBox(10, btnAdd, btnUpdate, btnDelete, btnClear);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        
        form.add(buttonBox, 0, 3, 4, 1);
        form.add(lblStatus, 0, 4, 4, 1);

        root.setBottom(form);


        btnAdd.setOnAction(e -> {
            String result = controller.addService(txtId.getText(), txtName.getText(), txtDesc.getText(), txtPrice.getText(), txtDuration.getText());
            handleResult(result);
        });

        btnUpdate.setOnAction(e -> {
            String result = controller.updateService(txtId.getText(), txtName.getText(), txtDesc.getText(), txtPrice.getText(), txtDuration.getText());
            handleResult(result);
        });

        btnDelete.setOnAction(e -> {
            Service selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblStatus.setText("Select a service to delete first!");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }
            String result = controller.deleteService(selected.getServiceId());
            handleResult(result);
        });

        btnClear.setOnAction(e -> clearFields());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("GoVlash Laundry - Admin Dashboard");
    }

    private void handleResult(String result) {
        if (result.equals("Success")) {
            lblStatus.setText("Operation Successful!");
            lblStatus.setStyle("-fx-text-fill: green;");
            refreshTable();
            clearFields();
        } else {
            lblStatus.setText(result);
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void refreshTable() {
        table.setItems(controller.getAllServices());
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtDesc.clear();
        txtPrice.clear();
        txtDuration.clear();
        txtId.setDisable(false); 
        table.getSelectionModel().clearSelection();
    }
}