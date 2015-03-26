package model.ga.validation;

public class ValidationData {

	private int generatedChildCount;
	private int validChildCount;

	public ValidationData() {
		generatedChildCount = 0;
		validChildCount = 0;
	}
	
	public ValidationData(int generatedChildCount, int validChildCount) {
		this.generatedChildCount = generatedChildCount;
		this.validChildCount = validChildCount;
	}

	public int getGeneratedChildCount() {
		return generatedChildCount;
	}

	public void setGeneratedChildCount(int generatedChildCount) {
		this.generatedChildCount = generatedChildCount;
	}

	public int getValidChildCount() {
		return validChildCount;
	}

	public void setValidChildCount(int validChildCount) {
		this.validChildCount = validChildCount;
	}

}
