<%@page import="kr.edu.mit.FruitVO"%>
<%@page import="kr.edu.mit.FruitStoreDAOImpl"%>
<%@page import="kr.edu.mit.FruitStoreDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>과일 입력 처리</title>
</head>
<body>
<%	
	request.setCharacterEncoding("utf-8");
	System.out.println(request.getParameter("fruit_name"));
	System.out.println(request.getParameter("price"));
	System.out.println(request.getParameter("quantity"));
	FruitStoreDAO dao = new FruitStoreDAOImpl();
	FruitVO vo = new FruitVO();
	
	vo.setFruit_name(request.getParameter("fruit_name"));
	
	vo.setPrice(Integer.parseInt(request.getParameter("price")));
	vo.setQuantity(Integer.parseInt(request.getParameter("quantity")));
	dao.insertFruit(vo);

%>
<script>
	alert("입력 완료되었습니다.")
	location.href="index.html"

</script>

</body>
</html>