<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/freeBoardList.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/realgrid/realgrid-style.css">
<style>
#realgrid {
  width: 1080px;
  height: 500px;
}
.div_board_write{
  top: -20px;
}

</style>
<div class="contents">
				<div class="content01">
					<div class="content01_h1">
                <h2>멤버 목록</h2>
					</div>
            
			<c:if test="${bnf ne null or de ne null}">
				<div class="alert alert-warning">
					목록 로딩 실패.
				</div>	
				<div class="div_button">
					<input type="button" onclick="history.back();" value="뒤로가기">
				</div>
			</c:if>      
            
            <c:if test="${bnf eq null and de eq null}">
            	<div class="div_search">
					<form name="search" action="${pageContext.request.contextPath }/member/memberList" method="post">
						<input type="hidden" name="curPage" value="${searchVO.curPage}"> 
						<input type="hidden" name="rowSizePerPage" value="${searchVO.rowSizePerPage}">
						<div>
							<label for="id_searchType">검색</label>
							&nbsp;&nbsp;
							<select id="id_searchType" name="searchType">
								<option value="ID" ${searchVO.searchType eq "ID" ? "selected='selected'" : ""}>아이디</option>
								<option value="NM" ${searchVO.searchType eq "NM" ? "selected='selected'" : ""}>닉네임</option>
							</select>
							<input type="text" name="searchWord" value="${searchVO.searchWord }" placeholder="검색어">
							&nbsp;&nbsp;&nbsp;&nbsp;	
							<button type="submit">검 색 </button>
							<button type="button" id="id_btn_reset" >초기화</button>
						</div>
					</form>
				</div>  	
            	
            	<div class="rowSizePerPage">
					<div>
						전체 ${searchVO.totalRowCount } 건 조회
						<select id="id_rowSizePerPage" name="rowSizePerPage">
							<c:forEach begin="10" end="50" step="10" var="i">
								<option value="${i }" ${searchVO.rowSizePerPage eq i ? "selected='selected'" : "" }>${i }</option>
							</c:forEach>
						</select>
					</div>
				</div>
            	
	        	<div id="realgrid"></div>
	              <!-- paging -->
	            <div class="div_paging">
	              
	                <div class="div_board_write">
	                		<input type="button" onclick="fn_commit()" value="수정">
	                		<input type="button" onclick="fn_delete()" value="삭제">                		
	                </div>
	            </div>
           	</c:if>
        </div>
    </div>

	 
 <script type="text/javascript" src="${pageContext.request.contextPath }/realgrid/realgrid-lic.js"></script>
 <script type="text/javascript" src="${pageContext.request.contextPath }/realgrid/realgrid.2.6.3.min.js"></script>
 <script src="${pageContext.request.contextPath }/realgrid/jszip.min.js"></script>
 <script type="text/javascript">

 let fields = [
		{
		  fieldName: "memId",
		  dataType: "text"
		},
		{
		  fieldName: "memName",
		  dataType: "text"
		},
		{
		  fieldName: "joinDate",
		  dataType: "datetime",
		  datetimeFormat: "yyyy-MM-dd",
		  amText: "오전",
		  pmText: "오후"
		},
		{
		  fieldName: "memRole",
		  dataType: "text"
		},
		{
			fieldName: "memMail",
			 dataType: "text"
			},
	];
 
 let columns = [
		{
		  name: "memId",
		  fieldName: "memId",
		  width: "155",
		  header: {
		    text: "아이디"
		  }
		},
		{
		  name: "memName",
		  fieldName: "memName",
		  width: "200",
		  header: {
		    text: "이름"
		  }
		},
		{
		  name: "joinDate",
		  fieldName: "joinDate",
		  width: "150",
		  header: {
		    text: "가입일"
		  },
		  styleName: "right-column"
		},
	
		{
		  name: "memRole",
		  fieldName: "memRole",
		  width: "150",
		  sortable: false,
		   lookupDisplay: true
		  ,values: [
		        "ADMIN",
		        "MEMBER"
		    ],
		    labels: [
		        "관리자",
		        "일반회원"
		    ],
		    editor: {
		        type: "dropdown"
		    },
		    header: {
			    text: "권한"
			}
		},
		{
		  name: "memMail",
		  fieldName: "memMail",
		  width: "350",
		  header: {
		    text: "이메일"
		  }
		}
	];
 
 let container, provider, gridView;
 document.addEventListener('DOMContentLoaded', function () {
 	container = document.getElementById('realgrid');
 	provider = new RealGrid.LocalDataProvider(false); 
 	gridView = new RealGrid.GridView(container);
 	gridView.setDataSource(provider);
 	
 	
 	provider.setFields(fields); 
 	gridView.setColumns(columns);
 	
 	gridView.displayOptions.rowHeight = 30;	
 	gridView.displayOptions.syncGridHeight="always"; 
 	gridView.columnByName("memId").editable = false; 	
 	gridView.columnByName("joinDate").editable = false; 	
 	gridView.checkBar.exclusive = false;
 	
 	provider.onRowUpdating = function(provider, row) {
 		let result = confirm("수정?")
 		if(result){
 			let item = gridView.getEditingItem();
 			
 			return true;
 		}else{
 			return false;
 		}
 	};
 	
 	provider.onRowUpdated = function(provider, row){
 		let rowData = provider.getJsonRow(row);
 		console.log("rowData : ", rowData);
 		console.log("rowData json: ", JSON.stringify(rowData));
 		
 		$.ajax({
 			url:"<c:url value='/member/memberGridUpdate' />"
 			,type:"post"
 			,data:JSON.stringify(rowData)
 			,contentType:"application/json; charset=utf-8;"
 			,success:function(data){
 				console.log("data : ", data);
 				if(data){
 					alert("수정 완료.");
 					fn_memberGrid();
 				}else{
 					alert("수정 실패.");
 				}
 			}
 			,error:function(){
 				alert("수정 실패.");
 			}
 			
 		});
 	}
 });
 
 
$(function(){
	fn_memberGrid();
});

let sf = $("form[name='search']");
function fn_memberGrid(){
	console.log("sf.serialize() : ", sf.serialize());
	
	$.ajax({
		url:"<c:url value='/member/memberGrid' />"
		,type:"post"
		,data: sf.serialize()
		,success:function(data){
			console.log("data", data);
			let gridData = data.memberList;
			
			provider.setRows(gridData); 	
		 	gridView.refresh();		
		 	
		 	$(".div_paging > ul").remove();
		 	let paging_str = '';

		 	paging_str +=		'<ul class="pagination">';
		 	if(data.firstPage > 10){
		 		paging_str +='		<li><a href="javascript:fn_paging(' + (data.firstPage-1) +')">&laquo;</a></li> ';
		 	}
		 	if(data.curPage != 1){
		 		paging_str +='		<li><a href="javascript:fn_paging(' + (data.curPage-1) +')">&lt;</a></li> ';
		 	}
		 	for(let i = data.firstPage; i<= data.lastPage; i++){
		 		if(data.curPage != i){
		 			paging_str +='	<li><a href="javascript:fn_paging(' + (i) +')"> '+ i +'</a></li> ';
		 		}else{
		 			paging_str +='	<li class="curPage_a">'+ i +'</li> ';
		 		}
		 	}
		 	if(data.lastPage != data.totalPageCount ){
		 		paging_str +='		<li><a href="javascript:fn_paging(' + (data.curPage+1) +')">&gt;</a></li> ';
		 		paging_str +='		<li><a href="javascript:fn_paging(' + (data.lastPage+1) +')">&raquo;</a></li> ';
		 	}
		 	paging_str +='		</ul> ';

		 	$(".div_paging").prepend(paging_str); 
		}
		,error:function(){
			alert("error");
		}
	});
	
} 


function fn_paging(curPage){
	console.log(" curPage : ", curPage);
	$("input[name='curPage']").val(curPage);
	fn_memberGrid();
	
}

$("#id_rowSizePerPage").change(function(){
	sf.find("input[name='curPage']").val(1);
	sf.find("input[name='rowSizePerPage']").val($(this).val());
	fn_memberGrid();
});


$("#id_btn_reset").click(function(){
	sf.find("input[name='searchWord']").val("");
	sf.find("input[name='curPage']").val(1);
	sf.find("input[name='rowSizePerPage']").val(10);
	
	fn_memberGrid();
});

function fn_commit(){
	gridView.commit();   
	
}

function fn_delete(){
	let checkRows = gridView.getCheckedRows();
	console.log("checkRows :", checkRows);
	
	if(!checkRows.length > 0){
		alert("선택 멤버 없음.");
		return;
	}
	
	let memId_arr = [];
	for(let i=0; i< checkRows.length; i++){
		let row =  provider.getValues(checkRows[i]);
		memId_arr.push(row[0])
	}
	console.log("memId_arr :", memId_arr);
	
	$.ajax({
		url:"<c:url value='/member/memberGridMultiDelete' />"
		,type: "post"
		,data:{ memIdArr : memId_arr }
		,success:function(data){
			console.log("data :", data);
			if(data){
				alert("삭제 완료.");
				fn_memberGrid();
			}else{
				alert("삭제 실패.");	
			}
		}
		,error:function(){
			alert("삭제 실패.");
		}		
	});
}
</script>
 
 
