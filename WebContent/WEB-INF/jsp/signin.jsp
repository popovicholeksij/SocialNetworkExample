<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Увійти</title>
		<link href="webjars/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="webjars/bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css" rel="stylesheet">
		<link href="./сss/signin.css" rel="stylesheet">
		<script src="webjars/jquery/2.1.1/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	</head>
	<body>

		<div class="container">
			<form method="post" class="form-signin">
				<h2 class="form-signin-heading">Увійти</h2>
				<label for="inputEmail" class="sr-only">Email адреса</label> <input type="email" id="inputEmail" class="form-control" name="email" placeholder="email" required autofocus>
			    <label for="inputPassword" class="sr-only">Пароль</label> <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Пароль" required>
				<button class="btn btn-lg btn-primary btn-block" type="submit">Увійти</button>
				<h4><a href="signup.action" class="form-signin-heading">Реєстрація</a></h4>
			</form>
		</div>
	
	</body>
</html>