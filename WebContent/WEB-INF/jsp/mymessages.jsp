<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Мої повідомлення</title>
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
			<br>
			<h3>Діалоги</h3>
			<c:forEach var="eachlist" items="${messagesGroupList}">	
				<div class="well" style="font-weight:bold; font-size:medium;">
					<a href="dialog.action?senderid=${eachlist.get(0).getSender().getId()}&receiverid=${eachlist.get(0).getReceiver().getId()}">	
						Від: ${eachlist.get(0).getSender().getSecondName()} ${eachlist.get(0).getSender().getFirstName()}
					 	до: ${eachlist.get(0).getReceiver().getSecondName()} ${eachlist.get(0).getReceiver().getFirstName()}  ${eachlist.get(0).getFormatedDate()}
					</a>	
				</div>
			</c:forEach>
			<br>
			<br>
			
			
				<form method="post" action="mymessages.action">
					<h3>Написати повідомлення</h3>
						<c:choose>
							<c:when test="${userIdsFriendsList.contains(receiverId) || receiverId == null}">						
									<select name="receiverid" required>
									<option value="">Оберіть отримувача</option>
								<c:forEach var="each" items="${usersFriendsList}">
									<option value="${each.getId()}" <c:if test="${each.getId() == receiverId}"> selected</c:if>>${each.getFirstName()}  ${each.getSecondName()}</option>
								</c:forEach>
									</select>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="receiverid"  value="${receiverId}">
							</c:otherwise>
						</c:choose>
					<br>
					<br>
					<textarea name="message" wrap="soft" COLS="100" ROWS="7" placeholder="Ваше повідомлення..."></textarea><br>
					<br>
					<br>
					<input type="submit" value="Відправити повідомлення" class="btn btn-default">
				</form>
			
			</div>
	</body>
</html>