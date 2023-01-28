package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Cart;
import model.Product;
import util.Database;

public class HomeController {
	ArrayList<Cart> carts = new ArrayList<>();
	Database db = Database.getInstance();
	
	public void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	public ArrayList<Product> getProduct(Database db, ArrayList<Product> products) {
		products.clear();
		
		String query = "SELECT * FROM products";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			db.rs = psQuery.executeQuery();
			while(db.rs.next()) {
				products.add(new Product(db.rs.getInt("id"), db.rs.getString("productname"), db.rs.getString("productauthor"), db.rs.getDouble("productprice"), db.rs.getInt("productstock")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return products;
	}

	public void insertToCart(Database db, Integer userId, Integer productId, Integer quantity) {
		getCart();
		boolean flag = false;
		
		if(carts != null) {
			for(Cart cart : carts) {
				if(cart.getProductId() == productId && cart.getUserId() == userId) {
					Integer newqty = quantity + cart.getQuantity();
					updateCart(newqty, userId, productId);
					flag = true;
					break;
				}
			}
			if(flag == false) {
				String query = "INSERT INTO carts (userid, productid, quantity) "+"VALUES (?, ?, ?)";
				PreparedStatement prepQuery = db.preparedStatement(query);
				
				try {
					prepQuery.setInt(1, userId);
					prepQuery.setInt(2, productId);
					prepQuery.setInt(3, quantity);
					prepQuery.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			String query = "INSERT INTO carts (userid, productid, quantity) "+"VALUES (?, ?, ?)";
			PreparedStatement prepQuery = db.preparedStatement(query);
			
			try {
				prepQuery.setInt(1, userId);
				prepQuery.setInt(2, productId);
				prepQuery.setInt(3, quantity);
				prepQuery.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void updateProduct(Database db, Integer newstock, Integer productid) {
		String query = "UPDATE products SET productstock = ? WHERE id = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setInt(1, newstock);
			prepQuery.setInt(2, productid);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateStock(Database db, ArrayList<Product> products, Integer id, Integer qty) {
		for(Product product : products) {
			if(product.getId() == id) {
				updateProduct(db, product.getStock() - qty, id);
				break;
			}
		}
	}
	
	public void getCart() {
		carts.clear();
		
		String query = "SELECT * FROM carts";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			db.rs = psQuery.executeQuery();
			while(db.rs.next()) {
				carts.add(new Cart(db.rs.getInt("id"), db.rs.getInt("userid"), db.rs.getInt("productid"), db.rs.getInt("quantity")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCart(Integer quantity, Integer userid, Integer productid) {
		String query = "UPDATE carts SET quantity = ? WHERE userid = ? AND productid = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setInt(1, quantity);
			prepQuery.setInt(2, userid);
			prepQuery.setInt(3, productid);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
