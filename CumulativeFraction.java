/*
 (C) Copyright 2011 by Solutions For Hire.
    This file is part of EarSar.

    EarSar is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.solutionsforhire.earsar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CumulativeFraction {
  // Determines number of Species and abundance of individuals for each species
  // then computes a CumulativeFraction based on the abundance of each species,
  // which is ordered from greater to lesser.  
  private ArrayList <Double>  cumulativeFract = new ArrayList<Double>();

  CumulativeFraction(Random rand) {
    int numberSpecies=0;
    int numberIndividualAllSpecies=
              UserInputInt.TOTAL_NUMBER_INDIVIDUALS.getValue();
    double alpha= UserInputDoub.ALPHA.getValue();  // Fishers's Alpha
    double random;
    ArrayList <Integer> abundance = new ArrayList<Integer>();

    // Initialize first species created.
    abundance.add(1);
    cumulativeFract.add(1.0);
    numberSpecies++;
    // Create species and individuals until we have created the total number
    // of individuals specified by the user for all species.
    for (int i=1; i < numberIndividualAllSpecies; i++) {
      random=rand.nextDouble();
      // Create new species?
      if ( random <= alpha/(alpha+i) ) {
        // New species.
        abundance.add(1);
        cumulativeFract.add(0.0);
        numberSpecies++;
        continue;
      }
      // Compute Cumulative Fraction of total Abundance for each species.
      cumulativeFract.set(0,(double)abundance.get(0)/i);
      for (int j=1; j < numberSpecies; j++) {
        cumulativeFract.set(j,cumulativeFract.get(j-1) + 
                             (double)abundance.get(j)/i);  
      }
      // Use random number and culmaltive fraction to determine which
      // species gets a new member.
      cumulativeFract.set(numberSpecies-1,1.00001);// Insures Individual made
      for (int sp=0; sp < numberSpecies; sp++) {
        if (random >= cumulativeFract.get(sp)) { continue; }
        abundance.set(sp,abundance.get(sp)+1);
        break;
      }  // End sp loop which determine which species gets a new individual.
    }  // End i loop which create the user specied number of individuals.
    // Rank Species by abundance.
    Collections.sort(abundance);
    Collections.reverse(abundance);
    // Compute the cumulative fraction of abundance for reordered species.
    cumulativeFract.set(0,(double)abundance.get(0)/
                         numberIndividualAllSpecies);
    for (int sp=1; sp < numberSpecies; sp++) {
      cumulativeFract.set(sp,cumulativeFract.get(sp-1) + 
                       (double)abundance.get(sp)/numberIndividualAllSpecies);
    }
  }
  
  public ArrayList<Double> getCumulativeFraction() { return cumulativeFract; }
}
