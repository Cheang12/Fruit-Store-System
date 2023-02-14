package kr.edu.mit;

import java.util.ArrayList;

public interface FruitStoreDAO {
	
////////////쿼리별로 구현////////////
//과일등록
void insertFruit(FruitVO vo);
//과일목록보여주기
ArrayList<FruitVO> listFruit();
//수량업데이트
void updateQuantityFruit(FruitVO vo);

//과일별 총가격알려주기
int totalFruit(FruitVO vo);

//1.판매내용 추가 - 2.판매내용 추가 키값 알아오기 - 3.교차테이블 갱신(추가) - 4.재고수정처리
void insertSales(int fruit_code,int quantity);  //키값을 리턴
	
//총 판매금액
long totalPrice();

//매출내역보기
ArrayList<SalesVO> listSales();
	
	
}
