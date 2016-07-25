package edu.kaist.mrlab.srdf.data;

public class Sentence {
	private String textOfSentence;
	private String subject = "";
	private boolean isContainsSBJ;
	
	public String getTextOfSentence() {
		return textOfSentence;
	}

	public void setTextOfSentence(String textOfSentence) {
		this.textOfSentence = textOfSentence;
	}

	public boolean isContainsSBJ() {
		return isContainsSBJ;
	}

	public void setContainsSBJ(boolean isContainsSBJ) {
		this.isContainsSBJ = isContainsSBJ;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Sentence(String text, boolean isSBJ){
		this.textOfSentence = text;
		this.isContainsSBJ = isSBJ;
	}
	
	public Sentence(String text, boolean isSBJ, String subject){
		this.textOfSentence = text;
		this.isContainsSBJ = isSBJ;
		this.subject = subject;
	}
	
	public String print(){
		return textOfSentence;
	}
	
	public String printAll(){
		return textOfSentence + " / " + isContainsSBJ + " / " + subject;
	}
}
