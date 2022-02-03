package ontology.tool;

import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;

import java.io.FileNotFoundException;

public class OntoCodeMaker {

    OntologyParser ontoParser = new OntologyParser();


    private String[] inputFiles;
    private String formatName;

    public  OntoCodeMaker(String[] files){
        inputFiles = files;
    }

    public void setInputFiles(String[] files){
        inputFiles = files;
    }


    public String[] getInputFiles(){
        return inputFiles;
    }

    public void setFormatName(String format){
        formatName = format;
    }


    public void generateCodeFromOntology(){
        Model modelOfTriples;
        try {
            modelOfTriples = ontoParser.parseOntology(inputFiles,formatName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
