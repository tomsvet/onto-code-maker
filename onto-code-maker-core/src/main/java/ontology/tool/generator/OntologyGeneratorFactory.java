package ontology.tool.generator;

import ontology.tool.generator.language_generators.JavaGenerator;

public class OntologyGeneratorFactory {

    public OntologyGenerator getOntologyGenerator(String generator){
        if(generator.equalsIgnoreCase(JavaGenerator.GENERATOR_LANGUAGE_JAVA)){
            return new JavaGenerator();
        }

           return null;
    }

}
