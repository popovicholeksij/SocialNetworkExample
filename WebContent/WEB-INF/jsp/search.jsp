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
		<script src="webjars/jquery/2.1.4/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<title>Пошук користувачів</title>
		<script type="text/javascript">
			function search() {
				var userName = document.getElementById("username").value;
				var secondName = document.getElementById("secondname").value;
				var gender = document.getElementById("gender").value;
				var fromAge = document.getElementById("fromage").value;
				var toAge = document.getElementById("toage").value;
				var country = document.getElementById("country").value;
				var city = document.getElementById("city").value;
				var phoneNumber = document.getElementById("phonenumber").value;
				var email = document.getElementById("email").value;
				var school = document.getElementById("school").value;
				var university = document.getElementById("university").value;
				
				var url = "http://localhost:8080/SocialNetwork/search-ajax.action?username=" + userName + "&secondname=" + secondName + "&gender=" + gender 
						+ "&fromage=" + fromAge + "&toage=" + toAge + "&country=" + country + "&city=" + city + "&phonenumber=" + phoneNumber + "&email=" + email 
						+ "&school=" + school + "&university=" + university;
				$.get( url, function( data ) {
	            	document.getElementById("search-results").innerHTML = data;
				});
			}
		
		</script>
		
	</head>
	<body>
		<div class="container">
			<jsp:include page="menu.jsp" /><br>
			<h1>Пошук користувачів</h1>
			<div style="width: 400px; float: left">
				<input type="text" id="username" placeholder="Ім'я">
				<br>
				<br>
				<input type="text" id="secondname" placeholder="Прізвище">
				<br>
				<br>
				<select id="gender">
					<option value="">Виберіть стать</option>
					<option value="1">Чоловіча</option>
					<option value="2">Жіноча</option>
				</select>
				<br>
				<br>
				<input type="text" id="fromage" placeholder="вік від" pattern="\d{2}">
				<input type="text" id="toage" placeholder="вік до" pattern="\d{2}">
				<br>
				<br>
				<select id="country">
					<option value="">Виберіть країну</option>
					<c:forEach var="each" items="${countriesList}">
						<option value="${each.getId()}">${each.getName()}</option>
					</c:forEach>
				</select>
				<br>
				<br>
				<input type="text" id="city" placeholder="Місто">
				<br>
				<br>
				<input type="text" id="phonenumber" placeholder="+380979132103">
				<br>
				<br>
				<input type="text" id="email" placeholder="e-mail">
				<br>
				<br>
				<input type="text" id="school" placeholder="Школа № 11">
				<br>
				<br>
				<input type="text" id="university" placeholder="ЧДТУ">
				<br>
				<br>
				<button class="btn btn-default" onclick="search()">Пошук</button>
			</div>
							
			<div id="search-results" style="width: 600px; float: right" ></div>
		</div>
		
	</body>
</html>