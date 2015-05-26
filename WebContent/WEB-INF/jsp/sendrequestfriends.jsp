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
		<script type="text/javascript"> 
			function declineFriendship(userId) {
				// Initialize the Ajax request.
				var xhr = new XMLHttpRequest();
				xhr.open('get', 'declinefriendship.action?friendid=' + userId);
				 
				// Track the state changes of the request.
				xhr.onreadystatechange = function () {
				    var DONE = 4; // readyState 4 means the request is done.
				    var OK = 200; // status 200 is a successful return.
				    if (xhr.readyState === DONE) {
				        if (xhr.status === OK) {
				            document.getElementById(userId).remove();
				        } else {
				            alert('Error: ' + xhr.status); // An error occurred during the request.
				        }
				    }
				};
				 
				// Send the request to send-ajax-data.php
				xhr.send(null);
			}
		</script>
	</head>
	<body>
		
		<div class="container">
		
			<jsp:include page="menu.jsp" /><br>
			<br>
			<a href="friends.action"><input value="Друзі" type="button" class="btn btn-default"  style="margin-left: 5px"></a>
			<a href="incomefrienship.action"><input value="Заявки на дружбу зі мною" type="button" class="btn btn-default"  style="margin-left: 5px"></a>
			<a href="sendfrienship.action"><input value="Мої заявки на дружбу" type="button" class="btn btn-default"  style="boxing-s"></a>
			<a href="ignore.action"><input value="Заблоковані користувачі" type="button" class="btn btn-default"  style="margin-left: 5px"></a>
			
			<h3>Запрошення у друзі</h3>
			<c:choose>
				<c:when test="${sendRequestFriendshipList.isEmpty()}">
					<p>Ви не відправляли запрошень прийняти у друзі інших користувачів</p>
				</c:when>
				<c:otherwise>
					<c:forEach var="eachUser" items="${sendRequestFriendshipList}">	
						<div id="${eachUser.getId()}" style="width: 210px; float: left;">
							<a href="profile.action?id=${eachUser.getId()}">
								<img width="100" height="100" src="${eachUser.getAvatarPath()}"><br>
								${eachUser.getFirstName()} ${eachUser.getSecondName()}
							</a>
							<a href="#" onclick="declineFriendship(${eachUser.getId()})"><span class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span></a>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		
		</div>
		
	</body>
</html>


