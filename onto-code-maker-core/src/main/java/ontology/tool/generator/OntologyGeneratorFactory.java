package ontology.tool.generator;

import ontology.tool.generator.language_generators.JavaGenerator;
import ontology.tool.generator.language_generators.PythonGenerator;

public class OntologyGeneratorFactory {

    public OntologyGenerator getOntologyGenerator(String generator){
        if(generator.equalsIgnoreCase(JavaGenerator.GENERATOR_LANGUAGE_JAVA)){
            return new JavaGenerator();
        }

        if(generator.equalsIgnoreCase(PythonGenerator.GENERATOR_LANGUAGE_PYTHON)){
            return new PythonGenerator();
        }

           return null;
    }

}
