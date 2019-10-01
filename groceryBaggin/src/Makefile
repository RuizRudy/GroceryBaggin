JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	GroceryBaggin.java\
	Bag.java\
	Item.java\
	WorldState.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
