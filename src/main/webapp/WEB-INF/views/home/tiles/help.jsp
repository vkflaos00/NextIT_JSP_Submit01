<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HEALTHY</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/home.css">

<script>
	var apiUrl = "https://api.odcloud.kr/api/15018867/v1/uddi:9fdf2982-70c8-4295-8174-a958d32f7056?page=1&perPage=10&serviceKey=oTZF%2FauXJZcuxpo6Nq2Sy5LIy68VV%2B%2FDisNVz8G6HcqBntJgCQsYNSGPMRds1xzonxMBE%2BD77qZuSKquOC3%2B4Q%3D%3D";

	fetch(apiUrl).then(function(response) {
		return response.json();
	}).then(function(data) {
		var dataArr = data.data;

		var table = document.createElement("table");
		var headerRow = table.insertRow();
		for ( var key in dataArr[0]) {
			var headerCell = document.createElement("th");
			headerCell.textContent = key;
			headerRow.appendChild(headerCell);
		}

		for (var i = 0; i < dataArr.length; i++) {
			var rowData = dataArr[i];
			var row = table.insertRow();
			for ( var key in rowData) {
				var cell = row.insertCell();
				cell.textContent = rowData[key];
			}
		}

		var container = document.getElementById("table-container");
		if (container) {
			container.appendChild(table);
		} else {
			console.error("table-container element not found");
		}
	}));
</script>




</head>
<body>
	<div id="map" style="width: 500px; height: 200px;"></div>

	<div id="table-container"></div>
</body>

</html>