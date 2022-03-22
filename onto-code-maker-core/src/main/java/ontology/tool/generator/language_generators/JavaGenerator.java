package ontology.tool.generator.language_generators;

import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.VocabularyConstant;
import ontology.tool.generator.representations.*;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import java.util.*;


public class JavaGenerator extends OntologyGenerator {

    public static final String GENERATOR_LANGUAGE_JAVA = "java";

    static{
        VOCABULARY_TEMPLATE_NAME = "/JavaTemplates/vocabularyTemplateJava.ftl";
        CLASS_TEMPLATE_NAME = "/JavaTemplates/entitiesTemplateJava.ftl";
        SERIALIZATION_TEMPLATE_NAME = "/JavaTemplates/serializationTemplateJava.ftl";
        FACTORY_TEMPLATE_NAME = "/JavaTemplates/factoryTemplateJava.ftl";
        FILE_EXTENSION = ".java";

        dataTypes.put(XSD.ANYURI,"java.net.URL");
        dataTypes.put(XSD.BOOLEAN,"boolean");
        dataTypes.put(XSD.BYTE,"byte");
        dataTypes.put(XSD.DATE,"java.util.Date");
        dataTypes.put(XSD.DATETIME,"java.util.Date");
        dataTypes.put(XSD.DATETIMESTAMP,"java.sql.Timestamp");
        dataTypes.put(XSD.DAYTIMEDURATION,"java.time.Duration");
        dataTypes.put(XSD.DECIMAL,"float");
        dataTypes.put(XSD.DOUBLE,"double");
        dataTypes.put(XSD.DURATION,"java.time.Duration");
        dataTypes.put(XSD.FLOAT,"float");
        dataTypes.put(XSD.INT,"int");
        dataTypes.put(XSD.INTEGER,"java.lang.Integer");
        dataTypes.put(XSD.LONG,"long");
        dataTypes.put(XSD.SHORT,"short");
        dataTypes.put(XSD.TIME,"java.time.LocalTime");
        dataTypes.put(XSD.NON_NEGATIVE_INTEGER,"int");
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

        List<VocabularyConstant> properties = new ArrayList<>();

        // ontology constants
        if(ontology != null) {
            VocabularyConstant ontCon = new VocabularyConstant();
            ontCon.setName(ontology.getName().toUpperCase() + "_ONTOLOGY_IRI");
            ontCon.setValue( ontology.getStringIRI());
            ontCon.setConstantOf("ontology");
            ontCon.setObjectName(ontology.getName());
            properties.add(ontCon);
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
                properties.add(propC);
            }

            for(PropertyRepresentation property: generatedClass.getProperties()){
                VocabularyConstant propP = new VocabularyConstant();
                propP.setName( property.getConstantName());
                propP.setValue(property.getStringIRI());
                propP.setConstantOf("property");
                propP.setObjectName(property.getName());
                properties.add(propP);
            }

        }
        return properties;
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

    public List<String> getAllSuperClassInterfaceNames(DefaultClassRepresentation classRep){
        ArrayList<String> retVals = new ArrayList<>();
        for(ClassRepresentation superClass:classRep.getSuperClasses()){
            if(superClass.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)){
                retVals.add(superClass.getName() + ENTITY_ABSTRACTCLASS_SUFFIX);
            }else{
                retVals.add(superClass.getName() + ENTITY_INTERFACE_SUFFIX);
            }
        }
        return retVals;
    }

    public Map<String, Object> getEquivalentClassEntityData(String className, ClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",className + ENTITY_INTERFACE_SUFFIX);
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
        data.put("isInterface",true);
        data.put("mainInterface",false);
        data.put("isExtends",true);
        data.put("isAbstract",false);
        data.put("isImplements",false);
        if (classRep.hasSuperClass()) {
            data.put("extendClasses",getAllSuperClassInterfaceNames(classRep));
        } else {
            data.put("extendClasses",new ArrayList<>(Collections.singletonList(CLASS_ENTITY_FILE_NAME)));
        }
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }

    public Map<String, Object> getAbstractClassEntityData(AbstractClassRepresentation classRep){
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName() + ENTITY_ABSTRACTCLASS_SUFFIX);
        data.put("classRep",classRep);
        data.put("isInterface",false);
        data.put("isAbstract",true);
        data.put("mainInterface",false);
        data.put("isExtends",false);
        data.put("isImplements",false);
        data.put("extendedInterface", true);

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
        if( classRep.getEquivalentClass() != null){
            data.put("isImplements",true);
            data.put("implementClass", classRep.getEquivalentClass().getName() + ENTITY_INTERFACE_SUFFIX);
        }else {
            if (classRep.isHasInterface()) {
                data.put("isImplements",true);
                data.put("implementClass", classRep.getName() + ENTITY_INTERFACE_SUFFIX);
            } else {
                if (classRep.hasOneSuperClass()) {
                    ClassRepresentation superClass = classRep.getSuperClasses().get(0);
                    data.put("implementClass", superClass.getClassType().equals(ClassRepresentation.CLASS_TYPE.ABSTRACT)? ((AbstractClassRepresentation)superClass).getName() + ENTITY_ABSTRACTCLASS_SUFFIX:superClass.getName());
                    data.put("isImplements",true);
                    data.put("extendedInterface", false);
                } else if (!classRep.hasSuperClass()) {
                    data.put("isImplements",true);
                    data.put("implementClass", CLASS_ENTITY_FILE_NAME);
                } else {
                    data.put("isExtends",true);
                    data.put("extendClasses", getAllSuperClassInterfaceNames(classRep));
                }
            }
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

        return data;
    }

    public Map<String, Object> getMainSerializationData(){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("isInterface",true);
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        return data;
    }

    public Map<String, Object> getFactoryData(String fileName,List<NormalClassRepresentation> classes){
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",fileName);
        data.put("serializationClasses",classes);
        data.put("package",this.packageName);
        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        data.put("serializationPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("entityPackage", this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        return data;
    }
}
