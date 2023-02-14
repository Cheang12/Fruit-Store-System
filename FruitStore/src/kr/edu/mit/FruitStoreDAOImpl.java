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
	
	
	//db연결
	public void dbConn() {
		try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "aaa", "Wpqkfehlfk@0");
				System.out.println("연결성공");
		} catch (Exception e) {
				e.printStackTrace();
		}
		
	}
		
	//db닫기
	public void dbClose() {
		//객체를 닫는 명령문은 따로따로 넣어야 함. 선행 명령문을 닫는 과정에서 오류가 나면 그 아래의 명령문은 실행되지 않으므로 try catch문을 각각 써줘야 함.
		if(result!=null) try {result.close();} catch (SQLException e) {e.printStackTrace();}
		if(pstmt!=null) try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(conn!=null) try {conn.close();} catch (SQLException e) {e.printStackTrace();}
	}
	
	//과일 추가
	public void insertFruit(FruitVO vo) {
		dbConn();
		
		try {
			pstmt= conn.prepareStatement("insert into fruit (fruit_name, price, quantity) values (?, ?, ?)");
			pstmt.setString(1, vo.getFruit_name());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setInt(3, vo.getQuantity());
			
			pstmt.executeUpdate(); //삽입, 삭제, 수정시에는 executeUpdate() - 반환값 : int 처리된 행의 갯수, read(select)시에는 excuteQuery()사용 - 반환값 ResultSet객체를 결과값으로 돌려줌
		} catch (Exception e) {
			System.out.println("DB연결실패");
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
			ResultSet rs = pstmt.executeQuery(); //쿼리를 수행한 값이 ResultSet 객체인 rs에 저장되어 있음.
			
			while(rs.next()){ //다음행을 가리킴, 리턴은 다음행 가리키는게 성공이면 true, 없으면 false >> 다음행이 없으면 반복 종료
				FruitVO vo = new FruitVO(); // ** 한번의 while이 작동하면 그 전 값은 이미 list에 저장됐으니 사라지게 두고, 동일한 변수명으로 계속 새 값을 받아야 함. >> 이전값이 바뀌면 안되기 때문 >> 클래스는 참조변수라서 값 자체가 아닌 그 주소값을 저장함. >> vo를 가리키고 있는데 vo 주소값이 계속 바뀌니까 모두 그 주소값을 가리키게 되는것
				vo.setFruit_code(rs.getInt("fruit_code"));  //1행을 출력하기 위해 변수에 저장함. 숫자로 넣을 수도 있고, 이름으로 넣을수도 있음 db의 속성명을 가져옴
				vo.setFruit_name(rs.getString("fruit_name"));
				vo.setPrice(rs.getInt("price"));
				vo.setQuantity(rs.getInt("quantity"));
				vo.setDate(rs.getTimestamp("date"));
				list.add(vo); //while이 끝날떄 까지 list에 순차적으로 저장(인덱스 0,1,...)
				
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
			
			result = pstmt.executeQuery(); //쿼리를 수행한 값이 ResultSet 객체인 rs에 저장되어 있음.
			
			result.next();
			a = result.getInt(1); //resultset 에서 값을 가져오는 방법
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbClose();
		}

		return a;
	}
	
	public void insertSales(int fruit_code,int quantity) { //판매내용 추가, 판매내용 추가 키값 알아오기, 교차테이블 내용 갱신, 재고수정처리
		dbConn();
		try {
			conn.setAutoCommit(false); //mysql은 자동으로 커밋하므로 트랜젝션으로 처리를 위해 auto commit false설정
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			pstmt= conn.prepareStatement("insert into sales (sales_quantity) values (?)");
			pstmt.setInt(1, quantity);

			pstmt.executeUpdate(); //삽입, 삭제, 수정시에는 executeUpdate() - 반환값 : int 처리된 행의 갯수, read(select)시에는 excuteQuery()사용 - 반환값 ResultSet객체를 결과값으로 돌려줌
			
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
			System.out.println("판매실패"); //try 도중 문제 생기면 판매실패를 출력
			e.printStackTrace();
			try {
				conn.rollback(); //중간에 문제가 생기면 롤백. 상위 캐치에 포함되어 있음.
			} catch (SQLException e1) {
				e1.printStackTrace(); //문제가 생겨 진행한 롤백에 또 문제가 생기면 오류 출력
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
						System.out.println("실행횟수 : "+i);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("뭔가 문제 있음");
					e.printStackTrace();
				}finally {
					dbClose();
				}
			
			
		}
		
	}
	

}
