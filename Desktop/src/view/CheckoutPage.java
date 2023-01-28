package view;

import java.text.DecimalFormat;
import java.util.ArrayList;

import controller.CartController;
import controller.Controller;
import controller.HomeController;
import controller.PromoController;
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
import model.Cart;
import model.Product;
import model.Promo;
import util.Database;

public class CheckoutPage extends Application implements EventHandler<ActionEvent>{
	private ArrayList<Cart> selectedCart;
	private ArrayList<Product> products;
	private ArrayList<Promo> promos;
	private Scene scene;
	private BorderPane root;
	private Label headerLabel, paymentLabel, cardNumLabel;
	private MenuBar menuBar;
	private Menu menu;
	private MenuItem menuItem, logout;
	private VBox vbox;
	private Database db;
	private TableView<Promo> promoList;
	private TableColumn<Promo, Integer> id, code, discount, note;
	private TextField paymentMethod, cardNumber;
	private GridPane grid;
	private PromoController pc;
	private CartController cc;
	private Button confirm;
	private Integer selectedId;
	private HomeController hc;
	private TransactionController tc;
	private Controller controller;
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public void init() {
		selectedCart = CartPage.getFinalCart();
		promos = new ArrayList<>();
		products = new ArrayList<>();
		root = new BorderPane();
		headerLabel = new Label("Purple Lane Bookstore");
		scene = new Scene(root, 1200, 600);
		menuBar = new MenuBar();
		vbox = new VBox(headerLabel, menuBar);
		menu = new Menu("Menu");
		db = Database.getInstance();
		promoList = new TableView<>();
		paymentLabel = new Label("Payment Method :");
		cardNumLabel = new Label("Card Number :");
		paymentMethod = new TextField();
		cardNumber = new TextField();
		grid = new GridPane();
		menuItem = new MenuItem("Cart");
		logout = new MenuItem("Log Out");
		pc = new PromoController();
		confirm = new Button("Confirm");
		cc = new CartController();
		hc = new HomeController();
		tc = new TransactionController();
		controller = new Controller();
		selectedId = null;
		
		id = new TableColumn<Promo, Integer>("Promo ID");
		code = new TableColumn<Promo, Integer>("Promo Code");
		discount = new TableColumn<Promo, Integer>("Promo Discount");
		note = new TableColumn<Promo, Integer>("Promo Note");
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
    	
    	grid.add(paymentLabel, 0, 0);
    	grid.add(paymentMethod, 1, 0);
    	grid.add(cardNumLabel, 3, 0);
    	grid.add(cardNumber, 4, 0);
    	grid.add(promoList, 0, 1);
    	grid.setAlignment(Pos.CENTER);
    	grid.add(confirm, 5, 0);
    	
    	promoList.setPrefWidth(1000);
    	promoList.setPrefHeight(350);
    	GridPane.setColumnSpan(promoList, 5);
    	GridPane.setRowSpan(promoList, 2);
    	grid.setHgap(15);
    	grid.setVgap(20);
	}
	
	private void setTable() {
		id.setCellValueFactory(new PropertyValueFactory<>("promoId"));
		id.setMinWidth(100);
		
		code.setCellValueFactory(new PropertyValueFactory<>("promoCode"));
		code.setMinWidth(200);
		
		discount.setCellValueFactory(new PropertyValueFactory<>("promoDisc"));
		discount.setMinWidth(100);
		
		note.setCellValueFactory(new PropertyValueFactory<>("promoNote"));
		note.setMinWidth(600);
		
		promoList.getColumns().setAll(id, code, discount, note);
		promoList.setOnMouseClicked(selectPromo());
		
		refreshPromo();
	}
	
	private EventHandler<MouseEvent> selectPromo() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				TableSelectionModel<Promo> tableSM = promoList.getSelectionModel();
				tableSM.setSelectionMode(SelectionMode.SINGLE);
				Promo selected = tableSM.getSelectedItem();
				
				if(selected != null) {
					selectedId = selected.getPromoId();
				}
			}
		};
	}
	
	private void refreshPromo() {
		promos = pc.getPromo(db, promos);
		ObservableList<Promo> promoObsList = FXCollections.observableArrayList(promos);
		promoList.setItems(promoObsList);
	}
	
	private void events() {
		menuItem.setOnAction(e -> handle(e));
		logout.setOnAction(e -> handle(e));
		confirm.setOnAction(e -> handle(e));
	}
	
	private void setStyle() {
		headerLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px; -fx-background-color: skyblue; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);");
	}

	@Override
	public void start(Stage stage) throws Exception {
		init();
		initMenu();
		setPosition();
		setTable();
		setStyle();
		events();
		
		stage.setTitle("Purple Lane Bookstore");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
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
		else if(e.getSource() == confirm) {
			boolean flag = pc.isNumeric(cardNumber.getText());
			
			int result = pc.validate(paymentMethod.getText(), cardNumber.getText());
				
			if(result == -1) {
				paymentMethod.setStyle("-fx-border-color: red;");
				pc.showAlert("Invalid Payment Method", "Payment Method must be either Debit or Credit");
			}
			else if(result == -2) {
				cardNumber.setStyle("-fx-border-color: red;");
				pc.showAlert("Invalid Card Number", "Card Number must be exactly 16 digits");
			}
			else if(flag == false) {
				cardNumber.setStyle("-fx-border-color: red;");
				pc.showAlert("Invalid Card Number", "Card Number must only contain numbers");
			}
			else {
				double totalprice = 0;
				products = hc.getProduct(db, products);
				
				for(Cart cart : selectedCart) {
					for(Product product : products) {
						if(cart.getProductId() == product.getId()) {
							totalprice = totalprice + (product.getPrice() * cart.getQuantity());
						}
					}
				}
				if(selectedId != null) {
					double disc = 0;
					for(Promo promo : promos) {
						if(promo.getPromoId() == selectedId) {
							disc = promo.getPromoDisc().doubleValue() / 100;
							totalprice = totalprice * disc;
							break;
						}
					}
				}
				totalprice = Double.parseDouble(df.format(totalprice));
				tc.addNewTransaction(db, selectedCart, products, paymentMethod.getText(), cardNumber.getText(), totalprice);
				cc.clearCart(db);
				hc.showAlert("Success", "Checkout Successful");
				controller.moveToHomePage();
			}
		}
	}
	
}
