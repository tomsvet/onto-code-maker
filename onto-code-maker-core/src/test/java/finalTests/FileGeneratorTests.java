package finalTests;

import ontology.tool.OntoCodeMaker;
import ontology.tool.parser.OntologyParser;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileGeneratorTests {
    public static Model model = new TreeModel();
    public String inputDir = "src/test/resources/inputs/";
    public static String outputDir = "src/test/resources/generatedOutputs/";
    public String simpleClassesDir = inputDir + "classes/";

    @BeforeAll
    public static void setUp() throws IOException {
        File folderOutput = new File(outputDir);
        FileUtils.deleteDirectory(folderOutput);
    }

    public void generateFromRDMXMLToJava(String[] inputFiles,String testName) throws Exception {
        OntoCodeMaker maker = new OntoCodeMaker.Builder(inputFiles)
                .format(RDFFormat.RDFXML.getName())
                .language("java")
                .outputDir(outputDir + testName)
                .packageName("generatedOutputs." + testName)
                .build();
        maker.generateCodeFromOntology();
    }

    public void checkGeneratedFiles(File folderEntities,File folderSerialization,List<String> listOfEntityClasses,List<String> listOfInterfaces){
        File[] entityFiles = folderEntities.listFiles();
        assertNotNull(entityFiles,"Dir is not created.");
        assertEquals(listOfEntityClasses.size() + listOfInterfaces.size(), entityFiles.length, "Different number of files as expected.");
        for(File file : entityFiles) {
            assertTrue(listOfEntityClasses.contains(file.getName().replace(".java","")) || listOfInterfaces.contains(file.getName().replace(".java","")) ,"File "+file.getName()+" is not expecting.");
        }
        File[] serializationFiles = folderSerialization.listFiles();
        assertEquals(listOfEntityClasses.size() +1, serializationFiles.length, "Different number of files as expected in serialization file.");

    }


    @Test
    @Order(1)
    @DisplayName("Generate one class ontology.")
    public void generateOneClassOWL(){
        String[] inputFiles = { simpleClassesDir + "simpleOWLCLass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"oneClassOWL"));
    }

    @Test
    @Order(2)
    @DisplayName("Check generated files from family1.owl.")
    public void checkOneClassOWLFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Human");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "oneClassOWL/entities/");
        File folderSerialization = new File(outputDir+ "oneClassOWL/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(3)
    @DisplayName("Generate one class ontology.")
    public void generateOneClassRDFS() throws Exception {
        String[] inputFiles = { simpleClassesDir + "simpleRDFSCLass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"simpleRDFSCLass"));
    }

    @Test
    @Order(4)
    @DisplayName("Check generated files from family1.owl.")
    public void checkOneClassRDFSFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Human");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "oneClassOWL/entities/");
        File folderSerialization = new File(outputDir+ "oneClassOWL/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(5)
    @DisplayName("Generate more classes.")
    public void generateMoreClasses() throws Exception {
        String[] inputFiles = { simpleClassesDir + "moreClasses.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"moreClasses"));
    }

    @Test
    @Order(6)
    @DisplayName("Check generated files from moreClasses.owl.")
    public void checkMoreClassesFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Human","Animal","Thing");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "moreClasses/entities/");
        File folderSerialization = new File(outputDir+ "moreClasses/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(7)
    @DisplayName("Generate equivalent class.")
    public void generateEquivalentClass(){
        String[] inputFiles = { simpleClassesDir + "equivalentClass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"equivalentClass"));
    }

    @Test
    @Order(8)
    @DisplayName("Check generated files from equivalentClass.owl.")
    public void checkEquivalentClassFiles() {
        List<String> listOfEntityClasses = Arrays.asList("Human","Person");
        List<String> listOfInterfaces = Arrays.asList("EquivalenceHumanPerson","OntoEntity");
        File folderEntities = new File(outputDir+ "equivalentClass/entities/");
        File folderSerialization = new File(outputDir+ "equivalentClass/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(9)
    @DisplayName("Generate subclassof.")
    public void generateSubClassOf(){
        String[] inputFiles = { simpleClassesDir + "subClassOf.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"subClassOf"));
    }

    @Test
    @Order(10)
    @DisplayName("Check generated files from subClassOf.owl.")
    public void checkSubClassOfFiles(){
        List<String> listOfEntityClasses = Arrays.asList("Human","Men");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "subClassOf/entities/");
        File folderSerialization = new File(outputDir+ "subClassOf/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(11)
    @DisplayName("Generate subclassof TwoClasses.")
    public void generateSubClassOfTwoClasses() throws Exception {
        String[] inputFiles = { simpleClassesDir + "subClassOfTwoClasses.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"subClassOfTwoClasses"));
    }

    @Test
    @Order(12)
    @DisplayName("Check generated files from subClassOfTwoClasses.owl.")
    public void checkSubClassOfTwoClassesFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Human","Men","Person");
        List<String> listOfInterfaces = Arrays.asList("HumanInt","PersonInt","OntoEntity");
        File folderEntities = new File(outputDir+ "subClassOfTwoClasses/entities/");
        File folderSerialization = new File(outputDir+ "subClassOfTwoClasses/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(13)
    @DisplayName("Generate MoreEquivalentClasses.")
    public void generateMoreEquivalentClasses() throws Exception {
        String[] inputFiles = { simpleClassesDir + "moreEquivalentClasses.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"moreEquivalentClasses"));
    }

    @Test
    @Order(14)
    @DisplayName("Check generated files from moreEquivalentClasses.owl.")
    public void checkMoreEquivalentClassesFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Human","Human2","Person","Person2");
        List<String> listOfInterfaces = Arrays.asList("EquivalenceHumanHuman2PersonPerson2","OntoEntity");
        File folderEntities = new File(outputDir+ "moreEquivalentClasses/entities/");
        File folderSerialization = new File(outputDir+ "moreEquivalentClasses/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(15)
    @Disabled // not supported
    @DisplayName("Generate restriction class.")
    public void generateRestrictionClass() {
        String[] inputFiles = { simpleClassesDir + "restrictionClass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"restrictionClass"));
    }

    @Test
    @Order(16)
    @Disabled // not supported
    @DisplayName("Check generated files from restrictionClass.owl.")
    public void checkRestrictionClassFiles() {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "restrictionClass/entities/");
        File folderSerialization = new File(outputDir+ "restrictionClass/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(15)
    @DisplayName("Generate equivalent restriction class.")
    public void generateEQRestrictionClass() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqRestrictionClass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqRestrictionClass"));
    }

    @Test
    @Order(16)
    @DisplayName("Check generated files from eqRestrictionClass.owl.")
    public void checkEQRestrictionClassFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity");
        File folderEntities = new File(outputDir+ "eqRestrictionClass/entities/");
        File folderSerialization = new File(outputDir+ "eqRestrictionClass/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(17)
    @DisplayName("Generate equivalent union of class.")
    public void generateEQUnionOfClass() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqUnionOfClass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqUnionOfClass"));
    }

    @Test
    @Order(18)
    @DisplayName("Check generated files from eqUnionOfClass.owl.")
    public void checkEQUnionOfClassFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","Father","Mother");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfFatherMother","EquivalenceParentUnionOfFatherMother");
        File folderEntities = new File(outputDir+ "eqUnionOfClass/entities/");
        File folderSerialization = new File(outputDir+ "eqUnionOfClass/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    private String EQ_INTERSECTIONOF = "eqIntersectionOfClass";
    @Test
    @Order(19)
    @DisplayName("Generate equivalent intersection of class.")
    public void generateEQIntersectionClass() throws Exception {
        String[] inputFiles = { simpleClassesDir + EQ_INTERSECTIONOF + ".owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,EQ_INTERSECTIONOF));
    }

    @Test
    @Order(20)
    @DisplayName("Check generated files from eqIntersectionOfClass.owl.")
    public void checkEQIntersectionOfClassFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","Father","Men");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","MenInt","ParentInt","IntersectionOfMenParent","EquivalenceFatherIntersectionOfMenParent");
        File folderEntities = new File(outputDir+ EQ_INTERSECTIONOF+ "/entities/");
        File folderSerialization = new File(outputDir+ EQ_INTERSECTIONOF +"/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    private String EQ_COMPLEMNETOF = "eqComplementOfClass";
    @Test
    @Order(21)
    @DisplayName("Generate equivalent complement of class.")
    public void generateEQComplementOfClass() throws Exception {
        String[] inputFiles = { simpleClassesDir + EQ_COMPLEMNETOF + ".owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,EQ_COMPLEMNETOF));
    }

    @Test
    @Order(22)
    @DisplayName("Check generated files from eqIntersectionOfClass.owl.")
    public void checkEQComplementOfClassFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","ChildlessPerson");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","ComplementOfParent","EquivalenceChildlessPersonComplementOfParent");
        File folderEntities = new File(outputDir+ EQ_COMPLEMNETOF+ "/entities/");
        File folderSerialization = new File(outputDir+ EQ_COMPLEMNETOF +"/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(23)
    @DisplayName("Generate equivalent union of restrictions.")
    public void generateEQUnionOfRestrictions() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqUnionOfRestrictions.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqUnionOfRestrictions"));
    }

    @Test
    @Order(24)
    @DisplayName("Check generated files from eqUnionOfRestrictions.owl.")
    public void checkEQUnionOfRestrictionsFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfRestrHasAgeRestrHasChild","EquivalenceParentUnionOfRestrHasAgeRestrHasChild");
        File folderEntities = new File(outputDir+ "eqUnionOfRestrictions/entities/");
        File folderSerialization = new File(outputDir+ "eqUnionOfRestrictions/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }


    @Test
    @Order(25)
    @DisplayName("Generate equivalent union of restriction and class.")
    public void generateEQUnionOfRestrictionAndClass() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqUnionOfRestrictionAndClass.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqUnionOfRestrictionAndClass"));
    }

    @Test
    @Order(26)
    @DisplayName("Check generated files from eqUnionOfRestrictionAndClass.owl.")
    public void checkEQUnionOfRestrictionANdCLassFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","ClassTest1");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfClassTest1RestrHasChild","EquivalenceParentUnionOfClassTest1RestrHasChild");
        File folderEntities = new File(outputDir+ "eqUnionOfRestrictionAndClass/entities/");
        File folderSerialization = new File(outputDir+ "eqUnionOfRestrictionAndClass/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(27)
    @DisplayName("Generate equivalent union of restriction and class.")
    public void generateEQUnionOfClassAndUnion() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqUnionOfClassAndUnion.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqUnionOfClassAndUnion"));
    }

    @Test
    @Order(28)
    @DisplayName("Check generated files from eqUnionOfClassAndUnion.owl.")
    public void checkEQUnionOfClassAndUnionFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","ClassTest1","UnionClass1","UnionClass2");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfUnionClass1UnionClass2","EquivalenceIntersectionOfClassTest1UnionOfUnionClass1UnionClass2Parent","IntersectionOfClassTest1UnionOfUnionClass1UnionClass2","ClassTest1Int");
        File folderEntities = new File(outputDir+ "eqUnionOfClassAndUnion/entities/");
        File folderSerialization = new File(outputDir+ "eqUnionOfClassAndUnion/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }



    @Test
    @Order(29)
    @DisplayName("Generate equivalent union of restriction and class.")
    public void generateEQIntersectionOfClassAndUnionOfRestriction() throws Exception {
        String[] inputFiles = { simpleClassesDir + "eqIntersectionOfClassAndUnionOfRestriction.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"eqIntersectionOfClassAndUnionOfRestriction"));
    }

    @Test
    @Order(30)
    @DisplayName("Check generated files from eqIntersectionOfClassAndUnionOfRestriction.owl.")
    public void checkEQIntersectionOfClassAndUnionOfRestrictionFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","ClassTest1","UnionClass1","UnionClass2");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfRestrHasChildUnionClass1","ClassTest1Int","IntersectionOfClassTest1UnionOfRestrHasChildUnionClass1","EquivalenceIntersectionOfClassTest1UnionOfRestrHasChildUnionClass1Parent");
        File folderEntities = new File(outputDir+ "eqIntersectionOfClassAndUnionOfRestriction/entities/");
        File folderSerialization = new File(outputDir+ "eqIntersectionOfClassAndUnionOfRestriction/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }

    @Test
    @Order(31)
    @DisplayName("Generate subProperty union of classes.")
    public void generateSubclassOfClasses(){
        String[] inputFiles = { simpleClassesDir + "subUnionOfClasses.owl"};
        assertDoesNotThrow(() -> generateFromRDMXMLToJava(inputFiles,"subUnionOfClasses"));
    }


    @Test
    @Order(32)
    @DisplayName("Check generated files from eqIntersectionOfClassAndUnionOfRestriction.owl.")
    public void checkSubclassOfClassesFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Person","Father","Mother");
        List<String> listOfInterfaces = Arrays.asList("OntoEntity","UnionOfFatherMother");
        File folderEntities = new File(outputDir+ "subUnionOfClasses/entities/");
        File folderSerialization = new File(outputDir+ "subUnionOfClasses/serialization/");

        checkGeneratedFiles(folderEntities,folderSerialization,listOfEntityClasses,listOfInterfaces);
    }








    @Test
    @Order(100)
    @DisplayName("Generate from family1.owl.")
    public void generateFamilyOntology() throws Exception {
        String[] inputFiles = { inputDir + "family.owl"};
        generateFromRDMXMLToJava(inputFiles,"family");
    }

    @Test
    @Order(101)
    @DisplayName("Check generated files from family.owl.")
    public void checkFamilyOntologyFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Parent","Cat","Child","Dog","Person","Human","Men","Woman","ChildlessPerson","Doggy","Father","Mother","Pet");
        List<String> listOfInterfaces = Arrays.asList("ParentInt","HumanInt","WomanInt","UnionOfCatDog","EquivalenceHumanPerson","OntoEntity","ComplementOfParent","EquivalenceChildlessPersonComplementOfParent","EquivalenceIntersectionOfParentWomanMother","EquivalencePetUnionOfCatDog","IntersectionOfParentWoman");
        File folderEntities = new File(outputDir+ "family/entities/");
        File folderSerialization = new File(outputDir+ "family/serialization/");

        File[] entityFiles = folderEntities.listFiles();
        assertEquals(listOfEntityClasses.size() + listOfInterfaces.size(), entityFiles.length, "Different number of files as expected.");
        for(File file : entityFiles) {
            assertTrue(listOfEntityClasses.contains(file.getName().replace(".java","")) || listOfInterfaces.contains(file.getName().replace(".java","")),"File "+file.getName()+" is not expecting.");
        }
        File[] serializationFiles = folderSerialization.listFiles();
        assertEquals(listOfEntityClasses.size() +1, serializationFiles.length, "Different number of files as expected in serialization file.");

    }


    @Test
    @Order(102)
    @DisplayName("Generate from collections.owl.")
    void generateCollectionsOntology() throws Exception {
        String[] inputFiles = { inputDir + "collections.owl"};
        generateFromRDMXMLToJava(inputFiles, "collections");
    }

    @Test
    @Order(103)
    @DisplayName("Check generated files from collections.owl.")
    public void checkCollectionOntologyFiles() throws Exception {
        List<String> listOfEntityClasses = Arrays.asList("Class1","Class2","Class3","Class4","Class5","Class6","Class7","Class8","Class9","Complement","Complement2","EquivalentCollection","IntersectionOfCollection","SubclassCollection","UnionOfCollection");
        List<String> listOfInterfaces = Arrays.asList("Class1Int","Class2Int","Class5Int","Class6Int","EquivalenceClass3Class4EquivalentCollection","OntoEntity");
        File folderEntities = new File(outputDir+ "collections/entities/");
        File folderSerialization = new File(outputDir+ "collections/serialization/");

        File[] entityFiles = folderEntities.listFiles();
        assertEquals(listOfEntityClasses.size()+listOfInterfaces.size(), entityFiles.length, "Different number of files as expected.");
        for(File file : entityFiles) {
            assertTrue(listOfEntityClasses.contains(file.getName().replace(".java","")) || listOfInterfaces.contains(file.getName().replace(".java","")),"File"+file.getName()+" is not expecting.");
        }
        File[] serializationFiles = folderSerialization.listFiles();
        assertEquals(listOfEntityClasses.size() +1 , serializationFiles.length, "Different number of files as expected in serialization file.");

    }
}
