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
		<title>Пошук користувачів</title>
	</head>
	<body>
		<div class="container">
			<jsp:include page="menu.jsp" /><br>
			<h1>Налаштування профілю</h1>
		
			<form method="post" action="editprofile.action">
				<input type="hidden" name="id" value="${user.getId()}" >
				
				<input type="text" name="username" placeholder="Ім'я" value="${user.getFirstName()}" required>
				<br>
				<br>
				<input type="text" name="secondname" placeholder="Прізвище" value="${user.getSecondName()}" required>
				<br>
				<br>
				<select name="gender" required >
					<option disabled>Виберіть стать</option>
					<option value="1" <c:if test="${user.getGender() == 1}">selected</c:if>>Чоловіча</option>
					<option value="2" <c:if test="${user.getGender() == 2}">selected</c:if>>Жіноча</option>
				</select>
				<br>
				<br>
				<input type="text" name="dateofbirth" placeholder="дд-мм-рррр" value="${user.getDateOfBirth()}" pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}" required>
				<br>
				<br>
				<select name="country" required>
					<option disabled>Виберіть країну</option>
					<c:forEach var="each" items="${countriesList}">
						<option value="${each.getId()}" <c:if test="${user.getCountry().getId() == each.getId()}">selected</c:if>>${each.getName()}</option>
					</c:forEach>
				</select>
				<br>
				<br>
				<input type="text" name="city" placeholder="Місто">
				<br>
				<br>
				<input type="text" name="phonenumber" placeholder="+380979132103" value="${user.getPhoneNumber()}" required>
				<br>
				<br>
				<input type="text" name="email" placeholder="e-mail" value="${user.getEmail()}" required>
				<br>
				<br>
				<input type="text" name="school" placeholder="Школа № 11" value="${user.getSchool()}">
				<br>
				<br>
				<input type="text" name="university" placeholder="ЧДТУ" value="${user.getUniversity()}">
				<br>
				<br>
				<input type="submit" type="button" class="btn btn-default" value="Змінити дані">
			</form>
		</div>
	</body>
</html>