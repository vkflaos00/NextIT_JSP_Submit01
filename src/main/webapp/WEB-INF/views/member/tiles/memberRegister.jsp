<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/login.css">
</head>
<body>
	<div class="container">
		<c:if test="${bne eq null && bde eq null && se eq null}">
			<h3>회원등록 성공</h3>
			<div class="alert alert-success">
				<div class="btn-area">
					<button type="button"
						onclick="location.href='${pageContext.request.contextPath}/login/login.jsp'">확인</button>
				</div>
			</div>
		</c:if>
		<c:if test="${bde ne null }">
			<h3>회원등록 실패</h3>
			<div class="alert alert-success">
				<p>사용중인 아이디. 다른 아이디를 사용해라.</p>
				<div class="btn-area">
					<button type="button" onclick="history.back();">뒤로가기</button>
				</div>
			</div>
		</c:if>
		<c:if test="${bne ne null or se ne null }">
			<h3>회원등록 실패</h3>
			<div class="alert alert-success">
				<div class="btn-area">
					<button type="button" onclick="history.back();">뒤로가기</button>
				</div>
			</div>
		</c:if>
	</div>
</body>
</html>