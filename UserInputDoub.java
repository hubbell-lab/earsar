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
public enum UserInputDoub {
  // Used to create enum for double User Input. 
  DISTANCE_DECAY(.05,0.0,1000000.0),
  EXPONENT_NEIGHBOR_EFFECT(.6,0.0,10.0),
  STRENGTH_NEIGHBORHOOD_EFFECT(1251.0,0.0,1000000.0),
  ALPHA(40.0,0.0,1000000.0);

  UserInputDoub(double defaultDoub, double min, double max) { 
    // Constructor that set default values and minimums and maximum.
     value=defaultDoub; 
     this.min=min;
     this.max=max;
  }
  
  private double value;
  private double min;
  private double max;

  public  double getValue() { return value; }
  public  double getMin() { return min; }
  public  double getMax() { return max; }
  public  void setValue(double value) { this.value=value; } 
}

