# EARSAR

    July 11, 2011
    (C) Copyright 2011 by Solutions For Hire.

## Introduction

EARSAR is a program to simulate SAR and EAR sampling distributions for a given 
relative abundance distribution and degree of population clumping. It runs on 
Java 6.

EARSAR is distributed in the hope that it will be useful, but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

EARSAR is distributed in an executable JAR file.

The contents of the JAR file are:
 * META-INF/
 * META-INF/MANIFEST.MF
 * com/
 * com/solutionsforhire/
 * com/solutionsforhire/earsar/
 * com/solutionsforhire/earsar/Cell.class
 * com/solutionsforhire/earsar/CumulativeFraction.class
 * com/solutionsforhire/earsar/EarSar.class
 * com/solutionsforhire/earsar/FileTypes.class
 * com/solutionsforhire/earsar/GetUserInput.class
 * com/solutionsforhire/earsar/GNU_GENERAL_PUBLIC_LICENSE.txt
 * com/solutionsforhire/earsar/Population.class
 * com/solutionsforhire/earsar/PrintFiles.class
 * com/solutionsforhire/earsar/README.txt
 * com/solutionsforhire/earsar/UserInputDoub.class
 * com/solutionsforhire/earsar/UserInputInt.class
 * com/solutionsforhire/earsar/UserInputString.class
 * com/solutionsforhire/earsar/Cell.java
 * com/solutionsforhire/earsar/CumulativeFraction.java
 * com/solutionsforhire/earsar/EarSar.java
 * com/solutionsforhire/earsar/FileTypes.java
 * com/solutionsforhire/earsar/GetUserInput.java
 * com/solutionsforhire/earsar/Population.java
 * com/solutionsforhire/earsar/PrintFiles.java
 * com/solutionsforhire/earsar/UserInputDoub.java
 * com/solutionsforhire/earsar/UserInputInt.java
 * com/solutionsforhire/earsar/UserInputString.java

## Java Files

The EARSAR program is composed of  10 Java Files 

Cell.java                 Creates a cell and the methods for getting information
                          from it.  Number individual in cell, species in cell, 
                          number of individual of species, etc. 
CumulativeFraction.java   Determines number of Species and abundance of 
                          individuals for each species then computes a 
                          CumulativeFraction based on the abundance of each 
                          species, which is ordered from greater to lesser.
                          A method returns the ordered abundance array.	
EarSar.java	          Main program that implement simulation.
FileTypes.java	          ENUM object containing names for output files.
GetUserInput.java	  Gets the input from the user.
Population.java           Places individuals from different species into the 
                          cell array until the number of individuals specified 
                          by the users have been deployed to the cells.
PrintFiles.java           Contains methods for printing out result files.
UserInputInt.java	  ENUM object for integer user input, which stores the 
                          input and contains min and max allowed value for user 
                          and initially a default value.
UserInputDoub.java        ENUM object for double user input, which stores the 
                          input and contains min and max allowed value for user
                          and initially a default value.	
UserInputString.java      ENUM object for string user input, which stores the 
                          input and contains min and max allowed value for user
                          and initially a default value.

## To run EARSAR

    1. Download the JAR file
        
        https://github.com/downloads/hubbell-lab/earsar/earsar.jar
        
    1. If Java is not installed on your machine install it.
    1. Enter the command: 

        java -jar earsar.jar

## Documentation 

The java source code includes comments that describe how the code works.

