package com.aurionpro.MultiPageQuizApplication.model;

import java.sql.Timestamp;

public class Result {
	private int id, userId, categoryId, score, total;
	private Timestamp takenAt;
	private String categoryName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int v) {
		this.userId = v;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int v) {
		this.categoryId = v;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int v) {
		this.score = v;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int v) {
		this.total = v;
	}

	public Timestamp getTakenAt() {
		return takenAt;
	}

	public void setTakenAt(Timestamp takenAt) {
		this.takenAt = takenAt;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
