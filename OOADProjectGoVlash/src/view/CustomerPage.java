package view;

import controller.NotificationController;
import controller.ServiceController;
import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Notification;
import model.Service;
import model.User;

public class CustomerPage {

    private Stage stage;
    private User customer;
    
    private NotificationController notifController;
    private ServiceController serviceController;
    private TransactionController transController;
    
    private TableView<Notification> notifTable;
    private TableView<Service> serviceTable;

    public CustomerPage(Stage stage, User customer) {
        this.stage = stage;
        this.customer = customer;
        this.notifController = new NotificationController();
        this.serviceController = new ServiceController();
        this.transController = new TransactionController();
        initialize();
    }

    private void initialize() {
        BorderPane root = new BorderPane();

        Label lblTitle = new Label("Welcome, " + customer.getUsername());
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new LoginPage(stage));
        
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(10));
        header.setLeft(lblTitle);
        header.setRight(btnLogout);
        root.setTop(header);

        TabPane tabPane = new TabPane();

        Tab serviceTab = new Tab("Services", createServiceView());
        serviceTab.setClosable(false);

        Tab notifTab = new Tab("Notifications", createNotificationView());
        notifTab.setClosable(false);
        
        tabPane.getTabs().addAll(serviceTab, notifTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.setTitle("GoVlash - Customer Dashboard");
    }

    @SuppressWarnings("unchecked")
	private VBox createServiceView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label lbl = new Label("Available Laundry Services");
        
        serviceTable = new TableView<>();
        TableColumn<Service, String> colName = new TableColumn<>("Service Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        
        TableColumn<Service, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        
        TableColumn<Service, Integer> colDur = new TableColumn<>("Duration (Days)");
        colDur.setCellValueFactory(new PropertyValueFactory<>("serviceDuration"));
        
        serviceTable.getColumns().addAll(colName, colPrice, colDur);
        serviceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        serviceTable.setItems(serviceController.getAllServices());
        
        TextField txtWeight = new TextField();
        txtWeight.setPromptText("Total Weight (Kg)");

        TextArea txtNotes = new TextArea();
        txtNotes.setPromptText("Notes (e.g., Separate whites)");
        txtNotes.setPrefHeight(60);

        Button btnOrder = new Button("Order Service");
        btnOrder.setOnAction(e -> {
            Service selected = serviceTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a service!").show();
                return;
            }
            
            String res = transController.createTransaction(
                customer.getUserId(), 
                selected.getServiceId(), 
                txtWeight.getText(), 
                txtNotes.getText()
            );

            if (res.equals("Success")) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, res).show();
            }
        });

        box.getChildren().addAll(lbl, serviceTable, txtWeight, txtNotes, btnOrder);
        return box;
    }

    @SuppressWarnings("unchecked")
	private VBox createNotificationView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        
        notifTable = new TableView<>();
        TableColumn<Notification, String> colMsg = new TableColumn<>("Message");
        colMsg.setCellValueFactory(new PropertyValueFactory<>("message"));
        TableColumn<Notification, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        notifTable.getColumns().addAll(colMsg, colStatus);
        notifTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshNotifTable();

        Button btnView = new Button("View Details");
        btnView.setOnAction(e -> {
            Notification n = notifTable.getSelectionModel().getSelectedItem();
            if (n != null) {
                if(!n.isRead()) {
                    notifController.markAsRead(n.getNotificationId());
                    refreshNotifTable();
                }
                new Alert(Alert.AlertType.INFORMATION, n.getMessage()).show();
            }
        });
        
        box.getChildren().addAll(new Label("My Notifications"), notifTable, btnView);
        return box;
    }

    private void refreshNotifTable() {
        notifTable.setItems(notifController.getNotificationsForUser(customer.getUserId()));
    }
}