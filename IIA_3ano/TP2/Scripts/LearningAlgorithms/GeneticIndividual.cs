using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GeneticIndividual : Individual {


	public GeneticIndividual(int[] topology) : base(topology) {
	}

	public override void Initialize () 
	{
        for (int i = 0; i < totalSize; i++)
        {
            genotype[i] = Random.Range(-1.0f, 1.0f);
        }
    }
		
	public override void Crossover (Individual partner, float probability)
    {
        if (Random.Range(0.0f, 1.0f) < probability)
        {
            float limit1 = Random.Range(0.0f, totalSize - 1);
            float limit2 = Random.Range(limit1, totalSize - 1);

            for (int i = 0; i < totalSize; i++)
            {
                if (i > limit1 && i < limit2)
                {
                    this.genotype[i] = partner.genotype[i];
                }
            }
        }
    }

    public override void Mutate (float probability)
	{
        for (int i = 0; i < totalSize; i++)
        {
            if (Random.Range(0.0f, 1.0f) < probability)
            {
                genotype[i] = Random.Range(-1.0f, 1.0f);
            }
        }
    }

	public override Individual Clone ()
	{
		GeneticIndividual new_ind = new GeneticIndividual(this.topology);

		genotype.CopyTo (new_ind.genotype, 0);
		new_ind.fitness = this.Fitness;
		new_ind.evaluated = false;

		return new_ind;
	}

}
