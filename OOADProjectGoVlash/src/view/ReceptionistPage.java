package view;

import controller.EmployeeController;
import controller.TransactionController;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import model.User;

public class ReceptionistPage {
    private Stage stage;
    private User user;
    private TransactionController transController;
    private EmployeeController empController;
    private TableView<Transaction> table;
    private ComboBox<User> cmbStaff;

    public ReceptionistPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.transController = new TransactionController();
        this.empController = new EmployeeController();
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Receptionist Dashboard - Pending Orders");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new LoginPage(stage));
        
        BorderPane top = new BorderPane();
        top.setLeft(lblTitle);
        top.setRight(btnLogout);
        root.setTop(top);

        table = new TableView<>();
        setupTable();
        refreshTable();
        root.setCenter(table);

        VBox bottom = new VBox(10);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        
        Label lblAssign = new Label("Assign Selected Order to:");
        cmbStaff = new ComboBox<>();
        
        ObservableList<User> allStaff = empController.getAllEmployees();
        cmbStaff.setItems(allStaff.filtered(u -> u.getRole().equals("Laundry Staff")));
        
        cmbStaff.setConverter(new javafx.util.StringConverter<User>() {
            public String toString(User u) { return u == null ? "" : u.getUsername(); }
            public User fromString(String string) { return null; }
        });

        Button btnAssign = new Button("Assign Order");
        btnAssign.setOnAction(e -> {
            Transaction tr = table.getSelectionModel().getSelectedItem();
            User staff = cmbStaff.getValue();
            
            if (tr == null || staff == null) {
                new Alert(Alert.AlertType.WARNING, "Select Transaction and Staff!").show();
                return;
            }
            
            transController.assignStaff(tr.getTransactionId(), user.getUserId(), staff.getUserId());
            refreshTable();
            new Alert(Alert.AlertType.INFORMATION, "Order Assigned!").show();
        });

        bottom.getChildren().addAll(lblAssign, cmbStaff, btnAssign);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("GoVlash - Receptionist");
    }

    @SuppressWarnings("unchecked")
	private void setupTable() {
        TableColumn<Transaction, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        TableColumn<Transaction, String> colCust = new TableColumn<>("Cust ID");
        colCust.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        TableColumn<Transaction, Integer> colWeight = new TableColumn<>("Total Weight");  
        colWeight.setCellValueFactory(new PropertyValueFactory<>("totalWeight"));
        TableColumn<Transaction, String> colNotes = new TableColumn<>("Notes");
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        TableColumn<Transaction, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        table.getColumns().addAll(colId, colCust, colWeight, colNotes, colStatus);
    }

    private void refreshTable() {
        table.setItems(transController.getPendingTransactions());
    }
}