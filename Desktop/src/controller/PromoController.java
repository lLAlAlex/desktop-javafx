package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Promo;
import util.Database;

public class PromoController {
	
	public void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	public ArrayList<Promo> getPromo(Database db, ArrayList<Promo> promos) {
		promos.clear();
		
		String query = "SELECT * FROM promos";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			db.rs = psQuery.executeQuery();
			while(db.rs.next()) {
				promos.add(new Promo(db.rs.getInt("id"), db.rs.getString("promocode"), db.rs.getInt("promodiscount"), db.rs.getString("promonote")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return promos;
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
	
	public int validateInsert(Database db, Integer promoId, String promoCode, ArrayList<Promo> promos) {
		promos = getPromo(db, promos);
		
		for(Promo promo : promos) {
			if(promo.getPromoId() == promoId) {
				return -1;
			}
			else if(promo.getPromoCode().equals(promoCode)) {
				return -2;
			}
		}
		return 1;
	}
	
	public int validateUpdate(Database db, Integer promoId, String promoCode, ArrayList<Promo> promos, Integer oldId) {
		promos = getPromo(db, promos);
		
		if(promoId != oldId) {
			return -1;
		}
		for(Promo promo : promos) {
			if(promo.getPromoCode().equals(promoCode)) {
				return -2;
			}
		}
		return 1;
	}
	
	public int validateDelete(Database db, Integer promoId, String promoCode, Integer promoDisc, String promoNote, ArrayList<Promo> promos) {
		promos = getPromo(db, promos);
		
		for(Promo promo : promos) {
//			if(promo.getPromoId() == promoId && promo.getPromoCode().equals(promoCode) && promo.getPromoDisc() == promoDisc && promo.getPromoNote().equals(promoNote)) {
//				return 1;
//			}
			if(promo.getPromoId() == promoId) {
				return 1;
			}
		}
		return -1;
	}
	
	public void insertPromo(Database db, Integer promoId, String promoCode, Integer promoDisc, String promoNote) {
		String query = "INSERT INTO promos (id, promocode, promodiscount, promonote) "+"VALUES (?, ?, ?, ?)";
		PreparedStatement prepQuery = db.preparedStatement(query);
			
		try {
			prepQuery.setInt(1, promoId);
			prepQuery.setString(2, promoCode);
			prepQuery.setInt(3, promoDisc);
			prepQuery.setString(4, promoNote);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePromo(Database db, Integer promoId, String promoCode, Integer promoDisc, String promoNote) {
		String query = "UPDATE promos SET promocode = ?, promodiscount = ?, promonote = ? WHERE id = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setString(1, promoCode);
			prepQuery.setInt(2, promoDisc);
			prepQuery.setString(3, promoNote);
			prepQuery.setInt(4, promoId);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deletePromo(Database db, Integer promoId) {
		String query = "DELETE FROM promos WHERE id = ?";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		try {
			prepQuery.setInt(1, promoId);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
