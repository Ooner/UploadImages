package com.uploadImages;

import java.util.ArrayList;
import java.util.List;

public class UploadedImages {

	private ArrayList<ImageInfo> lists;

	public UploadedImages(List<ImageInfo> uploadedImages) {
		this.lists = (ArrayList<ImageInfo>) uploadedImages;
	}

	public ArrayList<ImageInfo> getUploadedImages() {
		return lists;
	}

	public void setUploadedImages(ArrayList<ImageInfo> uploadedImages) {
		this.lists = uploadedImages;
	}

}
