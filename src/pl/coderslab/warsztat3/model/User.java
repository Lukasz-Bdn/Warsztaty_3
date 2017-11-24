package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	private long id;
	private String username;
	private String email;
	private String password;
	private int user_group_id;
	
	public User() {
		super();
		this.id = 0;
		this.username = "";
		this.email = "";
		this.password = "";
		this.user_group_id = 0;
	}
	
	public User(String username, String email, String password) {
		super();
		this.id = 0;
		this.username = username;
		this.email = email;
		setPassword(password);
		this.user_group_id = 0;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	public boolean checkPassword (String password) {
		return BCrypt.checkpw(password, this.password);
	}

	public int getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(int user_group_id) {
		this.user_group_id = user_group_id;
	}

	public long getId() {
		return id;
	}
	
	public void saveToDb(Connection conn) throws SQLException {
		/*
		 * Saves an object to database.
		 */
		if (this.id == 0) {
			String sql = "INSERT INTO users(username, email, password, user_group_id) "
					+ "VALUES(?, ?, ?, ?);";
			String[] generatedColumns = {"ID"};
			PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
			ps.setString(1, this.username);
			ps.setString(2, this.email);
			ps.setString(3, this.password);
			ps.setInt(4, user_group_id);
			ps.executeUpdate();
			ResultSet gk = ps.getGeneratedKeys();
			if (gk.next()) {
				this.id = gk.getLong(1); // reads automatically generated id (AUTO_INCREMENT)
			}
			gk.close();
			ps.close();
			
		} else {
			String sql = "UPDATE users SET username =?, email=?, password=?, user_group_id =? "
					+ "WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.username);
			ps.setString(2,  this.email);
			ps.setString(3, this.password);
			ps.setInt(4, this.user_group_id);
			ps.setLong(5, this.id);
			ps.executeUpdate();
			ps.close();
		}
	}
	
	public static User loadUserById(Connection conn, int id) throws SQLException {
		/*
		 * Reads a record from users table in database and returns a User object that
		 * represents it.
		 */
		String sql = "SELECT * FROM users WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			User loadedUser = new User();
			loadedUser.id = rs.getInt("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			loadedUser.user_group_id = rs.getInt("user_group_id");
			rs.close();
			ps.close();
			return loadedUser;
		}
		ps.close();
		return null;
	}
	
	public static User[] loadAll(Connection conn) throws SQLException {
		/*
		 * Reads all records from users table in database and returns an array of 
		 * user objects. This method is static.
		 */
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User loadedUser = new User();
			loadedUser.id = rs.getInt(1);
			loadedUser.username = rs.getString(2);
			loadedUser.email = rs.getString(3);
			loadedUser.password = rs.getString(4);
			loadedUser.user_group_id = rs.getInt(5);
			users.add(loadedUser);
		}
		rs.close();
		User[] userArray = new User[users.size()];
		userArray = users.toArray(userArray);
		return userArray;
	}
	
	public void deleteFromDb(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM users WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, this.id);
			ps.executeUpdate();
			this.id = 0;
			ps.close();
		}
	}
	
	public static User[] loadAllByGroupId(Connection conn, int user_group_id) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users WHERE user_group_id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, user_group_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			User loadedUser = new User();
			loadedUser.id = rs.getInt(1);
			loadedUser.username = rs.getString(2);
			loadedUser.email = rs.getString(3);
			loadedUser.password = rs.getString(4);
			users.add(loadedUser);
		}
		rs.close();
		User[] userArray = new User[users.size()];
		userArray = users.toArray(userArray);
		return userArray;

	}
}