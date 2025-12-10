package view;

import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import model.User;

public class LaundryStaffPage {
    private Stage stage;
    private User user;
    private TransactionController controller;
    private TableView<Transaction> table;

    public LaundryStaffPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.controller = new TransactionController();
        initialize();
    }

    @SuppressWarnings("unchecked")
	private void initialize() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label lblTitle = new Label("Laundry Staff - My Tasks");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new LoginPage(stage));
        
        BorderPane top = new BorderPane();
        top.setLeft(lblTitle);
        top.setRight(btnLogout);
        root.setTop(top);

        table = new TableView<>();
        TableColumn<Transaction, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        TableColumn<Transaction, String> colNotes = new TableColumn<>("Notes");
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        TableColumn<Transaction, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(colId, colNotes, colStatus);
        
        table.setItems(controller.getTransactionsByStaff(user.getUserId()));
        root.setCenter(table);

        Button btnFinish = new Button("Mark as Finished");
        btnFinish.setOnAction(e -> {
            Transaction tr = table.getSelectionModel().getSelectedItem();
            if (tr != null) {
                if (tr.getStatus().equals("Finished")) {
                    new Alert(Alert.AlertType.WARNING, "Already Finished!").show();
                    return;
                }
                controller.finishTransaction(tr.getTransactionId());
                table.setItems(controller.getTransactionsByStaff(user.getUserId()));
                new Alert(Alert.AlertType.INFORMATION, "Task Finished!").show();
            }
        });

        VBox bottom = new VBox(btnFinish);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(bottom);

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("GoVlash - Staff");
    }
}