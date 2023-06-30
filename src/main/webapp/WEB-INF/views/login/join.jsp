<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>join</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/join.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

<script>
$(function() {
		$("#memId").click(function() {
			$(this).next().removeClass("warning");
		});
		$("#memPass").click(function() {
			$(this).next().removeClass("warning");
		});
		$("#memPassCheck").click(function() {
			$(this).next().removeClass("warning");
		});
		$("#memName").click(function() {
			$(this).next().removeClass("warning");
		});
		$("#memMail").click(function() {
			$(this).next().removeClass("warning");
		});

		$("input, select").click(function() {
			$(this).next("span").remove();
		});
		$("input").change(function() {
			if ($(this).val() == "") {
				$(this).nextAll("label").removeAttr("style");
			}
		});
		window.setTimeout(function() {
			if ($("#memId").val() != "") {
				$("label[for='memId']").css({
					"top" : "35px",
					"font-size" : "13px",
					"color" : "#166cea"
				});
			}
			if ($("#memName").val() != "") {
				$("label[for='memName']").css({
					"top" : "90px",
					"font-size" : "13px",
					"color" : "#166cea"
				});
			}
			if ($("#memPass").val() != "") {
				$("label[for='memPass']").css({
					"top" : "145px",
					"font-size" : "13px",
					"color" : "#166cea"
				});
			}
			if ($("#memMail").val() != "") {
				$("label[for='memMail']").css({
					"top" : "450px",
					"font-size" : "13px",
					"color" : "#166cea"
				});
			}
		}, 500);
	});
let idCheck = false;
function join(){

    console.log("join");  
   let memId = $("#memId").val();
	console.log("memId:", memId);
	$.ajax({
		url: "<c:url  value='/join/idCheck' />"
		,type:"post"
		,data:{"memId": memId}
		,async:false
		,success:function(data){
			console.log("data: ", data);
			if(data){
				idCheck = true;
			}else{
				$("#memId").val("");
				alert("이미 사용중인 아이디.");
			}
		}
		,error:function(){
			alert("error");
		}
	});
	
    if(!idCheck){
    	return;
    }
    
    let f =  document.loginForm;
    f.action = "${pageContext.request.contextPath}/member/memberRegister";
    f.submit();
};
 
function fn_checkId(){
	
	let memId = $("#memId").val();
	console.log("memId:", memId);
	
	$.ajax({
		url: "<c:url  value='/join/idCheck' />"
		,type:"post"
		,data:{"memId": memId}
		,success:function(data){
			console.log("data: ", data);
			if(data){
				alert("사용 가능 아이디.");
				idCheck = true;
			}else{
				$("#memId").val("");
				alert("이미 사용중인 아이디.");
			}
		}
		,error:function(){
			alert("error");
		}
		
	});
}
</script>
</head>
<body>
	<section class="login_form">
		<form:form name="loginForm" method="post" modelAttribute="member"
			enctype="multipart/form-data">
			<div class="profile_image">
				<div class="upload">
					<!-- 사진이 보여줄 위치 -->
				</div>
				<input type="file" class="real-upload" name="profilePhoto"
					accept="image/*">
				<ul class="image-preview"></ul>
			</div>
			<div class="int-area">
				<form:input path="memId" placeholder="ID" autocomplete="off" />
				<form:errors path="memId"></form:errors>
				<label for=memId></label>
				<button type="button" id="check_id" name="check_id"
					onclick="fn_checkId()">ID확인</button>
			</div>
			<div class="int-area">
				<form:password path="memPass" placeholder="PASSWORD"
					autocomplete="off" />
				<form:errors path="memPass" />
				<label for=memPass></label>
			</div>
			<div class="int-area">
				<label for=memPassCheck></label> <input type="password"
					id="memPassCheck" name="memPassCheck" placeholder="PASSWORDCHECK"
					value="" autocomplete="off" required="required">
			</div>
			<div class="int-area">
				<form:input path="memName" placeholder="NICKNAME" autocomplete="off" />
				<form:errors path="memName" />
				<label for=memName></label>
			</div>
			<div class="int-area">
				<form:input path="memMail" placeholder="E-MAIL" autocomplete="off" />
				<form:errors path="memMail" />
				<label for=memMail></label>
				<button type="button" id="mailAuth">mail인증</button>
			</div>
			<div class="btn-area">
				<button type="button" id="btn_join" name="btn_join" onclick="join()">가입</button>
			</div>
		</form:form>
	</section>

	<script>
function getImageFiles(e) {
	const files = e.currentTarget.files;
	const imagePreview = document.querySelector('.image-preview');
	const file = files[0];
	const reader = new FileReader();
	reader.onload =  function(e){  
		const preview = createElement(e, file);
		let imageLiTag = document.querySelector('.image-preview > li');
		if(imageLiTag){
			imagePreview.removeChild(imagePreview.firstElementChild);
		}
		imagePreview.appendChild(preview);
	};
	reader.readAsDataURL(file); 
}

function createElement(e, file) {
	const li = document.createElement('li');
	const img = document.createElement('img');
	img.setAttribute('src', e.target.result); 
	img.setAttribute('data-file', file.name);
	li.appendChild(img);
	return li;
}

const realUpload = document.querySelector('.real-upload');
const upload = document.querySelector('.upload');

upload.addEventListener('click', function(e){
	realUpload.click();  
});

realUpload.addEventListener('change', getImageFiles);
</script>

	<script type="text/javascript">
$("#mailAuth").on("click", function(){
	let memMail = $("#memMail");
	console.log("memMail: ", memMail.val());
	
	let checkMemMail = /^[-_a-zA-z0-9]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,4}$/i;
	console.log("checkMemMail: ", checkMemMail.test(memMail.val()) );
	if( ! checkMemMail.test(memMail.val()) ){
    	alert("이메일 양식 틀림"); 
    	memMail.val("");    	
    	memMail.next("label").addClass("warning");
        return;
    }
	
	$.ajax({
		url:"<c:url value='/join/mailAuth' />"
		,type:"post"
		,data:{"mail" : memMail.val()}
		,success:function(data){
			console.log("data :", data);
			
			let popupWidth = 600;
			let popupHeight = 150;
			let winWidth = document.body.offsetWidth;
			let winHeight = document.body.offsetHeight;
			let popupX = (winWidth/2) - (popupWidth/2);  
			let popupY = (winHeight/2) - (popupHeight/2);

			if(data){
				let myWin = window.open("<c:url value='/join/mailWindow'/>" 
					,"mywin"
					,"left="+popupX+"px,"
					+"top="+popupY+"px, "
					+"width="+popupWidth+"px, "
					+"height="+popupHeight+"px");
			}else{
				alert("메일 발송 중 문제 발생.");
			}
		}
		,error:function(){
			alert("error");
		}
		,beforeSend:function(){
			let width = 150;
			let height = 150;
			let top = 50;
			let left = 50;
			$('body').append('<div id="div_ajax_load_image" style="position:absolute; top:' 
				+ top + '%; left:' 
				+ left + '%; width:' 
				+ width + 'px; height:' 
				+ height + 'px; z-index:9999;'
				+'background:transparent; '
				+'filter:alpha(opacity=50); '
				+'opacity:alpha*0.5; '
				+'margin:auto; '
				+'padding:0; ">'
				+'<img src="<c:url value ='/images/ajax_loader6.gif'/>"'
				+'style="width:150px; height:150px;"></div>');	
		}
		,complete:function(){
			$("#div_ajax_load_image").remove();
		}
	});
});

</script>
</body>
</html>