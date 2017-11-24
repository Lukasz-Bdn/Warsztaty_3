package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {
	private int id;
	private String title;
	private String description;
	
	public Exercise() {
		super();
		this.id=0;
		this.title = "";
		this.description ="";
	}
	
	public Exercise(String title, String description) {
		super();
		this.id = 0;
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	
	public void saveToDb(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO exercise(title, description) " + "VALUES(?, ?);";
			String[] generatedKeys = { "ID" };
			PreparedStatement ps = conn.prepareStatement(sql, generatedKeys);
			ps.setString(1, this.title);
			ps.setString(2, this.description);
			ps.executeUpdate();
			ResultSet gk = ps.getGeneratedKeys();
			if (gk.next()) {
				this.id = gk.getInt(1);
			}
			gk.close();
			ps.close();
		} else {
			String sql = "UPDATE exercise SET title=?, description=? WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.title);
			ps.setString(2, this.description);
			ps.setInt(3, this.id);
			ps.executeUpdate();
			ps.close();
		}
	}

	public static Exercise loadById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM exercise WHERE id=?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = rs.getInt("id");
			loadedExercise.title = rs.getString("title");
			loadedExercise.description = rs.getString("description");
			rs.close();
			ps.close();
			return loadedExercise;
		}
		return null;
	}
	
	public static Exercise[] loadAll(Connection conn) throws SQLException {
		ArrayList<Exercise> exerciseArrList = new ArrayList<Exercise>();
		String sql = "SELECT * FROM exercise";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = rs.getInt("id");
			loadedExercise.title = rs.getString("title");
			loadedExercise.description = rs.getString("description");
			exerciseArrList.add(loadedExercise);
		}
		rs.close();
		ps.close();
		Exercise[] exerciseArray = new Exercise[exerciseArrList.size()];
		exerciseArray = exerciseArrList.toArray(exerciseArray);
		return exerciseArray;
	}
	
	public void deleteFromDb(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM exercise WHERE id=?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, this.id);
			ps.executeUpdate();
			this.id = 0;
			ps.close();
		}
	}
	
	public static Exercise[] loadAllByUserId(Connection conn, int id) throws SQLException {
		ArrayList<Exercise> exerciseArrayList = new ArrayList<Exercise>();
		String sql = "SELECT exercise.id,exercise.title,exercise.description "
				+ "FROM users "
				+ "JOIN solution ON users.id=solution.users_id "
				+ "JOIN exercise ON solution.exercise_id=exercise.id "
				+ "WHERE users.id=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Exercise loadedExercise = new Exercise();
			loadedExercise.id = rs.getInt("exercise.id");
			loadedExercise.title = rs.getString("exercise.title");
			loadedExercise.description = rs.getString("exercise.description");
			exerciseArrayList.add(loadedExercise);			
		}
		rs.close();
		ps.close();
		Exercise[] exerciseArray = new Exercise[exerciseArrayList.size()];
		exerciseArray = exerciseArrayList.toArray(exerciseArray);
		return exerciseArray;
		
	}
	
}