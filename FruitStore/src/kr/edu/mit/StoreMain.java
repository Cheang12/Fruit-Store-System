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
			System.out.println("1. �����Է�\n2. ����ľ�\n3. �Ǹ��ϱ�\n4. ����Ȯ��");
			System.out.println("\n�޴��� �������ּ��� (���� : 0)");
			menuNum = in.nextInt();
			
			if (menuNum == 1) {
				FruitVO vo1 = new FruitVO();
				ArrayList<String> list1 = dao.listName(vo1);
				ArrayList<Integer> list2 = dao.listPrice(vo1);
				String fruit_name = null;
				int price = 0;
				int select = -1;
				
				do {
					System.out.println("1. ���ο� ���� �߰�\n2. ���� ���� �԰�");
					System.out.println("\n�޴��� ������ �ּ���. (���ư��� : 0)");
					select = in.nextInt();
					int overlap = 0;
					if (select == 1) {
						do {
							System.out.println("���� �߰��� ������ �̸��� �Է����ּ���.");
							fruit_name = in.next();
							System.out.println("������ ������ �Է����ּ���.");
							price = in.nextInt();
							vo1.setFruit_name(fruit_name); //vo1 ��ü�� ���� �־�� �ϴ��� �޼��忡�� �� ���� �ν��� ���� �޾ƿ�
							vo1.setPrice(price);
							if (list1.contains(fruit_name)) {
								if (list2.contains(price)) {
									System.out.println(+dao.findCode(vo1)+"���� �̹� ���� �����Ͱ� �ֽ��ϴ�. �ٸ� �����͸� �Է����ּ���.\n");
									overlap = 1;
								}else {
									overlap = 0;
								}
							}
						} while (overlap == 1);
						
						System.out.println("������ �԰� ������ �Է����ּ���.");
						int quantity = in.nextInt();
						
						vo1.setFruit_name(fruit_name); //vo��ü���� ���� �̸��� set �����μ� �ϴ��� insertFruit �޼��忡�� �� ���� get�� ����� �� ����
						vo1.setPrice(price);
						vo1.setQuantity(quantity);
						dao.insertFruit(vo1);
						System.out.println(fruit_name+" "+quantity+"���� �԰�ó���� �Ϸ�Ǿ����ϴ�.\n");
					}else if(select == 2) {
						FruitVO vo2 = new FruitVO();
						
						List<FruitVO> list = dao.listFruit();
						for (FruitVO temp : list) {
							System.out.println(temp);
						}
						System.out.println("\n���� ����Դϴ�. �԰�ó�� �� ���Ϲ�ȣ�� �������ּ���.");
						int fruit_code = in.nextInt();
						System.out.println("�԰������ �Է����ּ���.");
						int quantity = in.nextInt();
						vo2.setFruit_code(fruit_code);
						vo2.setQuantity(quantity);
						dao.updateQuantityFruit(vo2);
						System.out.println(quantity+"���� �԰�ó���� �Ϸ�Ǿ����ϴ�.\n");
					}
				} while (select != 0);
				
			}else if(menuNum == 2) {
				System.out.println("\n���� ��� ������ ���� ����Դϴ�.\n");
				List<FruitVO> list = dao.listFruit();
				for (FruitVO temp : list) {
				System.out.println(temp);
			}
				System.out.println("");
			
			}else if(menuNum == 3) {
				//���ϸ�� �����ֱ� (DB)
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
					//����� ����
					System.out.println("\n���� ����Դϴ�. ���Ϲ�ȣ�� �������ּ���.");
					fruit_code = in.nextInt();
					System.out.println("���ż����� �Է����ּ���.");
					quantity = in.nextInt();
					//���ұݾ� �ȳ�, ���Ϻ� �� ���� �˷��ֱ� (DB)
					vo.setFruit_code(fruit_code);
					exist_quantity = dao.exist_quantity(vo); //���� ���� �� ��
					vo.setQuantity(quantity);
					
					if (exist_quantity < vo.getQuantity()) {
						System.out.println("������ ���������� ���ż����� �����ϴ�. �ִ� "+exist_quantity+"�� ���� ���� �����մϴ�.\n");
					}	
				} while (true == exist_quantity < vo.getQuantity());
				
				System.out.println("�� ���űݾ��� "+decFormat.format(dao.totalFruit(vo))+"�� �Դϴ�.");
				System.out.println("�����Ͻðڽ��ϱ�?(1: ����       2: ���)");
				//�Ǹ� �Ϸ�, �Ǹ� ó��
				if (in.nextInt() == 1) {
					dao.insertSales(fruit_code, quantity);
					System.out.println("���ſϷ� �Ǿ����ϴ�.");
					}
				
			}else if(menuNum == 4) {
				List<SalesVO> list = dao.listSales();
				for (SalesVO salesVO : list) {
					System.out.println(salesVO);
				}
				
				System.out.println("�� ������� "+decFormat.format(dao.totalPrice())+"�� �Դϴ�.\n");

			}else if(menuNum == 0){
				System.out.println("�̿����ּż� �����մϴ�.\n");
			}
			
		} while (menuNum != 0);
			
		
		
		

		
	}

}
