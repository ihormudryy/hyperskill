package carsharing;

import com.beust.jcommander.Parameter;

public class Args {

    @Parameter(
            names = "-databaseFileName",
            description = "Database name",
            required = false
    )
    protected String dbName;

    public String getDbName() {
        return dbName;
    }
}
