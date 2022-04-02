package ontology.tool.console;

import ontology.tool.OntoCodeMaker;


public class OntoCodeMakerCLI {

    public static void main(String[] args) {

        System.out.println("Welcome to  OntoCodeMaker");

        CLIOptionsParser parser = new CLIOptionsParser();
        if(!parser.parseCLI(args)){
            return;
        }

        if(parser.getHelp()){
            return;
        }

        OntoCodeMaker maker = new OntoCodeMaker.Builder(parser.getInputFiles())
                                .format(parser.getFormat())
                                .language(parser.getLanguage())
                                .outputDir(parser.getDestination())
                                .packageName(parser.getPackageName())
                                .build();
        maker.generateCodeFromOntology();

        if(parser.getInputFiles().length == 1) {
            System.out.println("The code for your ontology is successfully generated.");
        }else if(parser.getInputFiles().length > 1){
            System.out.println("The code for your ontologies is successfully generated.");

        }
    }
}
