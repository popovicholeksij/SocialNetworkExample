<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Діалог</title>
		<link href="webjars/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="webjars/bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css" rel="stylesheet">
		<link href="./css/navbar.css" rel="stylesheet">
		<script src="webjars/jquery/2.1.1/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="container">
			<jsp:include page="menu.jsp" /><br>
				<c:choose>
					<c:when test="${!receiverIgnoreIdsList.contains(user.getId())}">
						<form method="post" action="dialog.action">
							<h3>Написати повідомлення</h3>
							<br>
							<textarea name="message" wrap="soft" COLS="100" ROWS="3" placeholder="Ваше повідомлення..."></textarea><br>
							<br>
							<input type="hidden" value="<c:if test="${dialogList.get(0).getSender().getId() != user.getId()}">${dialogList.get(0).getSender().getId()}</c:if><c:if test="${dialogList.get(0).getReceiver().getId() != user.getId()}">${dialogList.get(0).getReceiver().getId()}</c:if>" name="receiverid">
							<input type="submit" value="Відправити повідомлення" class="btn btn-default">
						</form>
					</c:when>
					<c:otherwise>
						<strong>Цей користувач заблокував вас</strong>
					</c:otherwise>
				</c:choose>	
				<h3>Діалог</h3>
				<c:forEach var="each" items="${dialogList}">	
					<div class="well" style="font-size:medium;">
						<p>
								Від: ${each.getSender().getSecondName()} ${each.getSender().getFirstName()}
							 	До: ${each.getReceiver().getSecondName()} ${each.getReceiver().getFirstName()}  ${each.getFormatedDate()}
						</p> 
							<c:choose>
								<c:when test="${!each.isReaded()}">
				    						<strong>${each.getMessage()}</strong>
								</c:when>
								<c:otherwise>
				    						${each.getMessage()}
				    			</c:otherwise>									
							</c:choose>
						</div>	
				</c:forEach>
			</div>
	</body>
</html>