<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to SMART Homepage</title>
</head>
<body>
	<br>
	<table border="1">
		<tr>
			<th>Services To Monitor</th>
		</tr>
		<c:forEach items="${ServiceList}" var="conf">
			<tr>

				<td>${conf}</td>

			</tr>
		</c:forEach>


	</table>
	<div>
		<form action="/editMonitor" method="post">

			<input type="text" name="serviceId" placeholder="Add to Service List">

			<button>Modify Monitoring List</button>

		</form>
	</div>
	<br>
	<table border="1">
		<tr>
			<th>Mail Recipients</th>
		</tr>
		<c:forEach items="${MailList}" var="conf">
			<tr>

				<td>${conf}</td>
		</c:forEach>
		</tr>
	</table>
	<div>
		<form action="/editMail" method="post">
			<input type="text" name="mailId" placeholder="Add to mail List">
			<button>Modify Mail List</button>
		</form>
	</div>


</body>
</html>