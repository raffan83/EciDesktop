package it.ncsnetwork.EciDesktop.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
	private SimpleLongProperty id;
	private SimpleStringProperty username;
	private SimpleStringProperty password;
	private SimpleStringProperty accessToken;
	public static long userId;

	public User() {
		this.id = new SimpleLongProperty();
		this.username = new SimpleStringProperty();
		this.password = new SimpleStringProperty();
		this.accessToken = new SimpleStringProperty();
	}

	// id
	public Long getId() {
		return id.get();
	}

	public void setId(long l) {
		this.id.set(l);
	}

	public LongProperty idProperty() {
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

	public void setPassword(String p) {
		this.password.set(p);
	}

	public StringProperty passwordProperty() {
		return password;
	}
	
	// access token
	public String getAccessToken() {
		return accessToken.get();
	}

	public void setAccessToken(String ac) {
		this.accessToken.set(ac);
	}

	public StringProperty accessTokenProperty() {
		return accessToken;
	}

	// userId
	public static Long getUserId() {
		return userId;
	}

	public static void setUserId(long l) {
		userId = l;
	}

}
