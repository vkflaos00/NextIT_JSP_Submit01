<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HEALTHY</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/home.css">

<script>
	function fn_reset() {
		let ret = confirm("정말 피우셨습니까? 장난치면 곤란합니다.");
		if (ret) {
			location.href = "${pageContext.request.contextPath}/member/memberReset";
		}
	}

	$(document).ready(
			function() {
				var count = parseInt(getCookie("hitButton"));

				if (isNaN(count)) {
					count = 0;
				}
				$("#count").text(count);

				function fn_hit() {
					count++;
					$("#count").text(count);

					document.cookie = "hitButton="
							+ count
							+ "; expires="
							+ new Date(new Date().getTime()
									+ (2 * 365 * 24 * 60 * 60 * 1000))
									.toUTCString();
				}

				$("#hitButton").click(fn_hit);

				function getCookie(name) {
					var value = "; " + document.cookie;
					var parts = value.split("; " + name + "=");
					if (parts.length === 2) {
						return parts.pop().split(";").shift();
					}
					return "";
				}
			});
</script>

</head>
<body>
             	<c:choose>
             		<c:when test="${bne eq null and de eq null }">
						<c:if test="${not empty member.atchNo }">
	             			<img alt="프로필사진" src="<c:url value='/image/${member.atchNo } '/>">
						</c:if>	
             		</c:when>
             	</c:choose>
	<div id="content-container">
		<div class="lung">
			<img src="${pageContext.request.contextPath}/images/lung.png"
				alt="${memberVO.memName} 의  폐">
		</div>

		<div class="content">

			<p class="text-center">${memberVO.memName} 탈출까지${memberVO.dDay}일</p>
			<button type="button" id="hitButton" name="hitButton">말릴때마다
				누르는 버튼</button>
			<p class="text-center">
				현재까지 <span id="count"></span> 번 만큼 참아냈단다.
			</p>
			<button type="button" id="resetButton" name="resetButton" value="버튼"
				onclick="fn_reset()">피우면 누르는 버튼</button>
		</div>
	</div>
</body>
</html>