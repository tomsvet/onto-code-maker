package ontology.tool;

import ontology.tool.generator.OntologyGeneratorFactory;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.representations.EntityRepresentation;
import ontology.tool.mapper.OntologyMapper;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class OntoCodeMaker {

    OntologyParser ontoParser = new OntologyParser();


    private String[] inputFiles;
    private String formatName;
    private String language;
    private String outputDir;

    public  OntoCodeMaker(Builder builder){
        this.inputFiles = builder.inputFiles;
        this.formatName = builder.formatName;
        this.language = builder.language;
        this.outputDir = builder.outputDir;

    }

    public void generateCodeFromOntology(){
        Model modelOfTriples;
        try {
            modelOfTriples = ontoParser.parseOntology(inputFiles,formatName);

            OntologyMapper mapper = new OntologyMapper(modelOfTriples);
            EntityRepresentation ontology = mapper.getOWLOntologies();
            List<ClassRepresentation> classes = mapper.mapClasses();

            OntologyGeneratorFactory factory = new OntologyGeneratorFactory();
            OntologyGenerator generator = factory.getOntologyGenerator("java");
            generator.addClasses(classes);
            generator.setOntology(ontology);
            generator.generateVocabulary();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Builder {
        private String[] inputFiles;
        private String formatName;
        private String language;
        private String outputDir;

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

        public OntoCodeMaker build(){
            return new OntoCodeMaker(this);
        }
    }

}
