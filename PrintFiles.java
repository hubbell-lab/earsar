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
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class PrintFiles {
  private PrintWriter [] outFiles = new PrintWriter[FileTypes.values().length];
  PrintFiles () { 
    String dirStem="./" + UserInputString.STEM_FILENAME.getValue() + "/" +
                   UserInputString.STEM_FILENAME.getValue();
    FileTypes [] fileTypes = FileTypes.values();
    FileWriter out;
    for (int i=0; i < fileTypes.length; i++) {
      try {
        out=new FileWriter(dirStem + fileTypes[i].getValue());
        outFiles[i] = new PrintWriter(out);
      } catch (IOException e) { e.printStackTrace(); }
    }
    writeHearderOutputFiles();
  }

  public void writeHearderOutputFiles() {
    outFiles[1].println("species\tabundance");
    outFiles[2].println("run\tz");
    outFiles[3].println("run\tarea\tlog area\tsar\tlogsar\tobs ear\tlog ear\t" +
                        "pred ear\tlog pred ear\tz");
    outFiles[4].println("area\tmean_sar\tsd_sar\tmean_obs_ear\tsd_obs_ear\t" +
                        "mean_exp_ear\tsd_exp_ear");
    outFiles[5].println("area\tlog_area\tmean_sar\tlog_mean_sar\tmean_obs_ear" +
                        "\tlog_mean_obs_ear\tmean_exp_ear\tlog_mean_exp_ear\t" +
                        "mean_z_from_sar\tsd_z_from");
    outFiles[6].println("Cell\tCell Abundance\tNumber Species\tspecies\t" +
                        "number\tspecies\tnumber\tspecies\tnumber");
  }

  public void printParams() {
    //  Print out the parameters that discribe the EAR/SAR simulation. 
    Date date = new Date(); 
    outFiles[0].println("EAR/SAR Simulation " + date);
    outFiles[0].println("Stem file name: " + 
                        UserInputString.STEM_FILENAME.getValue());
    outFiles[0].println("Distance decay of neighborhood effect " + 
                        UserInputDoub.DISTANCE_DECAY.getValue());
    outFiles[0].println("Exponent weighting effect of number neighbors in " +
                        "cell(i) " + 
                        UserInputDoub.EXPONENT_NEIGHBOR_EFFECT.getValue());
    outFiles[0].println("Strength of effect of neighborhood " +
                        UserInputDoub.STRENGTH_NEIGHBORHOOD_EFFECT.getValue());
    outFiles[0].println("Fisher's alpha " + UserInputDoub.ALPHA.getValue());
    outFiles[0].println("Total number of individuals of all species " +
                        UserInputInt.TOTAL_NUMBER_INDIVIDUALS.getValue());
    outFiles[0].println("Number of Runs "  + 
                        UserInputInt.NUMBER_RUNS.getValue());
    outFiles[0].println("Minimum area for SAR/EAR calculation " +
                        UserInputInt.MINIMUM_AREA.getValue());
    outFiles[0].close();                  
  } 

  public void printRelativeAbundance(int [] abundance) {
    // Print out the species and abundance.
    for (int i=0; i < abundance.length; i++) {
      outFiles[1].println(i + "\t" + abundance[i]);
    }
    outFiles[1].close();
  }
  
  public void printCellsContent(Cell[][] cell) {
    //  Prints out a cells contents, abundance, number of species, 
    //  the species name, and number species members in the cell 
    //  for all the cells. 
    int [] temp; 
    for (int i=0; i < UserInputInt.LENGTH.getValue(); i++) {
      for (int j=0; j < UserInputInt.WIDTH.getValue(); j++) {
        outFiles[6].print(" "+ i + "," + j + "\t   " + 
                          cell[i][j].getNumberIndividuals()  + "\t         " +
                          "    " + cell[i][j].getNumberSpecies());
        temp = cell[i][j].getSpecies(); 
        for (int sp=0; sp < temp.length; sp++) {
          if (sp==0) {
            outFiles[6].print("\t          " + temp[sp] + "\t  " + 
                            cell[i][j].getIndividualPerSpecies(temp[sp]));
          }
          else {
            outFiles[6].print("\t  " + temp[sp] + "\t  " + 
                            cell[i][j].getIndividualPerSpecies(temp[sp]));
          }
        }
        outFiles[6].println(""); 
      }
    } 
    outFiles[6].close();    
  }
  
  public void printSarEar(int [] ear, int [] sar, double [] logArea, 
                           double [] logEar, double [] logSar, 
                           double [] preDear, double []logPreDear,
                           int r, double z) {
    int length=UserInputInt.LENGTH.getValue();
    int width=UserInputInt.WIDTH.getValue();
    int minimumArea=UserInputInt.MINIMUM_AREA.getValue();
    for (int c=minimumArea; c < length*width+1; c++) {
      if ( ear[c-1] == 0 ) {
        outFiles[3].println(r + "\t" + c + "\t" + 
                   String.format("%.4f",logArea[c-1]) + "\t        " + 
                   sar[c-1] + "\t" + String.format("%.4f",logSar[c-1]) + 
                   "\t  " + ear[c-1] + "\t\t" + 
                   String.format("%.4f",preDear[c-1]) + "\t       " + 
                   String.format("%.4f",logPreDear[c-1]) + "\t" + 
                   String.format("%.4f",z));
      }
      else {
        outFiles[3].println(r + "\t" + c + "\t" + 
                    String.format("%.4f",logArea[c-1]) + "\t        " + 
                    sar[c-1] + "\t" + String.format("%.4f",logSar[c-1]) + 
                    "\t  " + ear[c-1] + "\t" + 
                    String.format("%.4f",logEar[c-1]) + "\t" + 
                    String.format("%.4f",preDear[c-1]) + "\t       " + 
                    String.format("%.4f",logPreDear[c-1]) + "\t        " + 
                    String.format("%.4f",z));
      }
    }
  }

  public void printSarEarClose() { outFiles[3].close(); }  

  public void printZvalues(double [] z) {
    int numberRuns=UserInputInt.NUMBER_RUNS.getValue();
    for (int r=1; r < numberRuns + 1; r++) {
      outFiles[2].println(r + "\t" + String.format("%.4f",z[r-1]));
    }
    outFiles[2].close();
  }
  
  public void printMeanSdSarEar(double [] meanSar, double [] sdSar,
                               double [] meanEar, double [] sdEar,
                               double [] meanExpEar, double [] sdExpEar) {
    int length=UserInputInt.LENGTH.getValue();
    int width=UserInputInt.WIDTH.getValue();
    int minimumArea=UserInputInt.MINIMUM_AREA.getValue();
    for (int c=minimumArea; c < length*width+1; c++) {
      outFiles[4].println(c + "\t" +  String.format("%.4f",meanSar[c-1]) + 
                 "\t " + String.format("%.4f",sdSar[c-1]) + 
                 "\t  " + String.format("%.4f",meanEar[c-1]) + "\t   " + 
                 String.format("%.4f",sdEar[c-1]) + "\t     " + 
                 String.format("%.4f",meanExpEar[c-1]) +
                 "\t" + String.format("%.4f",sdExpEar[c-1]));
    }
    outFiles[4].close();
  }
  
  public void printMeanSarEar(double [] logArea, double [] meanSar,
                   double [] logMeanSar, double [] meanObsEar,
                   double [] logMeanObsEar, double [] meanExpEar,
                   double [] logMeanExpEar, double meanZ, double sdZ )  {
    int length=UserInputInt.LENGTH.getValue();
    int width=UserInputInt.WIDTH.getValue();
    int minimumArea=UserInputInt.MINIMUM_AREA.getValue();
    for (int c=minimumArea; c < length*width+1; c++) {
      if ( meanObsEar[c-1]==0 ) {
        outFiles[5].println(c + "\t" + String.format("%.4f",logArea[c-1]) +  
                   "\t       " + String.format("%.4f",meanSar[c-1]) + 
                   "\t          " + String.format("%.4f",logMeanSar[c-1]) +                        "\t     " + String.format("%.4f",meanObsEar[c-1]) +                             "\t\t                  " +
                   String.format("%.4f",meanExpEar[c-1]) + "\t        " +
                   String.format("%.4f",logMeanExpEar[c-1]) + 
                   "\t          " +  String.format("%.4f",meanZ) + 
                   "\t " + String.format("%.4f",sdZ));
      }
      else {
        outFiles[5].println(c + "\t" + String.format("%.4f",logArea[c-1]) + 
                   "\t       " +  String.format("%.4f",meanSar[c-1]) + 
                   "\t          " + String.format("%.4f",logMeanSar[c-1]) +                        "\t     " + String.format("%.4f",meanObsEar[c-1]) +
                   "\t        " + String.format("%.4f",logMeanObsEar[c-1]) + 
                   "\t          " + String.format("%.4f",meanExpEar[c-1]) + 
                   "\t        " + String.format("%.4f",logMeanExpEar[c-1]) + 
                   "\t          " + String.format("%.4f",meanZ) + 
                   "\t " + String.format("%.4f",sdZ));
      }
    }
    outFiles[5].close();
  }
}
 


