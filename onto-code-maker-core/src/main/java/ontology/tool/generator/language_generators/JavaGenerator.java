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
        VOCABULARY_TEMPLATE_NAME = "/JavaTemplates/vocabularyTemplateJava.ftl";
        CLASS_TEMPLATE_NAME = "/JavaTemplates/entitiesTemplateJava.ftl";
        SERIALIZATION_TEMPLATE_NAME = "/JavaTemplates/serializationTemplateJava.ftl";
        FACTORY_TEMPLATE_NAME = "/JavaTemplates/factoryTemplateJava.ftl";
        FILE_EXTENSION = ".java";
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
            prop.setName( generatedClass.getConstantName());
            prop.setType("IRI");
            prop.setValue("Values.iri(\" " +generatedClass.getIRI()+ " \")");
            properties.add(prop);
        }
        return properties;
    }


    public Map<String, Object> getEntityData(ClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("isAbstract",false);
        data.put("isExtended",true);
        data.put("extendClass",CLASS_ENTITY_FILE_NAME);
        data.put("vocabularyClassConstant",classRep.getConstantName());
        data.put("properties",new ArrayList<>()); //classRep.
        data.put("package","");
        data.put("imports",new ArrayList<>());
        return data;
    }

    public Map<String, Object> getAbstractEntityData(){
        Map<String, Object> data = new HashMap<>();
        data.put("className",CLASS_ENTITY_FILE_NAME);
        data.put("isAbstract",true);
        data.put("isExtended",false);
        data.put("properties",new ArrayList<>());
        data.put("package","");
        data.put("imports",new ArrayList<>());
        return data;
    }

    public Map<String, Object> getSerializationData(ClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX);
        data.put("className",classRep.getName());
        data.put("isInterface",false);
        data.put("serializationModelName",SERIALIZATION_MODEL_FILE_NAME);
        //data.put("properties",new ArrayList<>());
        data.put("package","");
       // data.put("imports",new ArrayList<>());
        return data;
    }

    public Map<String, Object> getInterfaceSerializationData(){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("isInterface",true);
        data.put("package","");
        //data.put("imports",new ArrayList<>());
        return data;
    }





}
