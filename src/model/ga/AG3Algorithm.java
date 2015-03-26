package model.ga;
import  model.ga.IGeneticAlgorithmStep;
import model.ga.crossing.CrossingWithotUnwantedEffects;
import model.ga.mutation.MutationWithotUnwantedEffects;
import model.ga.save.SaveChilds;
import model.ga.selection.RouletteSelection;
import model.ga.validation.ValidationType1;

/**
 * Class implemented for the algorithm AG3
 * who it inherits of GeneticAlgorithmTemplate
 * It aspires to generate new  formulations of problem
 *  with similar root causes 
 * @author Ruben
 *
 */
public class AG3Algorithm extends GeneticAlgorithmTemplate {

	private IGeneticAlgorithmStep selection = null;
	private IGeneticAlgorithmStep crossing = null;
	private IGeneticAlgorithmStep mutation = null;
	private IGeneticAlgorithmStep validation = null;
	private IGeneticAlgorithmStep saveChildsToDB = null;


	@Override
	public IGeneticAlgorithmStep getCrossing() {
		if (crossing == null)
			crossing = new CrossingWithotUnwantedEffects();
		return crossing;
	}


	@Override
	public IGeneticAlgorithmStep getMutation() {
		if (mutation == null)
			mutation = new MutationWithotUnwantedEffects();
		return mutation;
	}

	@Override
	public IGeneticAlgorithmStep getSelection() {
		if (selection == null)
			selection = new RouletteSelection();
		return selection;
	}

	@Override
	public IGeneticAlgorithmStep getValidation() {
		if (validation == null)
			validation = new ValidationType1();
		return validation;
	}

	@Override
	public String toString() {
		return "Algorithm AG3";
	}


	@Override
	public IGeneticAlgorithmStep getSeveChiles() {
		if (saveChildsToDB == null)
			saveChildsToDB = new SaveChilds();
		return saveChildsToDB;
	}



}
