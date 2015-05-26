<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Реєстрація користувача</title>
	</head>
	<body>
		<h1>Реєстрація користувача</h1>
	
		<form method="post">
			<input type="text" name="username" placeholder="Ім'я" required>
			<br>
			<br>
			<input type="text" name="secondname" placeholder="Прізвище" required>
			<br>
			<br>
			<select name="gender" required>
				<option disabled>Виберіть стать</option>
				<option value="1">Чоловіча</option>
				<option value="2">Жіноча</option>
			</select>
			<br>
			<br>
			<input type="text" name="dateofbirth" placeholder="дд-мм-рррр" pattern="(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-(19[4-9][0-9]|200[0-3])" required>
			<br>
			<br>
			<select name="country" required>
				<option disabled>Виберіть країну</option>
				<c:forEach var="each" items="${countriesList}">
					<option value="${each.getId()}">${each.getName()}</option>
				</c:forEach>
			</select>
			<br>
			<br>
			<input type="text" name="phonenumber" placeholder="+380979132103" required>
			<br>
			<br>
			<input type="text" name="email" placeholder="e-mail" required>
			<br>
			<br>
			<input type="text" name="school" placeholder="Школа № 11">
			<br>
			<br>
			<input type="text" name="university" placeholder="ЧДТУ">
			<br>
			<br>
			<input type="password" name="password" placeholder="Пароль" required>
			<br>
			<br>
			<input type="submit" value="Зареєструватись">
		</form>
	</body>
</html>