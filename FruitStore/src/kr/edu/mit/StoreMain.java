package kr.edu.mit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StoreMain {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		DecimalFormat decFormat = new DecimalFormat("###,###");
		int menuNum = 0;
		
		FruitStoreDAOImpl dao = new FruitStoreDAOImpl();

		
		
		do {
			System.out.println("1. 과일입력\n2. 재고파악\n3. 판매하기\n4. 매출확인");
			System.out.println("\n메뉴를 선택해주세요 (종료 : 0)");
			menuNum = in.nextInt();
			
			if (menuNum == 1) {
				FruitVO vo1 = new FruitVO();
				ArrayList<String> list1 = dao.listName(vo1);
				ArrayList<Integer> list2 = dao.listPrice(vo1);
				String fruit_name = null;
				int price = 0;
				int select = -1;
				
				do {
					System.out.println("1. 새로운 과일 추가\n2. 기존 과일 입고");
					System.out.println("\n메뉴를 선택해 주세요. (돌아가기 : 0)");
					select = in.nextInt();
					int overlap = 0;
					if (select == 1) {
						do {
							System.out.println("새로 추가할 과일의 이름을 입력해주세요.");
							fruit_name = in.next();
							System.out.println("과일의 가격을 입력해주세요.");
							price = in.nextInt();
							vo1.setFruit_name(fruit_name); //vo1 객체에 값을 넣어야 하단의 메서드에서 그 값을 인식해 값을 받아옴
							vo1.setPrice(price);
							if (list1.contains(fruit_name)) {
								if (list2.contains(price)) {
									System.out.println(+dao.findCode(vo1)+"번에 이미 동일 데이터가 있습니다. 다른 데이터를 입력해주세요.\n");
									overlap = 1;
								}else {
									overlap = 0;
								}
							}
						} while (overlap == 1);
						
						System.out.println("과일의 입고 수량을 입력해주세요.");
						int quantity = in.nextInt();
						
						vo1.setFruit_name(fruit_name); //vo객체에서 과일 이름을 set 함으로서 하단의 insertFruit 메서드에서 그 값을 get해 사용할 수 있음
						vo1.setPrice(price);
						vo1.setQuantity(quantity);
						dao.insertFruit(vo1);
						System.out.println(fruit_name+" "+quantity+"개의 입고처리가 완료되었습니다.\n");
					}else if(select == 2) {
						FruitVO vo2 = new FruitVO();
						
						List<FruitVO> list = dao.listFruit();
						for (FruitVO temp : list) {
							System.out.println(temp);
						}
						System.out.println("\n과일 목록입니다. 입고처리 할 과일번호를 선택해주세요.");
						int fruit_code = in.nextInt();
						System.out.println("입고수량을 입력해주세요.");
						int quantity = in.nextInt();
						vo2.setFruit_code(fruit_code);
						vo2.setQuantity(quantity);
						dao.updateQuantityFruit(vo2);
						System.out.println(quantity+"개의 입고처리가 완료되었습니다.\n");
					}
				} while (select != 0);
				
			}else if(menuNum == 2) {
				System.out.println("\n현재 재고를 보유한 과일 목록입니다.\n");
				List<FruitVO> list = dao.listFruit();
				for (FruitVO temp : list) {
				System.out.println(temp);
			}
				System.out.println("");
			
			}else if(menuNum == 3) {
				//과일목록 보여주기 (DB)
				FruitVO vo = new FruitVO();
				int fruit_code = 0;
				int quantity = 0;
				int exist_quantity = 0;
				int select = 0;
				
				do {
					List<FruitVO> list = dao.listFruit();
					for (FruitVO temp : list) {
						System.out.println(temp);
					}
					//사용자 선택
					System.out.println("\n과일 목록입니다. 과일번호를 선택해주세요.");
					fruit_code = in.nextInt();
					System.out.println("구매수량을 입력해주세요.");
					quantity = in.nextInt();
					//지불금액 안내, 과일별 총 가격 알려주기 (DB)
					vo.setFruit_code(fruit_code);
					exist_quantity = dao.exist_quantity(vo); //기존 재고와 값 비교
					vo.setQuantity(quantity);
					
					if (exist_quantity < vo.getQuantity()) {
						System.out.println("과일의 재고수량보다 구매수량이 많습니다. 최대 "+exist_quantity+"개 까지 구매 가능합니다.\n");
					}	
				} while (true == exist_quantity < vo.getQuantity());
				
				System.out.println("총 구매금액은 "+decFormat.format(dao.totalFruit(vo))+"원 입니다.");
				System.out.println("구매하시겠습니까?(1: 구매       2: 취소)");
				//판매 완료, 판매 처리
				if (in.nextInt() == 1) {
					dao.insertSales(fruit_code, quantity);
					System.out.println("구매완료 되었습니다.");
					}
				
			}else if(menuNum == 4) {
				List<SalesVO> list = dao.listSales();
				for (SalesVO salesVO : list) {
					System.out.println(salesVO);
				}
				
				System.out.println("총 매출액은 "+decFormat.format(dao.totalPrice())+"원 입니다.\n");

			}else if(menuNum == 0){
				System.out.println("이용해주셔서 감사합니다.\n");
			}
			
		} while (menuNum != 0);
			
		
		
		

		
	}

}
