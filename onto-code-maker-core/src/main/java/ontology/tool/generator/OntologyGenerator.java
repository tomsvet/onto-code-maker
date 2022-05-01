package ontology.tool.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ontology.tool.generator.representations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


abstract public class OntologyGenerator {

    enum TEMPLATE_TYPE {
        CLASS,
        VOCABULARY,
        SERIALIZATION,
        FACTORY
    }

    private static final Logger logger = LogManager.getLogger(OntologyGenerator.class);

    public final String  DEFAULT_OUTPUT_DIR = System.getProperty("user.dir");
    public final String DIR_NAME_ENTITIES = "entities";
    public final String DIR_NAME_SERIALIZATION = "serialization";

    protected static String VOCABULARY_TEMPLATE_NAME;
    protected static String CLASS_TEMPLATE_NAME;
    protected static String SERIALIZATION_TEMPLATE_NAME;
    protected static String FACTORY_TEMPLATE_NAME;
    protected static String FILE_EXTENSION;


    protected static String VOCABULARY_FILE_NAME = "Vocabulary";
    protected static String SERIALIZATION_MODEL_FILE_NAME = "SerializationModel";
    public static String SERIALIZATION_FILE_NAME_SUFFIX = "Serialization";
    protected static String CLASS_ENTITY_FILE_NAME = "OntoEntity";
    //protected static String CLASS_ENTITY_FILE_NAME = CLASS_ENTITY_NAME ;
    public static String ENTITY_INTERFACE_SUFFIX = "Int";
    public static String ENTITY_ABSTRACTCLASS_SUFFIX = "";
    public static String ENTITY_EQUIVALENCE_PREFIX = "Equivalence";
    public static HashMap<IRI, String> dataTypes = new HashMap<>();

    protected static String SERIALIZATION_FACTORY_FILE_NAME = "SerialializationFactory" ;
    protected static String FACTORY_FILE_NAME_SUFFIX = "Factory" ;
    protected static String DEFAULT_FACTORY_FILE_NAME = "Ontology" + FACTORY_FILE_NAME_SUFFIX;
    Configuration cfg;
    
    protected List<OntologyRepresentation> ontologies = new ArrayList<>();
    protected List<ClassRepresentation> classes = new ArrayList<>();
    protected List<PropertyRepresentation> properties = new ArrayList<>();

    protected String outputDir = DEFAULT_OUTPUT_DIR + "/";
    protected String packageName = "";

    private List<String> primitiveDataTypes = Arrays.asList("int","long","short","boolean","float");


    public OntologyGenerator(){
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(OntologyGenerator.class, "/");
        cfg.setDefaultEncoding("UTF-8");
    }

    public void setOutputDir(String pathName){
        File outputDir = new File(pathName);
        if (!outputDir.exists()) {
            this.outputDir += pathName;
        }else{
            this.outputDir = pathName;
        }

        if (!this.outputDir.endsWith("/")) {
            this.outputDir += "/";
        }
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

    /*public void addClasses( List<ClassRepresentation> classes,List<AbstractClassRepresentation> abstractClasses){
        this.allClasses.addAll(classes);
        this.allClasses.addAll(abstractClasses);
        this.entityClasses.addAll(classes);
    }*/

    public List<NormalClassRepresentation> getNormalClassReps(){
        return classes.parallelStream().filter(classRep -> classRep instanceof NormalClassRepresentation).map(foundClass -> (NormalClassRepresentation)foundClass).collect(Collectors.toList());
    }

    public List<AbstractClassRepresentation> getAbstractClassReps(){
        return classes.parallelStream().filter(classRep -> classRep instanceof AbstractClassRepresentation).map(foundClass -> (AbstractClassRepresentation)foundClass).collect(Collectors.toList());
    }

    public List<AbstractClassRepresentation> getAbstractClassWithoutInterface(){
        return classes.parallelStream().filter(classRep -> classRep instanceof AbstractClassRepresentation && !classRep.isHasInterface()).map(foundClass -> (AbstractClassRepresentation)foundClass).collect(Collectors.toList());
    }

    public List<AbstractClassRepresentation> getAbstractClassWithoutInterfaceFromSuperClass(ClassRepresentation classRep){
        return classRep.getSuperClasses().parallelStream().filter(superClass -> superClass instanceof AbstractClassRepresentation && !superClass.isHasInterface()).map(foundClass -> (AbstractClassRepresentation)foundClass).collect(Collectors.toList());
    }

    public void addClasses( Collection<ClassRepresentation> classes){
        this.classes.addAll(classes);
    }

    public void addProperties( Collection<PropertyRepresentation> properties){
        this.properties.addAll(properties);
    }


   /* public void addEntityClasses( List<ClassRepresentation> classes){
        this.entityClasses.addAll(classes);
    }*/

    /*public void setOntology(OntologyRepresentation ontology){
        this.ontology = ontology;
    }*/

    public void setOntologies(List<OntologyRepresentation> ontologies){
        this.ontologies = ontologies;
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

    private void createDir(String pathName){
        File outputDir = new File(pathName);
        if (!outputDir.exists()){
            if( ! outputDir.mkdirs()){
                logger.error("Cannot create destination directory. " + pathName);
            }
        }
    }

    public void generateCode() {
        createDir(this.outputDir);

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
            generateFactories();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateStringVersionOfTypes(){
        for(ClassRepresentation classRep:classes){
            for(PropertyRepresentation property :classRep.getProperties()){
                if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.DATATYPE)){
                    if(property.getRangeResource() != null){
                        String dataType = dataTypes.get(property.getRangeResource());
                        if(dataType != null && !dataType.isEmpty()){
                            property.setRangeDatatype(dataType);
                        }else{
                            property.setRangeDatatype("String");
                        }
                    }else{
                        property.setRangeDatatype("String");
                    }
                }else if(property.getType().equals(PropertyRepresentation.PROPERTY_TYPE.OBJECT)){
                    if(property.getRangeClass() != null && property.getRangeResource() != null){
                        //String name = property.getRangeResource().isIRI()? ((IRI)property.getRangeResource()).getLocalName():((BNode)property.getRangeResource()).getID();
                        if(property.getRangeClass().isHasInterface()){
                            property.setRangeDatatype(property.getRangeClass().getName() + ENTITY_INTERFACE_SUFFIX);
                        }else{
                            property.setRangeDatatype(property.getRangeClass().getName());
                        }

                    }
                }

            }
        }
    }

    public void setUpInterfaces(){
        for(ClassRepresentation classRep:this.classes) {
            findOutInterfaceClassValueOfClass(classRep);
        }

    }


    public void generateMainEntityClass(Template templateFile,String entitiesOutputFile){
        String interfaceClassPath = entitiesOutputFile + CLASS_ENTITY_FILE_NAME +  FILE_EXTENSION;
        //generate main OntoEntity class/interface
        Map<String, Object> data = getMainEntityData();
        try (Writer fileWriter = new FileWriter(new File(interfaceClassPath))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void generateEquivalentClass(Template templateFile, String entitiesOutputFile, ClassRepresentation generatedClass){
        //generatedClass.getEquivalentClass().setClassNameWithConcatEquivalentClasses();
        String equivalentClassName = generatedClass.getEquivalentClass().getName();
        String equivalentClassFileName = entitiesOutputFile + equivalentClassName +  FILE_EXTENSION;

        Map<String, Object> dataInt = getEquivalentClassEntityData(equivalentClassName,generatedClass);
        try (Writer fileWriter = new FileWriter(new File(equivalentClassFileName))) {
            templateFile.process(dataInt, fileWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void generateInterfaceClass(Template templateFile, String entitiesOutputFile, ClassRepresentation generatedClass){
        String entitiesInterfaceClassPath = entitiesOutputFile + generatedClass.getName() + ENTITY_INTERFACE_SUFFIX +  FILE_EXTENSION;
        Map<String, Object> dataInt = getInterfaceEntityData(generatedClass);
        try (Writer fileWriter = new FileWriter(new File(entitiesInterfaceClassPath))) {
            templateFile.process(dataInt, fileWriter);
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void findOutInterfaceClassValueOfClass(ClassRepresentation generatedClass){
        if(!(generatedClass instanceof AbstractClassRepresentation && generatedClass.isUnionOf())) {
            if (generatedClass.hasSubClass() && !generatedClass.isHasInterface()) {
                //If SubClasses Have More Super Classes
                // this is needed when subclasses of generated Class  have more than one superclass because it need extends more classes (in java only interfaces)
                List<ClassRepresentation> resultList = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getSuperClasses().size() > 1).collect(Collectors.toList());
                boolean result1 = false;
                if (!resultList.isEmpty()) {
                    result1 = true;
                    generatedClass.getSuperClasses().parallelStream().forEach(superClass-> superClass.setHasInterface(true));
                    /*List<ClassRepresentation> res = resultList.parallelStream().filter(classRep -> classRep.getSuperClasses().size() > 2 || classRep.getSuperClasses().get(0).getClassType() == classRep.getSuperClasses().get(1).getClassType()).collect(Collectors.toList());
                    if (res.size() == resultList.size()) {
                        result1 = true;
                    }*/
                }

                // if subclass is interface
                if(!result1){
                    List<ClassRepresentation> resultList2 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.isHasInterface()).collect(Collectors.toList());
                    result1 = !resultList2.isEmpty();
                }

                // if subclasses have equivalent class
                Optional<ClassRepresentation> result2 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getEquivalentClass() != null).findFirst();

                //Optional<ClassRepresentation> result3 = generatedClass.getSubClasses().parallelStream().filter(classRep -> classRep.getSuperClasses().size() == 2 && classRep.getSuperClasses().get(0).getClassType() != classRep.getSuperClasses().get(1).getClassType()).findFirst();

                generatedClass.setHasInterface(result1 || result2.isPresent());
            }
        }
    }


    public void generateClasses() throws IOException {

        setUpInterfaces();

        Template templateFile = getTemplate(TEMPLATE_TYPE.CLASS);
        if(templateFile == null){
            return;
        }

        //create serializationFile if is not exist
        String entitiesOutputFile = this.outputDir + DIR_NAME_ENTITIES +"/";
        createDir(entitiesOutputFile);

        generateMainEntityClass(templateFile,entitiesOutputFile);


        for(ClassRepresentation generatedClass: classes){

            // generate equivalent class (f.e in java it is interface)
            if(generatedClass.getEquivalentClass() != null ){
                generateEquivalentClass(templateFile,entitiesOutputFile,generatedClass);
            }

            //findOutInterfaceClassValueOfClass(generatedClass);

            if(generatedClass.isHasInterface()){
                generateInterfaceClass(templateFile,entitiesOutputFile,generatedClass);
            }

            String filepath;
            Map<String, Object> classData;
            if(generatedClass instanceof AbstractClassRepresentation){
                AbstractClassRepresentation abstractGeneratedClass =(AbstractClassRepresentation) generatedClass;
                filepath = entitiesOutputFile + abstractGeneratedClass.getName() + FILE_EXTENSION;
                classData = getAbstractClassEntityData(abstractGeneratedClass);
            }else{
                filepath = entitiesOutputFile + generatedClass.getName() + FILE_EXTENSION;
                classData = getEntityData((NormalClassRepresentation)generatedClass);
            }

            try (Writer fileWriter = new FileWriter(new File(filepath))) {
                templateFile.process(classData, fileWriter);
            } catch (TemplateException e) {
                e.printStackTrace();
            }

        }
    }

    public void generateVocabulary() throws IOException {
        Writer fileWriter = new FileWriter(new File(this.outputDir + VOCABULARY_FILE_NAME + FILE_EXTENSION));

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

        //create serializationFile if is not exist
        String serializationOutputFile = this.outputDir + DIR_NAME_SERIALIZATION + "/";
        createDir(serializationOutputFile);

        //create serialization interface
        Map<String, Object> data = getMainSerializationData();
        try (Writer fileWriter = new FileWriter(new File(serializationOutputFile + SERIALIZATION_MODEL_FILE_NAME + FILE_EXTENSION))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        // create serialization classes
        for (ClassRepresentation classRep : classes){
            if(classRep.getClassType().equals(ClassRepresentation.CLASS_TYPE.NORMAL)) {
                Map<String, Object> classData = getSerializationData((NormalClassRepresentation)classRep);
                try (Writer fileWriter = new FileWriter(new File(serializationOutputFile + classRep.getName() + SERIALIZATION_FILE_NAME_SUFFIX + FILE_EXTENSION))) {
                    templateFile.process(classData, fileWriter);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void generateFactories() throws IOException {
        Template templateFile = getTemplate(TEMPLATE_TYPE.FACTORY);
        if (templateFile == null) {
            logger.error("Problem loading factory template.");
            return ;
        }

        Map<String, Object> dataSer = getSerializationFactoryData(getNormalClassReps());
        try (Writer fileWriter = new FileWriter(new File(this.outputDir + SERIALIZATION_FACTORY_FILE_NAME + FILE_EXTENSION))) {
            templateFile.process(dataSer, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        String fileName;
        if(ontologies.size() == 1 && !ontologies.get(0).getName().isEmpty()){
            fileName = ontologies.get(0).getName() + FACTORY_FILE_NAME_SUFFIX;
        }else if(ontologies.size() > 1) {
            fileName = "Ontologies" + FACTORY_FILE_NAME_SUFFIX;
        }else{
            fileName = DEFAULT_FACTORY_FILE_NAME ;
        }

        Map<String, Object> data = getOntologyFactoryData(ontologies,fileName,getNormalClassReps());
        try (Writer fileWriter = new FileWriter(new File(this.outputDir + fileName + FILE_EXTENSION))) {
            templateFile.process(data, fileWriter);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

    }

    abstract public Map<String, Object> getVocabularyData(List<VocabularyConstant> properties);

    abstract public Map<String, Object> getEntityData(NormalClassRepresentation classRep);

    abstract public Map<String, Object> getInterfaceEntityData(ClassRepresentation classRep);

    abstract public Map<String, Object> getMainEntityData();

    abstract public Map<String, Object> getSerializationData(NormalClassRepresentation classRep);

    abstract public Map<String, Object> getMainSerializationData();


    abstract public  List<VocabularyConstant> createVocabularyConstants();

    abstract public Map<String, Object> getEquivalentClassEntityData(String interfaceName, ClassRepresentation classRep);

    abstract public Map<String, Object> getAbstractClassEntityData(AbstractClassRepresentation classRep);

    abstract public Map<String, Object> getOntologyFactoryData(List<OntologyRepresentation> ontologies,String fileName,List<NormalClassRepresentation> classes);

    abstract public Map<String, Object> getSerializationFactoryData(List<NormalClassRepresentation> classes);
}
