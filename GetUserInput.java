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
import java.io.IOException;
import java.util.Scanner;
public class GetUserInput {
  private Scanner stdin = new Scanner(System.in);
  
  public void getStemName() {
    //  Gets the stem name from the user and uses it to make a sub-directory
    //  of the current directory named the stem name.   It is in this 
    //  sub-directory that the output files starting with the stem name
    //  will be created. 
    String root = "./";
    File dir, useDirectory;
    useDirectory = new File(""); //Current directory
    while ( true ) {
      System.out.print("\nInput Stem file name for storing results: ");
      if (stdin.hasNext() ) {
        UserInputString.STEM_FILENAME.setValue(stdin.next());
        dir = new File(root,UserInputString.STEM_FILENAME.getValue());
        // Request new name from the user if a sub-directory with that
        // name exists.
        if (dir.exists()) {  
          System.out.print(" Directory name: " + dir.getAbsolutePath());
          System.out.println(" already exists.\n Choose a new Stem File name.");
        }
        else { break; } // No sub-directory with stem name exist.
      } 
    } // Exit get stem file name loop.
    stdin.nextLine();  // Clean up so that next nextLine will work properly.
    // Create sub-directory.
    try { dir.mkdir(); }
    catch (Exception e) { System.err.println("Error: " + e.getMessage()); }
 }

  public void getUserInputInt() {
    // Gets all the integer user input.   
    String input;
    // Loops through the UserInputInt ENUMs and get the default, minimum,
    // and maximum values and make sure the users value is with in the 
    // minimum and maximum range.
    for (UserInputInt ui: UserInputInt.values()) {
      if ( ui == UserInputInt.MINIMUM_AREA) { 
        // Set new default MINIMUM AREA as 10% of LENGTH * WIDTH.
        ui.setValue((int)(.1 * UserInputInt.LENGTH.getValue() *
                               UserInputInt.WIDTH.getValue()));
      }
      while (true) { //Validate user input.
        System.out.print(ui.toString() + " <" + ui.getValue() + "> = ");
        input = stdin.nextLine();
        if (input.equals(""))  { break; } // Take default.
        //  Zero number runs used to only generate cellcontents and abundance
        //  output files.
        if ( (ui == UserInputInt.NUMBER_RUNS) && (input.trim().equals("0")) ) {
          ui.setValue(0); 
          break; 
        }
        // Check input valid.
        if ( inputValid(input.trim(),ui.getMin(),ui.getMax()) ) {
          ui.setValue(Integer.parseInt(input.trim())); 
          break; // Exit validation loop user input valid. 
        }
        // Display valid range and ask user for a valid value.
        System.out.println(ui.toString() + " range " + ui.getMin() + "-" + 
                           ui.getMax() + ".  Please enter a number in range.");
      } // End validation loop.
    } //  End UserInput loop.
  }
  
  public void getUserInputDouble() {
    // Gets all the double user input.
    String input;
    // Loops through the UserInputDoub ENUMs and get the default, minimum,
    // and maximum values and make sure the users value is with in the 
    // minimum and maximum range.
    for (UserInputDoub ui: UserInputDoub.values()) {
      while (true) { //Validate user input.
        System.out.print(ui.toString() + " <" + ui.getValue() + "> = ");
        input = stdin.nextLine();
        if (input.equals(""))  { break; }  // Take default. 
        if ( inputValid(input,ui.getMin(),ui.getMax()) ) {
          ui.setValue(Double.parseDouble(input)); 
          break;  // Exit validation loop user input valid.
        }
        // Display valid range and ask user for a valid value.
        System.out.println(ui.toString() + " range " + ui.getMin() + "-" + 
                           ui.getMax() + ".  Please enter a number in range.");
      } // End validation loop.
    } //  End UserInput loop.
  }

  public static boolean inputValid(String input,int lowerValue,int upperValue) {
    // returns true if input integer and within in range.
    if (!isValidInteger(input)) { return false; }
    int value = Integer.parseInt(input);
    if ((value >= lowerValue) && (value <= upperValue)) { return true; }
    return false;
  }

   public static boolean inputValid(String input,double lowerValue, 
                                    double upperValue) { 
    // returns true if input double and within in range.
    if (!isValidDouble(input)) {  return false; }
    double value = Double.parseDouble(input);
    if ((value >= lowerValue) && (value <= upperValue)) { return true; }
    return false;
  }

   public static boolean isValidInteger(String s) {
    // returns true if String passed to method represents an integer.
    try { Integer.parseInt(s); return true; }
    catch (NumberFormatException e) { return false; }
  }

  public static boolean isValidDouble(String s) {
    // returns true if String passed to method represents a double.
    try { Double.parseDouble(s); return true; }
    catch (NumberFormatException e) { return false; }
  }

  public static void main(String[] args) {
    GetUserInput ui = new GetUserInput();
    ui.getUserInputInt();
    ui.getUserInputDouble();
  }
}
