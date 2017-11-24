package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroup {
	private int id;
	private String name;
	
	public UserGroup() {
		super();
		this.id = 0;
		this.name = "";
	}
	
	public UserGroup(String name) {
		super();
		this.id = 0;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}
	
	public void saveToDb(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO user_group(name) VALUES(?);";
			String[] generatedKeys = { "ID" };
			PreparedStatement ps = conn.prepareStatement(sql, generatedKeys);
			ps.setString(1, this.name);
			ps.executeUpdate();
			ResultSet gk = ps.getGeneratedKeys();
			if (gk.next()) {
				this.id = gk.getInt(1);
			}
			gk.close();
			ps.close();
		} else {
			String sql = "UPDATE users SET name=? WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.name);
			ps.setInt(2, this.id);
			ps.executeUpdate();
			ps.close();
		}
	}
	
	public static UserGroup loadById(Connection conn, int id) throws SQLException {
		String sql  = "SELECT * FROM user_group WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			UserGroup loadedUserGroup = new UserGroup();
			loadedUserGroup.id = rs.getInt("id");
			loadedUserGroup.name = rs.getString("name");
			rs.close();
			ps.close();
			return loadedUserGroup;
		}
		ps.close();
		return null;
	}
	
	public static UserGroup[] loadAll(Connection conn) throws SQLException {
		ArrayList<UserGroup> ugArrayList = new ArrayList<UserGroup>();
		String sql = "SELECT * FROM user_group;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			UserGroup userGroup = new UserGroup();
			userGroup.id = rs.getInt("id");
			userGroup.name = rs.getString("name");
			ugArrayList.add(userGroup);
		}
		ps.close();
		rs.close();
		UserGroup[] ugArray = new UserGroup[ugArrayList.size()];
		ugArray = ugArrayList.toArray(ugArray);
		return ugArray;
	}
	
	public void deleteFromDb(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			this.id = 0;
			ps.close();
		}
	}
}