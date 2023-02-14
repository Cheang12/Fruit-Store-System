<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.SalesVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>매출 확인</title>
</head>
<body>
<%
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	ArrayList<SalesVO> list=dao.listSales();
%>
	<table border = "1">
		<tr><th>과일코드</th> <th>과일이름</th> <th>판매수량</th> <th>판매금액</th> <th>판매일자</th>
		</tr>
<%
	DecimalFormat decFormat = new DecimalFormat("###,###");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a HH:mm:ss");
	for(SalesVO vo:list){ %>
		<tr> <td><%=vo.getFruit_code()%></td>
			 <td width = "100"><%=vo.getFruit_name()%></td>
			 <td width = "50"><%=vo.getSales_quantity()%></td>
			 <td width = "100"><%=decFormat.format(vo.getTotal())%></td>
			 <td width = "250"><%=simpleDateFormat.format(vo.getSales_date())%></td>
			 </tr>
<%}%>
		<tr> <th colspan="5" align="right">총 매출액 : <%= decFormat.format(dao.totalPrice())%> 원 </th>
			</tr>
	</table>
<button type="button" onclick="gotitle()">메뉴로 돌아가기</button> <input type="checkbox" onclick="gotitle()">돌아가기
<script>
	function gotitle() {
		history.back();
	}
</script>

</body>
</html>