
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일 목록</title>
</head>
<body>

<%	

	FruitStoreDAO dao = new FruitStoreDAOImpl();
	ArrayList<FruitVO> list=dao.listFruit();
	Date now = new Date();
	Calendar cal = Calendar.getInstance();
	int expirationDate = 7;
%>
	<table border = "1" >
		<tr><th>이름</th> <th>가격</th> <th>재고</th> <th>입고일</th> <th>폐기 예정일</th> <th>잔여 유통기한</th>
		</tr>
<%
	DecimalFormat decFormat = new DecimalFormat("###,###");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");
	for(FruitVO vo:list){ %>
		<tr> <td width = "100"> <%=vo.getFruit_name()%> </td>
			 <td width = "100"> <%=decFormat.format(vo.getPrice())%> </td>
			 <td width = "50"> <%=vo.getQuantity()%> </td>
			 <td width = "250"> <%=simpleDateFormat.format(vo.getDate())%> </td>
<% 			 
			 cal.setTime(vo.getDate());
			 cal.add(Calendar.DATE,expirationDate);
%>
			 <td width = "250"> <%= simpleDateFormat.format(cal.getTime())%> </td>
<%			
			 String del = null;
			 int dateResult = 0;
			 if(now.compareTo(cal.getTime()) > 0){ //호출한 date 객체가 괄호 안의 객체보다 이후 날짜라면 0보다 큰 값이 호출 됨. >> 현재 날짜가 유통기한 날짜보다 이후 날짜라면 유통기한이 지났으므로 폐기처리 필요
				 del = "폐기 처리 필요";
			 }else{
				 del = " ";
			 }
%>			 
			 <td width = "150"> <%= del%> </td>
			 </tr>
<%}%>
		
	</table>
	<h1> </h1>
	<hr>
<button type="button" onclick="gotitle()">메뉴로 돌아가기</button> &nbsp; <button type="button" onclick="goDelete()">재고 폐기</button>

<script>
	function gotitle() {
		location.href="index.html"
	}
	
	function goDelete() {
		location.href="deleteFruit.jsp"
	}
</script>

</body>
</html>