<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일 인증</title>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<style type="text/css">
body{
    background: lightgray;
    text-align: center;
}
input{
    margin-top: 60px;
    height: 25px;
    border-radius: 10px;
}
button{
	border-radius: 10px;
}
</style>

</head>
<body>

인증번호 <input type="text" id="key" name="key"	value="" >
<button type="button" id="authKeyCompare">인증키 확인</button>

<script type="text/javascript">
$(document).ready(function(){
	$("#authKeyCompare").on("click", function(){
		
		let mail = opener.document.getElementById("memMail").value;
		let key = $("input[name='key']").val();
		
		$.ajax({
			url: "<c:url value='/join/authKeyCompare'/>"
			,data : {"mail":  mail , "key": key }
			,success: function(data){
				if(data){
					alert("인증 완료.");
					window.close();
				}else{
					alert("인증키 틀림.");
				}
			}
			,error : function(e){
				alert("메일 인증 처리 문제");
			}
		});
		
	});
});
</script>
</body>
</html>