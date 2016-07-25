package edu.kaist.mrlab.srdf.data;

public class Triple {
	private String subject;
	private String predicate;
	private String object;
	private int position;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Triple(String s, String p, String o) {
		this.subject = s;
		this.predicate = p;
		this.object = o;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	public int hashCode() {
		int hashcode = 0;
		hashcode = position * 20;
		hashcode += object.hashCode();
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Triple) {
			Triple tt = (Triple) obj;

			if (isStringDouble(tt.subject) && isStringDouble(this.subject)) {

				int ts1 = (int) Double.parseDouble(tt.subject);
				int tp1 = (int) Double.parseDouble(tt.predicate);
				int to1 = 0;
				if(tt.object.equals("ANONYMOUS")){
					to1 = -1;
				} 
				

				int ts2 = (int) Double.parseDouble(this.subject);
				int tp2 = (int) Double.parseDouble(this.predicate);
				int to2 = 0;
				if(this.object.equals("ANONYMOUS")){
					to2 = -1;
				} 

				return (ts1 == ts2 && tp1 == tp2 && to1 == to2);
			} else {
				return (tt.subject.equals(this.subject)
						&& tt.predicate.equals(this.predicate)
						&& tt.object.equals(this.object) && tt.predicate == this.predicate);
			}

		} else {
			return false;
		}
	}

	public static boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Triple(String s, String p, String o, int position) {
		this.subject = s;
		this.predicate = p;
		this.object = o;
		this.position = position;
	}
	
	public String show(){
		return "<" + this.subject + ", " + this.predicate + ", " + this.object + ">"; 
	}
	
	public String showAll(){
		return "<" + this.subject + ", " + this.predicate + ", " + this.object + ", " + this.position + ">"; 
	}
}
