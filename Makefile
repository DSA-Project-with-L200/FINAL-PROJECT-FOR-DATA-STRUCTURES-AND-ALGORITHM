# ==============================================================================
# Makefile for UG Campus Dispatch & Optimization System
# ==============================================================================
#
# QUICK RUN COMMAND (This is all you need to start the app):
#   make run
#
# OTHER USEFUL COMMANDS:
#   make run        - Compiles the code and launches the interactive application
#   make web        - Launches the interactive Web UI demonstrator in your browser
#   make db-init   - Create SQLite schema & load baseline CSV data (50 locations, 100 roads, 300 requests, 30 taxis)
#   make db-reset  - Delete local database and re-initialize from scratch
#   make build      - Compile all Java source files into out/
#   make clean      - Remove compiled class files and local database
#   make help      - Display all available Makefile targets
# ==============================================================================

# Directories & Classpaths
SRC_DIR    := src
OUT_DIR    := out
LIB_DIR    := lib
CLASSPATH  := "out:lib/*"
MAIN_CLASS := campusdispatch.CampusDispatchApp
DB_SETUP   := campusdispatch.database.DBSetup
DB_FILE    := campus_dispatch.db

# Find all Java source files
JAVA_FILES := $(shell find $(SRC_DIR) -name "*.java")

.PHONY: all run run-app web gui build compile db-init db-setup db-reset clean help

# Launch the Modern React Vite Web UI Demonstrator
web:
	@echo "=================================================================="
	@echo "   Launching UG Campus Dispatch React Vite Web UI...             "
	@echo "=================================================================="
	@cd web && npm run dev -- --open

web-build:
	@echo "Building React Web Application bundle..."
	@cd web && npm run build

gui: web

# Default target: compile, ensure DB is ready, and launch the application
all: run

# ------------------------------------------------------------------------------
# 1. RUN TARGET - Launches the interactive application
# ------------------------------------------------------------------------------
# Usage: make run
# How it works:
#   1. Compiles Java source files if necessary
#   2. Checks if campus_dispatch.db exists; if missing, automatically runs db-init
#   3. Starts the main interactive CLI menu
run: build check-db
	@echo "=================================================================="
	@echo "   Launching UG Campus Dispatch System & Web Application...      "
	@echo "   - Java REST API Backend:  http://localhost:8080/api/status   "
	@echo "   - React Web UI Server:    http://localhost:5173/             "
	@echo "=================================================================="
	@(cd web && npm run dev -- --open &)
	@java -cp $(CLASSPATH) $(MAIN_CLASS)

# Alias for make run
run-app: run

# ------------------------------------------------------------------------------
# 2. DATABASE MANAGEMENT TARGETS
# ------------------------------------------------------------------------------
# Usage: make db-init
# Creates database tables and seeds CSV data (locations, roads, requests, taxis)
db-init: build
	@echo "=================================================================="
	@echo "   Initializing & Seeding SQLite Database (campus_dispatch.db)... "
	@echo "=================================================================="
	@java -cp $(CLASSPATH) $(DB_SETUP)

# Alias for db-init
db-setup: db-init

# Usage: make db-reset
# Removes current database file and seeds a fresh database from scratch
db-reset:
	@echo "Resetting database..."
	@rm -f $(DB_FILE)
	@$(MAKE) db-init

# Usage: make db-view
# Inspects tables, record counts, and sample data inside campus_dispatch.db
db-view: check-db
	@echo "=================================================================="
	@echo "   Inspecting SQLite Database (campus_dispatch.db)...            "
	@echo "=================================================================="
	@echo "TABLES:"
	@sqlite3 $(DB_FILE) ".tables"
	@echo "\nRECORD COUNTS:"
	@sqlite3 $(DB_FILE) "SELECT 'locations: ', count(*) FROM locations;"
	@sqlite3 $(DB_FILE) "SELECT 'roads: ', count(*) FROM roads;"
	@sqlite3 $(DB_FILE) "SELECT 'service_requests: ', count(*) FROM service_requests;"
	@sqlite3 $(DB_FILE) "SELECT 'resources: ', count(*) FROM resources;"
	@sqlite3 $(DB_FILE) "SELECT 'audit_events: ', count(*) FROM audit_events;"
	@echo "\nSAMPLE LOCATIONS (First 5):"
	@sqlite3 $(DB_FILE) -header -column "SELECT locationId, name, zone, latitude, longitude FROM locations LIMIT 5;"
	@echo "\nSAMPLE SERVICE REQUESTS (First 5):"
	@sqlite3 $(DB_FILE) -header -column "SELECT requestId, requesterName, userCategory, status FROM service_requests LIMIT 5;"

# Helper check target used by 'make run'
check-db:
	@if [ ! -f $(DB_FILE) ]; then \
		echo "Database file '$(DB_FILE)' not found. Initializing now..."; \
		$(MAKE) db-init; \
	fi

# ------------------------------------------------------------------------------
# 3. BUILD / COMPILE TARGETS
# ------------------------------------------------------------------------------
# Usage: make build
build: compile

compile: $(JAVA_FILES)
	@echo "Compiling Java source files..."
	@mkdir -p $(OUT_DIR)
	@javac -cp "lib/*" -d $(OUT_DIR) $(JAVA_FILES)
	@echo "Compilation successful!"

# ------------------------------------------------------------------------------
# 4. CLEAN & HELP TARGETS
# ------------------------------------------------------------------------------
# Usage: make clean
clean:
	@echo "Cleaning compiled classes and database..."
	@rm -rf $(OUT_DIR)
	@rm -f $(DB_FILE)
	@echo "Clean completed."

# Usage: make help
help:
	@echo "=================================================================="
	@echo "   UG Campus Dispatch System Makefile Instructions                "
	@echo "=================================================================="
	@echo "  make run        : Build project, initialize DB (if needed), and run app"
	@echo "  make run-app    : Alias for 'make run'"
	@echo "  make db-init    : Initialize database schema & load CSV seed data"
	@echo "  make db-reset   : Wipe database and re-seed from scratch"
	@echo "  make build      : Compile all Java files into out/"
	@echo "  make clean      : Remove out/ directory and database file"
	@echo "  make help       : Show this help menu"
	@echo "=================================================================="
