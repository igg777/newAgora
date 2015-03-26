package model.ga.internal;

import system.rdf.dataBase.Problem;

public class Single {
Problem problem;
private double [] fitnes;
public Single(Problem p, double[] fitnes) {
	super();
	this.problem = p;
	this.fitnes = fitnes;
}
public Problem getP() {
	return problem;
}
public void setP(Problem p) {
	this.problem = p;
}
public double[] getFitnes() {
	return fitnes;
}
public void setFitnes(double[] fitnes) {
	this.fitnes = fitnes;
}

}
