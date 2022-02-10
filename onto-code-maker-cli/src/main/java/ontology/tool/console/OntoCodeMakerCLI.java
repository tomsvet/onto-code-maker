package ontology.tool.console;

import ontology.tool.OntoCodeMaker;


public class OntoCodeMakerCLI {

    public static void main(String[] args) {

        System.out.println("Welcome to  OntoCodeMaker");

        CLIOptionsParser parser = new CLIOptionsParser();
        parser.parseCLI(args);

        if(parser.getHelp()){
            return;
        }

        if(parser.getInputFiles().length == 0){
            return;
        }

        OntoCodeMaker maker = new OntoCodeMaker.Builder(parser.getInputFiles())
                                .format(parser.getFormat())
                                .language(parser.getLanguage())
                                .outputDir(parser.getDestination())
                                .build();
        maker.generateCodeFromOntology();


    }
}
