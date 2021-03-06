package ontology.tool;

import ontology.tool.generator.OntologyGeneratorFactory;
import ontology.tool.mapper.representations.*;
import ontology.tool.generator.OntologyGenerator;
import ontology.tool.mapper.OntologyMapper;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;

import java.util.Collection;
import java.util.List;

/**
 * OntoCodeMaker.java
 * Base core class
 * It integrates 3 parts of OntoCodeMaker : Parser, Mapper and Generator.
 *
 * @author Tomas Svetlik
 *  2022
 *
 * OntoCodeMaker
 */
public class OntoCodeMaker {

    private String[] inputFiles;
    private String formatName;
    private String language;
    private String outputDir;
    private String packageName;

    public  OntoCodeMaker(Builder builder){
        this.inputFiles = builder.inputFiles;
        this.formatName = builder.formatName;
        this.language = builder.language;
        this.outputDir = builder.outputDir;
        this.packageName = builder.packageName;

    }

    public  OntoCodeMaker(String[] inputFiles) {
        this.inputFiles = inputFiles;
    }

    /**
     * Main class of OntoCodeMaker Core
     * It parser ontology from file in inputFiles parameter of this class
     * It map ontology from RDF model to inner model
     * It generate source code from inner model
     *
     */
    public void generateCodeFromOntology() throws Exception {
        OntologyParser ontoParser = new OntologyParser();
        Model modelOfTriples = ontoParser.parseOntology(inputFiles,formatName);
        if(modelOfTriples == null){
            throw new Exception("Error: Model is empty.");
        }

        OntologyMapper mapper = new OntologyMapper(modelOfTriples);
        List<OntologyRepresentation> ontologies = mapper.getOWLOntologies();

        mapper.mapping();
        Collection<ClassRepresentation> classes = mapper.getCollectionOfMappedClasses();
        Collection<PropertyRepresentation> properties = mapper.getCollectionOfMappedProperties();
        OntologyGeneratorFactory factory = new OntologyGeneratorFactory();
        if(language == null || language.isEmpty()){
            language = "java";
        }
        OntologyGenerator generator = factory.getOntologyGenerator(language);
        if(generator == null){
            throw new Exception("The language " + language + " is not supported. Supported languages are defined in the help message.");
        }
        generator.addClasses(classes);
        generator.addProperties(properties);

        generator.setOntologies(ontologies);
        if(outputDir!= null && !outputDir.isEmpty()){
            generator.setOutputDir(outputDir);
        }
        if(packageName!= null && !packageName.isEmpty()){
            generator.setPackageName(packageName);
        }

        generator.generateCode();


        if(inputFiles.length == 1) {
            System.out.println("The code for your ontology has been successfully generated.");
        }else if(inputFiles.length > 1){
            System.out.println("The code for your ontologies has been successfully generated.");

        }
    }

    /**
     * OntoCodeMaker Builder class
     */
    public static class Builder {
        private String[] inputFiles;
        private String formatName;
        private String language;
        private String outputDir;
        private String packageName;

        public Builder(String[] inputFiles) {
            this.inputFiles = inputFiles;
        }

        public Builder format(String formatName){
            this.formatName = formatName;
            return this;
        }

        public Builder language(String language){
            this.language = language;
            return this;
        }

        public Builder outputDir(String outputDir){
            this.outputDir = outputDir;
            return this;
        }

        public Builder packageName(String packageName){
            this.packageName = packageName;
            return this;
        }

        public OntoCodeMaker build(){
            return new OntoCodeMaker(this);
        }
    }

}
