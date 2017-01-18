package com.uploadImages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filename;
	private String Description;
	private Date date;
	private int displayTimes = 0;
	private byte[] bytes;

	public ImageInfo(String filename, String description, Date date, byte[] bytes) {
		this.filename = filename;
		Description = description;
		this.date = date;
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	// Get length of byte
	public int getLengthOfBytes() {
		return bytes.length;
	}

	public String getFile() {
		return filename;
	}

	public String getDescription() {
		return Description;
	}

	public Date getDate() {
		return date;
	}

	public int getDisplayTimes() {
		return displayTimes;
	}

	// increment display times
	public void incrementDisplayTimes() {
		displayTimes++;
	}

	// Return pretty date
	public String getDateString() {
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
		return ft.format(date);
	}

}
