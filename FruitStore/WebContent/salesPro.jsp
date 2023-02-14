<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>구매처리</title>
</head>
<body>
<%	
	int fruit_code=Integer.parseInt(request.getParameter("fruit_code"));
	int quantity = Integer.parseInt(request.getParameter("sales_quantity"));	
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	dao.insertSales(fruit_code, quantity);
%>


	구매가 완료되었습니다.

</body>
</html>