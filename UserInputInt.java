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
public enum UserInputInt {
  // Used to create enum for integer User Input.
  NUMBER_RUNS(20,2,100), 
  TOTAL_NUMBER_INDIVIDUALS(20000,100,1000000),
  LENGTH(50,20,200),
  WIDTH(25,10,100),
  MINIMUM_AREA(100,10,100000);   // The MINIMUM_AREA will be reset to .1 of the
                                 // LENGTH time the WIDTH;
  
  UserInputInt (int defaultInt, int min, int max)   {  
     // Constructor that sets default values and minimums and maximum.
     value=defaultInt;
     this.min=min;
     this.max=max; 
  }
  
  private int value;
  private int min;
  private int max;
  public  int getValue()    { return value; }
  public  int getMin() { return min; }
  public  int getMax() { return max; }
  public  void setValue(int value) { this.value=value; }  
}
