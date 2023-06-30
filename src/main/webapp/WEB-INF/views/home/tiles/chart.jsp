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

<script type="text/javascript"
	src="https://www.gstatic.com/charts/loader.js"></script>


</head>
<body>
			
			<p class="text-center3">현 시점 1위부터 5위까지</p>
			<div id="barchart_values"></div>

			<script type="text/javascript">
				google.charts.load("current", {
					packages : [ "corechart" ]
				});
				google.charts.setOnLoadCallback(drawChart);

				function drawChart() {
					var memberData =
			<%=request.getAttribute("memberData")%>
				;
					var colors = [ '#800000', '#600000', '#400000', '#200000',
							'#000000' ];

					var data = new google.visualization.DataTable();
					data.addColumn('string', '날짜');
					data.addColumn('number', '금연일자');
					data.addColumn({
						type : 'string',
						role : 'style'
					});
					var xLabels = [];

					for (var i = 0; i < memberData.length; i++) {
						var name = memberData[i][0];
						var dDayValue = 730 - parseInt(memberData[i][1]);
						var color = colors[i % colors.length];
						xLabels.push(name);
						data.addRow([ name, dDayValue, color ]);
					}

					var view = new google.visualization.DataView(data);
					view.setColumns([ 0, 1, {
						calc : "stringify",
						sourceColumn : 1,
						type : "string",
						role : "annotation"
					}, 2 ]);

					var options = {
						bar : {
							groupWidth : "100%"
						},
						legend : {
							position : "none"
						},
						hAxis : {
							direction : -1
						},
						series : {
							0 : {
								color : '#800000'
							},
							1 : {
								color : '#600000'
							},
							2 : {
								color : '#400000'
							},
							3 : {
								color : '#200000'
							},
							4 : {
								color : '#000000'
							}
						}
					};

					var chart = new google.visualization.BarChart(document
							.getElementById("barchart_values"));
					chart.draw(view, options);
				}
			</script>
</body>
</html>
