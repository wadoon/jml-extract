package jml;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import picocli.CommandLine;
import picocli.CommandLine.Help;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.eclipse.jdt.internal.compiler.util.Util.getInputStreamAsCharArray;


public class Main implements Callable<Integer> {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Option(names = {"-c", "-cp", "--classpath"}, arity = "1..*", paramLabel = "PATH",
            description = "specify classpath. Entries need to JAR files and folder. You can use ':' for sepearation")
    private List<String> classpaths = new ArrayList<>();

    @Option(names = {"-s", "--source"}, arity = "1..*", paramLabel = "PATH",
            description = "Source file to compile against. These files are no taken into account for dumping.")
    private List<String> sources = new ArrayList<>();

    @Option(names = {"--help", "-h"}, description = "show this help message")
    private boolean showHelp = false;

    @Option(names = {"-v", "--verbose"}, negatable = true)
    private boolean verbose = false;

    @Option(names = "--vm-classpath", negatable = true)
    private boolean vmClassPath = true;

    @Option(names = "--in", description = "read Java source from stdin")
    private boolean stdin = false;

    @Option(names = "--expr", description = "treat input as an expression")
    private boolean expression = false;

    @Option(names = "--stmt", description = "treat input as statements")
    private boolean statements = false;

    @Option(names = "--no-bodies", description = "do not parse the bodies of the methods")
    private boolean noBody = false;

    @Option(names = "-P", paramLabel = "KEY=VALUE", description = "set ecj compiler options. `--show-compiler-options` shows all options")
    Map<String, String> compilerOptions = new HashMap<>();

    @Option(names = "--show-compiler-options", description = "shows all underlying compiler options")
    boolean showCompilerOptions;


    @Option(names = "--version", description = "show program version")
    boolean showVersion;

    @Option(names = "--encoding", arity = "1", description = "set the encoding of the source files",
            defaultValue = "utf-8")
    private String encoding;

    @CommandLine.Parameters(arity = "*", index = "*", paramLabel = "PATH", description = "")
    private List<String> args = new LinkedList<>();

    @Option(names = "--java-version", description = "for future, currently not supported by JmlCore")
    private String javaVersion;

    @Override
    public Integer call() throws IOException {
        if (showVersion) {
            System.out.println("Version: " + JmlCore.getVersion());
            System.out.println("\t" + JmlCore.getEclipseVersion());
            return 0;
        }
        if (showHelp) {
            spec.commandLine().usage(System.out);
            return 0;
        }

        if ((statements || expression) && !stdin) {
            printe("-s and -e  only supported along with --stdin");
            return 1;
        }

        if (noBody && (statements || expression)) {
            printe("--stmt and --expr  does not support --no-bodies");
            return 1;
        }

        if (statements && expression) {
            printe("--stmt and --expr clashes");
            return 1;
        }

        JmlCore.init();
        JmlProject p = JmlCore.createProject();

        if (showCompilerOptions) {
            showCompilerOptions();
            return 0;
        }

        final String[] cp = getClasspaths();
        final String[] sources = getSourceEntries();
        printv(() -> "Sources: " + Arrays.toString(sources));
        printv(() -> "Classpath: " + Arrays.toString(cp));
        p.setEnvironment(cp, sources, encoding, vmClassPath);
        try (final OutputStreamWriter out = new OutputStreamWriter(System.out)) {
            if (stdin) {
                final String input = new String(getInputStreamAsCharArray(System.in, 4 * 1024 * 1024, encoding));
                if (statements) {
                    final ASTNode node = p.compileStatements(input);
                    p.dumpJson(out, Stream.of(node));
                } else if (expression) {
                    final ASTNode node = p.compileExpression(input);
                    p.dumpJson(out, Stream.of(node));
                    return 0;
                } else {
                    if (noBody) p.setIgnoreMethodBodies(true);
                    p.compileUnit("<unknown>", input.toCharArray());
                }
            } else {
                p.compileFiles(args.toArray(new String[0]));
            }
            //dumps all compilation units
            p.dumpJson(out);
        }
        return 0;
    }


    private void showCompilerOptions() {
        System.out.println("Default options:");
        Map<String, String> options = JavaCore.getDefaultOptions();
        printSorted(options);
        System.out.println("Options:");
        options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_13, options);
        printSorted(options);
    }

    private void printSorted(Map<String, String> options) {
        ArrayList<Map.Entry<String, String>> keys = new ArrayList<>(options.entrySet());
        keys.sort(Map.Entry.comparingByKey());
        keys.forEach((e) ->
                System.out.format("%s = %s\n", e.getKey(), e.getValue()));
    }


    private void printv(String message) {
        if (verbose) {
            System.out.print(Help.Ansi.Style.bg_blue.on());
            System.out.print(message);
            System.out.println(Help.Ansi.Style.bg_blue.off());
        }
    }

    private void printv(Supplier<String> message) {
        if (verbose) {
            printv(message.get());
        }
    }

    private void printe(String message) {
        System.out.print(Help.Ansi.Style.bg_blue.on());
        System.out.print(message);
        System.out.println(Help.Ansi.Style.bg_blue.off());
    }

    private String[] getSourceEntries() {
        List<String> sourceFolders = new LinkedList<>();
        for (String a : this.sources) {
            String[] b = a.split(":");
            sourceFolders.addAll(Arrays.asList(b));
        }
        return sourceFolders.toArray(new String[0]);
    }

    private String[] getClasspaths() {
        List<String> classpaths = new LinkedList<>();
        for (String a : this.classpaths) {
            String[] b = a.split(":");
            classpaths.addAll(Arrays.asList(b));
        }
        return classpaths.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Main main = new Main();
        CommandLine cli = new CommandLine(main)
                .setColorScheme(Help.defaultColorScheme(Help.Ansi.AUTO));
        int exit = cli.execute(args);
        System.exit(exit);
    }

}
