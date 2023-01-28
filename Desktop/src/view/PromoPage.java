package view;

import java.util.ArrayList;

import controller.Controller;
import controller.PromoController;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Promo;
import util.Database;

public class PromoPage extends Application implements EventHandler<ActionEvent>{
	private Scene scene;
	private BorderPane root;
	private Label headerLabel, idLabel, codeLabel, discLabel, noteLabel;
	private TextField idField, codeField, discField, noteField;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem logout;
	private VBox vbox;
	private ArrayList<Promo> promos;
	private TableView<Promo> promoList;
	private Database db;
	private GridPane grid;
	private TableColumn<Promo, Integer> id, code, discount, note;
	private PromoController pc;
	private Button insert, update, delete;
	private Integer oldId;
	private Controller controller;
	
	public void init() {
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		scene = new Scene(root, 1200, 600);
		menuBar = new MenuBar();
		vbox = new VBox(headerLabel, menuBar);
		menu = new Menu("Menu");
		logout = new MenuItem("Log Out");
		db = Database.getInstance();
		grid = new GridPane();
		promos = new ArrayList<>();
		promoList = new TableView<>();
		idLabel = new Label("Promo ID :");
		codeLabel = new Label("Promo Code :");
		discLabel = new Label("Promo Discount :");
		noteLabel = new Label("Promo Note :");
		idField = new TextField();
		codeField = new TextField();
		discField = new TextField();
		noteField = new TextField();
		pc = new PromoController();
		logout = new MenuItem();
		id = new TableColumn<>("Promo ID");
		code = new TableColumn<>("Promo Code");
		discount = new TableColumn<>("Promo Discount");
		note = new TableColumn<>("Promo Note");
		insert = new Button("Insert");
		update = new Button("Update");
		delete = new Button("Delete");
		oldId = null;
		controller = new Controller();
	}
	
	private void initMenu() {
		menuBar.getMenus().add(menu);
		menu.getItems().add(logout);
	}
	
	private void setPosition() {
		root.setTop(vbox);
		headerLabel.prefWidthProperty().bind(root.widthProperty());
    	headerLabel.setAlignment(Pos.CENTER);
    	root.setCenter(grid);
    	
    	grid.add(idLabel, 0, 0);
    	grid.add(idField, 1, 0);
    	grid.add(codeLabel, 2, 0);
    	grid.add(codeField, 3, 0);
    	grid.add(discLabel, 4, 0);
    	grid.add(discField, 5, 0);
    	grid.add(noteLabel, 6, 0);
    	grid.add(noteField, 7, 0);
    	grid.add(promoList, 0, 1);
    	grid.add(insert, 0, 3);
    	grid.add(update, 1, 3);
    	grid.add(delete, 2, 3);
    	grid.setAlignment(Pos.CENTER);
    	promoList.setPrefWidth(1000);
    	promoList.setPrefHeight(200);
    	GridPane.setColumnSpan(promoList, 8);
    	GridPane.setRowSpan(promoList, 2);
    	
    	grid.setVgap(20);
    	grid.setHgap(10);
	}
	
	private void setTable() {
		id.setCellValueFactory(new PropertyValueFactory<>("promoId"));
		id.setMinWidth(100);
		
		code.setCellValueFactory(new PropertyValueFactory<>("promoCode"));
		code.setMinWidth(200);
		
		discount.setCellValueFactory(new PropertyValueFactory<>("promoDisc"));
		discount.setMinWidth(200);
		
		note.setCellValueFactory(new PropertyValueFactory<>("promoNote"));
		note.setMinWidth(400);
		
		promoList.getColumns().setAll(id, code, discount, note);
		promoList.setOnMouseClicked(selectPromo());
		
		refreshPromo();
	}
	
	private void refreshPromo() {
		promos = pc.getPromo(db, promos);
		ObservableList<Promo> promoObsList = FXCollections.observableArrayList(promos);
		promoList.setItems(promoObsList);
	}
	
	private EventHandler<MouseEvent> selectPromo() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent a) {
				TableSelectionModel<Promo> tableSM = promoList.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Promo selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					idField.setText(selected.getPromoId().toString());
					codeField.setText(selected.getPromoCode());
					discField.setText(selected.getPromoDisc().toString());
					noteField.setText(selected.getPromoNote());
					oldId = Integer.parseInt(idField.getText());
				}
			}
		};
	}
	
	private void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
	}
	
	private void events() {
		logout.setOnAction(e -> handle(e));
		insert.setOnAction(e -> handle(e));
		update.setOnAction(e -> handle(e));
		delete.setOnAction(e -> handle(e));
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == logout) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToLoginPage();
		}
		else if(e.getSource() == insert) {
			if(idField != null) {
				try {
					if(idField.getText().length() < 1) {
						pc.showAlert("Error", "ID cannot be empty");
					}
					else if(codeField.getText().length() < 1) {
						pc.showAlert("Error", "Code cannot be empty");
					}
					else if(discField.getText().length() < 1) {
						pc.showAlert("Error", "Discount cannot be empty");
					}
					Integer.parseInt(discField.getText());
					if(Integer.parseInt(discField.getText()) < 15000) {
						pc.showAlert("Error", "Discount must be atleast 15000");
					}
					else if(noteField.getText().length() < 1) {
						pc.showAlert("Error", "Note cannot be empty");
					}
					else {
						int flag = pc.validateInsert(db, Integer.parseInt(idField.getText()), codeField.getText(), promos);
						if(flag == -1) {
							pc.showAlert("Error", "Promo ID already exists");
						}
						else if(flag == -2) {
							pc.showAlert("Error", "Promo Code already exists");
						}
						else {
							pc.insertPromo(db, Integer.parseInt(idField.getText()), codeField.getText(), Integer.parseInt(discField.getText()), noteField.getText());
						}
						refreshPromo();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Discount amount must be numeric");
				}
			}
		}
		else if(e.getSource() == update) {
			if(idField != null) {
				try {
					if(idField.getText().length() < 1) {
						pc.showAlert("Error", "ID cannot be empty");
					}
					else if(codeField.getText().length() < 1) {
						pc.showAlert("Error", "Code cannot be empty");
					}
					else if(discField.getText().length() < 1) {
						pc.showAlert("Error", "Discount cannot be empty");
					}
					Integer.parseInt(discField.getText());
					if(Integer.parseInt(discField.getText()) < 15000) {
						pc.showAlert("Error", "Discount must be atleast 15000");
					}
					else if(noteField.getText().length() < 1) {
						pc.showAlert("Error", "Note cannot be empty");
					}
					else {
						int flag = pc.validateUpdate(db, Integer.parseInt(idField.getText()), codeField.getText(), promos, oldId);
						if(flag == -1) {
							pc.showAlert("Error", "You cannot change promo ID");
						}
						else if(flag == -2) {
							pc.showAlert("Error", "Promo Code already exists");
						}
						else {
							pc.updatePromo(db, Integer.parseInt(idField.getText()), codeField.getText(), Integer.parseInt(discField.getText()), noteField.getText());
						}
						refreshPromo();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Discount amount must be numeric");
				}
			}
		}
		else if(e.getSource() == delete) {
			if(idField != null) {
				try {
					if(idField.getText().length() < 1) {
						pc.showAlert("Error", "ID cannot be empty");
					}
					else if(codeField.getText().length() < 1) {
						pc.showAlert("Error", "Code cannot be empty");
					}
					else if(discField.getText().length() < 1) {
						pc.showAlert("Error", "Discount cannot be empty");
					}
					Integer.parseInt(discField.getText());
					if(Integer.parseInt(discField.getText()) < 15) {
						pc.showAlert("Error", "Discount must be atleast 15");
					}
					else if(noteField.getText().length() < 1) {
						pc.showAlert("Error", "Note cannot be empty");
					}
					else {
						int flag = pc.validateDelete(db, Integer.parseInt(idField.getText()), codeField.getText(), Integer.parseInt(discField.getText()), noteField.getText(), promos);
						if(flag == -1) {
							pc.showAlert("Error", "Promo cannot be deleted");
						}
						else {
							pc.deletePromo(db, Integer.parseInt(idField.getText()));
						}
						refreshPromo();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Discount amount must be numeric");
				}
			}
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
