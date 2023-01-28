package view;

import java.util.ArrayList;

import controller.CartController;
import controller.Controller;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Cart;
import util.Database;

public class CartPage extends Application implements EventHandler<ActionEvent>{
	private Scene scene;
	private BorderPane root;
	private Label headerLabel;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem menuItem, logout;
	private VBox vbox;
	private Database db;
	private ArrayList<Cart> carts, selectedCarts;
	private TableView<Cart> cartList, currcarts;
	private TableColumn<Cart, Integer> id, userid, productid, quantity, id2, userid2, productid2, quantity2;
	private GridPane grid;
	private Button checkout;
	private CartController cc;
	private static ArrayList<Cart> finalCart;
	private Controller controller;

	public void init() {
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		scene = new Scene(root, 1200, 600);
		menuBar = new MenuBar();
		vbox = new VBox(headerLabel, menuBar);
		menu = new Menu("Menu");
		db = Database.getInstance();
		cartList = new TableView<>();
		currcarts = new TableView<>();
		checkout = new Button("Checkout");
		cc = new CartController();
		grid = new GridPane();
		
		menuItem = new MenuItem("Home");
		logout = new MenuItem("Log Out");
		
		carts = new ArrayList<>();
		id = new TableColumn<>("Cart ID");
		userid = new TableColumn<>("User ID");
		productid = new TableColumn<>("Product ID");
		quantity = new TableColumn<>("Quantity");
		id2 = new TableColumn<>("Cart ID");
		userid2 = new TableColumn<>("User ID");
		productid2 = new TableColumn<>("Product ID");
		quantity2 = new TableColumn<>("Quantity");
		
		selectedCarts = new ArrayList<>();
		
		finalCart = null;
		
		controller = new Controller();
	}
	
	private void initMenu() {
		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItem);
		menu.getItems().add(logout);
	}
	
	private void setPosition() {
		root.setTop(vbox);
		headerLabel.prefWidthProperty().bind(root.widthProperty());
    	headerLabel.setAlignment(Pos.CENTER);
    	root.setCenter(grid);
    	
    	grid.add(checkout, 1, 0);
    	grid.add(cartList, 1, 1);
    	root.setBottom(currcarts);
    	grid.setAlignment(Pos.CENTER);
    	cartList.setPrefWidth(1000);
    	cartList.setPrefHeight(200);
    	currcarts.setPrefWidth(1000);
    	currcarts.setPrefHeight(150);
    	GridPane.setColumnSpan(cartList, 4);
    	GridPane.setRowSpan(cartList, 4);
    	
    	grid.setVgap(20);
    	grid.setHgap(10);
	}
	
	private void setTable() {
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		id.setMinWidth(100);
		
		userid.setCellValueFactory(new PropertyValueFactory<>("userId"));
		userid.setMinWidth(500);
		
		productid.setCellValueFactory(new PropertyValueFactory<>("productId"));
		productid.setMinWidth(200);
		
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantity.setMinWidth(100);
		
		cartList.getColumns().setAll(id, userid, productid, quantity);
		cartList.setOnMouseClicked(selectCart());
		
		id2.setCellValueFactory(new PropertyValueFactory<>("id"));
		id2.setMinWidth(100);
		
		userid2.setCellValueFactory(new PropertyValueFactory<>("userId"));
		userid2.setMinWidth(500);
		
		productid2.setCellValueFactory(new PropertyValueFactory<>("productId"));
		productid2.setMinWidth(200);
		
		quantity2.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantity2.setMinWidth(100);
		
		currcarts.getColumns().setAll(id2, userid2, productid2, quantity2);
		currcarts.setOnMouseClicked(selectCurrCart());
		
		refreshCart();
	}
	
	private void refreshCart() {
		carts = cc.getCart(db, carts);
		ObservableList<Cart> cartObsList = FXCollections.observableArrayList(carts);
		cartList.setItems(cartObsList);
		
		ObservableList<Cart> currCartObsList = FXCollections.observableArrayList(selectedCarts);
		currcarts.setItems(currCartObsList);
	}
	
	private void refreshCurrCart() {
		ObservableList<Cart> currCartObsList = FXCollections.observableArrayList(selectedCarts);
		currcarts.setItems(currCartObsList);
	}
	
	private EventHandler<MouseEvent> selectCart() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent a) {
				TableSelectionModel<Cart> tableSM = cartList.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Cart selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					boolean flag = false;
					for(Cart cart : selectedCarts) {
						if(selected.getId() == cart.getId()) {
							flag = true;
							cc.showAlert("Error", "Cart is already selected!");
							break;
						}
					}
					if(flag == false) {
						selectedCarts.add(selected);
						refreshCurrCart();
					}
				}
			}
		};
	}
	
	private EventHandler<MouseEvent> selectCurrCart() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent a) {
				TableSelectionModel<Cart> tableSM = currcarts.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Cart selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					int index = 0;
					for(Cart cart : selectedCarts) {
						if(selected.getId() == cart.getId()) {
							break;
						}
						else {
							index++;
						}
					}
					selectedCarts.remove(index);
					refreshCurrCart();
				}
			}
		};
	}
	
	private void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
	}
	
	private void events() {
		logout.setOnAction(e -> handle(e));
		menuItem.setOnAction(e -> handle(e));
		checkout.setOnAction(e -> handle(e));
	}
	
	private void setFinalCart() {
		finalCart = selectedCarts;
	}
	
	public static ArrayList<Cart> getFinalCart() {
		return finalCart;
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == logout) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToLoginPage();
		}
		else if(e.getSource() == menuItem) {
			Stage currentStage = (Stage) headerLabel.getScene().getWindow();
			currentStage.close();
			
			controller.moveToHomePage();
		}
		else if(e.getSource() == checkout) {
			if(selectedCarts.isEmpty()) {
				cc.showAlert("Error", "You have not chosen a cart yet");
			}
			else {
				Stage currentStage = (Stage) headerLabel.getScene().getWindow();
				currentStage.close();
				
				Stage nextStage = new Stage();
				try {
					setFinalCart();
					new CheckoutPage().start(nextStage);
				} catch (Exception e1) {
					e1.printStackTrace();
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
