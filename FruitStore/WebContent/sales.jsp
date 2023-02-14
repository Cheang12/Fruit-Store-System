<%@page import="java.text.DecimalFormat"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일 구매</title>
</head>
<body>
	<table border = "1">
		<%	
	FruitStoreDAOImpl dao = new FruitStoreDAOImpl();
	ArrayList<FruitVO> list=dao.listFruit();
%>
	<table border = "1">
		<tr><th>과일코드</th> <th>이름</th> <th>가격</th> <th>재고</th>
		</tr>
<%
	DecimalFormat decFormat = new DecimalFormat("###,###");
	for(FruitVO vo:list){ %>
		<tr>	
			<td> <%=vo.getFruit_code()%> </td> 
			<td width = "100"> <%=vo.getFruit_name()%> </td>
			<td width = "100"> <%=decFormat.format(vo.getPrice())%> </td>
			 <td width = "50"> <%=vo.getQuantity()%> </td>
			 </tr>
<%}%>
		
	</table>
	<h1> </h1>
	<hr>
	<form action = "" method="post">
		과일코드 : <input type="number" name = "fruit_code"> <br>
		구매수량 : <input type = "number" name = "sales_quantity">
		<input type = "submit" value = "확인"> <br>  <br>
	</form>
	
<%
	String fruit_code =  request.getParameter("fruit_code");
	String sales_quantity =  request.getParameter("sales_quantity");
	
	if((fruit_code!=null && sales_quantity!=null)){
		if(fruit_code.length()!=0 && sales_quantity.length()!=0){
			FruitVO vo = new FruitVO();
			vo.setFruit_code(Integer.parseInt(fruit_code));
			vo.setQuantity(Integer.parseInt(sales_quantity));
			int price = dao.totalFruit(vo);
		%>
	<form action = "salesPro.jsp" method="post">
			<input type="hidden" name = "fruit_code" value = <%=fruit_code%>> <br>
			<input type = "hidden" name = "sales_quantity" value = <%=sales_quantity%>>
			총 가격은 <%=decFormat.format(price) %>원입니다.
			<input type="submit" value = "구매하기"> <button type="button" onclick="gotitle()">메뉴로 돌아가기</button>
		</form>
<%		}
	} %>
	
	<script>
	function gotitle() {
		location.href="index.html"
	}
	
</script>
	
	


</body>
</html>