#!/usr/bin/env bash
# ==============================================================================
# Run script for UG Campus Dispatch & Optimization System
# ==============================================================================

set -e

# Change to script directory
cd "$(dirname "$0")"

echo "Compiling Java source files..."
mkdir -p out
javac -cp "lib/*" -d out $(find src -name "*.java")

echo "Launching application..."
java -cp "out:lib/*" campusdispatch.CampusDispatchApp
