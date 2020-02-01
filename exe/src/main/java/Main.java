import jml.JmlCore;
import jml.JmlProject;
import org.docopt.Docopt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Main implements Runnable {
    private static final String PROGRAM_NAME = "jml-extractor 1.0";
    private static Main main = new Main();

    private List<String> classpaths = new ArrayList<>();
    private List<String> sources = new ArrayList<>();

    private boolean showHelp = false;
    private boolean verbose = false;
    private Map<String, Object> options;

    private void parseOptions(String[] args) throws IOException {
        try (InputStream in = Objects.requireNonNull(getClass().getResourceAsStream("/cli.txt"))) {
            String doc = new BufferedReader(new InputStreamReader(in))
                    .lines().collect(Collectors.joining("\n"));
            options = new Docopt(doc).withVersion(PROGRAM_NAME).parse(args);
            System.out.println(options);
        }
    }

    @Override
    public void run() {
        JmlCore.init();
        JmlProject p = JmlCore.createProject();
        //p.setEnvironment();
    }


    public static void main(String[] args) throws IOException {
        main.parseOptions(args);
        main.run();
    }

}
