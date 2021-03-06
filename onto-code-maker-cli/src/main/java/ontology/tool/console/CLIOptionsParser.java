package ontology.tool.console;

import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * CLIOptionsParser.java
 * Class for parsing command line arguments
 *
 * @author Tomas Svetlik
 *  2022
 *
 * OntoCodeMaker
 */
public class CLIOptionsParser {
    private static String FORMAT_OPTION_NAME = "format";
    private static String HELP_OPTION_NAME = "help";
    private static String LANGUAGE_OPTION_NAME = "language";
    private static String DESTINATION_OPTION_NAME = "destination";
    private static String PACKAGE_OPTION_NAME = "package";


    private String format;
    private String language;
    private String destination;
    private String packageName;
    private boolean help;
    private String[] inputFiles;

    public CLIOptionsParser(){
        destination = "";
        format = "";
        language = "";
        packageName ="";
        help=false;
    }

    /**
     * Setters and Getters
     *
     */

    public boolean getHelp(){
        return help;
    }

    public String getFormat(){
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLanguage(){
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDestination(){
        return destination;
    }

    public void setDestination(String destination){
        this.destination = destination;
    }

    public String[] getInputFiles(){
        return inputFiles;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    /**
     * Main class for parsing
     * @param args command line arguments
     * @return True value if everything is ok or false
     */
    public boolean parseCLI(String[] args){
        try {
            if(args.length == 0){
                throw new ParseException("Missing input file(s) and options. Check help option -h .");
            }

            Options options = getOptions();

            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(HELP_OPTION_NAME)) {
                printHelp(options);
                help = true;
                return true;
            }

            inputFiles = line.getArgs();

            if(inputFiles.length == 0){
                throw new ParseException("Missing input file(s).");
            }

            for(String inputFileName :inputFiles){
                Path file = Paths.get(inputFileName);
                if (!Files.exists(file)){
                    throw new FileNotFoundException("File " + inputFileName +" is not exist.");
                }
            }

            if (line.hasOption(FORMAT_OPTION_NAME)) {
                setFormat(line.getOptionValue(FORMAT_OPTION_NAME));
            }

            if(line.hasOption(LANGUAGE_OPTION_NAME)){
                setLanguage(line.getOptionValue(LANGUAGE_OPTION_NAME));
            }

            if(line.hasOption(DESTINATION_OPTION_NAME)){
                setDestination(line.getOptionValue(DESTINATION_OPTION_NAME));
            }

            if(line.hasOption(PACKAGE_OPTION_NAME)){
                setPackageName(line.getOptionValue(PACKAGE_OPTION_NAME));
            }

        } catch (ParseException | FileNotFoundException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * This method create arguments options
     * @return created Options
     */
    private Options getOptions(){
        Options options = new Options();

        Option help = Option.builder("h")
                .longOpt(HELP_OPTION_NAME)
                .desc("Print help message.")
                .hasArg(false)
                .argName(HELP_OPTION_NAME)
                .required(false)
                .build();

        Option format = Option.builder("f")
                .longOpt(FORMAT_OPTION_NAME)
                .desc("Syntax type of the input file. If absent it will try to guess.\n" +
                        "Supported formats: RDF/XML, Turtle, N-Triples, N-Quads, JSON-LD, TriG and TriX.")
                .hasArg(true).numberOfArgs(1)
                .argName(FORMAT_OPTION_NAME)
                .required(false)
                .build();

        Option language = Option.builder("l")
                .longOpt("language")
                .desc("Define the language of final source code.\n" +
                        "Default language is Java. \n" +
                        "All supported languages: java,python.")
                .hasArg(true).numberOfArgs(1)
                .argName(LANGUAGE_OPTION_NAME)
                .required(false)
                .build();

        Option destination = Option.builder("d")
                .longOpt("destination")
                .desc("Define the destination of generated source code.\n" +
                        " Default dir is actual dir.")
                .hasArg(true).numberOfArgs(1)
                .argName(DESTINATION_OPTION_NAME)
                .required(false)
                .build();

        Option packageName = Option.builder("p")
                .longOpt("package")
                .desc("Define the package of generated source code.\n" +
                        "Default package is empty.")
                .hasArg(true).numberOfArgs(1)
                .argName(PACKAGE_OPTION_NAME)
                .required(false)
                .build();


        options.addOption(format);
        options.addOption(language);
        options.addOption(destination);
        options.addOption(help);
        options.addOption(packageName);

        return options;
    }

    /**
     * Metho to print help message
     * @param options - all defined options
     */
    private void printHelp(Options options){
        HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printWrapped(writer, 80, 12, "Usage: OntoCodeMaker [options...] <input-file> [<input-file> ...]");
        formatter.printWrapped(writer, 80, 42, "  <input-file>                  The input file to read from (one or more)");
        formatter.printOptions(writer, 80, options, 2, 2);
        writer.flush();
        writer.close();

    }

}
