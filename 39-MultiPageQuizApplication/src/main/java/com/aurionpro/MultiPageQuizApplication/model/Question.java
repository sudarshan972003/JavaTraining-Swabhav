package com.aurionpro.MultiPageQuizApplication.model;

public class Question {
	private int id, categoryId;
	private String text, optA, optB, optC, optD, correct;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int c) {
		this.categoryId = c;
	}

	public String getText() {
		return text;
	}

	public void setText(String t) {
		this.text = t;
	}

	public String getOptA() {
		return optA;
	}

	public void setOptA(String v) {
		this.optA = v;
	}

	public String getOptB() {
		return optB;
	}

	public void setOptB(String v) {
		this.optB = v;
	}

	public String getOptC() {
		return optC;
	}

	public void setOptC(String v) {
		this.optC = v;
	}

	public String getOptD() {
		return optD;
	}

	public void setOptD(String v) {
		this.optD = v;
	}

	public String getCorrect() {
		return correct;
	}

	public void setCorrect(String v) {
		this.correct = v;
	}
}
