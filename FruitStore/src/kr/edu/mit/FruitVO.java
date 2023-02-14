package kr.edu.mit;

import java.util.Date;

public class FruitVO {
	//코드
	private int fruit_code;
	//이름
	private String fruit_name;
	//가격
	private int price;
	//수량
	private int quantity;
	//입고일
	private Date date;
	
	
	public int getFruit_code() {
		return fruit_code;
	}
	public void setFruit_code(int fruit_code) {
		this.fruit_code = fruit_code;
	}
	public String getFruit_name() {
		return fruit_name;
	}
	public void setFruit_name(String fruit_name) {
		this.fruit_name = fruit_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() { //출력용 문구로 오버라이딩
		return "FruitVO [fruit_code=" + fruit_code + ", fruit_name=" + fruit_name + ", price=" + price + ", quantity="
				+ quantity + "]";
	}
	
	
	
	
}
