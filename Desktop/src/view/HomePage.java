package view;

import java.util.ArrayList;

import controller.Controller;
import controller.HomeController;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import util.Database;

public class HomePage extends Application implements EventHandler<ActionEvent>{
	private Scene scene;
	private BorderPane root;
	private Label headerLabel;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem menuItem, menuItem2, logout;
	private Integer roleId;
	private VBox vbox;
	private ArrayList<Product> products;
	private TableView<Product> productList;
	private Database db;
	private GridPane grid;
	private TableColumn<Product, Integer> id, name, author, price, stock;
	private Integer userid, currqty;
	private Button addToCart;
	private TextField selectedqty;
	private Integer selectedid;
	private Label quantity;
	private Controller controller;
	private HomeController hc;
	
	public void init() {
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		scene = new Scene(root, 1200, 600);
		menuBar = new MenuBar();
		vbox = new VBox(headerLabel, menuBar);
		roleId = LoginPage.getRoleId();
		menu = new Menu("Menu");
		db = Database.getInstance();
		grid = new GridPane();
		products = new ArrayList<>();
		productList = new TableView<>();
		addToCart = new Button("Add to Cart");
		selectedqty = new TextField();
		quantity = new Label("Quantity :");
		selectedid = null;
		currqty = 0;
		controller = new Controller();
		hc = new HomeController();
		
		if(roleId == 1) {
			menuItem = new MenuItem("Cart");
		}
		else if(roleId == 2) {
			menuItem = new MenuItem("Manage Products");
		}
		else if(roleId == 3) {
			menuItem = new MenuItem("Manage Promos");
		}
		else if(roleId == 4) {
			menuItem = new MenuItem("Manage Transactions");
			menuItem2 = new MenuItem("Manage Staffs");
		}
		logout = new MenuItem("Log Out");
		
		id = new TableColumn<Product, Integer>("Product ID");
		name = new TableColumn<Product, Integer>("Product Name");
		author = new TableColumn<Product, Integer>("Product Author");
		price = new TableColumn<Product, Integer>("Product Price");
		stock = new TableColumn<Product, Integer>("Product Stock");
		
		userid = LoginPage.getUserId();
	}
	
	private void initMenu() {
		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItem);
		
		if(roleId == 4) {
			menu.getItems().add(menuItem2);
		}
		menu.getItems().add(logout);
	}
	
	private void setPosition() {
		root.setTop(vbox);
		headerLabel.prefWidthProperty().bind(root.widthProperty());
    	headerLabel.setAlignment(Pos.CENTER);
    	root.setCenter(grid);
    	
    	grid.add(addToCart, 1, 0);
    	grid.add(quantity, 3, 0);
    	grid.add(selectedqty, 4, 0);
    	grid.add(productList, 1, 1);
    	grid.setAlignment(Pos.CENTER);
    	productList.setPrefWidth(1000);
    	productList.setPrefHeight(350);
    	GridPane.setColumnSpan(productList, 5);
    	GridPane.setRowSpan(productList, 3);
    	
    	grid.setVgap(20);
    	grid.setHgap(10);
	}
	
	private void setTable() {
			id.setCellValueFactory(new PropertyValueFactory<>("id"));
			id.setMinWidth(100);
			
			name.setCellValueFactory(new PropertyValueFactory<>("name"));
			name.setMinWidth(500);
			
			author.setCellValueFactory(new PropertyValueFactory<>("author"));
			author.setMinWidth(200);
			
			price.setCellValueFactory(new PropertyValueFactory<>("price"));
			price.setMinWidth(100);
			
			stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
			stock.setMinWidth(100);
			
			productList.getColumns().setAll(id, name, author, price, stock);
			productList.setOnMouseClicked(selectProduct());
			
			refreshProduct();
	}
	
	private void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
	}
	
	private void events() {
		logout.setOnAction(e -> handle(e));
		addToCart.setOnAction(e -> handle(e));
		menuItem.setOnAction(e -> handle(e));
		if(roleId == 4) {
			menuItem2.setOnAction(e -> handle(e));
		}
	}
	
	private void refreshProduct() {
		products = hc.getProduct(db, products);
		ObservableList<Product> productObsList = FXCollections.observableArrayList(products);
		productList.setItems(productObsList);
	}
	
	private EventHandler<MouseEvent> selectProduct() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				TableSelectionModel<Product> tableSM = productList.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Product selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					selectedid = selected.getId();
					selectedqty.setText("1");
					currqty = selected.getStock();
				}
			}
		};
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == logout) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToLoginPage();
		}
		else if(e.getSource() == addToCart) {
			if(selectedid != null) {
				
				try {
					Integer.parseInt(selectedqty.getText());
					
					if(Integer.parseInt(selectedqty.getText()) > currqty) {
						hc.showAlert("Error", "Quantity cannot exceed current stock!");
					}
					else if(Integer.parseInt(selectedqty.getText()) < 1) {
						hc.showAlert("Error", "Quantity must be more than 0");
					}
					else {
						hc.insertToCart(db, userid, selectedid, Integer.parseInt(selectedqty.getText()));
						hc.updateStock(db, products, selectedid, Integer.parseInt(selectedqty.getText()));
						refreshProduct();
					}
				} catch(NumberFormatException a) {
					hc.showAlert("Error", "Quantity must be a number");
				}
			}
		}
		else if(e.getSource() == menuItem) {
			if(roleId == 1) {
				controller.moveToCartPage();
				Stage currentStage = (Stage) headerLabel.getScene().getWindow();
				currentStage.close();
				return;
			}
		}
		else if(e.getSource() == menuItem2) {
			
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
