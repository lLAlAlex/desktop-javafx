package view;

import java.util.ArrayList;

import controller.Controller;
import controller.TransactionController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Transaction;
import util.Database;

public class ManagerPage extends Application implements EventHandler<ActionEvent>{
	private Scene scene;
	private BorderPane root;
	private Label headerLabel, monthLabel, yearLabel;
	private Spinner<Integer> monthField, yearField;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem hire, logout;
	private VBox vbox;
	private ArrayList<Transaction> transactions;
	private TableView<Transaction> transactionList;
	private Database db;
	private GridPane grid;
	private TableColumn<Transaction, Integer> id, method, cardnumber, date, totalprice;
	private TransactionController tc;
	private Button search;
	private Controller controller;

	public void init() {
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		scene = new Scene(root, 1200, 600);
		menuBar = new MenuBar();
		vbox = new VBox(headerLabel, menuBar);
		menu = new Menu("Menu");
		hire = new MenuItem("Hire New Staff");
		logout = new MenuItem("Log Out");
		db = Database.getInstance();
		grid = new GridPane();
		transactions = new ArrayList<>();
		transactionList = new TableView<>();
		monthLabel = new Label("Month :");
		yearLabel = new Label("Year :");
		monthField = new Spinner<>();
		yearField = new Spinner<>();
		tc = new TransactionController();
		logout = new MenuItem();
		id = new TableColumn<>("Transaction ID");
		method = new TableColumn<>("Payment Method");
		cardnumber = new TableColumn<>("Card Number");
		date = new TableColumn<>("Transaction Date");
		totalprice = new TableColumn<>("Total Price");
		search = new Button("Search Transaction");
		controller = new Controller();
		
		SpinnerValueFactory<Integer> monthValue = new IntegerSpinnerValueFactory(1, 12, 1);
		SpinnerValueFactory<Integer> yearValue = new IntegerSpinnerValueFactory(1950, 2050, 2023);
		
		monthField.setValueFactory(monthValue);
		yearField.setValueFactory(yearValue);
	}
	
	private void initMenu() {
		menuBar.getMenus().add(menu);
		menu.getItems().add(hire);
		menu.getItems().add(logout);
	}
	
	private void setPosition() {
		root.setTop(vbox);
		headerLabel.prefWidthProperty().bind(root.widthProperty());
    	headerLabel.setAlignment(Pos.CENTER);
    	root.setCenter(grid);
    	
    	grid.add(monthLabel, 0, 0);
    	grid.add(monthField, 1, 0);
    	grid.add(yearLabel, 2, 0);
    	grid.add(yearField, 3, 0);
    	grid.add(search, 5, 0);
    	grid.add(transactionList, 0, 1);
    	grid.setAlignment(Pos.CENTER);
    	transactionList.setPrefWidth(1000);
    	transactionList.setPrefHeight(200);
    	GridPane.setColumnSpan(transactionList, 6);
    	GridPane.setRowSpan(transactionList, 2);
    	
    	grid.setVgap(20);
    	grid.setHgap(10);
	}
	
	private void setTable() {
		id.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		id.setMinWidth(100);
		
		method.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
		method.setMinWidth(300);
		
		cardnumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
		cardnumber.setMinWidth(200);
		
		date.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		date.setMinWidth(200);
		
		totalprice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
		totalprice.setMinWidth(200);
		
		transactionList.getColumns().setAll(id, method, cardnumber, date, totalprice);
		
		refreshTransaction();
	}
	
	private void refreshTransaction() {
		transactions = tc.getTransaction(db, transactions);
		ObservableList<Transaction> transactionObsList = FXCollections.observableArrayList(transactions);
		transactionList.setItems(transactionObsList);
	}
	
	private void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
	}
	
	private void events() {
		logout.setOnAction(e -> handle(e));
		search.setOnAction(e -> handle(e));
		hire.setOnAction(e -> handle(e));
	}
	
	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == logout) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToLoginPage();
		}
		else if(e.getSource() == search) {
			transactions = tc.getSearchedTransaction(db, transactions, monthField.getValue(), yearField.getValue());
			ObservableList<Transaction> transactionObsList = FXCollections.observableArrayList(transactions);
			transactionList.setItems(transactionObsList);
		}
		else if(e.getSource() == hire) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToRegisterPage();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		init();
		initMenu();
		setPosition();
		setStyle();
		setTable();
		events();
		
		stage.setTitle("Purple Lane Bookstore");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
}
