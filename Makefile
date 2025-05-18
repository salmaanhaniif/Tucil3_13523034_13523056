# Variables
SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = Main  # Change to your main class

# Find all .java files (using Windows style)
SOURCES = $(shell dir /b /s $(SRC_DIR)\*.java)

# Convert src path to bin path for .class files
CLASSES = $(patsubst $(SRC_DIR)\%.java,$(BIN_DIR)\%.class,$(SOURCES))

.PHONY: all build run clear

all: build run

build:
	javac -d bin $(SRC_DIR)/*.java  

run:
	java -cp $(BIN_DIR) $(MAIN_CLASS)

clear:
	del /q $(BIN_DIR)\*.class