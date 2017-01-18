package com.uploadImages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@PropertySource("classpath:myprop.properties")
@Configuration
public class UploadImagesController {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = System.getProperty("user.dir");

	@Autowired
	private UploadedImages images;

	// Get maxiumum file number from properties file
	@Value("${maximumFileNumber}")
	private int maximumFileNumber;

	@RequestMapping(value = "/uploadImages", method = RequestMethod.GET)
	public String upload() {
		return "uploadImages";
	}

	@RequestMapping(value = "/uploadImages", params = "Upload", method = RequestMethod.POST)
	public String Upload(@RequestParam("Imagefile") MultipartFile file, @RequestParam String description,
			ModelMap model) {

		String uploadPath = "Upload\\" + file.getOriginalFilename();
		String uploadObjectPath = "UploadObject\\ObjectFile.txt";

		// Get Bean from File
		if (images.getUploadedImages().size() == 0) {
			// Read Bean from file
			FileInputStream fin = null;
			ObjectInputStream ois = null;
			try {
				fin = new FileInputStream(FilenameUtils.concat(UPLOADED_FOLDER, uploadObjectPath));
				ois = new ObjectInputStream(fin);
				ArrayList<ImageInfo> ar = (ArrayList<ImageInfo>) ois.readObject();
				if (ar != null) {
					images.setUploadedImages(ar);
				}

			} catch (Exception e) {
			}

		}

		// Prevent duplicate file
		if (hasFile(images.getUploadedImages(), file.getOriginalFilename())) {
			return "uploadImages";
		}

		// Get all files in directory
		File[] files = new File(FilenameUtils.concat(UPLOADED_FOLDER, "Upload\\")).listFiles();

		// Ýf reach maximum number delete file has maximum size
		if (files.length == maximumFileNumber) {
			// Find file has maximum size
			File maxFile = FindMaxSizeFile(files);
			// Delete maximum file from bean
			int index = findFileIndexByName(images.getUploadedImages(), maxFile.getName());
			images.getUploadedImages().remove(index);
			maxFile.delete();
		}

		// Create uploaded time
		Date date = new Date();
		// Create Image Info to add bean
		ImageInfo image = null;
		try {
			image = new ImageInfo(file.getOriginalFilename(), description, date, file.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Add image to the bean
		ArrayList<ImageInfo> inf = images.getUploadedImages();
		inf.add(image);

		try {
			// Get the file and save it
			byte[] bytes = file.getBytes();
			System.out.println(bytes.length);
			Path path = Paths.get(FilenameUtils.concat(UPLOADED_FOLDER, uploadPath));
			Files.write(path, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Write bean to file
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(FilenameUtils.concat(UPLOADED_FOLDER, uploadObjectPath));
			oos = new ObjectOutputStream(fout);
			oos.writeObject(images.getUploadedImages());

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		model.put("LoadingStatus", "File was uploaded");
		return "uploadImages";

	}

	@RequestMapping(value = "/uploadImages", params = "displayAllImages", method = RequestMethod.POST)
	public String displayAllImages(ModelMap model) {

		String uploadObjectPath = "UploadObject\\ObjectFile.txt";

		// Get Bean from File
		if (images.getUploadedImages().size() == 0) {
			// Read Bean from file
			FileInputStream fin = null;
			ObjectInputStream ois = null;
			try {
				fin = new FileInputStream(FilenameUtils.concat(UPLOADED_FOLDER, uploadObjectPath));
				ois = new ObjectInputStream(fin);
				ArrayList<ImageInfo> ar = (ArrayList<ImageInfo>) ois.readObject();
				if (ar != null) {
					images.setUploadedImages(ar);
				}

			} catch (Exception e) {
			} finally {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// Sort bean by date
		ImageInfo[] imageInfo = sortByDate(images.getUploadedImages());

		// Put each file to model
		ArrayList<ImageInfo> results = new ArrayList<>();
		for (ImageInfo in : imageInfo) {
			results.add(in);
		}
		model.put("results", results);

		return "displayImages";
	}

	@RequestMapping(value = "/displayImages/{result.getFile()}", method = RequestMethod.GET)
	public String display(@PathVariable("result.getFile()") String argument, ModelMap model) {

		String uploadPath = "Upload\\" + argument;
		String uploadObjectPath = "UploadObject\\ObjectFile.txt";

		// Get Bean from File
		if (images.getUploadedImages().size() == 0) {
			// Read Bean from file
			FileInputStream fin = null;
			ObjectInputStream ois = null;
			try {
				fin = new FileInputStream(FilenameUtils.concat(UPLOADED_FOLDER, uploadObjectPath));
				ois = new ObjectInputStream(fin);
				ArrayList<ImageInfo> ar = (ArrayList<ImageInfo>) ois.readObject();
				if (ar != null) {
					images.setUploadedImages(ar);
				}

			} catch (Exception e) {
			} finally {
				try {
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// Get displayed Image
		model.put("img", FilenameUtils.concat(UPLOADED_FOLDER, uploadPath));
		model.put("title", argument);

		// Find and increment displayed times
		int index = findFileIndexByName(images.getUploadedImages(), argument);
		ImageInfo image = images.getUploadedImages().get(index);
		image.incrementDisplayTimes();

		// Write bean to file
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {

			fout = new FileOutputStream(FilenameUtils.concat(UPLOADED_FOLDER, uploadObjectPath));
			oos = new ObjectOutputStream(fout);
			oos.writeObject(images.getUploadedImages());

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "image";
	}

	// Find file has maxiumum size
	private File FindMaxSizeFile(File[] files) {
		double maxFileSize = files[0].length();
		File maxFile = files[0];
		for (File file : files) {
			if (file.isFile()) {
				if (file.length() > maxFileSize) {
					maxFileSize = file.length();
					maxFile = file;
				}
			}
		}
		return maxFile;
	}

	// Get file index in bean
	private int findFileIndexByName(ArrayList<ImageInfo> infoList, String filename) {
		int index = 0;
		for (int i = 0; i < infoList.size(); i++) {
			if (infoList.get(i).getFile().equals(filename)) {
				index = i;
			}
		}
		return index;
	}

	// Check Bean has file or not
	private boolean hasFile(ArrayList<ImageInfo> infoList, String filename) {
		for (ImageInfo inf : infoList) {
			if (inf.getFile().equals(filename)) {
				return true;
			}
		}
		return false;
	}

	// Sort file by date(Selection sort)
	private ImageInfo[] sortByDate(ArrayList<ImageInfo> info) {
		ArrayList<ImageInfo> imgg = new ArrayList<>(info);
		ImageInfo[] inf = new ImageInfo[info.size()];
		int k = 0;
		for (ImageInfo image : imgg) {
			inf[k] = image;
			k++;
		}
		ImageInfo temp;
		int min;
		for (int i = 0; i < inf.length - 1; i++) {
			min = i;
			for (int j = i + 1; j < inf.length; j++) {
				if (!checkMin(inf[i].getDate(), inf[j].getDate())) {
					min = j;
				}
			}
			temp = inf[i];
			inf[i] = inf[min];
			inf[min] = temp;
		}
		return inf;
	}

	// Compare to date
	private boolean checkMin(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		if (cal1.get(Calendar.YEAR) <= cal2.get(Calendar.YEAR)) {
			if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
				if (cal1.get(Calendar.MONTH) <= cal2.get(Calendar.MONTH)) {
					if (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
						if (cal1.get(Calendar.DAY_OF_MONTH) <= cal2.get(Calendar.DAY_OF_MONTH)) {
							if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {
								if (cal1.get(Calendar.HOUR_OF_DAY) >= cal2.get(Calendar.HOUR_OF_DAY)) {
									if (cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)) {
										if (cal1.get(Calendar.MINUTE) >= cal2.get(Calendar.MINUTE)) {
											if (cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)) {
												if (cal1.get(Calendar.SECOND) >= cal2.get(Calendar.SECOND)) {
													return false;
												} else {
													return true;
												}

											} else {
												return false;
											}
										} else {
											return true;
										}
									} else {
										return false;
									}
								} else {
									return true;
								}
							} else {
								return false;
							}
						} else {
							return true;
						}
					} else {
						return false;
					}
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
}
