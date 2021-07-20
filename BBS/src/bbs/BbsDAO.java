package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BbsDAO {
	
	private Connection conn; // �����ͺ��̽��� �����ϰ� ���ִ� �ϳ��� ��ü
	private ResultSet rs; 
	
	// ������ �����ͺ��̽��� �����ؼ� ��� �����͸� �������ų� �����͸� �ִ� ������ �ϴ� ������ ���� ��ü
	public BbsDAO() { // ������
		try {
			String dbURL = "jdbc:mysql://localhost:3307/BBS"; // �� ��ǻ�Ϳ� ��ġ�� mysql ����
			String dbID = "root";
			String dbPassword = "Dbsdk2631!";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace(); // ������ ���� �������� ������ֵ��� ����
		}
	}
	
	/**
	 * ���� �ð��� �������� �Լ�
	 */
	public String getDate() {
		String SQL = "SELECT NOW()"; //���� �ð��� �������� mysql ���� 
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return ""; // �����ͺ��̽� ������ ��ȯ
	}
	
	/**
	 * 
	 */
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // ���簡 ù��° �Խù��� ���
		} catch(Exception e) {
			e.printStackTrace();	
		}
		return -1; // �����ͺ��̽� ����
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
		return -1; // �����ͺ��̽� ����
	}
}