package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn; // 데이터베이스에 접근하게 해주는 하나의 객체
	private PreparedStatement pstmt;  
	private ResultSet rs; 
	
	// 실제로 데이터베이스에 접근해서 어떠한 데이터를 가져오거나 데이터를 넣는 역할을 하는 데이터 접근 객체
	public UserDAO() { // 생성자
		try {
			String dbURL = "jdbc:mysql://localhost:3307/BBS"; // 내 컴퓨터에 설치된 mysql 서버
			String dbID = "root";
			String dbPassword = "Dbsdk2631!";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace(); // 오류가 나면 무엇인지 출력해주도록 설정
		}
	}
	
	// 실제로 로그인을 시도하는 하나의 함수
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		
		try {
			pstmt = conn.prepareStatement(SQL); 
			pstmt.setString(1, userID); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; // 로그인 성공
				}
				else
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음.
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // 데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}
}
