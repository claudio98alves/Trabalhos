using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GeneticAlgorithm : MetaHeuristic {
	public float mutationProbability;
	public float crossoverProbability;
	public int tournamentSize;
	public bool elitist;
    public float elistismoValor;
  
	public override void InitPopulation () {
        //You should implement the code to initialize the population 
        //tournmentsize atribuido ja
        this.population = new List<Individual>();
        for(int i=0;i< populationSize; i++)
        {
            GeneticIndividual a = new GeneticIndividual(this.topology);
            a.Initialize();
            this.population.Add(a);
        }
	}

    //The Step function assumes that the fitness values of all the individuals in the population have been calculated.
    public override void Step() {
        //criar a nova populacao
        List<Individual> new_pop = new List<Individual>();

        updateReport(); //called to get some stats
                        // fills the rest with mutations of the best!
        if (elitist) { 
            for (int i = 0; i < populationSize - (elistismoValor * populationSize); i++)
            {

                GeneticIndividual best = (GeneticIndividual)new TournmentSelect().selectIndividuals(population, tournamentSize).Clone();
                GeneticIndividual best2 = (GeneticIndividual)new TournmentSelect().selectIndividuals(population, tournamentSize).Clone();
                best.Crossover(best2, crossoverProbability);
                best.Mutate(mutationProbability);
                new_pop.Add(best.Clone());
                Debug.Log(new_pop.Count);
            }

            
            float max2 = float.MaxValue;
            Individual elite = null;
            for (int i = 0; i <  (elistismoValor * populationSize); i++)
            {
                float max = float.MinValue;
                for (int j = 0; j < populationSize; j++)
                {
                    if (population[j].Fitness > max && population[j].Fitness<max2)
                    {
                        max = population[j].Fitness;
                        elite = (GeneticIndividual)population[j];
                    }
                }
                max2 = max;
                new_pop.Add(elite.Clone());
            }
        }
        else
        {
            for (int i = 0; i < populationSize; i++)
            {

                GeneticIndividual best = (GeneticIndividual)new TournmentSelect().selectIndividuals(population, tournamentSize).Clone();
                GeneticIndividual best2 = (GeneticIndividual)new TournmentSelect().selectIndividuals(population, tournamentSize).Clone();
                best.Crossover(best2, crossoverProbability);
                best.Mutate(mutationProbability);
                new_pop.Add(best.Clone());
                Debug.Log(new_pop.Count);
            }

        }

        population = new_pop;

        generation++;
    }

}
