package ontology.tool.console;

import org.apache.commons.cli.*;

import java.io.PrintWriter;

/**
 * @author Tomas Svetlik
 * 2022
 *  Class to parse input arguments
 *
 *  Usage: OntoCodeMaker [options...] <input-file> [<input-file> ...]
 *
 * <input-file>               the input file with ontology to read from (more files are allowed)
 * -a, --all                  do not hide entries starting with .
 *
 */
public class CLIOptionsParser {
    private static String ONTOLOGIES = "ontologies";
    private static String FORMAT_OPTION_NAME = "format";
    private static String HELP_OPTION_NAME = "help";
    private static String LANGUAGE_OPTION_NAME = "language";


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

    public String getLanguage(){
        return language;
    }

    public String getDestination(){
        return destination;
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


           /* if (line.hasOption(FORMAT_OPTION_NAME)) {

            }*/

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


        options.addOption(format);

        options.addOption(help);

        options.addOption("l","final-language",true,"Define the language of final source code.\n" +
                "Default language is Java.");






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
