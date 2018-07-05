package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private SimpleIntegerProperty id;
	private SimpleStringProperty username;
	private SimpleStringProperty password;
	static int userId;

	public User() {
		this.id = new SimpleIntegerProperty();
		this.username = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
	}

	// id
	public int getId() {
		return id.get();
	}

	public void setId(int i) {
		this.id.set(i);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	// username
	public String getUsername() {
		return username.get();
	}

	public void setUsername(String u) {
		this.username.set(u);
	}

	public StringProperty usernameProperty() {
		return username;
	}

	// password
	public String getPassword() {
		return password.get();
	}

	public void setType(String p) {
		this.password.set(p);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	// userId
	public static int getUserId() {
		return userId;
	}

	public static void setUserId(int i) {
		userId = i;
	}

}
