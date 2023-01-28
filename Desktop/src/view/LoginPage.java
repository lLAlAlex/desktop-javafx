package view;

import java.util.ArrayList;

import controller.Controller;
import controller.LoginController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javafx.stage.Stage;
import model.User;
import util.Database;

public class LoginPage extends Application implements EventHandler<ActionEvent>{
	private BorderPane root;
	private GridPane grid;
	private Label headerLabel, title, emailLabel, passwordLabel;
	private TextField emailField;
	private PasswordField passwordField;
	private Button registerButton, loginButton;
	private HBox hbBtn;
	private Scene scene;
	private Database db;
	private Controller controller;
	private ArrayList<User> users;
	private Image img;
	private BackgroundImage bgimg;
	private Background bg;
	private LoginController lc;
	private static Integer roleid = 0;
	private static Integer userid = 0;
	
	public static Integer getRoleId() {
		return roleid;
	}
	
	public static Integer getUserId() {
		return userid;
	}
	
	public void init() {
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		title = new Label("Login");
		grid = new GridPane();
    	emailLabel = new Label("Email :");
    	emailField = new TextField();
    	passwordLabel = new Label("Password :");
    	passwordField = new PasswordField();
    	registerButton = new Button("Register");
    	loginButton = new Button("Login");
    	lc = new LoginController();
    	hbBtn = new HBox(10);
    	scene = new Scene(root, 1200, 600);
    	db = Database.getInstance();
    	controller = new Controller();
    	users = new ArrayList<>();
    	img = new Image("https://img.freepik.com/premium-photo/banner-with-books-stack-border-copy-space-bookstore-ad-white-background_361816-3775.jpg?w=2000", 1200, 500, false, true);
    	bgimg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
    	bg = new Background(bgimg);
	}

	public void setPosition() {
		root.setTop(headerLabel);
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
		grid.add(emailLabel, 0, 0);
		grid.add(emailField, 1, 0);
		grid.add(passwordLabel, 0, 1);
		grid.add(passwordField, 1, 1);
		
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(registerButton);
		hbBtn.getChildren().add(loginButton);
		grid.add(hbBtn, 1, 4);
	}

	private void events() {
		registerButton.setOnAction(e -> handle(e));
		loginButton.setOnAction(e -> handle(e));
	}
	
	public void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
		title.setStyle("-fx-font-size: 30px;");
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		init();
		setPosition();
		setStyle();
		events();
	
		stage.setTitle("Register Page");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
	private void clearFields() {
		emailField.clear();
		passwordField.clear();
	}
	
	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == loginButton) {
			lc.getUsers(db, users);
			boolean validate = false;
			
			for(User user : users) {
				if(user.getEmail().equals(emailField.getText()) && user.getPassword().equals(passwordField.getText())) {
					validate = true;
					
					roleid = user.getRoleid();
					userid = user.getId();
					
					lc.showAlert("Information", "Login Success");
					clearFields();
					
					if(user.getRoleid() == 1) {
						controller.moveToHomePage();
					}
					else if(user.getRoleid() == 2) {
						controller.moveToAdminPage();
					}
					else if(user.getRoleid() == 3) {
						controller.moveToPromoPage();
					}
					else if(user.getRoleid() == 4) {
						controller.moveToManagerPage();
					}
					Stage currentStage = (Stage) headerLabel.getScene().getWindow();
					currentStage.close();
					
					return;
				}
			}
			if(validate == false) {
				lc.showAlert("Login Error", "User does not exist");
			}
		}
		else if(e.getSource() == registerButton) {
			clearFields();
			controller.moveToRegisterPage();
			
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
		}
	}

}
