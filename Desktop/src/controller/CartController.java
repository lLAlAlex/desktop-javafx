package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Cart;
import util.Database;

public class CartController {
	
	public void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	public ArrayList<Cart> getCart(Database db, ArrayList<Cart> carts) {
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
		
		return carts;
	}
	
	public void clearCart(Database db) {
		String query = "TRUNCATE TABLE carts";
		
		PreparedStatement psQuery = db.preparedStatement(query);
		try {
			psQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Cart> clearCartArr(ArrayList<Cart> carts) {
		carts.clear();
		
		return carts;
	}
}
