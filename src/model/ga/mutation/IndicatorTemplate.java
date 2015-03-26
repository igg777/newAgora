package model.ga.mutation;

import java.util.ArrayList;

public class IndicatorTemplate {
    String indicatorName;
    ArrayList<String> subProcess;
    ArrayList<String>process;

    public IndicatorTemplate(String nombre) {
	super();
	this.indicatorName = nombre;
	subProcess = new ArrayList<String>();
	process = new ArrayList<String>();
    }

    public void addSubProses(String proces) {
	this.subProcess.add(proces);
    }

    public void addProcess(String process){
	this.process.add(process);
    }

    public String getNombre() {
	return indicatorName;
    }

    public void setNombre(String nombre) {
	this.indicatorName = nombre;
    }

    public ArrayList<String> getSubProcess() {
	return subProcess;
    }

    public void setSubProcess(ArrayList<String> process) {
	this.subProcess = process;
    }

    public ArrayList<String> getProcess() {
	return process;
    }

    public void setProcess(ArrayList<String> process) {
	this.process = process;
    }

}
