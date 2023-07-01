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
<style>
h1 {
    position: absolute;
    top: 50px;

}
h1::before,
h1::after {
    content: "";
    display: block;
    height: 1px;
    background-color: #000;
    margin-bottom: 10px;
}

</style>
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

</body>
</html>