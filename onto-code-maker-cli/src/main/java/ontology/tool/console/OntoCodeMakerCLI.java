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
        try {
            maker.generateCodeFromOntology();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
