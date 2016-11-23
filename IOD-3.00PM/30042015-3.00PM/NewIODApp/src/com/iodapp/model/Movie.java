package com.iodapp.model;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Movie {
	private String title, thumbnailUrl;
	private String year,exp;
	private String rating,speciality,available,doctore_id,queue_no;
	private Bitmap imageBitmap;
	

	

	
	private ArrayList<String> genre;

	public Movie() {
	}

	public Movie(String name, String thumbnailUrl, String year, String rating,
			ArrayList<String> genre,String speciality) {
		this.title = name;
		this.thumbnailUrl = thumbnailUrl;
		this.year = year;
		this.rating = rating;
		this.genre = genre;
		this.speciality = speciality;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public ArrayList<String> getGenre() {
		return genre;
	}

	public void setGenre(ArrayList<String> genre) {
		this.genre = genre;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getSpeciality() {
		return speciality;
	}
	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	/**
	 * @return the exp
	 */
	public String getExp() {
		return exp;
	}

	/**
	 * @param exp the exp to set
	 */
	public void setExp(String exp) {
		this.exp = exp;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}

	public String getDoctore_id() {
		return doctore_id;
	}

	public void setDoctore_id(String doctore_id) {
		this.doctore_id = doctore_id;
	}

	public String getQueue_no() {
		return queue_no;
	}

	public void setQueue_no(String queue_no) {
		this.queue_no = queue_no;
	}
}
