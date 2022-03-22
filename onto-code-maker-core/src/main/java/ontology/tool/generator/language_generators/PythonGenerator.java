package ontology.tool.generator.language_generators;

import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.VocabularyConstant;
import ontology.tool.generator.representations.AbstractClassRepresentation;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.NormalClassRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import org.eclipse.rdf4j.model.vocabulary.XSD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PythonGenerator extends OntologyGenerator {

    public static final String GENERATOR_LANGUAGE_PYTHON = "python";

    static{
        VOCABULARY_TEMPLATE_NAME = "/PythonTemplates/vocabularyTemplatePython.ftl";
        CLASS_TEMPLATE_NAME = "/PythonTemplates/entitiesTemplatePython.ftl";
        SERIALIZATION_TEMPLATE_NAME = "/PythonTemplates/serializationTemplatePython.ftl";
        FACTORY_TEMPLATE_NAME = "/PythonTemplates/factoryTemplatePython.ftl";
        FILE_EXTENSION = ".py";

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
        //dataTypes.put(XSD.UNSIGNED_INT,"long");
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

    @Override
    public List<VocabularyConstant> createVocabularyConstants() {
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

            //properties constants
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

    @Override
    public Map<String, Object> getEntityData(NormalClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName());
        data.put("classRep",classRep);
        data.put("isClass",true);
        data.put("isMainClass",false);
        data.put("isExtended",true);
        if( classRep.getEquivalentClass() != null){
            data.put("extendClass", classRep.getEquivalentClass().getName() + ENTITY_INTERFACE_SUFFIX);
        }else {

            if (classRep.isHasInterface()) {
                data.put("extendClass", classRep.getName() + ENTITY_INTERFACE_SUFFIX);
            } else {
                if (classRep.hasOneSuperClass()) {
                    data.put("extendClass", classRep.getSuperClasses().get(0).getName());
                } else if (!classRep.hasSuperClass()) {
                    data.put("extendClass", CLASS_ENTITY_FILE_NAME);
                }
            }
        }

        data.put("vocabularyFileName",VOCABULARY_FILE_NAME);
        //data.put("properties",new ArrayList<>()); //classRep.
        data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getInterfaceEntityData(ClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",classRep.getName() + ENTITY_INTERFACE_SUFFIX);
        data.put("isClass",false);
        data.put("isMainClass",false);
        data.put("isExtended",true);
        if (classRep.hasSuperClass()) {
            data.put("classRep",classRep);
        } else {
            data.put("extendClass",CLASS_ENTITY_FILE_NAME);
        }
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getEquivalentClassEntityData(String interfaceName, ClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("className",interfaceName + ENTITY_INTERFACE_SUFFIX);
        data.put("isClass",false);
        data.put("isMainClass",false);
        data.put("isExtended",true);
        if (classRep.hasSuperClass()) {
            data.put("classRep",classRep);
        } else {
            data.put("extendClass",CLASS_ENTITY_FILE_NAME);
        }
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getAbstractClassEntityData(AbstractClassRepresentation classRep) {
        return null;
    }

    @Override
    public Map<String, Object> getMainEntityData() {
        Map<String, Object> data = new HashMap<>();
        data.put("className",CLASS_ENTITY_FILE_NAME);
        data.put("isMainClass",true);
        data.put("isClass",false);
        data.put("isExtended",false);
        //data.put("package",this.getPackageName() + (this.packageName.isEmpty() ? "":".") + DIR_NAME_ENTITIES);
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getSerializationData(NormalClassRepresentation classRep) {
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX);
        data.put("classRep",classRep);
        data.put("isSerializationModel",false);
        data.put("serializationModelName",SERIALIZATION_MODEL_FILE_NAME);
        //data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getMainSerializationData() {
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",SERIALIZATION_MODEL_FILE_NAME);
        data.put("isSerializationModel",true);
        //data.put("package",this.packageName + (this.packageName.isEmpty() ? "":".") + DIR_NAME_SERIALIZATION);
        data.put("imports",new ArrayList<>());
        return data;
    }

    @Override
    public Map<String, Object> getFactoryData(String fileName, List<NormalClassRepresentation> classes) {
        Map<String, Object> data = new HashMap<>();
        data.put("classFileName",fileName);
        data.put("serializationClasses",classes);
        data.put("package",this.packageName);
        data.put("entityClassName",CLASS_ENTITY_FILE_NAME);
        data.put("imports",new ArrayList<>());
        return data;
    }
}
