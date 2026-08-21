package campusdispatch.database;

/**
 * Command line utility to initialize/reset the SQLite database and seed CSV data.
 */
public class DBSetup {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  Initializing & Seeding SQLite Database ");
        System.out.println("=========================================");
        System.out.println("1. Creating database tables...");
        SchemaInitializer.initializeDatabase();
        System.out.println("2. Loading CSV seed datasets...");
        CSVDataLoader.loadAll();
        System.out.println("=========================================");
        System.out.println("Database setup complete! (campus_dispatch.db)");
        System.out.println("=========================================");
    }
}
