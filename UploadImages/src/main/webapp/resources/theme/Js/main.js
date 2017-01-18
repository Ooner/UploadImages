$(document).ready(function() {

	$("#myform").submit(function(e) {
	
		// Get event source
		var btn = $(this).find("input[type=submit]:focus");
		
		
		// if event from upload check validation
		if (btn.val().localeCompare("UploadFile") === 0) {
			// Check file was selected
			var file = $("#file").val();
			if (file === '') {
			
				alert("dosya seçiniz");
				e.preventDefault();
			} else {
				
				// Check file is image file
				var etxArray = [ "jpg", "jpeg", "bmp", "gif", "png" ];
				var re = /(?:\.([^.]+))?$/;
				var ext = re.exec(file)[1];
				var ext = ext.toLowerCase();
				if (!etxArray.includes(ext)) {
					alert("resim dosyası seçiniz");
					e.preventDefault();
				}
				// Check for file size grater than max size
				// 5mb max file size given 
				var maxSize = 5*1024*1024;
				var fileSize = $("#file")[0].files[0].size;
				if (fileSize > maxSize) {
					alert("Dosya boyutu çok fazla");
					e.preventDefault();
				}
			}
		}
	});
});

