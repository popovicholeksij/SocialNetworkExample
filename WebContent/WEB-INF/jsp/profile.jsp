<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Соціальна мережа - ${thisUser.getFirstName()} ${thisUser.getSecondName()}</title>
		<link href="webjars/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="webjars/bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css" rel="stylesheet">
		<link href="./css/navbar.css" rel="stylesheet">
		<script src="webjars/jquery/2.1.1/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<style>
			table.profiletable td{
				padding: 10px;
			}
		</style>
	</head>
	<body>

	<div class="container">

		<jsp:include page="menu.jsp" />
		<div style="width: 1140px; overflow: hidden">
			<div style="float: left; width: 550px" >
				<a href="${thisUser.getAvatarPath()}" target="_blank"><img
					class="img-thumbnail" src="${thisUser.getAvatarPath()}"
					height="auto" width="450" vspace="25"></a>
					
					<c:choose>
					<c:when test="${thisUser.getId() == user.getId()}">             					<%-- якщо це наша сторінка --%>
						<c:choose>
							<c:when test="${haveAvatar}">
								<form action=deleteavatar.action method="post">
									<input type="hidden" name="userid" value="${user.getId()}" /> 
									<span class="input-group-btn">
										<button class="btn btn-default" type="submit" style="margin-left: 5px">Видалити аватар</button>
									</span>
								</form>
							</c:when>
							<c:when test="${!haveAvatar}">
								<form method="post" action="avatar.action" accept="image/jpeg" enctype="multipart/form-data">
									<input type="file" name="file" required />
									<button type="submit" class="btn btn-default">Змінити фотографію</button>
								</form>
							</c:when>
						</c:choose>
					</c:when>
						
					<c:when test="${friendsIdsList.contains(thisUser.getId())}">														<%-- якщо це сторінка друга --%>
						<br>
						<strong>Це ваш друг</strong><br><br>
						<form action="mymessages.action"  class="sendmessagebutton">
				 			<input type="hidden" name="receiverid" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Надіслати повідомлення</button>
						</form>
						<br>
						<a href="gallery.action?userid=${thisUser.getId()}"><input value="Галерея користувача" type="button" class="btn btn-default"  style="margin-left: 5px"></a>
						
						<br>
					</c:when>
						
					<c:when test="${thisUserIgnoreIdsList.contains(user.getId())}">													<%-- сторінка користувача, який заблокував нас --%>
						<br>
						<strong>Цей користувач заблокував вас</strong>
						<br>
						<form action="addtoignore.action" class="sendmessagebutton" method="post">
				 			<input type="hidden" name="ignoreUserId" value="${thisUser.getId()}" />
						<button class="btn btn-default" type="submit" style="margin-left: 5px">Заблокувати користувача</button>
						</form>
					</c:when>
						
					<c:when test="${ignoreIdsList.contains(thisUser.getId())}">													<%-- сторінка користувача, якого заблокували ми --%>
						<br><strong>Ви заблокували цього користувача</strong><br>
					</c:when>
						
					<c:when test="${requestFriendshipIdsList.contains(user.getId())}">												<%-- сторінка користувача, який відправив нам запит у друзі --%>
						<br><strong>Цей користувач відправив вам запит у друзі</strong><br>
						<form action="mymessages.action"  class="sendmessagebutton">
				 			<input type="hidden" name="receiverid" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Надіслати повідомлення</button>
						</form>
					</c:when>	
						
					<c:when test="${sendRequestFriendshipIdsList.contains(thisUser.getId())}">							  		   <%-- сторінка користувача, якому ми відправили запит у друзі --%>
						<br><strong>Ви відправили запит у друзі цьому користувачу</strong><br>
						<form action="mymessages.action"  class="sendmessagebutton">
				 			<input type="hidden" name="receiverid" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Надіслати повідомлення</button>
						</form>
					</c:when>
						
					<c:when test="${thisUser.getId() != user.getId()}">																<%-- сторінка незнайомого користувача (не в друзях, не в ігнорі) --%>
						<form action="mymessages.action"  class="sendmessagebutton">
				 			<input type="hidden" name="receiverid" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Надіслати повідомлення</button>
						</form>
						<form action="addtofriends.action" class="sendmessagebutton" method="post">
						 	<input type="hidden" name="friendid" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Долучити до друзів</button>
						</form>
						<form action="addtoignore.action" class="sendmessagebutton" method="post">
				 			<input type="hidden" name="ignoreUserId" value="${thisUser.getId()}" />
							<button class="btn btn-default" type="submit" style="margin-left: 5px">Заблокувати користувача</button>
						</form>
					</c:when>
				</c:choose>
			</div>

			<div style="width: 550px; float: right; font-size: x-large; margin-top: 25px;">
				<table class="profiletable">
					<tr>
						<td><strong>${thisUser.getFirstName()}
								${thisUser.getSecondName()}</strong></td>
					</tr>
					<tr>
						<td>Дата народження</td>
						<td>${thisUser.getDateOfBirth()}</td>
					</tr>
					<tr>
						<td>Країна</td>
						<td>${thisUser.getCountry().getName()}</td>
					</tr>
					<c:if test="${!thisUser.getCity().isEmpty()}">
						<tr>
							<td>Місто</td>
							<td>${thisUser.getCity()}</td>
						</tr>
					</c:if>
					<tr>
						<td>Телефон</td>
						<td>${thisUser.getPhoneNumber()}</td>
					</tr>
					<tr>
						<td>email</td>
						<td>${thisUser.getEmail()}</td>
					</tr>
					<c:if	test="${!thisUser.getSchool().isEmpty() || !thisUser.getUniversity().isEmpty() }">
						<tr>
							<td><strong>Освіта:</strong></td>
						</tr>
						<c:if test="${!thisUser.getSchool().isEmpty()}">
							<tr>
								<td>Школа</td>
								<td>${thisUser.getSchool()}</td>
							</tr>
						</c:if>
						<c:if test="${!thisUser.getUniversity().isEmpty()}">
							<tr>
								<td>ВНЗ</td>
								<td>${thisUser.getUniversity()}</td>
							</tr>
						</c:if>
					</c:if>
				</table>
			</div>
		</div>
		<br>
			
			<br>
			<br>
			
			<c:if test="${thisUser.getId() == user.getId() || friendsIdsList.contains(thisUser.getId())}">
				<div class="col-lg-6" style="float:right; width:1140px; padding:0px">
					<form class= "wallmessageField" method="post">
						<div class="input-group">
							<input type="hidden" name="receiverid" value="${thisUser.getId()}"/>
							<input type="text" name= "wallmessage" class="form-control" placeholder="Залишити запис на стіні..." required style="height: 40px;"> 
							<span class="input-group-btn">
								<button class="btn btn-default" style="height: 40px;" type="submit">Відправити</button>
							</span>
						</div>
					</form>
				</div>
				<br>
				<br>
				<br>
				<br>
			</c:if>
			
			
			
			<c:forEach var="each" items="${wallMessageList}">
				<c:choose>
					<c:when test="${each.getSender().getId() == thisUser.getId()}">
						<div class="well" style="font-weight:bold;">
							<p>Від: ${each.getSender().getSecondName()} ${each.getSender().getFirstName()} ${each.getFormatedDate()}</p> 
							<p>${each.getMessage()}</p>
						</div>
					</c:when>
					<c:otherwise>
						<div class="well">
							<p>Від: ${each.getSender().getSecondName()} ${each.getSender().getFirstName()} ${each.getFormatedDate()}</p> 
							<p>${each.getMessage()}</p>
						</div>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			
		</div>
			
	</body>
</html>