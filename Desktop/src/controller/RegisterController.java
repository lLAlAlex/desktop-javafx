package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import util.Database;

public class RegisterController {
	
	public void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

	public int validate(String email, String name, String password) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
	            "[a-zA-Z0-9_+&*-]+)*@" +
	            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
	            "A-Z]{2,7}$";
		
		Pattern pat = Pattern.compile(emailRegex);
		Matcher matcher = pat.matcher(email);
		
		if(name.length() < 3) {
			return -1;
		}
		else if(!matcher.matches()) {
			return -2;
		}
		else if(password.length() < 4) {
			return -3;
		}
		else {
			return 1;
		}
	}
	
	public void addData(String name, String email, String password, String role, Database db) {
		int roleId = 0;
		if(role.equals("Customer")) {
			roleId = 1;
		}
		else if(role.equals("Admin")) {
			roleId = 2;
		}
		else if(role.equals("Promotion")) {
			roleId = 3;
		}
		else if(role.equals("Manager")) {
			roleId = 4;
		}
		
		String query = "INSERT INTO users (username, useremail, userpassword, userrole) VALUES ('"+name+"', '"+email+"', '"+password+"', '"+roleId+"')";
		db.execUpdate(query);
	}
	
	public void clearFields(TextField nameField, TextField emailField, PasswordField passwordField) {
		nameField.clear();
		emailField.clear();
		passwordField.clear();
	}
	
	public void addStaff(Database db, String name, String email, String password, Integer roleId) {
		
	}

}
