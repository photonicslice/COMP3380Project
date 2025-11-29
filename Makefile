# Makefile for Brazilian E-Commerce Database Analyzer
# COMP 3380 - Database Project

# Compiler and flags
JAVAC = javac
JAVA = java
JFLAGS = -d bin -cp ".:DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar"
RUNFLAGS = -cp "bin:DemoJavaProjectRelease/mssql-jdbc-11.2.0.jre11.jar"

# Source files
SRC_DIR = src
BIN_DIR = bin
SOURCES = $(wildcard $(SRC_DIR)/*.java)
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Main class
MAIN = BrazilianECommerceAnalyzer

# Default target
all: compile

# Create bin directory if it doesn't exist
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Compile all Java source files
compile: $(BIN_DIR)
	$(JAVAC) $(JFLAGS) $(SOURCES)
	@echo "Compilation successful!"

# Run the application
run: compile
	$(JAVA) $(RUNFLAGS) $(MAIN)

# Clean compiled files
clean:
	rm -rf $(BIN_DIR)
	@echo "Cleaned build directory."

# Help target
help:
	@echo "Brazilian E-Commerce Database Analyzer - Makefile"
	@echo ""
	@echo "Available targets:"
	@echo "  make         - Compile all Java source files"
	@echo "  make compile - Compile all Java source files"
	@echo "  make run     - Compile and run the application"
	@echo "  make clean   - Remove all compiled files"
	@echo "  make help    - Display this help message"
	@echo ""
	@echo "Before running, ensure:"
	@echo "  1. auth.cfg exists with your database credentials"
	@echo "  2. Database schema has been created (run schema.sql)"
	@echo "  3. JDBC driver is in DemoJavaProjectRelease/"

.PHONY: all compile run clean help
