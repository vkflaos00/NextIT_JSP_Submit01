<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HEALTHY</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/login.css">
<script type="text/javascript">

	function fn_login() {
		let f = document.loginForm;
		console.log("f:", f);
		f.action = "${pageContext.request.contextPath}/login/loginCheck";
		f.submit();
	}
	
	$(function(){
		window.setTimeout(function(){
			let lc = $("#loginCheck").val();
			if(lc == 'fail'){
				alert("로그인 실패. ID 또는 PASSWORD 확인.");
			}else if( lc == 'none'){
				alert("로그인 해야함");
			}else if( lc == 'error'){
				alert("에러 발생");
			}else if( lc == 'sign'){
				alert("회원가입완료.");
			}else if( lc == 'quit'){
				alert("회원탈퇴완료.");
			}
		},200);
	});
</script>
</head>
<body>
	<input type="hidden" id="loginCheck" value="${msg }">
	<section class="login_form">

		<form name="loginForm" method="post">
			<div class="login">
				<img src="${pageContext.request.contextPath }/images/login.png">
			</div>

			<div class="int-area">
				<label for="memId"></label> <input type="text" id="memId"
					name="memId" value="${memId }" placeholder="ID" autocomplete="off"
					required>
			</div>
			<div class="int-area">
				<label for="memPass"></label> <input type="password" id="memPass"
					name="memPass" placeholder="PASSWORD" autocomplete="off" required>
			</div>
			<div class="div_rememberMe">
				<label for="rememberMe"> <input type="checkbox"
					id="rememberMe" name="rememberMe" value="Y" ${checkBox } /> ID 저장
				</label>
			</div>
			<div class="btn-area">
				<button type="button" id="btn_id" name="sbtn_id"
					onclick="fn_login()">LOGIN</button>
				<button type="button" id="btn_join" name="btn_join"
					onclick="location.href='${pageContext.request.contextPath}/login/join'">JOIN</button>
			</div>
		</form>
	</section>
</body>
</html>