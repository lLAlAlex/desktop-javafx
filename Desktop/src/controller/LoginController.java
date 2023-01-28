package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.User;
import util.Database;

public class LoginController {

	public void getUsers(Database db, ArrayList<User> users) {
		String query = "SELECT * FROM users";
		
		PreparedStatement prepQuery = db.preparedStatement(query);

		try {
			db.rs = prepQuery.executeQuery();
			while(db.rs.next()) {
				users.add(new User(db.rs.getInt("id"), db.rs.getString("username"), db.rs.getString("useremail"), db.rs.getString("userpassword"), db.rs.getInt("userrole")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void showAlert(String title, String message) {
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}

}
