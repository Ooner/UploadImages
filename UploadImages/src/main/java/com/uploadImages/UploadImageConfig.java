package com.uploadImages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadImageConfig {

	public static final String IMAGELIST = "IMAGELIST";

	@Bean(name = IMAGELIST)
	public List<ImageInfo> imageList() {
		return new ArrayList<ImageInfo>();
	}

	@Bean
	public UploadedImages UploadedImages(@Value("#{" + IMAGELIST + "}") List<ImageInfo> imageList) {
		return new UploadedImages(imageList);
	}
}
