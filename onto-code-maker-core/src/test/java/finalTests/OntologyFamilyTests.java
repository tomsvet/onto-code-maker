package finalTests;

import ontology.tool.OntoCodeMaker;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OntologyFamilyTests {
    public static Model model = new TreeModel();
    public String inputDir = "src/test/resources/inputs/";
    public String outputDir = "src/test/java/generatedOutputs/";

    public void generateFromRDMXMLToJava(String[] inputFiles,String testName){
        OntoCodeMaker maker = new OntoCodeMaker.Builder(inputFiles)
                .format(RDFFormat.RDFXML.getName())
                .language("java")
                .outputDir(outputDir + testName)
                .packageName("generatedOutputs." + testName)
                .build();
        maker.generateCodeFromOntology();
    }

    @BeforeEach
    void setUp() throws FileNotFoundException {

    }

    @Test
    @Order(1)
    @DisplayName("Generate from family1.owl.")
    public void generateFamilyOntology() throws Exception {
        String[] inputFiles = { inputDir + "collections.owl"};
        generateFromRDMXMLToJava(inputFiles,"collections");
    }

    @Test
    @Order(2)
    @DisplayName("Check generated files from family1.owl.")
    public void checkFamilyOntologyFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Cat","Child","Dog","Person","Human","Men","Woman");
        List<String> listOfInterfaces = Arrays.asList("PersonHumanInt","OntoEntity");
        File folderEntities = new File(outputDir+ "collections/entities/");
        File folderSerialization = new File(outputDir+ "collections/serialization/");

        File[] entityFiles = folderEntities.listFiles();
        assertEquals(listOfEntityClasses.size()+listOfInterfaces.size(), entityFiles.length, "Different number of files as expected.");
        for(File file : entityFiles) {
            assertTrue(listOfEntityClasses.contains(file.getName().replace(".java","")),"File"+file.getName()+" is not expecting.");
        }
        File[] serializationFiles = folderSerialization.listFiles();
        assertEquals(listOfEntityClasses.size(), serializationFiles.length, "Different number of files as expected in serialization file.");

    }

    @Test
    @Order(3)
    @DisplayName("Generate from collections.owl.")
    void generateCollectionsOntology() throws Exception {

    }
}
