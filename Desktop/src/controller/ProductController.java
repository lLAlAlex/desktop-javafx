package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Product;
import util.Database;

public class ProductController {
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
	
	public int validate(String paymentMethod, String cardNumber) {
		if(!paymentMethod.equals("Debit") && !paymentMethod.equals("Credit")) {
			return -1;
		}
		else if(cardNumber.length() != 16) {
			return -2;
		}
		else {
			return 1;
		}
	}
	
	public static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e) {  
		    return false;  
		  }  
	}
	
	public int validateInsert(Database db, Integer productId, ArrayList<Product> products) {
		products = getProduct(db, products);
		
		for(Product product : products) {
			if(product.getId() == productId) {
				return -1;
			}
		}
		return 1;
	}
	
	public int validateUpdate(Database db, Integer productId, ArrayList<Product> products, Integer oldId) {
		products = getProduct(db, products);
		
		if(productId != oldId) {
			return -1;
		}
		return 1;
	}
	
	public int validateDelete(Database db, Integer productId, ArrayList<Product> products) {
		products = getProduct(db, products);
		
		for(Product product : products) {
			if(product.getId() == productId) {
				return 1;
			}
		}
		return -1;
	}
	
	public void insertProduct(Database db, Integer productId, String productName, String productAuthor, Integer productPrice, Integer productStock) {
		String query = "INSERT INTO products (id, productname, productauthor, productprice, productstock) "+"VALUES (?, ?, ?, ?, ?)";
		PreparedStatement prepQuery = db.preparedStatement(query);
			
		try {
			prepQuery.setInt(1, productId);
			prepQuery.setString(2, productName);
			prepQuery.setString(3, productAuthor);
			prepQuery.setInt(4, productPrice);
			prepQuery.setInt(5, productStock);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProduct(Database db, Integer productId, String productName, String productAuthor, Integer productPrice, Integer productStock) {
		String query = "UPDATE products SET productname = ?, productauthor = ?, productprice = ?, productstock = ? WHERE id = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setString(1, productName);
			prepQuery.setString(2, productAuthor);
			prepQuery.setInt(3, productPrice);
			prepQuery.setInt(4, productStock);
			prepQuery.setInt(5, productId);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProduct(Database db, Integer productId) {
		String query = "DELETE FROM products WHERE id = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setInt(1, productId);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
