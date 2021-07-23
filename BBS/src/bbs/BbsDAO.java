package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {

	private Connection conn; // 데이터베이스에 접근하게 해주는 하나의 객체
	private ResultSet rs; 

	// 실제로 데이터베이스에 접근해서 어떠한 데이터를 가져오거나 데이터를 넣는 역할을 하는 데이터 접근 객체
	public BbsDAO() { // 생성자
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

	/**
	 * 현재 시간을 가져오는 함수
	 */
	public String getDate() {
		String SQL = "SELECT NOW()"; //현재 시간을 가져오는 mysql 문장 

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return ""; // 데이터베이스 오류를 반환
	}

	/**
	 * 다음으로 작성될 글 번호 반환
	 */
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 현재가 첫번째 게시물인 경우
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return -1; // 데이터베이스 오류
	}

	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)"; 

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);

			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return -1; // 데이터베이스 오류
	}

	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10 );
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return list; 
	}
	
	/**
	 * 페이징 처리를 위해 존재하는 함수
	 */
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10 );
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return false;
	}
	
	/**
	 * 하나의 글 내용을 불러오는 함수
	 */
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return null;
	}
	
	/**
	 * 글 내용을 수정하는 함수
	 */
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?"; 

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);

			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return -1; // 데이터베이스 오류
	}
	
	/**
	 * 글을 삭제하는 함수
	 */
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?"; 

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);

			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return -1; // 데이터베이스 오류
	}

}
