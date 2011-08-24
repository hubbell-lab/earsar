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
import java.util.Random;

public class Population {
  private int numberNonZeroSpecies=0;
  private int numberAllIndividuals=
                       UserInputInt.TOTAL_NUMBER_INDIVIDUALS.getValue();
  private int length=UserInputInt.LENGTH.getValue();
  private int width=UserInputInt.WIDTH.getValue();
  private int maxIndividualsPerCell=2*numberAllIndividuals/(length+width);
  private int [] individualsInCells;
  private int [] abundance; 
 
  Population ( Cell[][] cells, Random rand) {
    int numberSpecies, sp;
    int [] rename;
    double random;
    ArrayList <Double>  cumulativeFract;
    // Computes cumulative Fraction of abundance which is used determine 
    // species of individual to be placed in a cell.
    CumulativeFraction cf = new CumulativeFraction(rand);
    cumulativeFract=cf.getCumulativeFraction();
    numberSpecies=cumulativeFract.size();
    rename=new int[numberSpecies];
    sp=numberSpecies-1;
    individualsInCells=new int[numberSpecies];
    for (int j=0; j < numberSpecies; j++) {individualsInCells[j]=0;}
    // Place individuals from different species into cells until the total
    // number of individuals that the user specified are distributed to the 
    // cells.
    for (int i=0; i < numberAllIndividuals; i++) {
      // Determine species of individual to be placed in cell.
      cumulativeFract.set(numberSpecies-1,1.0);
      random=rand.nextDouble();
      for (int j=0; j<numberSpecies; j++) {
        if (random > cumulativeFract.get(j)) { continue; }
        sp=j;
        break;
      }
      placeIndividualInCell(cells,rand,sp);
    }
    // Determine number of zero abundance species and create an array to
    // allow us to remove them and rename the species in the cells 
    // and create the abundance array for the renamed species.
    for (int j=0; j<numberSpecies; j++) {
      if ( individualsInCells[j]==0 ) { rename[j]=-1; } 
      else { rename[j]=numberNonZeroSpecies++; }
    }
    // Compute the abundance for species that have individuals in cells.
    abundance = new int[numberNonZeroSpecies];
    for (int j=0,in=0; j<numberSpecies; j++) {
      if ( individualsInCells[j]!=0 ) { 
        abundance[in++]= individualsInCells[j]; 
      }
    }
    // Rename the species in the cells.
    for (int i=0; i < length; i++) {
       for (int j=0; j < width; j++) { cells[i][j].renameSpecies(rename); }
    } 
  } 

  public int getNumberNonZeroSpecies()  { return numberNonZeroSpecies; }
  public int [] getAbundance()          { return abundance; }
  public int [] getIndividualsInCells() { return individualsInCells; }

  public int [] getCell(Cell [][] cell, Random rand) {
    //
    // Randomly select a cell until you select one that is not full and return
    // the indexs x1 and y1 (in an integer array) that determine which cell was 
    // chosen.
    //
    // cell    Two demensional array of cells.
    // rand    A random number generator.
    boolean cellFull=true;
    int x1=0, y1=0;
    while (cellFull) {
      x1=rand.nextInt(length);
      y1=rand.nextInt(width);
      if (cell[x1][y1].getNumberIndividuals() ==  maxIndividualsPerCell) {
        x1=rand.nextInt(length);
        y1=rand.nextInt(width);
      }
    cellFull=false;
    }
    int [] xy={x1,y1};
    return xy;
  }

  public void placeIndividualInCell( Cell [][] cell, Random rand, int sp ) {
    //  Places an individual in a cell.
    //
    // cell    Two demensional array of cells.
    // rand    A random number generator.
    // sp      The current species with which we are working.
    // 
    int [] xy = new int[2]; 
    int x1, x2, y1, y2;
    // Find a cell that is not full.
    xy=getCell(cell,rand);
    x1=xy[0];
    y1=xy[1];
    // Place first individual of a species randomly in a cell.
    if (individualsInCells[sp] == 0 ) {
      cell[x1][y1].incrementSpecies(sp);
      individualsInCells[sp]++;
      return;
    }
    // Individuals of this species already present in cells.  Decide if we 
    // should place individual in current cell.
    boolean notPlaced=true;
    double dist, probPlacement;
    while (notPlaced) {
      double exponent=0;
      // Compute the exponent of logistic function based on cell species density
      for (x2=0; x2 < length; x2++) {
        for (y2=0; y2 < width; y2++) {
          if (cell[x2][y2].containsSpecies(sp)) {
            dist=Math.sqrt(Math.pow(Math.abs(x1-x2),2) + 
                           Math.pow(Math.abs(y1-y2),2));
            exponent=exponent-Math.exp(-UserInputDoub.DISTANCE_DECAY.getValue()*
                                       dist) * 
                             Math.pow(cell[x2][y2].getNumberIndividuals(),
                             UserInputDoub.EXPONENT_NEIGHBOR_EFFECT.getValue());
          }
        }
      }
      // Compute logistic probability of placement.
      probPlacement=1.00/(1.00 +
                          UserInputDoub.STRENGTH_NEIGHBORHOOD_EFFECT.getValue()*
                          Math.exp(exponent));
      // No placement pick another cell/
      if (rand.nextDouble() >  probPlacement) { 
        xy=getCell(cell,rand);
        x1=xy[0];
        y1=xy[1];
      }
      // Placement leave while loop.
      else { notPlaced=false; break; }
    }
    // Update cell and individual information.
    cell[x1][y1].incrementSpecies(sp);
    individualsInCells[sp]++;
    return;
  }


}
