package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolutionDao {

	public static List<SolutionWithAuthor> loadAllWithAuthor(Connection conn, int count) throws SQLException {
		List<SolutionWithAuthor> result = new ArrayList<>();
		String sql = "SELECT solution.id, solution.title, solution.submission_date, solution.user_id, "
				+ "user.name FROM solution JOIN user "
				+ "ON solution.user_id=user.id "
				+ "ORDER BY solution.submission_date DESC "
				+ "LIMIT ?;";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, count);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int solutionId = rs.getInt("solution.id");
			String solutionTitle = rs.getString("solution.title");
			Date date = rs.getDate("solution.submission_date");
			String author = rs.getString("user.name");
			SolutionDTO sDTO = new SolutionDTO(solutionId, solutionTitle, 0, date) ;
			result.add(new SolutionWithAuthor(sDTO, author));
		}
		return result;
	}
}