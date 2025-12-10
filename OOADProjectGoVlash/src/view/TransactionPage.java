package view;

import controller.NotificationController;
import controller.TransactionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Transaction;
import model.User;

import java.util.stream.Collectors;

public class TransactionPage {

    private Stage stage;
    private User adminUser;

    private TransactionController transController;
    private NotificationController notifController;

    private TableView<Transaction> table;
    private ToggleButton btnToggleFilter; 

    public TransactionPage(Stage stage, User adminUser) {
        this.stage = stage;
        this.adminUser = adminUser;
        this.transController = new TransactionController();
        this.notifController = new NotificationController();
        initialize();
    }

    @SuppressWarnings("unchecked")
	private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Transaction Management");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setOnAction(e -> new AdminPage(stage, adminUser));
        
        BorderPane topBar = new BorderPane();
        topBar.setLeft(lblTitle);
        topBar.setRight(btnBack);
        root.setTop(topBar);

        table = new TableView<>();
        
        TableColumn<Transaction, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        
        TableColumn<Transaction, Integer> colCust = new TableColumn<>("Cust ID");
        colCust.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Transaction, String> colService = new TableColumn<>("Service");
        colService.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        
        TableColumn<Transaction, String> colRecep = new TableColumn<>("Receptionist");
        colRecep.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getReceptionistId();
            return new SimpleStringProperty(id == null || id == 0 ? "-" : String.valueOf(id));
        });

        TableColumn<Transaction, String> colStaff = new TableColumn<>("Staff");
        colStaff.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getStaffId();
            return new SimpleStringProperty(id == null || id == 0 ? "-" : String.valueOf(id));
        });

        TableColumn<Transaction, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colStatus.setCellFactory(column -> new TableCell<Transaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("Finished")) {
                        setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: orange;");
                    }
                }
            }
        });

        table.getColumns().addAll(colId, colCust, colService, colRecep, colStaff, colStatus);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        refreshTable(false);
        root.setCenter(table);

        HBox bottomBox = new HBox(15);
        bottomBox.setAlignment(Pos.CENTER_LEFT);
        bottomBox.setPadding(new Insets(15, 0, 0, 0));

        btnToggleFilter = new ToggleButton("Show Finished Transactions Only");
        btnToggleFilter.setOnAction(e -> {
            refreshTable(btnToggleFilter.isSelected());
        });

        Button btnNotify = new Button("Notify Customer");
        btnNotify.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnNotify.setOnAction(e -> {
            Transaction selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "Select a transaction first!");
                return;
            }

            if (!"Finished".equalsIgnoreCase(selected.getStatus())) {
                showAlert(Alert.AlertType.ERROR, "You can only notify customers for Finished transactions!");
                return;
            }

            notifController.createNotification(selected.getCustomerId());
            showAlert(Alert.AlertType.INFORMATION, "Notification sent to Customer ID: " + selected.getCustomerId());
        });

        bottomBox.getChildren().addAll(btnToggleFilter, new Separator(), btnNotify);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("GoVlash - Transaction Management");
    }

    private void refreshTable(boolean showFinishedOnly) {
        ObservableList<Transaction> allData = transController.getAllTransactions();
        
        if (showFinishedOnly) {
            ObservableList<Transaction> filtered = FXCollections.observableArrayList(
                allData.stream()
                       .filter(t -> "Finished".equalsIgnoreCase(t.getStatus()))
                       .collect(Collectors.toList())
            );
            table.setItems(filtered);
        } else {
            table.setItems(allData);
        }

        if (table.getItems().isEmpty()) {
            table.setPlaceholder(new Label("No transactions found."));
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.show();
    }
}