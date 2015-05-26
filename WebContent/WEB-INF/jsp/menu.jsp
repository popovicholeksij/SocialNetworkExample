<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<div class="navbar-brand">Соціальна мережа</div>
			</div>
			<div id="navbar" class="navbar-collapse collapse" style="font-weight:bolder">
				<ul class="nav navbar-nav">
					<li><a href="./">Моя сторінка</a></li>
					<li><a href="friends.action">Друзі</a></li>
					<li> <a href="mynews.action">Новини</a></li>
					<li><a href="gallery.action?userid=${user.getId()}">Галерея</a></li>
					<li><a href="mymessages.action">Повідомлення</a></li>
					<li><a href="editprofile.action">Налаштування</a></li>
					<li><a href="search.action">Пошук</a></li>
					<li>
						<c:if test="${user == null}"><a href="signin.action">Зайти</a></c:if>
						<c:if test="${user != null}"><a href="signout.action">Вийти [${user.getFirstName()} ${user.getSecondName()}]</a></c:if>
					</li>
				</ul>
			</div>
		</div>
	</nav>