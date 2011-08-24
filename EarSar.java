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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
   Program to simulate SAR and EAR sampling distributions for a given 
   relative abundance distribution and degree of population clumping. 
   Once a spatial pattern of species is establihsed, the program samples
   the SAR and EAR up to 100 times for random patterns of habitat destruction,
   and then compares the mean observed EAR curve with the mean EAR curve 
   calcculated from the mean SAR z value.  The gid size is LENGTH x WIDTH 
   cells (like the number of 20x20m quadrats in the BCI plot) where LENGTH
   and WIDTH are provided by the user.  The degree of clumping of species
   distributions is controlled by a logistic individual placement model, 
   which is:
   
   Pr{placement}= 1/{1 + c*exp[-∑exp(-a*dist(i))*number(i)^b]}

   where "a" is DISTANCE_DECAY of neighborhood effect.
   where "b" is EXPONENT_NEIGHBOR_EFFECT weighting of number of neighbors in a
             cell.
   where "c" is STRENGTH_NEIGHBORHOOD_EFFECT
*/
public class EarSar {
  
  public void firstLastFound( Cell [][] cell, int [] abundance, int [] first, 
                              int [] last, int numberSpecies) {
    boolean foundFirstMemberSpecies;
    int length=UserInputInt.LENGTH.getValue();
    int width=UserInputInt.WIDTH.getValue();
    int cellCount=0;
    int [] sumSpecies = new int [numberSpecies];

   SpeciesLoop:
      // Loop over all species.
      for (int sp=0; sp < numberSpecies; sp++) {
        // Intialize for each run.
        foundFirstMemberSpecies=false;
        sumSpecies[sp]=0;
        // Loop over all cells determine for each species the cell at which
        // a species is first found and last found.
        for (int ix=0; ix < length; ix++) {
          for (int iy=0; iy < width; iy++) {
            sumSpecies[sp]+=cell[ix][iy].getIndividualPerSpecies(sp);
            cellCount=ix*width+iy+1;
            if ( foundFirstMemberSpecies) { 
               // Is it the last individual in the species.
               if (  sumSpecies[sp] == abundance[sp] ) {
                 last[sp]=cellCount;
                 continue SpeciesLoop;
               }
               continue; // First individual for species has been found next iy 
            }
            if ( cell[ix][iy].getIndividualPerSpecies(sp) == 0 ) { continue; }
            else { 
              foundFirstMemberSpecies=true;
              first[sp]=cellCount;
              // Check if species only exists in this cell and last individual
              // is in this cell.
               if (  sumSpecies[sp] == abundance[sp] ) {
                 last[sp]=cellCount;
                 continue SpeciesLoop;
              }
              continue; // First individual for species has been found next iy 
            }
          } // End iy Loop
        }   // End ix Loop
      }     // End Species Loop
  }

  public void randomize(Cell [][] cell, int length, int width) {
    // Shuffle the cell arrays elements so they are randomized.
    //
    // cell    Two demensional array of cells.
    // length  The number of cells in the cell arrays x direction.
    // width   The number of cells in the cell arrays y direction.
    //
    int start=0; 
    Cell [] temp = new Cell [length*width];
    // Convert two dimensional array into one dimensional array.
    for (int i=0; i < length; i++) {
      System.arraycopy(cell[i],0,temp,start,width);
      start+=width;
    }
    // Shuffle one dimensional array.
    Collections.shuffle(Arrays.asList(temp));
    // Convert one dimensional array to two dimensional array.
    start=0;
    for (int i=0; i < length; i++) {
      System.arraycopy(temp,start,cell[i],0,width);
      start+=width;
    }
  }
 
  public static void main(String[] args) {
    int numberSpecies=0;
    double denom, meanArea, meansar, numer, random;
    EarSar earsar = new EarSar();
    Random rand = new Random();
    System.out.println("\n      Welcome to EAR-SAR " );
    //  Print default to screen for Dynamate and let the user change them.
    System.out.println("\nAnything inside <> is a default value. To accept "
                       + "press return.  \nTo change enter a new value and "
                       + "press return.\n");
    // Get user input. 
    GetUserInput ui = new GetUserInput();
    ui.getStemName();
    ui.getUserInputInt();
    ui.getUserInputDouble(); 
    //  Set users input variables.
    int length=UserInputInt.LENGTH.getValue();
    int width=UserInputInt.WIDTH.getValue();
    int minimumArea=UserInputInt.MINIMUM_AREA.getValue();
    int numberIndividualAllSpecies=
              UserInputInt.TOTAL_NUMBER_INDIVIDUALS.getValue();
    int numberRuns=UserInputInt.NUMBER_RUNS.getValue();
    double numc = (double)numberRuns;
    // Setup to print output and print out.
    PrintFiles pfs = new PrintFiles();
    pfs.printParams();
    int numberCells=length*width;
    int [] ear  = new int [numberCells];
    int [] sar  = new int [numberCells];
    double [] ssqSar = new double [numberCells];
    double [] ssqObsEar = new double [numberCells];
    double [] sumSar = new double [numberCells];
    double [] sumObsEar = new double [numberCells];
    double [] logArea = new double [numberCells];
    double [] logEar  = new double [numberCells];
    double [] logSar  = new double [numberCells];
    double [] zsar = new double[numberRuns];
    double fractionAreaLost = 0;
    double oneMinusAz = 0;
    double [] preDear = new double [numberCells];
    double [] logPreDear = new double [numberCells];
    double [] sumExpEar  = new double [numberCells];
    double [] ssqExpEar  = new double [numberCells];

    Cell[][] cell = new Cell [length][width];
    // Create the cell that species will inhabit.
    for (int i=0; i < length; i++) {
      for (int j=0; j < width; j++) { cell[i][j]=new Cell(); }
    } 
    Population pop = new Population(cell,rand);
    numberSpecies=pop.getNumberNonZeroSpecies();
    int [] abundance=pop.getAbundance();
    pfs.printCellsContent(cell);
    pfs.printRelativeAbundance(abundance);
    if (numberRuns==0) { System.exit(0); }
    int sum=0;
    for (int sp=0; sp < numberSpecies; sp++) { sum+=abundance[sp]; }
    int [] first    = new int [numberSpecies];
    int [] last     = new int [numberSpecies];
    int [] sumSpecies = new int [numberSpecies];
    //  Loop for resampling with different patterns of habitat loss.
    for (int r=1; r < numberRuns + 1; r++) {
      // Randomize the cell array.
      earsar.randomize(cell,length,width);
      earsar.firstLastFound(cell,abundance,first,last,numberSpecies); 
      //  Loop through all the cells and compute SAR and EAR
      for (int c=1; c < length*width+1; c++) {
        // Loop over all species and find all species encounterd and 
        // lost in the current cell.
        int SpeciesAdded=0;
        int SpeciesLost=0;
        for (int sp=0; sp < numberSpecies; sp++) {
          if ( first[sp] == c ) { SpeciesAdded++; } 
          if ( last[sp] == c )  { SpeciesLost++; }
        }   // End Species Loop
        if ( c > 1) {   
          sar[c-1]=sar[c-2]+SpeciesAdded;
          ear[c-1]=ear[c-2]+SpeciesLost;
        }
        else {
          sar[0]=SpeciesAdded;
          ear[0]=SpeciesLost;
        }
        logSar[c-1]=Math.log10(sar[c-1]);
        if ( ear[c-1] != 0 ) { logEar[c-1]=Math.log10(ear[c-1]); }
        logArea[c-1]=Math.log10(c-1);
       }     // End c loop over all cells.
      double sumArea=0.0;
      double sumsar=0.0;
      for (int c=minimumArea; c < length*width+1; c++) {
        sumArea+=logArea[c-1];
        sumsar+=logSar[c-1];
      }
      meanArea=sumArea/(length*width-minimumArea);
      meansar=sumsar/(length*width-minimumArea);
      numer=0.0;
      denom=0.0;
      for (int c=minimumArea; c < length*width+1; c++) {
        numer+=(logArea[c-1]-meanArea)*(logSar[c-1]-meansar);
        denom+=(logArea[c-1]-meanArea)*(logArea[c-1]-meanArea); 
      }
      zsar[r-1]=numer/denom;
      for (int c=1; c < length*width+1; c++) {
        fractionAreaLost=(double)c/(double)(length*width);
        oneMinusAz=Math.pow((1.000 - fractionAreaLost),zsar[r-1]);
        preDear[c-1]=(1.000-oneMinusAz)*numberSpecies;
        logPreDear[c-1]=Math.log10(preDear[c-1]);
      }
      pfs.printSarEar(ear,sar,logArea,logEar,logSar,preDear,logPreDear,r,
                       zsar[r-1]);
      // Needed for Ear Sar mean standard deviation, and variance computations
      for (int c=minimumArea; c < length*width + 1; c++) {
        sumSar[c-1]+=sar[c-1];
        ssqSar[c-1]+=sar[c-1]*sar[c-1];
        sumObsEar[c-1]+=ear[c-1];
        ssqObsEar[c-1]+=ear[c-1]*ear[c-1];
        sumExpEar[c-1]+=preDear[c-1];
        ssqExpEar[c-1]+=preDear[c-1]*preDear[c-1]; 
      }
    }   // End Run
    double [] meanSar = new double [numberCells];
    double [] varSar = new double [numberCells];
    double [] sdSar = new double [numberCells];
    double [] meanObsEar = new double [numberCells];
    double [] varObsEar = new double [numberCells];
    double [] sdObsEar = new double [numberCells];
    double [] meanExpEar = new double [numberCells];
    double [] varExpEar = new double [numberCells];
    double [] sdExpEar = new double [numberCells];
    double [] logMeanSar = new double [numberCells];
    double [] logMeanObsEar = new double [numberCells];
    double [] logMeanExpEar = new double [numberCells];
    // Compute Ear Sar mean, standard deviation, and variance.
    for (int c=minimumArea; c < length*width+1; c++) {
      meanSar[c-1]=sumSar[c-1]/numc;
      varSar[c-1]=(ssqSar[c-1]-numc*meanSar[c-1]*meanSar[c-1])/
                  (numc-1);
      sdSar[c-1]=Math.sqrt(varSar[c-1]);
      meanObsEar[c-1]=sumObsEar[c-1]/numc;
      varObsEar[c-1]=(ssqObsEar[c-1]-numc*meanObsEar[c-1]*meanObsEar[c-1])/
                   (numc-1);
      sdObsEar[c-1]=Math.sqrt(varObsEar[c-1]);
      meanExpEar[c-1]=sumExpEar[c-1]/numc;
      varExpEar[c-1]=(ssqExpEar[c-1]-numc*meanExpEar[c-1]*meanExpEar[c-1])/
                   (numc-1);
      sdExpEar[c-1]=Math.sqrt(varExpEar[c-1]);
      logMeanSar[c-1]=Math.log10(meanSar[c-1]);
      logMeanObsEar[c-1]=Math.log10(meanObsEar[c-1]);
      logMeanExpEar[c-1]=Math.log10(meanExpEar[c-1]);
    }  
    pfs.printSarEarClose();
    pfs.printZvalues(zsar);
    pfs.printMeanSdSarEar(meanSar,sdSar,meanObsEar,sdObsEar,meanExpEar,
                          sdExpEar); 
    // Compute the Z mean, standard deviation, and variance across all runs.
    double meanZ, varZ, sdZ;
    double sumZ=0.0;
    double ssqZ=0.0;
    for (int r=1; r < numberRuns + 1; r++) {
      sumZ+=zsar[r-1];
      ssqZ+=zsar[r-1]*zsar[r-1];
    }
    meanZ=sumZ/numberRuns;
    varZ=(ssqZ-numberRuns*meanZ*meanZ)/(numberRuns-1);
    sdZ=Math.sqrt(varZ);
    pfs.printMeanSarEar(logArea,meanSar,logMeanSar,meanObsEar,logMeanObsEar,
                        meanExpEar,logMeanExpEar,meanZ,sdZ);
  }
}
