#!/bin/sh

javac GroceryBaggin.java Bag.java Item.java WorldState.java
java GroceryBaggin $1 $2
