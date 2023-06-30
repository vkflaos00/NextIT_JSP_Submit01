<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>RESET</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/login.css">
</head>
<body>
	<div class="container">
		<c:if test="${bpe eq null and bne eq null and se eq null }">

			<div>
				<p>한 대라도 피우면 금연 중이 아니란다.</p>
				<p>너의 도전은 리셋되었다.</p>
				<p>마음을 다잡고 다시 도전해라.</p>
				<div class="btn-area">
					<button type="button"
						onclick="location.href='${pageContext.request.contextPath}/login/login.jsp'">확인</button>
				</div>
			</div>
		</c:if>
		<c:if test="${bne ne null or se ne null }">
			<h3>실패????</h3>
			<div>
				<div class="btn-area">
					<button type="button" onclick="history.back();">뒤로가기</button>
					<button type="button"
						onclick="location.href='${pageContext.request.contextPath}/home/home.jsp'">홈</button>
				</div>
			</div>
		</c:if>
	</div>
</body>
</html>