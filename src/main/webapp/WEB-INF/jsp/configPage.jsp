<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Config Page</title>
</head>
<body>
	<table>
		<c:forEach items="${ServiceList}" var="conf"></c:forEach>
		<tr>
			<td>${conf}</td>
		</tr>

	</table>
	<div>
		<form action="/config" method="post">
			<button>Modify Mail List</button>
			<button>Modify Monitoring List</button>
			
		</form>
	</div>
</body>
</html>