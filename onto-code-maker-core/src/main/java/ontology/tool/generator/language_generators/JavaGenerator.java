package ontology.tool.generator.language_generators;

import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.VocabularyConstant;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.EntityRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaGenerator extends OntologyGenerator {
    public static final String GENERATOR_LANGUAGE_JAVA = "java";
    public static final String VOCABULARY_FILE_NAME = "Vocabulary.java";

    static{
        VOCABULARY_TEMPLATE_NAME = "/templateJava.ftl";
        CLASS_TEMPLATE_NAME = "/templateJava.ftl";
    }
    public JavaGenerator() {
        super();
    }


    public String getVocabularyFileName() {
        return VOCABULARY_FILE_NAME;
    }

    public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties){
        Map<String, Object> data = new HashMap<>();

        data.put("className","Vocabulary");
        data.put("properties",properties);
        data.put("package","");
        data.put("imports",new ArrayList<>());
        return data;
    }

    public  List<VocabularyConstant> createVocabularyConstants(){

        List<VocabularyConstant> properties = new ArrayList<VocabularyConstant>();

        // ontology constants
        if(ontology != null) {
            VocabularyConstant ontCon = new VocabularyConstant();
            ontCon.setName(ontology.getName() + "_ONTOLOGY_IRI");
            ontCon.setType("IRI");
            ontCon.setValue("Values.iri(\" " + ontology.getIRI() + " \")");
            properties.add(ontCon);
        }

        //classes constants
        for (ClassRepresentation generatedClass : classes){
            VocabularyConstant prop = new VocabularyConstant();
            prop.setName( generatedClass.getConstantName().toUpperCase());
            prop.setType("IRI");
            prop.setValue("Values.iri(\" " +generatedClass.getIRI()+ " \")");
            properties.add(prop);
        }
        return properties;
    }

}
