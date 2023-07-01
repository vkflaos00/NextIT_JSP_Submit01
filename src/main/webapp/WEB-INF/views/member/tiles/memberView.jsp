<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/home.css">

<div id="div_table">
	<c:choose>
		<c:when test="${bne ne null or de ne null}">
			<h3>회원 정보 조회 실패</h3>
			<div class="alert alert-success">
				<p>회원 정보 조회 실패 하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850</p>
				<div class="btn-area">
					<button type="button" onclick="history.back();">뒤로가기</button>
				</div>
			</div>
		</c:when>
		<c:when test="${bne eq null and de eq null }">
			<c:if test="${not empty member.atchNo }">
				<img alt="프로필사진" src="<c:url value='/image/${member.atchNo } '/>"
					class="center-align">
			</c:if>
			<table>
				<tbody>
					<tr>
						<td class="td_left">아이디</td>
						<td class="td_right">${member.memId }</td>
					</tr>
					<tr>
						<td class="td_left">닉네임</td>
						<td class="td_right">${member.memName }</td>
					</tr>
					<tr>
						<td class="td_left">권한</td>
						<td class="td_right">${member.memRole }</td>
					</tr>
					<tr>
						<td class="td_left">메일</td>
						<td class="td_right">${member.memMail }</td>
					</tr>
				</tbody>
			</table>
		</c:when>
	</c:choose>
</div>