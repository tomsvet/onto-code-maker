package ontology.tool.generator.language_generators;

import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.VocabularyConstant;
import ontology.tool.mapper.representations.*;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import java.util.*;

/**
 *  JavaGenerator.java
 *
 *  Class to model data for templates generating Java code
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class JavaGenerator extends OntologyGenerator {

    public static final String GENERATOR_LANGUAGE_JAVA = "java";

    static{
        VOCABULARY_TEMPLATE_NAME = "/JavaTemplates/vocabularyTemplateJava.ftl";
        CLASS_TEMPLATE_NAME = "/JavaTemplates/entitiesTemplateJava.ftl";
        SERIALIZATION_TEMPLATE_NAME = "/JavaTemplates/serializationTemplateJava.ftl";
        FACTORY_TEMPLATE_NAME = "/JavaTemplates/factoryTemplateJava.ftl";
        FILE_EXTENSION = ".java";

        dataTypes.put(XSD.ANYURI,"java.net.URL");
        dataTypes.put(XSD.BOOLEAN,"Boolean");
        dataTypes.put(XSD.BYTE,"Byte");
        dataTypes.put(XSD.DATE,"java.util.Date");
        dataTypes.put(XSD.DATETIME,"java.util.Date");
        dataTypes.put(XSD.DATETIMESTAMP,"java.sql.Timestamp");
        dataTypes.put(XSD.DAYTIMEDURATION,"java.time.Duration");
        dataTypes.put(XSD.DECIMAL,"Float");
        dataTypes.put(XSD.DOUBLE,"Double");
        dataTypes.put(XSD.DURATION,"java.time.Duration");
        dataTypes.put(XSD.FLOAT,"Float");
        dataTypes.put(XSD.INT,"Integer");
        dataTypes.put(XSD.INTEGER,"Integer");
        dataTypes.put(XSD.LONG,"Long");
        dataTypes.put(XSD.SHORT,"Short");
        dataTypes.put(XSD.TIME,"java.time.LocalTime");
        dataTypes.put(XSD.NON_NEGATIVE_INTEGER,"Integer");
        dataTypes.put(XSD.STRING,"String");
    }
    public JavaGenerator() {
        super();
    }

    public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties){
        Map<String, Object> data = new HashMap<>();
        data.put("className",VOCABULARY_FILE_NAME);
        data.put("properties",properties);
        data.put("package",this.packageName);
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

    public Map<String, Object> getMainEntityData(){
        Map<String, Object> data = new HashMap<>();
        data.put("className",CLASS_ENTITY_FILE_NAME);
        data.put("isInterface",true);
        data.put("isAbstract",false);
        data.put("mainInterface",true);
        data.put("package",this.getPackageName() + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("imports",new ArrayList<>());
        return data;
    }

    private List<String> getAllSuperClassInterfaceNames(DefaultClassRepresentation classRep){
        ArrayList<String> retVals = new ArrayList<>();
        for(ClassRepresentation superClass:classRep.getSuperClasses()){
            if(superClass.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)){
                if(superClass.isHasInterface()) {
                    retVals.add(superClass.getName() + ENTITY_INTERFACE_SUFFIX);
                }else if(superClass.isUnionOf()){
                    retVals.add(superClass.getName());
                }
            }else{
                retVals.add(superClass.getName() + ENTITY_INTERFACE_SUFFIX);
            }
        }
        return retVals;
    }

    public Map<String, Object> getEquivalentClassEntityData(String className, ClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className", className);
        data.put("classRep",classRep);
        data.put("isEquivalent",true);
        data.put("isInterface",true);
        data.put("mainInterface",false);
        data.put("isExtends",true);
        data.put("isAbstract",false);
        data.put("isImplements",false);
        if (classRep.getEquivalentClass().hasSuperClass()) {
            data.put("extendClasses",getAllSuperClassInterfaceNames(classRep.getEquivalentClass()));
        } else {
            data.put("extendClasses",new ArrayList<>(Collections.singletonList(CLASS_ENTITY_FILE_NAME)));
        }
        data.put("package", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    public Map<String, Object> getInterfaceEntityData(ClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName() + ENTITY_INTERFACE_SUFFIX);
        data.put("classRep",classRep);
        data.put("isInterface",true);
        data.put("mainInterface",false);
        data.put("isExtends",true);
        data.put("isAbstract",false);
        data.put("isImplements",false);
        ArrayList<Object> extendedList = new ArrayList<>();
        if(classRep.getEquivalentClass() != null){
            extendedList.add(classRep.getEquivalentClass().getName());
        }
        if (classRep.hasSuperClass()) {
            extendedList.addAll(getAllSuperClassInterfaceNames(classRep));
        }
        if(extendedList.isEmpty()){
            extendedList.add(CLASS_ENTITY_FILE_NAME);
        }
        data.put("extendClasses",extendedList);
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    public Map<String, Object> getAbstractClassEntityData(AbstractClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("classRep",classRep);
        data.put("mainInterface",false);
        data.put("isExtends",false);
        data.put("isImplements",false);
        data.put("extendedInterface", true);
        ArrayList<String> extendClasses =new ArrayList<>();
        ArrayList<String> implementClasses =new ArrayList<>();
        if(classRep.isUnionOf()) {
            data.put("isInterface",true);
            data.put("isAbstract",false);
            if (classRep.getEquivalentClass() != null) {
                extendClasses.add(classRep.getEquivalentClass().getName());
            }
            if (classRep.hasSuperClass()) {
                extendClasses.addAll( getAllSuperClassInterfaceNames(classRep));
            } else {
                if(extendClasses.isEmpty()){
                    extendClasses.add(CLASS_ENTITY_FILE_NAME);
                }
            }
        }else {
            data.put("isInterface",false);
            data.put("isAbstract",true);
            if (classRep.isHasInterface()) {
                implementClasses.add(classRep.getName() + ENTITY_INTERFACE_SUFFIX);
            } else {
                if (classRep.getEquivalentClass() != null) {
                    implementClasses.add(classRep.getEquivalentClass().getName());
                }
                if (classRep.hasOneSuperClass()) {
                    ClassRepresentation superClass = classRep.getSuperClasses().get(0);
                    extendClasses.add(superClass.getName());
                    data.put("extendedInterface", false);
                } else if (!classRep.hasSuperClass()) {
                    implementClasses.add(CLASS_ENTITY_FILE_NAME);
                } else {
                    implementClasses.addAll(getAllSuperClassInterfaceNames(classRep));
                }
            }
        }
        if (!implementClasses.isEmpty()) {
            data.put("isImplements", true);
            data.put("implementClasses", implementClasses);
        }
        if (!extendClasses.isEmpty()) {
            data.put("isExtends", true);
            data.put("extendClasses", extendClasses);
        }
        data.put("rawPackage",this.packageName);
        data.put("package", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    public Map<String, Object> getEntityData(NormalClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("classRep",classRep);
        data.put("isInterface",false);
        data.put("isAbstract",false);
        data.put("mainInterface",false);
        data.put("isExtends",false);
        data.put("isImplements",false);
        data.put("extendedInterface", true);
        ArrayList<String> extendClasses =new ArrayList<>();
        ArrayList<String> implementClasses =new ArrayList<>();
        if (classRep.isHasInterface()) {
            implementClasses.add(classRep.getName() + ENTITY_INTERFACE_SUFFIX);
        } else {
            if( classRep.getEquivalentClass() != null){
                implementClasses.add(classRep.getEquivalentClass().getName());
            }
            if (classRep.hasOneSuperClass()) {
                ClassRepresentation superClass = classRep.getSuperClasses().get(0);
                if(superClass.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT) && superClass.isUnionOf()){
                    implementClasses.add(superClass.getName());
                    data.put("extendedInterface", true);
                }else{
                    extendClasses.add(superClass.getName());
                    data.put("extendedInterface", false);
                }

            } else if (!classRep.hasSuperClass()) {
                if(implementClasses.isEmpty()){
                    implementClasses.add(CLASS_ENTITY_FILE_NAME);
                }
            } else {
                implementClasses.addAll(getAllSuperClassInterfaceNames(classRep));
            }
        }
        if(!implementClasses.isEmpty()){
            data.put("isImplements",true);
            data.put("implementClasses", implementClasses);
        }
        if(!extendClasses.isEmpty()){
            data.put("isExtends",true);
            data.put("extendClasses",extendClasses);
        }

        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("properties",new ArrayList<>()); //classRep.
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("rawPackage",this.packageName);
        data.put("imports",new ArrayList<>());
        return data;
    }

    public Map<String, Object> getSerializationData(NormalClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX);
        data.put("classRep",classRep);
        data.put("isInterface",false);
        data.put("serializationModelName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("entityPackage",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("rawPackage",this.packageName);
        data.put("subClasses",classRep.getSubClasses());
        data.put("serializationFactory",SERIALIZATION_FACTORY_FILE_NAME);

        return data;
    }

    public Map<String, Object> getMainSerializationData(){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("isInterface",true);
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("entityPackage",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    public Map<String, Object> getOntologyFactoryData(List<OntologyRepresentation> ontologies,String fileName,List<NormalClassRepresentation> classes){
        Map<String, Object> data = new HashMap<>();
        data.put("ontologies",ontologies);
        data.put("classFileName",fileName);
        data.put("serializationClasses",classes);
        data.put("serializationFactory",SERIALIZATION_FACTORY_FILE_NAME);
        data.put("typeOfFactory","Ontology");
        data.put("package",this.packageName);
        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        data.put("serializationPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("entityPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        return data;
    }

    public Map<String, Object> getSerializationFactoryData(List<NormalClassRepresentation> classes){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_FACTORY_FILE_NAME);
        data.put("serializationClasses",classes);
        data.put("typeOfFactory","Serialization");
        data.put("package",this.packageName);
        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        data.put("serializationPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("entityPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        data.put("ontologies",new ArrayList<>());
        return data;
    }
}
