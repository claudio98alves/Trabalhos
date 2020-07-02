using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TournmentSelect : SelectionMethod
{
    override
    public Individual selectIndividuals(List<Individual> oldpop, int num)
    {

       
        List<Individual> torneio = new List<Individual>();
        List<int> memoria = new List<int>();
        for (int i = 0; i < num; i++)
        {
            int indice = Random.Range(0, oldpop.Count-1);
            if (!memoria.Contains(indice))
            {
                Individual tmp = oldpop[indice];
                torneio.Add(tmp);
                memoria.Add(indice);
            }
            else
            {
                i--;
            }
        }
       
        float max = float.MinValue;
        Individual best = null;
        for (int j = 0; j < num ; j++)
        {
            if (torneio[j].Fitness > max)
            {
                max = torneio[j].Fitness;
               best= (GeneticIndividual)torneio[j];
            }

        }

        return best;
    }
}
