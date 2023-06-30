<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/header.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>

</head>
<body>
 <div id="wrap">
	<div class="header">
		<div class="top_nav">
			<tiles:insertAttribute name="header"/>
		</div>
	</div>

	<tiles:insertAttribute name="body"/>

</div>
	
<hr>
<h1>가만히 있어도 성공하는 세상에서 가장 쉬운 도전</h1>
<hr>
</body>
</html>