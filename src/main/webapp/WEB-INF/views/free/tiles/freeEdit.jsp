<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/freeBoardView.css">
<script type="text/javascript" src="${pageContext.request.contextPath }/smarteditor2-2.8.2.3/js/HuskyEZCreator.js" charset="utf-8"></script>

<script>
let oEditors = [];
smartEditor = function() {
  console.log("Naver SmartEditor")
  nhn.husky.EZCreator.createInIFrame({
    oAppRef: oEditors,
    elPlaceHolder: "editorTxt",
    sSkinURI: "${pageContext.request.contextPath }/smarteditor2-2.8.2.3/SmartEditor2Skin.html",
    fCreator: "createSEditor2"
  })
}
$(document).ready(function() {
  smartEditor();
});
</script>

<script>
function fn_submitCheck(){
	console.log("fn_submitCheck");
	
	oEditors.getById["editorTxt"].exec("UPDATE_CONTENTS_FIELD", []);  
	
	if($("input[name='boTitle']").val() == ""){
		alert("제목.");
		return;
	}else if($("input[name='boPass']").val() == ""){
		alert("비밀번호.");
		return;
	}else if($("input[name='boCategory']").val()==""){
		alert("글 분류.");
		return;
	}

		let f= document.freeModify;
		f.submit();
}
</script>

<style type="text/css">
.file_area > div > div:first-child > span{
	float: left;
}
.file_area > div > div:first-child > a{
	float: left;
}
.file_area > div > div:first-child > button{
	float: right;
}
.file_area > div > div:last-child{
	clear:both;
}
.btn_delete{
	float: right;
}
.td_right a{
	color: #166cea;
}
.td_right a:hover{
	color: #ed5422;
}

.td_right{
	width:600px;
}
#editorTxt{
	width: 595px;
}

</style>

    <div class="contents">
        <div class="content01">
            <div class="content01_h1">
            </div>
            
				<c:if test="${bne ne null or de ne null }">
					<div class="alert alert-warning">
							글로드실패
					</div>	
					<div class="div_button">
	                      <input type="button" onclick="location.href='${pageContext.request.contextPath}/free/freeList'" value="목록">
	                </div>
                </c:if>
            
            	<c:if test="${bne eq null and de eq null }">

					<form:form name="freeModify" action="${pageContext.request.contextPath }/free/freeModify" 
            			method="post" 
            			modelAttribute="freeBoard" 
           				enctype="multipart/form-data">
            	      <div id="div_table">
	                      <table>
	                          <colgroup>
	                              <col width="200">
	                              <col width="400">
	                          </colgroup>
	                          <tr>
	                              <td class="td_left">글번호</td>
	                              <td class="td_right">
	                                  ${freeBoard.boNo }
	                                  <input type="hidden" name="boNo" value="${freeBoard.boNo }">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글제목</td>
	                              <td class="td_right">
	                              		<form:input path="boTitle"/>
										<form:errors path="boTitle"/>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">작성자명</td>
	                              <td class="td_right">
										${freeBoard.boWriter } 
										<input type="hidden" name="boWriter" value="${freeBoard.boWriter }">
										<form:errors path="boWriter"/>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글 비번</td>
	                              <td class="td_right">
										<form:password path="boPass"/>
										<form:errors path="boPass"/>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글 분류</td>
	                              <td class="td_right">
	                              		<form:select path="boCategory">
											<form:option value="">-- 선택하세요--</form:option>
											<form:options items="${categoryList}" itemLabel="commNm" itemValue="commCd"/>
										</form:select>
										<form:errors path="boCategory"/>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">내용</td>
	                              <td class="td_right">
	                              
						             <textarea name="boContent" id="editorTxt" 
						                  rows="20" cols="10">${freeBoard.boContent }</textarea>
						              <form:errors path="boContent"/>    
					                       
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">IP</td>
	                              <td class="td_right">
	                              		${pageContext.request.remoteAddr}
	                              		<input type="hidden" name="boIp" value="${pageContext.request.remoteAddr}">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">조회수</td>
	                              <td class="td_right">
	                              		${freeBoard.boHit }
	                              </td>
	                          </tr>
	                      </table>
	                  </div>
	                  
		                <!-- 버튼 -->
		                <div class="div_button">
		                	<c:if test="${freeBoard.boWriter eq memberVO.memId  }">
			                    <input type="button" onclick="fn_submitCheck()" value="저장">
		                	</c:if>
		                    <input type="button" onclick="location.href='${pageContext.request.contextPath}/free/freeList'" value="목록">
		                </div>
	                </form:form>
             	</c:if>
        </div>
    </div>


<script type="text/javascript">
$('#id_btn_new_file').click(function(){ 
	$('.file_area').append('<div class="file_div">' 
	+ '<input type="file" name="boFiles">' 
	+ ' <button type="button" class="btn_delete">삭제</button>'
	+ '</div>');
}); 
	
$('.file_area').on('click', '.btn_delete', function(){
	$(this).closest('div').remove();
});

$(".btn_file_delete").click(function(){
	alert("btn_file_delete");
	$(this).closest('.file_area').append('<input type="hidden" name="delAtchNos" value="' + $(this).data("atch-no")  + '" />');
	$(this).closest('div').parent().remove();
	
});
</script>

