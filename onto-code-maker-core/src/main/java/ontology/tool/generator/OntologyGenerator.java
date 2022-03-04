package ontology.tool.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.EntityRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import ontology.tool.parser.OntologyParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;

import java.io.*;
import java.util.*;

enum TEMPLATE_TYPE {
    CLASS,
    VOCABULARY,
    SERIALIZATION,
    FACTORY
}

abstract public class OntologyGenerator {

    private static final Logger logger = LogManager.getLogger(OntologyGenerator.class);

    public final String  DEFAULT_OUTPUT_DIR = System.getProperty("user.dir");
    public final String DIR_PATH = "out/";

    protected static String VOCABULARY_TEMPLATE_NAME;
    protected static String CLASS_TEMPLATE_NAME;
    protected static String SERIALIZATION_TEMPLATE_NAME;
    protected static String FACTORY_TEMPLATE_NAME;
    protected static String FILE_EXTENSION;


    protected static String VOCABULARY_FILE_NAME = "Vocabulary";
    protected static String SERIALIZATION_MODEL_FILE_NAME = "SerializationModel";
    public static String SERIALIZATION_FILE_NAME_SUFFIX = "Serialization";
    protected static String FACTORY_FILE_NAME_SUFFIX = "Factory" ;
    protected static String DEFAULT_FACTORY_FILE_NAME = "Ontology" + FACTORY_FILE_NAME_SUFFIX;
    protected static String CLASS_ENTITY_FILE_NAME = "OntoEntity";
    //protected static String CLASS_ENTITY_FILE_NAME = CLASS_ENTITY_NAME ;
    protected static String ENTITY_INTERFACE_SUFFIX = "Int";
    public static HashMap<IRI, String> dataTypes = new HashMap<>();

    Configuration cfg;
    
    protected EntityRepresentation ontology = null;
    protected List<ClassRepresentation> classes = new ArrayList<>();

    protected String outputDir = DEFAULT_OUTPUT_DIR;
    protected String packageName = "";



    public OntologyGenerator(){
        //Properties properties = System.getProperties();
        // Java 8
        //properties.forEach((k, v) -> System.out.println(k + ":" + v));
        //String a = System.getProperty("freemarker.version");
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        //cfg.setClassForTemplateLoading(FreeMarkerConsoleEx.class, "/");
        cfg.setClassForTemplateLoading(OntologyGenerator.class, "/");
        cfg.setDefaultEncoding("UTF-8");
    }

    public void setOutputDir(String outputDir){
        this.outputDir = outputDir;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public String getOutputDir(){
        return outputDir;
    }

    public String getPackageName(){
        return packageName;
    }

    public void addClasses( List<ClassRepresentation> classes){
        this.classes.addAll(classes);
    }

    public void setOntology(EntityRepresentation ontology){
        this.ontology = ontology;
    }

    public Template getTemplate(TEMPLATE_TYPE type) throws IOException {
        Template template = null;
        switch (type){
            case CLASS:
                template = cfg.getTemplate(CLASS_TEMPLATE_NAME);
                break;
            case VOCABULARY:
                template = cfg.getTemplate(VOCABULARY_TEMPLATE_NAME);
                break;
            case SERIALIZATION:
                template = cfg.getTemplate(SERIALIZATION_TEMPLATE_NAME);
                break;
            case FACTORY:
                template = cfg.getTemplate(FACTORY_TEMPLATE_NAME);
                break;
        }
        return template;
    }

    private void createOutputDir(){
        File outputDir = new File(DEFAULT_OUTPUT_DIR);
        if (!outputDir.exists()){
            if( ! outputDir.mkdirs()){
                logger.error("Cannot create destination directory.");
            }
        }
    }

    public void generateCode() {
        createOutputDir();

        generateStringVersionOfTypes();

        try {
            generateVocabulary();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            generateClasses();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            generateSerializationClasses();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            generateFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateStringVersionOfTypes(){
        for(ClassRepresentation classRep:classes){
            for(PropertyRepresentation property :classRep.getProperties()){
                if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.DATATYPE)){
                    if(property.getRangeIRI() != null){
                        String dataType = dataTypes.get(property.getRangeIRI());
                        if(dataType != null && !dataType.isEmpty()){
                            property.setRangeDatatype(dataType);
                        }else{
                            property.setRangeDatatype("String");
                        }
                    }
                }else if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.OBJECT)){
                    if(property.getRangeClass() != null && property.getRangeIRI() != null){
                        if(property.getRangeClass().isHasInterface()){
                            property.setRangeDatatype(property.getRangeIRI().getLocalName() + ENTITY_INTERFACE_SUFFIX);
                        }else{
                            property.setRangeDatatype(property.getRangeIRI().getLocalName());
                        }

                    }
                }

            }
        }
    }


    public void generateClasses() throws IOException {

        Template templateFile = getTemplate(TEMPLATE_TYPE.CLASS);
        if(templateFile == null){
            return;
        }

        String interfaceClassPath = DIR_PATH + CLASS_ENTITY_FILE_NAME +  FILE_EXTENSION;
        Map<String, Object> data = getInterfaceEntityData();
        try (Writer fileWriter = new FileWriter(new File(interfaceClassPath))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        for(ClassRepresentation generatedClass: classes){

            if(generatedClass.getEquivalentClasses().size() >0 && generatedClass.getEquivalentInterfaceName()== null){
                String equivalentInterfaceName = generatedClass.getName();
                for(ClassRepresentation eqClassRep: generatedClass.getEquivalentClasses()){
                    equivalentInterfaceName = equivalentInterfaceName.concat(eqClassRep.getName());
                    for(ClassRepresentation superClass: eqClassRep.getSuperClasses()){
                        if(!generatedClass.getSuperClasses().contains(superClass)){
                            generatedClass.addSuperClasses(superClass);
                        }
                    }
                }

                generatedClass.setEquivalentInterfaceName(equivalentInterfaceName);
                for(ClassRepresentation eqClassRep: generatedClass.getEquivalentClasses()){
                    eqClassRep.setEquivalentInterfaceName(equivalentInterfaceName);
                }

                String equivalentInterfaceClassPath = DIR_PATH + equivalentInterfaceName + ENTITY_INTERFACE_SUFFIX +  FILE_EXTENSION;

                Map<String, Object> dataInt = getEquivalentInterfaceEntityData(equivalentInterfaceName,generatedClass);
                try (Writer fileWriter = new FileWriter(new File(equivalentInterfaceClassPath))) {
                    templateFile.process(dataInt, fileWriter);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }

            }


            if(generatedClass.hasSubClass()){
                Optional<ClassRepresentation> pom = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getSuperClasses().size() > 1).findFirst();
                generatedClass.setHasInterface(pom.isPresent());
            }

            if(generatedClass.isHasInterface()){
               String entitiesInterfaceClassPath = DIR_PATH + generatedClass.getName() + ENTITY_INTERFACE_SUFFIX +  FILE_EXTENSION;
                Map<String, Object> dataInt = getInterfaceEntityData(generatedClass);
                try (Writer fileWriter = new FileWriter(new File(entitiesInterfaceClassPath))) {
                    templateFile.process(dataInt, fileWriter);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }

            Map<String, Object> classData = getEntityData(generatedClass);

            String filepath = DIR_PATH + generatedClass.getName() + FILE_EXTENSION;
            try (Writer fileWriter = new FileWriter(new File(filepath))) {
                templateFile.process(classData, fileWriter);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateVocabulary() throws IOException {
        Writer fileWriter = new FileWriter(new File(DIR_PATH + VOCABULARY_FILE_NAME + FILE_EXTENSION));

        Template templateFile = getTemplate(TEMPLATE_TYPE.VOCABULARY);

        if(templateFile == null){
            return;
        }
        try {
            List<VocabularyConstant> properties = createVocabularyConstants();
            Map<String, Object> data = getVocabularyData(properties);
            templateFile.process(data, fileWriter);

        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }

    public void generateSerializationClasses() throws IOException {
        Template templateFile = getTemplate(TEMPLATE_TYPE.SERIALIZATION);
        if (templateFile == null) {
            logger.error("Problem loading serialization template.");
            return ;
        }


        //create serialization interface
        Map<String, Object> data = getInterfaceSerializationData();
        try (Writer fileWriter = new FileWriter(new File(DIR_PATH + SERIALIZATION_MODEL_FILE_NAME + FILE_EXTENSION))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        // create serialization classes

        for (ClassRepresentation classRep : classes){
            Map<String, Object> classData = getSerializationData(classRep);
            try (Writer fileWriter = new FileWriter(new File(DIR_PATH +  classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX + FILE_EXTENSION))) {
                templateFile.process(classData, fileWriter);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFactory() throws IOException {
        Template templateFile = getTemplate(TEMPLATE_TYPE.FACTORY);
        if (templateFile == null) {
            logger.error("Problem loading factory template.");
            return ;
        }

        String fileName;
        if(ontology != null){
            fileName = ontology.getName() + FACTORY_FILE_NAME_SUFFIX;
        }else{
            fileName = DEFAULT_FACTORY_FILE_NAME ;
        }

        Map<String, Object> data = getFactoryData(fileName,classes);
        try (Writer fileWriter = new FileWriter(new File(DIR_PATH + fileName + FILE_EXTENSION))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }

    abstract public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties);

    abstract public Map<String, Object> getEntityData(ClassRepresentation classRep);

    abstract public Map<String, Object> getInterfaceEntityData(ClassRepresentation classRep);

    abstract public Map<String, Object> getInterfaceEntityData();

    abstract public Map<String, Object> getSerializationData(ClassRepresentation classRep);

    abstract public Map<String, Object> getInterfaceSerializationData();

    abstract public Map<String, Object> getFactoryData(String fileName,List<ClassRepresentation> classes);

    abstract public  List<VocabularyConstant> createVocabularyConstants();

    abstract public Map<String, Object> getEquivalentInterfaceEntityData(String interfaceName,ClassRepresentation classRep);


}
