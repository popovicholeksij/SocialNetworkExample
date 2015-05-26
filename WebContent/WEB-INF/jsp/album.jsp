<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Альбом ${album.getTitle()}</title>
		<link href="webjars/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
		<link href="webjars/bootstrapvalidator/0.5.0/css/bootstrapValidator.min.css" rel="stylesheet">
		<link href="./css/navbar.css" rel="stylesheet">
		<script src="webjars/jquery/2.1.4/jquery.min.js"></script>
		<script src="webjars/bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
		<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			$( document ).ready(function() {
				$("button[name='removeimage']").click(function () {
					var imageId = $(this).parent().parent().parent().attr("id");
					var id = imageId.replace("image", "");
					var url = "deleteimage.action?albumid=" + ${album.getId()} + "&imageid=" + id;
					$.get( url, function( data ) {
						if (data === "OK") {
							$("#" + imageId).remove();
						}
					});
				});
			});
		</script>
	</head>
	<body>
		<div class="container">
			<jsp:include page="menu.jsp" /><br>
			<h3>Галерея користувача ${user.getFirstName()} ${user.getSecondName()} Альбом ${album.getTitle()}</h3>
			<c:choose>
				<c:when test="${imagesList.isEmpty()}">
					<p>Немає фотографій в цьому альбомі</p>
				</c:when>
				<c:otherwise>
			<div style="width: 1140px; overflow: hidden">
				<c:forEach var="each" items="${imagesList}">	
					<div class="col-sm-6 col-md-4 " style="width: 350 px" id="image${each.getId()}">
						<div class="thumbnail">
							<a href="galleries/${each.getOwnerId()}/${album.getTitle()}/${each.getFilename()}">
								<img src="galleries/${each.getOwnerId()}/${album.getTitle()}/${each.getFilename()}">
							</a>
							<div class="caption">
								${each.getTitle()}<br>
								Створено: ${each.getFormatedDate()}
								<c:if test="${album.getOwnerId() == user.getId()}">
									<button class="close" name="removeimage">&times;</button>							
								</c:if>
							</div>
						</div>
					</div>
				</c:forEach>
			</div>	
				</c:otherwise>
			</c:choose>
			<br>
			<br>
			
			<div style="width: 1000 px;" align="left">
				<c:if test="${user.getId() == album.getOwnerId()}" >
					<form enctype="multipart/form-data" method="post">
				   		<p>Завантажити нові фотографії</p>
				   		<p>
				   			<input type="hidden" name="albumid" value="${album.getId()}">
				   			<input type="file" name="files[]" multiple accept="image/*,image/jpeg">
				   			<input type="submit" value="Отправить" class="btn btn-default" style="margin-top: 5px">
				   		</p>
			   		</form>
		   		</c:if>
		   	</div>	
		   	
			<br>
			<br>
					
			<c:if test="${pagesCount > 1}">
				<nav>
					<ul class="pagination">
						<c:forEach var="index" begin="1" end="${pagesCount}">
							<li><a href="album.action?id=${album.getId()}&page=${index}">${index}</a></li>
						</c:forEach>
					</ul>
				</nav>
			</c:if>
		</div>
	</body>
</html>