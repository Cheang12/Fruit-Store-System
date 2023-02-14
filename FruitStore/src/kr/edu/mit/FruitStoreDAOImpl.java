package kr.edu.mit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FruitStoreDAOImpl implements FruitStoreDAO {
	 Connection conn;
	 PreparedStatement pstmt;
	 ResultSet result;
	
	
	//db����
	public void dbConn() {
		try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");
				System.out.println("���Ἲ��");
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
		
	//db�ݱ�
	public void dbClose() {
		//��ü�� �ݴ� ��ɹ��� ���ε��� �־�� ��. ���� ��ɹ��� �ݴ� �������� ������ ���� �� �Ʒ��� ��ɹ��� ������� �����Ƿ� try catch���� ���� ����� ��.
		if(result!=null) try {result.close();} catch (SQLException e) {e.printStackTrace();}
		if(pstmt!=null) try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(conn!=null) try {conn.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
	//���� �߰�
	public void insertFruit(FruitVO vo) {
		dbConn();
		
		try {
			pstmt= conn.prepareStatement("insert into fruit (fruit_name, price, quantity) values (?, ?, ?)");
			pstmt.setString(1, vo.getFruit_name());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setInt(3, vo.getQuantity());
			
			pstmt.executeUpdate(); //����, ����, �����ÿ��� executeUpdate() - ��ȯ�� : int ó���� ���� ����, read(select)�ÿ��� excuteQuery()��� - ��ȯ�� ResultSet��ü�� ��������� ������
		} catch (Exception e) {
			System.out.println("DB�������");
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
	}


	public ArrayList<FruitVO> listFruit() {
		ArrayList<FruitVO> list = new ArrayList<>();
		
		dbConn();
		
		try {
			pstmt= conn.prepareStatement("select * from fruit order by date, fruit_name, price");
			ResultSet rs = pstmt.executeQuery(); //������ ������ ���� ResultSet ��ü�� rs�� ����Ǿ� ����.
			
			while(rs.next()){ //�������� ����Ŵ, ������ ������ ����Ű�°� �����̸� true, ������ false >> �������� ������ �ݺ� ����
				FruitVO vo = new FruitVO(); // ** �ѹ��� while�� �۵��ϸ� �� �� ���� �̹� list�� ��������� ������� �ΰ�, ������ ���������� ��� �� ���� �޾ƾ� ��. >> �������� �ٲ�� �ȵǱ� ���� >> Ŭ������ ���������� �� ��ü�� �ƴ� �� �ּҰ��� ������. >> vo�� ����Ű�� �ִµ� vo �ּҰ��� ��� �ٲ�ϱ� ��� �� �ּҰ��� ����Ű�� �Ǵ°�
				vo.setFruit_code(rs.getInt("fruit_code"));  //1���� ����ϱ� ���� ������ ������. ���ڷ� ���� ���� �ְ�, �̸����� �������� ���� db�� �Ӽ����� ������
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setPrice(rs.getInt("price"));
				vo.setQuantity(rs.getInt("quantity"));
				vo.setDate(rs.getTimestamp("date"));
				list.add(vo); //while�� ������ ���� list�� ���������� ����(�ε��� 0,1,...)
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}

		return list;
	}
	
	public void updateQuantityFruit(FruitVO vo) {
		dbConn();
		
		try {
			pstmt= conn.prepareStatement("update fruit set quantity = quantity+? where fruit_code = ?;");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
	}
	
	public int totalFruit(FruitVO vo) {
		
		dbConn();
		
		int a = -1;
		try {
			pstmt= conn.prepareStatement("select price * ? from fruit where fruit_code = ?");
			pstmt.setInt(1, vo.getQuantity());
			pstmt.setInt(2, vo.getFruit_code());
			
			result = pstmt.executeQuery(); //������ ������ ���� ResultSet ��ü�� rs�� ����Ǿ� ����.
			
			result.next();
			a = result.getInt(1); //resultset ���� ���� �������� ���
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbClose();
		}

		return a;
	}
	
	public void insertSales(int fruit_code,int quantity) { //�Ǹų��� �߰�, �Ǹų��� �߰� Ű�� �˾ƿ���, �������̺� ���� ����, ������ó��
		dbConn();
		try {
			conn.setAutoCommit(false); //mysql�� �ڵ����� Ŀ���ϹǷ� Ʈ���������� ó���� ���� auto commit false����
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			pstmt= conn.prepareStatement("insert into sales (sales_quantity) values (?)");
			pstmt.setInt(1, quantity);

			pstmt.executeUpdate(); //����, ����, �����ÿ��� executeUpdate() - ��ȯ�� : int ó���� ���� ����, read(select)�ÿ��� excuteQuery()��� - ��ȯ�� ResultSet��ü�� ��������� ������
			
			result = pstmt.executeQuery("select last_insert_id()");
			result.next();
			int num = result.getInt(1);
			pstmt.close();
			
			pstmt = conn.prepareStatement("insert into fruit_has_sales values(?,?)");
			pstmt.setInt(1, fruit_code);
			pstmt.setInt(2, num);
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement("update fruit set quantity = quantity-? where fruit_code = ?");
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, fruit_code);
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (Exception e) {
			System.out.println("�ǸŽ���"); //try ���� ���� ����� �ǸŽ��и� ���
			e.printStackTrace();
			try {
				conn.rollback(); //�߰��� ������ ����� �ѹ�. ���� ĳġ�� ���ԵǾ� ����.
			} catch (SQLException e1) {
				e1.printStackTrace(); //������ ���� ������ �ѹ鿡 �� ������ ����� ���� ���
			}
		}finally {
			dbClose();
		}

	}

	@Override
	public long totalPrice() {
		dbConn();
		long sum = -1;
		
		try {
			pstmt =  conn.prepareStatement("select sum(sales_quantity*price) " + 
					"from fruit " + 
					"join (select fruit_fruit_code, sales_date, sales_quantity " + 
					"	from fruit_has_sales " + 
					"	join sales on sales.sales_code = fruit_has_sales.sales_sales_code) t1 " + 
					"on fruit.fruit_code = t1.fruit_fruit_code;");
			result = pstmt.executeQuery();
			result.next();
			sum = result.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		return sum;
	}


	public ArrayList<SalesVO> listSales() {
		ArrayList<SalesVO> list = new ArrayList();
		dbConn();
		try {
			pstmt =  conn.prepareStatement("select fruit_name, fruit_code, sales_date, sales_quantity, sales_quantity*price " + 
					"from fruit " + 
					"join (select fruit_fruit_code, sales_date, sales_quantity " + 
					"	from fruit_has_sales" + 
					"	join sales on sales.sales_code = fruit_has_sales.sales_sales_code) t1 " + 
					"on fruit.fruit_code = t1.fruit_fruit_code");
			result = pstmt.executeQuery();
			while(result.next()) {
				SalesVO vo = new SalesVO();
				vo.setFruit_name(result.getString("fruit_name"));
				vo.setFruit_code(result.getInt("fruit_code"));
				vo.setSales_date(result.getTimestamp("sales_date"));
				vo.setSales_quantity(result.getInt("sales_quantity"));
				vo.setTotal(result.getInt("sales_quantity*price"));
				list.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
		
		return list;
	}
	
	public int exist_quantity(FruitVO vo) {
		dbConn();
		
		int exist_quantity = -1;
		try {
			pstmt = conn.prepareStatement("select quantity from fruit where fruit_code = ?");
			pstmt.setInt(1, vo.getFruit_code());
			result = pstmt.executeQuery();
			result.next();
			exist_quantity = result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
		return exist_quantity;
	}
	
	public ArrayList<String> listName(FruitVO vo) {
		ArrayList<String> list = new ArrayList();
		dbConn();
		try {
			pstmt = conn.prepareStatement("select fruit_name from fruit");
			result = pstmt.executeQuery();
			while(result.next()) {
				FruitVO vo1 = new FruitVO();
				vo1.setFruit_name(result.getString(1));
				list.add(vo1.getFruit_name());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
		return list;
	} 
	
	
	public ArrayList<Integer> listPrice(FruitVO vo) {
		ArrayList<Integer> list = new ArrayList();
		dbConn();
		try {
			pstmt = conn.prepareStatement("select price from fruit");
			result = pstmt.executeQuery();
			while(result.next()) {
				FruitVO vo1 = new FruitVO();
				vo1.setPrice(result.getInt(1));
				list.add(vo1.getPrice());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
		return list;
	}
	
	public int findCode(FruitVO vo) {
		dbConn();
		
		int findCode = -1;
		try {
			pstmt = conn.prepareStatement("select fruit_code from fruit where fruit_name = ? and price = ?");
			pstmt.setString(1, vo.getFruit_name());
			pstmt.setInt(2, vo.getPrice());
			result = pstmt.executeQuery();
			result.next();
			findCode = result.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			dbClose();
		}
		
		
		return findCode;
	}
	
	public void deleteFruit(String[] checks) {
		
		
		if(!(checks == null)){
			dbConn();

				try {
					for(int i = 0; i <checks.length; i++){
						pstmt= conn.prepareStatement("delete from fruit where fruit_code = ?");
						pstmt.setString(1, checks[i]);
						pstmt.executeUpdate();
						System.out.println("����Ƚ�� : "+i);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("���� ���� ����");
					e.printStackTrace();
				}finally {
					dbClose();
				}
			
			
		}
		
	}
	

}
