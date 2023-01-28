package view;

import controller.Controller;
import controller.RegisterController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Database;

public class RegisterPage extends Application implements EventHandler<ActionEvent>{
	private BorderPane root;
	private GridPane grid;
	private Label headerLabel, title, nameLabel, emailLabel, passwordLabel, roleLabel;
	private TextField nameField, emailField;
	private Spinner<String> roleField;
	private PasswordField passwordField;
	private Button submitButton, loginButton;
	private HBox hbBtn;
	private Scene scene;
	private Database db;
	private Controller controller;
	private Image img;
	private BackgroundImage bgimg;
	private Background bg;
	private RegisterController rc;
	private Integer roleId;
	private ObservableList<String> roleList;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem logout, transaction;
	private VBox vbox;
	
	public void init() {
		roleId = LoginPage.getRoleId();
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		title = new Label("Register");
		grid = new GridPane();
		nameLabel = new Label("Full Name :");
		nameField = new TextField();
    	emailLabel = new Label("Email :");
    	emailField = new TextField();
    	roleLabel = new Label("Role :");
    	roleField = new Spinner<>();
    	passwordLabel = new Label("Password :");
    	passwordField = new PasswordField();
    	submitButton = new Button("Submit");
    	loginButton = new Button("Login");
    	hbBtn = new HBox(10);
    	scene = new Scene(root, 1200, 600);
    	db = Database.getInstance();
    	controller = new Controller();
    	img = new Image("https://img.freepik.com/premium-photo/banner-with-books-stack-border-copy-space-bookstore-ad-white-background_361816-3775.jpg?w=2000", 1200, 500, false, true);
    	bgimg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    	bg = new Background(bgimg);
    	rc = new RegisterController();
    	menuBar = new MenuBar();
    	menu = new Menu("Menu");
		logout = new MenuItem("Log Out");
		transaction = new MenuItem("Transactions");
		
		if(roleId == 4) {
			vbox = new VBox(headerLabel, menuBar);
		}

		roleList = FXCollections.observableArrayList("Customer", "Admin", "Promotion", "Manager");
		SpinnerValueFactory<String> roleValue = new SpinnerValueFactory.ListSpinnerValueFactory<String>(roleList);
    	
		roleValue.setValue("Customer");
    	
    	roleField.setValueFactory(roleValue);
	}
	
	private void initMenu() {
		menuBar.getMenus().add(menu);
		menu.getItems().add(transaction);
		menu.getItems().add(logout);
	}
	
	public void setPosition() {
		if(roleId == 4) {
			root.setTop(vbox);
			headerLabel.prefWidthProperty().bind(root.widthProperty());
	    	headerLabel.setAlignment(Pos.CENTER);
		}
		else {
			root.setTop(headerLabel);
		}
		root.setCenter(title);
    	root.setBottom(grid);
    	root.setBackground(bg);
    	
    	headerLabel.prefWidthProperty().bind(root.widthProperty());
    	headerLabel.setAlignment(Pos.CENTER);
    	
    	grid.setAlignment(Pos.TOP_CENTER);
    	grid.setPrefHeight(350);
    	grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(25, 25, 25, 25));
    	
    	title.setAlignment(Pos.TOP_CENTER);
    	grid.add(nameLabel, 0, 0);
    	grid.add(nameField, 1, 0);
    	grid.add(emailLabel, 0, 1);
    	grid.add(emailField, 1, 1);
    	grid.add(passwordLabel, 0, 2);
    	grid.add(passwordField, 1, 2);
    	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	if(roleId != 4) {
    		hbBtn.getChildren().add(loginButton);
    	}
    	hbBtn.getChildren().add(submitButton);
    	
    	if(roleId == 4) {
    		grid.add(roleLabel, 0, 3);
    		grid.add(roleField, 1, 3);
    		grid.add(hbBtn, 1, 5);
    	}
    	else {
    		grid.add(hbBtn, 1, 4);
    	}
	}
	
	private void events() {
		submitButton.setOnAction(e -> handle(e));
		loginButton.setOnAction(e -> handle(e));
	}
	
	public void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
		title.setStyle("-fx-font-size: 30px;");
	}

	@Override
	public void start(Stage stage) throws Exception {
		init();
		if(roleId == 4) {
			initMenu();
		}
		setPosition();
		setStyle();
		events();

    	stage.setTitle("Register Page");
    	stage.setResizable(false);
    	stage.setScene(scene);
    	stage.show();
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == submitButton) {
			
			int result = rc.validate(emailField.getText(), nameField.getText(), passwordField.getText());
			
			if(result == -1) {
				nameField.setStyle("-fx-border-color: red;");
				rc.showAlert("Invalid Name", "Name should be at least 3 characters long.");
				return;
			}
			else if(result == -2) {
				emailField.setStyle("-fx-border-color: red;");
				rc.showAlert("Invalid Email", "Email format is invalid.");
				return;
			}
			else if(result == -3) {
				passwordField.setStyle("-fx-border-color: red;");
				rc.showAlert("Invalid Password", "Password should be at least 4 characters long.");
				return;
			}
			else {
				rc.addData(nameField.getText(), emailField.getText(), passwordField.getText(), roleField.getValue(), db);
				rc.showAlert("Success", "Your account has been created!");
				rc.clearFields(nameField, emailField, passwordField);
				if(roleId != 4) {
					controller.moveToLoginPage();
					
					Stage currentStage = (Stage) headerLabel.getScene().getWindow();
					currentStage.close();
				}
			}
		}
		if(e.getSource() == loginButton) {
			controller.moveToLoginPage();
			
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
		}
		if(e.getSource() == transaction) {
			controller.moveToManagerPage();
			
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
		}
	}

}
