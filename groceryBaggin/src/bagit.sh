#!/bin/sh
if [ $# -eq 0 ]
    then
	echo "Usage: ./bagit.sh 'filename.txt' [-slow/minstdv/local]"
	exit 0
fi
javac GroceryBaggin.java Bag.java Item.java WorldState.java
java GroceryBaggin $1 $2
