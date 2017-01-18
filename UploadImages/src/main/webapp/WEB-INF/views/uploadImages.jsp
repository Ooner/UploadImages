<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title>File Uploading Form</title>
<spring:url value="/resources/theme/Css/main.css" var="mainCss" />
<spring:url value="/resources/theme/Js/jquery-3.1.1.min.js"
	var="jqueryJs" />
<spring:url value="/resources/theme/Js/main.js" var="mainJs" />
<link href="${mainCss}" rel="stylesheet" />
<script src="${jqueryJs}"></script>
<script src="${mainJs}"></script>
</head>
<body>
	<h1>Spring MVC Image Upload</h1>
	<br>
	<br>
	<form id="myform" action="uploadImages" method="post"
		enctype="multipart/form-data" >
		<h3>File Upload: ${LoadingStatus}</h3>
		Description:<br /> 
		<input id="descripton" type="text" name="description">
		Select a file to upload: 
		<input id="file" type="file"
			name="Imagefile" size="50" /><br /> 
			<input id="submit"
			type="submit" name ="Upload" value="UploadFile"/>
			<input id = "display" type="submit" name="displayAllImages" value="display All Images" />
	</form>

</body>
</html>