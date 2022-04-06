package ontology.tool;

import ontology.tool.generator.OntologyGeneratorFactory;
import ontology.tool.generator.representations.AbstractClassRepresentation;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.NormalClassRepresentation;
import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.representations.OntologyRepresentation;
import ontology.tool.mapper.OntologyMapper;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;

import java.io.FileNotFoundException;
import java.util.List;

public class OntoCodeMaker {

    OntologyParser ontoParser = new OntologyParser();


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

    public void generateCodeFromOntology() throws Exception {
        Model modelOfTriples;
        modelOfTriples = ontoParser.parseOntology(inputFiles,formatName);
        if(modelOfTriples == null){
            throw new Exception("Error: Model is empty.");
        }

        OntologyMapper mapper = new OntologyMapper(modelOfTriples);
        List<OntologyRepresentation> ontologies = mapper.getOWLOntologies();
        for(OntologyRepresentation ontology:ontologies){
            mapper.mapOntologyInformations(ontology);
        }

        mapper.mapping();
        List<ClassRepresentation> classes = mapper.getMappedClasses();

        OntologyGeneratorFactory factory = new OntologyGeneratorFactory();
        if(language == null || language.isEmpty()){
            language = "java";
        }
        OntologyGenerator generator = factory.getOntologyGenerator(language);
        if(generator == null){
            throw new Exception("The language " + language + " is not supported. Supported languages are defined in the help message.");
        }
        generator.addClasses(classes);

        generator.setOntologies(ontologies);
        if(outputDir!= null && !outputDir.isEmpty()){
            generator.setOutputDir(outputDir);
        }
        if(packageName!= null && !packageName.isEmpty()){
            generator.setPackageName(packageName);
        }

        generator.generateCode();


        if(inputFiles.length == 1) {
            //todo change for logger
            System.out.println("The code for your ontology is successfully generated.");
        }else if(inputFiles.length > 1){
            System.out.println("The code for your ontologies is successfully generated.");

        }
    }

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
