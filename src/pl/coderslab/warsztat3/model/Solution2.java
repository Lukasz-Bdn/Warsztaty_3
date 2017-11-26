package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.coderslab.warsztat3.db.DbUtil;

public class Solution2 {
	public static List<SolutionWithAuthor> loadAllWithAuthor(int count) {
		Connection conn;
		List<SolutionWithAuthor> result = new ArrayList<>();
		try {
			conn = DbUtil.getConn();
			result = SolutionDao.loadAllWithAuthor(conn, count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}