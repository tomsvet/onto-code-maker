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
    MODEL,
    FACTORY
}

abstract public class OntologyGenerator {

    private static final Logger logger = LogManager.getLogger(OntologyGenerator.class);

    //Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
    //private String templatePath = "/com/github/ansell/rdf4j/schemagenerator/javaStaticClassRDF4J.ftl";
    public final String  DEFAULT_OUTPUT_DIR = "/out/output";
    protected static String VOCABULARY_TEMPLATE_NAME;
    protected static String CLASS_TEMPLATE_NAME;
    protected static String MODEL_TEMPLATE_NAME;
    protected static String FACTORY_TEMPLATE_NAME;


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
            case MODEL:
                template = cfg.getTemplate(MODEL_TEMPLATE_NAME);
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

    public void generateCode(){
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

        generateSerializationClasses();

        generateFactory();


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

            String filepath = DEFAULT_OUTPUT_DIR + "/" + generatedClass.getName() + ".java";

            try (Writer fileWriter = new FileWriter(new File(filepath))) {
                templateFile.process(data, fileWriter);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateVocabulary() throws IOException {
        Writer fileWriter = new FileWriter(new File("out/"+ getVocabularyFileName()));

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

    public void generateSerializationClasses(){

    }

    public void generateFactory(){

    }

    abstract public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties);

    abstract public  List<VocabularyConstant> createVocabularyConstants();

    abstract public String getVocabularyFileName();

}
