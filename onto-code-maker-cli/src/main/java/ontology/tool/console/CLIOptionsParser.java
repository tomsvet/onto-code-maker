package ontology.tool.console;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * CLIOptionsParser.java
 *
 * @author Tomas Svetlik
 *  2022
 *  Class to parse input arguments
 *
 *
 *
 */
public class CLIOptionsParser {
    private static String ONTOLOGIES = "ontologies";
    private static String FORMAT_OPTION_NAME = "format";
    private static String HELP_OPTION_NAME = "help";
    private static String LANGUAGE_OPTION_NAME = "language";
    private static String DESTINATION_OPTION_NAME = "destination";


    private String format;
    private String language;
    private String destination;
    private boolean help;
    private String[] inputFiles;

    CLIOptionsParser(){
        destination = System.getProperty("user.dir");
        format = "";
        language = "";
        help=false;
    }


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



    public void parseCLI(String[] args){

        CommandLineParser parser = new DefaultParser();

        Options options = getOptions();

        try {
            CommandLine line = parser.parse(options, args);

            if (line.hasOption(HELP_OPTION_NAME)) {
                printHelp(options);
                help = true;
                return;
            }

            inputFiles = line.getArgs();


            if (line.hasOption(FORMAT_OPTION_NAME)) {
                setFormat(line.getOptionValue(FORMAT_OPTION_NAME));
            }

            if(line.hasOption(LANGUAGE_OPTION_NAME)){
                setLanguage(line.getOptionValue(LANGUAGE_OPTION_NAME));
            }

            if(line.hasOption(DESTINATION_OPTION_NAME)){
                setDestination(line.getOptionValue(DESTINATION_OPTION_NAME));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Options getOptions(){
        Options options = new Options();

        Option help = Option.builder("h")
                .longOpt(HELP_OPTION_NAME)
                .desc("Print help message.")
                .hasArg(false)
                .argName(HELP_OPTION_NAME)
                .required(false)
                .build();

        Option format = Option.builder("f")
                .longOpt("format")
                .desc("Syntax type of the input file. If absent it will try to guess.")
                .hasArg(true).numberOfArgs(1)
                .argName(FORMAT_OPTION_NAME)
                .required(false)
                .build();

        Option language = Option.builder("l")
                .longOpt("language")
                .desc("Define the language of final source code.\n" +
                        "Default language is Java.")
                .hasArg(true).numberOfArgs(1)
                .argName(LANGUAGE_OPTION_NAME)
                .required(false)
                .build();

        Option destination = Option.builder("d")
                .longOpt("destination")
                .desc("Define the destination of generated source code.")
                .hasArg(true).numberOfArgs(1)
                .argName(DESTINATION_OPTION_NAME)
                .required(false)
                .build();


        options.addOption(format);
        options.addOption(language);
        options.addOption(destination);
        options.addOption(help);

        return options;
    }

    public void printHelp(Options options){
        HelpFormatter formatter = new HelpFormatter();
        final PrintWriter writer = new PrintWriter(System.out);
        formatter.printWrapped(writer,80,12,"  <input file>                  the input file to read from (one or more)");
        //formatter.printHelp("OntoCodeMaker", options);
        formatter.printHelp("OntoCodeMaker",options,true);
        //formatter.printUsage(writer,80,"OntoCodeMaker", options);
        writer.flush();
        writer.close();

    }

}
