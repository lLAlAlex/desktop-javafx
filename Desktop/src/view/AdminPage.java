package view;

import java.util.ArrayList;

import controller.Controller;
import controller.ProductController;
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
import model.Product;
import util.Database;

public class AdminPage extends Application implements EventHandler<ActionEvent>{
	private Scene scene;
	private BorderPane root;
	private Label headerLabel, idLabel, nameLabel, authorLabel, priceLabel, stockLabel;
	private TextField idField, nameField, authorField, priceField, stockField;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem logout;
	private VBox vbox;
	private ArrayList<Product> products;
	private TableView<Product> productList;
	private Database db;
	private GridPane grid;
	private TableColumn<Product, Integer> id, name, author, price, stock;
	private ProductController pc;
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
		products = new ArrayList<>();
		productList = new TableView<>();
		idLabel = new Label("Product ID :");
		nameLabel = new Label("Name :");
		authorLabel = new Label("Author :");
		priceLabel = new Label("Price :");
		stockLabel = new Label("Stock :");
		idField = new TextField();
		nameField = new TextField();
		authorField = new TextField();
		priceField = new TextField();
		stockField = new TextField();
		pc = new ProductController();
		logout = new MenuItem();
		id = new TableColumn<>("Product ID");
		name = new TableColumn<>("Product Name");
		author = new TableColumn<>("Product Author");
		price = new TableColumn<>("Product Price");
		stock = new TableColumn<>("Product Stock");
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
    	grid.add(nameLabel, 2, 0);
    	grid.add(nameField, 3, 0);
    	grid.add(authorLabel, 4, 0);
    	grid.add(authorField, 5, 0);
    	grid.add(priceLabel, 6, 0);
    	grid.add(priceField, 7, 0);
    	grid.add(stockLabel, 8, 0);
    	grid.add(stockField, 9, 0);
    	grid.add(productList, 0, 1);
    	grid.add(insert, 0, 3);
    	grid.add(update, 1, 3);
    	grid.add(delete, 2, 3);
    	grid.setAlignment(Pos.CENTER);
    	productList.setPrefWidth(1000);
    	productList.setPrefHeight(200);
    	GridPane.setColumnSpan(productList, 10);
    	GridPane.setRowSpan(productList, 2);
    	
    	grid.setVgap(20);
    	grid.setHgap(10);
	}
	
	private void setTable() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		id.setMinWidth(100);
		
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		name.setMinWidth(300);
		
		author.setCellValueFactory(new PropertyValueFactory<>("author"));
		author.setMinWidth(200);
		
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		price.setMinWidth(200);
		
		stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		stock.setMinWidth(200);
		
		productList.getColumns().setAll(id, name, author, price, stock);
		productList.setOnMouseClicked(selectProduct());
		
		refreshProduct();
	}
	
	private void refreshProduct() {
		products = pc.getProduct(db, products);
		ObservableList<Product> productObsList = FXCollections.observableArrayList(products);
		productList.setItems(productObsList);
	}
	
	private EventHandler<MouseEvent> selectProduct() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent a) {
				TableSelectionModel<Product> tableSM = productList.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Product selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					idField.setText(selected.getId().toString());
					nameField.setText(selected.getName());
					authorField.setText(selected.getAuthor());
					Integer price = selected.getPrice().intValue();
					priceField.setText(price.toString());
					stockField.setText(selected.getStock().toString());
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
					else if(nameField.getText().length() < 1) {
						pc.showAlert("Error", "Name cannot be empty");
					}
					else if(authorField.getText().length() < 1) {
						pc.showAlert("Error", "Author cannot be empty");
					}
					Integer.parseInt(priceField.getText());
					Integer.parseInt(stockField.getText());
					if(Integer.parseInt(priceField.getText()) < 1) {
						pc.showAlert("Error", "Price must be more than 0");
					}
					else if(Integer.parseInt(stockField.getText()) < 1) {
						pc.showAlert("Error", "Stock must be more than 0");
					}
					else {
						int flag = pc.validateInsert(db, Integer.parseInt(idField.getText()), products);
						if(flag == -1) {
							pc.showAlert("Error", "Product ID already exists");
						}
						else {
							pc.insertProduct(db, Integer.parseInt(idField.getText()), nameField.getText(), authorField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(stockField.getText()));
						}
						refreshProduct();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Price and Stock must be numeric");
				}
			}
		}
		else if(e.getSource() == update) {
			if(idField != null) {
				try {
					if(idField.getText().length() < 1) {
						pc.showAlert("Error", "ID cannot be empty");
					}
					else if(nameField.getText().length() < 1) {
						pc.showAlert("Error", "Name cannot be empty");
					}
					else if(authorField.getText().length() < 1) {
						pc.showAlert("Error", "Author cannot be empty");
					}
					Integer.parseInt(priceField.getText());
					Integer.parseInt(stockField.getText());
					if(Integer.parseInt(priceField.getText()) < 1) {
						pc.showAlert("Error", "Price must be more than 0");
					}
					else if(Integer.parseInt(stockField.getText()) < 1) {
						pc.showAlert("Error", "Stock must be more than 0");
					}
					else {
						int flag = pc.validateUpdate(db, Integer.parseInt(idField.getText()), products, oldId);
						if(flag == -1) {
							pc.showAlert("Error", "Product ID already exists");
						}
						else {
							pc.updateProduct(db, Integer.parseInt(idField.getText()), nameField.getText(), authorField.getText(), Integer.parseInt(priceField.getText()), Integer.parseInt(stockField.getText()));
						}
						refreshProduct();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Price and Stock must be numeric");
				}
			}
		}
		else if(e.getSource() == delete) {
			if(idField != null) {
				try {
					if(idField.getText().length() < 1) {
						pc.showAlert("Error", "ID cannot be empty");
					}
					else if(nameField.getText().length() < 1) {
						pc.showAlert("Error", "Name cannot be empty");
					}
					else if(authorField.getText().length() < 1) {
						pc.showAlert("Error", "Author cannot be empty");
					}
					Integer.parseInt(priceField.getText());
					Integer.parseInt(stockField.getText());
					if(Integer.parseInt(priceField.getText()) < 1) {
						pc.showAlert("Error", "Price must be more than 0");
					}
					else if(Integer.parseInt(stockField.getText()) < 1) {
						pc.showAlert("Error", "Stock must be more than 0");
					}
					else {
						int flag = pc.validateDelete(db, Integer.parseInt(idField.getText()), products);
						if(flag == -1) {
							pc.showAlert("Error", "Product cannot be deleted");
						}
						else {
							pc.deleteProduct(db, Integer.parseInt(idField.getText()));
						}
						refreshProduct();
					}
				} catch(NumberFormatException a) {
					pc.showAlert("Error", "Price and Stock must be numeric");
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
