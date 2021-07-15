package client;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Args {
    @Parameter
    protected List<String> parameters = new ArrayList<>();

    @Parameter(names = "-databaseFileName")
    protected String command;

    @Parameter(names = "-k")
    protected String key;

    @Parameter(names = "-v")
    protected String value;

    @Parameter(names = "-in")
    protected String file;
}
