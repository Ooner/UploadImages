<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Display Images</title>
<spring:url value="/resources/theme/Css/display.css" var="displaycss" />
<spring:url value="/resources/theme/Js/jquery-3.1.1.min.js"
	var="jqueryJs" />
<spring:url value="/resources/theme/Js/main.js" var="mainJs" />
<link href="${displaycss}" rel="stylesheet" />
<script src="${jqueryJs}"></script>
<script src="${mainJs}"></script>
</head>
<body>
	<h1 align="center">Spring MVC Image List</h1>
    <br>
	<table style="width:100%">
		<tr>
			<th>File Name</th>
			<th>Description</th>
			<th>Uploaded Date</th>
			<th>Displayed Times</th>
			<th>File Size</th>
		</tr>
		<c:forEach items="${results}" var="result">
			<tr>
				<td><a
					href="<c:url value="/spring-mvc/displayImages/${result.getFile()}"/>">${result.getFile()}</a></td>
				<td>${result.getDescription()}</td>
				<td>${result.getDateString()}</td>
				<td>${result.getDisplayTimes()}</td>
				<td>${result.getLengthOfBytes()}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>