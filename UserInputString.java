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
public enum UserInputString {
  // Used to create enum for String User Input. 
  STEM_FILENAME("");

  UserInputString (String defaultString) {
     // Constructor that sets default values.
     value=defaultString; 
   }
  
   private String value;
   public  String getValue() { return value; }
   public void setValue(String val) { value=val; }
}


