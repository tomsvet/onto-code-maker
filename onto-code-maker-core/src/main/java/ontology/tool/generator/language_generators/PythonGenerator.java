package ontology.tool.generator.language_generators;

import freemarker.template.Template;
import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.VocabularyConstant;
import ontology.tool.mapper.representations.*;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  PythonGenerator.java
 *
 *  Class to model data for templates generating Python code
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class PythonGenerator extends OntologyGenerator {

    public static final String GENERATOR_LANGUAGE_PYTHON = "python";

    static{
        VOCABULARY_TEMPLATE_NAME = "/PythonTemplates/vocabularyTemplatePython.ftl";
        CLASS_TEMPLATE_NAME = "/PythonTemplates/entitiesTemplatePython.ftl";
        SERIALIZATION_TEMPLATE_NAME = "/PythonTemplates/serializationTemplatePython.ftl";
        FACTORY_TEMPLATE_NAME = "/PythonTemplates/factoryTemplatePython.ftl";
        FILE_EXTENSION = ".py";

    }
    public PythonGenerator() {
        super();
    }


    @Override
    public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties) {
        Map<String, Object> data = new HashMap<>();
        data.put("className","Vocabulary");
        data.put("properties",properties);
        data.put("imports",new ArrayList<>());
        return data;
    }

    public  List<VocabularyConstant> createVocabularyConstants(){

        List<VocabularyConstant> propertiesConstants = new ArrayList<>();
        int index = 0;
        // ontology constants
        for(OntologyRepresentation ontology:ontologies) {
            VocabularyConstant ontCon = new VocabularyConstant();
            ontCon.setName(ontology.getName().isEmpty()?"Default"+ index + "_ONTOLOGY_IRI":ontology.getName().toUpperCase() + "_ONTOLOGY_IRI");
            ontCon.setValue( ontology.getStringIRI());
            ontCon.setConstantOf("ontology");
            ontCon.setObjectName(ontology.getName());
            propertiesConstants.add(ontCon);
            index++;
        }

        //classes constants
        for (ClassRepresentation generatedClass : classes){
            if(generatedClass.getClassType().equals(ClassRepresentation.CLASS_TYPE.NORMAL)) {
                NormalClassRepresentation genClass = (NormalClassRepresentation) generatedClass;
                VocabularyConstant propC = new VocabularyConstant();
                propC.setName(genClass.getConstantName());
                propC.setValue(genClass.getStringIRI());
                propC.setConstantOf("class");
                propC.setObjectName(genClass.getName());
                propertiesConstants.add(propC);
            }

        }

        for(PropertyRepresentation property: properties){
            VocabularyConstant propP = new VocabularyConstant();
            propP.setName( property.getConstantName());
            propP.setValue(property.getStringIRI());
            propP.setConstantOf("property");
            propP.setObjectName(property.getName());
            propertiesConstants.add(propP);
        }

        return propertiesConstants;
    }

    @Override
    public Map<String, Object> getMainEntityData() {
        Map<String, Object> data = new HashMap<>();
        data.put("className",CLASS_ENTITY_FILE_NAME);
        data.put("isMainClass",true);
        data.put("isClass",false);
        data.put("isAbstractClass",false);
        data.put("isExtends",false);
        return data;
    }

    @Override
    public Map<String, Object> getEntityData(NormalClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("classRep",classRep);
        data.put("isClass",true);
        data.put("isAbstractClass",false);
        data.put("isMainClass",false);
        data.put("isExtends",false);
        ArrayList<String> extendClasses =new ArrayList<>();
        if( classRep.getEquivalentClass() != null){
            extendClasses.add(classRep.getEquivalentClass().getName());
        }
        if (classRep.hasOneSuperClass()) {
            ClassRepresentation superClass = classRep.getSuperClasses().get(0);
            extendClasses.add(superClass.getName());
        } else if (!classRep.hasSuperClass()) {
            if(extendClasses.isEmpty()){
                extendClasses.add(CLASS_ENTITY_FILE_NAME);
            }
        } else {
            extendClasses.addAll(classRep.getSuperClasses().stream().map(c->c.getName()).collect(Collectors.toList()));
        }

        if(!extendClasses.isEmpty()){
            data.put("isExtends",true);
            data.put("extendClasses",extendClasses);
        }

        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("rawPackage",".");
        return data;
    }

    @Override
    public void generateInterfaceClass(Template templateFile, String entitiesOutputFile, ClassRepresentation generatedClass) {
    }

    public Map<String, Object> getInterfaceEntityData(ClassRepresentation classRep) {
    return null;
    }

    @Override
    public Map<String, Object> getEquivalentClassEntityData(String className, ClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",className);
        data.put("classRep",classRep);
        data.put("isAbstractClass",true);
        data.put("isClass",false);
        data.put("isMainClass",false);
        data.put("isExtends",true);
        if (classRep.getEquivalentClass().hasSuperClass()) {
            data.put("extendClasses",classRep.getEquivalentClass().getSuperClasses());
        } else {
            data.put("extendClasses",new ArrayList<>(Collections.singletonList(CLASS_ENTITY_FILE_NAME)));
        }
        data.put("imports",new ArrayList<>());
        data.put("rawPackage",".");
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        return data;
    }

    @Override
    public Map<String, Object> getAbstractClassEntityData(AbstractClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("classRep",classRep);
        data.put("isClass",true);
        data.put("isAbstractClass",true);
        data.put("isMainClass",false);
        data.put("isExtends",false);
        ArrayList<String> extendClasses =new ArrayList<>();
        if( classRep.getEquivalentClass() != null){
            extendClasses.add(classRep.getEquivalentClass().getName());
        }
        if (classRep.hasOneSuperClass()) {
            ClassRepresentation superClass = classRep.getSuperClasses().get(0);
            extendClasses.add(superClass.getName());
        } else if (!classRep.hasSuperClass()) {
            if(extendClasses.isEmpty()){
                extendClasses.add(CLASS_ENTITY_FILE_NAME);
            }
        } else {
            extendClasses.addAll(classRep.getSuperClasses().stream().map(c->c.getName()).collect(Collectors.toList()));
        }

        if(!extendClasses.isEmpty()){
            data.put("isExtends",true);
            data.put("extendClasses",extendClasses);
        }


        if(!extendClasses.isEmpty()){
            data.put("isExtends",true);
            data.put("extendClasses",extendClasses);
        }

        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("rawPackage",".");
        return data;
    }

    @Override
    public Map<String, Object> getSerializationData(NormalClassRepresentation classRep) {

        ArrayList<String> serializationClasses =new ArrayList<>();
        for(PropertyRepresentation property : classRep.getProperties()){
            if(property.getRangeClass() != null && property.getRangeClass().getClassType().getName() == "Normal"){
                String serializationClass = ((NormalClassRepresentation) property.getRangeClass()).getSerializationClassName();
                if(!serializationClasses.contains(serializationClass)){
                    serializationClasses.add(serializationClass);
                }

            }
        }
        for(ClassRepresentation superClass:classRep.getSuperClasses()){
            if(superClass.getClassType().getName() == "Normal" && (superClass.getProperties().size()>0 || superClass.getSuperClasses().size() > 0)){
                String serializationClass = ((NormalClassRepresentation)superClass).getSerializationClassName();
                if(!serializationClasses.contains(serializationClass)){
                    serializationClasses.add(serializationClass);
                }
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX);
        data.put("classRep",classRep);
        data.put("isSerializationModel",false);
        data.put("serializationModelName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("serializationClasses",serializationClasses);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("entityPackage",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("rawPackage",".");
        data.put("subClasses",classRep.getSubClasses());
        data.put("serializationFactory",SERIALIZATION_FACTORY_FILE_NAME);
        return data;
    }

    @Override
    public Map<String, Object> getMainSerializationData() {
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("isSerializationModel",true);
        data.put("imports",new ArrayList<>());
        data.put("entityPackage",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    @Override
    public Map<String, Object> getOntologyFactoryData(List<OntologyRepresentation> ontologies,String fileName, List<NormalClassRepresentation> classes) {
        Map<String, Object> data = new HashMap<>();
        data.put("ontologies",ontologies);
        data.put("classFileName",fileName);
        data.put("serializationClasses",classes);
        data.put("serializationFactory",SERIALIZATION_FACTORY_FILE_NAME);
        data.put("typeOfFactory","Ontology");
        data.put("serializationPackage", DIR_NAME_SERIALIZATION);
        data.put("entityPackage",  DIR_NAME_ENTITIES);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);

        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        return data;
    }

    public Map<String, Object> getSerializationFactoryData(List<NormalClassRepresentation> classes){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_FACTORY_FILE_NAME);
        data.put("serializationClasses",classes);
        data.put("typeOfFactory","Serialization");
        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        data.put("serializationPackage",  DIR_NAME_SERIALIZATION);
        data.put("entityPackage",  DIR_NAME_ENTITIES);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("ontologies",new ArrayList<>());
        return data;
    }
}
