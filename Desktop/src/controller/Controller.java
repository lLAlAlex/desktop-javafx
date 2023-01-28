package controller;

import javafx.stage.Stage;
import view.AdminPage;
import view.CartPage;
import view.HomePage;
import view.LoginPage;
import view.ManagerPage;
import view.PromoPage;
import view.RegisterPage;

public class Controller {
	
	public void moveToRegisterPage() {
		Stage nextStage = new Stage();
		try {
			new RegisterPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToHomePage() {
		Stage nextStage = new Stage();
		try {
			new HomePage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToLoginPage() {
		Stage nextStage = new Stage();
		try {
			new LoginPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToCartPage() {
		Stage nextStage = new Stage();
		try {
			new CartPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToPromoPage() {
		Stage nextStage = new Stage();
		try {
			new PromoPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToAdminPage() {
		Stage nextStage = new Stage();
		try {
			new AdminPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveToManagerPage() {
		Stage nextStage = new Stage();
		try {
			new ManagerPage().start(nextStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
