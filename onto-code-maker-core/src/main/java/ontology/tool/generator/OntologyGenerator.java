package ontology.tool.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.EntityRepresentation;
import ontology.tool.parser.OntologyParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public final String  DEFAULT_OUTPUT_DIR = "/out/output";
    public final String DIR_PATH = "out/";

    protected static String VOCABULARY_TEMPLATE_NAME;
    protected static String CLASS_TEMPLATE_NAME;
    protected static String SERIALIZATION_TEMPLATE_NAME;
    protected static String FACTORY_TEMPLATE_NAME;
    protected static String FILE_EXTENSION;


    protected static String VOCABULARY_FILE_NAME = "Vocabulary" + FILE_EXTENSION;
    protected static String SERIALIZATION_MODEL_FILE_NAME = "SerializationModel" + FILE_EXTENSION;
    protected static String SERIALIZATION_FILE_NAME_SUFFIX = "Serialization" + FILE_EXTENSION;
    protected static String FACTORY_FILE_NAME_SUFFIX = "Factory" + FILE_EXTENSION;
    protected static String DEFAULT_FACTORY_FILE_NAME = "Ontology" + FACTORY_FILE_NAME_SUFFIX;
    protected static String CLASS_ENTITY_FILE_NAME = "OntoEntity" + FILE_EXTENSION;

    Configuration cfg;
    
    protected EntityRepresentation ontology = null;
    protected List<ClassRepresentation> classes = new ArrayList<>();

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


    public void generateClasses() throws IOException {

        Template templateFile = getTemplate(TEMPLATE_TYPE.CLASS);
        if(templateFile == null){
            return;
        }
        //Writer out = new OutputStreamWriter(System.out);

        for(ClassRepresentation generatedClass: classes){
            Map<String, Object> data = new HashMap<>();
            data.put("generatedClass",generatedClass);

            String filepath = DIR_PATH + generatedClass.getName() + FILE_EXTENSION;

            try (Writer fileWriter = new FileWriter(new File(filepath))) {
                templateFile.process(data, fileWriter);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateVocabulary() throws IOException {
        Writer fileWriter = new FileWriter(new File(DIR_PATH + VOCABULARY_FILE_NAME));

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
            logger.error("Problem loading template.");
            return ;
        }


        //create serialization interface
        Writer fileWriter = new FileWriter(new File(DIR_PATH + SERIALIZATION_MODEL_FILE_NAME));


        // create serialization classes

        for (ClassRepresentation classRep : classes){
            fileWriter = new FileWriter(new File(DIR_PATH +  classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX));


        }



    }

    public void generateFactory() throws IOException {
        String fileName;
        if(ontology != null){
            fileName = ontology.getName() + FACTORY_FILE_NAME_SUFFIX;
        }else{
            fileName = DEFAULT_FACTORY_FILE_NAME;
        }

        Writer fileWriter = new FileWriter(new File(fileName));



    }

    abstract public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties);

    abstract public  List<VocabularyConstant> createVocabularyConstants();

    abstract public String getVocabularyFileName();

}
