<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Відправлені заявки - ${thisUser.getFirstName()} ${thisUser.getSecondName()}</title>
		<link href="webjars/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="webjars/bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css" rel="stylesheet">
		<link href="./css/navbar.css" rel="stylesheet">
		<script src="webjars/jquery/2.1.1/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<title>Мої Новини</title>
	</head>
	<body>
		<div class="container">
			<jsp:include page="menu.jsp" /><br>
			<c:choose>
				<c:when test="${userNewsList.isEmpty()}">
					<p>Немає новин</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="each" items="${userNewsList}">
						<c:choose>		
							<c:when test="${each.getMessageType() == each.getFriendAdded()}">
								<div class="well">
									Ваш друг:  <a href="profile.action?id=${each.getSender().getId()}">${each.getSender().getFirstName()} ${each.getSender().getSecondName()}</a> ${each.getFormattedCreationDate()}
									додав в друзі: <a href="profile.action?id=${each.getObjectId()}">користувача</a><br>
								</div>
							</c:when>
							<c:when test="${each.getMessageType() == each.getPhotosAdded()}">
								<div class="well">
									Ваш друг:  <a href="profile.action?id=${each.getSender().getId()}">${each.getSender().getFirstName()} ${each.getSender().getSecondName()}</a> ${each.getFormattedCreationDate()}
									додав нові фото в <a href="album.action?id=${each.getObjectId()}">альбом</a><br>
								</div>
							</c:when>
							<c:when test="${each.getMessageType() == each.getWallMessageAdded()}">
								<div class="well">
									Ваш друг:  <a href="profile.action?id=${each.getSender().getId()}">${each.getSender().getFirstName()} ${each.getSender().getSecondName()}</a> ${each.getFormattedCreationDate()}
									опублікував запис на <a href="profile.action?id=${each.getSender().getId()}">стіні</a><br>
								</div>
							</c:when>
							<c:when test="${each.getMessageType() == each.getFriendDeleted()}">
								<div class="well">
									Ваш друг:  <a href="profile.action?id=${each.getSender().getId()}">${each.getSender().getFirstName()} ${each.getSender().getSecondName()}</a> ${each.getFormattedCreationDate()}
									видалив з друзів: <a href="album.action?id=${each.getObjectId()}">користувача</a><br>
								</div>
							</c:when>
						</c:choose>
					
					
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<br>
			<br>
		
		

			<br>
			<br>
			<br>
			<br>
			<br>
			<br>
			<c:if test="${pagesCount > 1}">		
				<c:forEach var="index" begin="1" end="${pagesCount}">
					<a href="album.action?id=${album.getId()}&page=${index}">
						${index}
					</a>
				</c:forEach>
			</c:if>
		</div>	
	</body>
</html>