<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<ul class="nav">
	<li><a
		onclick="location.href ='${pageContext.request.contextPath}/home.home'">[
			${memberVO.memName} ]</a></li>
	<li><a
		onclick="location.href ='">도움</a></li>
	<li><a
		onclick="location.href ='">랭크</a></li>
	<li><a
		onclick="location.href ='${pageContext.request.contextPath}/free/freeList'">게시판</a></li>
	<li><a
		onclick="location.href ='${pageContext.request.contextPath}/login/logout'">로그아웃</a></li>
	<security:authorize access="hasAuthority('ADMIN')">
		<li><a
			href="${pageContext.request.contextPath}/member/memberList">ADMIN</a></li>
	</security:authorize>
</ul>
