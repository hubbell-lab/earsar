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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Cell {
  //  Creates a cell that can contain species and individuals.
  private static int numberCells=0;       // Number of Cells created.
  private int cellId=0;
  private int numberSpecies=0;            // Number of species in cell.
  private int numberIndividuals=0;        // Number of Individuals in cell.
  private Boolean empty=true;             // Nothing in cell.
  // Map key is Species, value is number individuals of the species in the cell
  private Map<Integer,Integer> individualsPerSpeceis;

  Cell() { // Cell constructor.
    cellId=numberCells;
    numberCells++;
    individualsPerSpeceis=new HashMap<Integer,Integer>(); 
  }  

  public boolean getEmpty()                       { return empty; }
  public static int getNumberCells()              { return numberCells; }
  public int getCellId()                          { return cellId; }
  public int getNumberSpecies()                   { return numberSpecies; }
  public int getNumberIndividuals()               { return numberIndividuals; }
  public int getIndividualPerSpecies(int species) { 
    // Return number of individuals for specified species in the cell.
    //
    // species  The species for which we want to know how many individual are
    //          in the cell. 
    if (individualsPerSpeceis.containsKey(species)) {
      return individualsPerSpeceis.get(species);
    } else { return 0; }
  }
 
  public int [] getSpecies() {
    // Returns an arraylist where the indexs are the species and the values
    // are the abundance of the species in the cell.
    int in=0;
    int [] temp = new int[numberSpecies];
    if (numberSpecies==0) { return temp; }
    for (Integer key: new TreeSet<Integer>(individualsPerSpeceis.keySet())) {
      temp[in++]=key;
    } 
    return temp;   
  } 

  public boolean containsSpecies(int species) {
    // Returns true if species is in the cell.
    //
    // species   the species we wish to know if it is in the cell
    if (individualsPerSpeceis.containsKey(species)) { return true; }
    return false;
  }

  public void incrementSpecies(int species) { 
    // Increments the number of species in the cell and keeps track of the
    // number of individuals for each species in the cell.
    //
    // species   the species to which the individual being placed in the cell 
    //           belongs.
    //
    if (empty) { empty=false; }      // Cell no longer empty
    numberIndividuals++;
    //  Store in Hash the number of individual for the species hash key.
    if (individualsPerSpeceis.containsKey(species)) {
      individualsPerSpeceis.put(species,individualsPerSpeceis.get(species)+1);
    }
    else {  individualsPerSpeceis.put(species,1); numberSpecies++; }
  } 

  public void renameSpecies(int []rename) {
    // Renames the species.  This is done by passing in an array where the
    // index is the current names and the value is either the new index or
    // a -1 indicating the species is removed by the renaming.
    //   
    // rename    array with index being current names and the values are the new
    //           indexs or -1.
    ArrayList<Integer> temp = new ArrayList<Integer>();
    // Find any name changes.  We use a TreeSet to sort the keyset to
    // avoid problems with losing information from deleting a species
    // prematurely. 
    for (Integer key: new TreeSet<Integer>(individualsPerSpeceis.keySet()))
{
      if (rename[key]== -1) { continue; }
      if (rename[key] != key)  { temp.add(key); }   // Name change.
     }
    // Rename species.
    for (int i=0; i < temp.size(); i++) {
      // Add new name.   
      individualsPerSpeceis.put(rename[temp.get(i)],
                           individualsPerSpeceis.get(temp.get(i))); 
      individualsPerSpeceis.remove(temp.get(i));    // Get rid of old name.
    }
  }
}
