import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.entities.Class1;
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.DatatypePropertiesTestFactory;
import ontology.generator.classes.examples.allVariantsOfDatatypeProperties.entities.Class2;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  AllDatatypePropTests.java
 *
 *  Unit tests for Generated source code.
 *  Source code was generated from allDatatypeProp ontology.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllDatatypePropTests {
    public static Model defaultModel = new TreeModel();
    public Model model = new TreeModel();
    DatatypePropertiesTestFactory factory;

    // argument to run onto-code-maker-result-tester/src/main/resources/ontologies/allVariantsOfDatatypeProperties.owl -d onto-code-maker-result-tester/src/main/java/ontology/generator/classes/examples/allVariantsOfDatatypeProperties  -p ontology.generator.classes.examples.allVariantsOfDatatypeProperties


    String Class1InstanceName = "http://example.com/Class1Instance";
    String Class2InstanceName = "http://example.com/Class2Instance";

    @BeforeEach
    void setUp() throws Exception {
        String[] inputFiles = {"src/main/resources/ontologies/allDatatypeProp.owl"};
        OntologyParser ontoParser = new OntologyParser();
        defaultModel.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        //model = defaultModel;
        factory = new DatatypePropertiesTestFactory(model);
    }

    //check if hasEmptyDatatypeProperty, inverse porperties are not in the class

    @Test
    @Order(1)
    @DisplayName("Create instance of class")
    void testCreateInstance() {
        Class1 h = factory.createClass1(Class1InstanceName);
        assertEquals(Class1InstanceName, h.getIri().stringValue(), "Problem create Instance of Class1");
    }

    @Test
    @Order(2)
    @DisplayName("Normal DatatypeProperty")
    void normalDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addHasInt(10);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasInt().size(),"Property value is not in the model.");
        assertEquals(10,  loadedC.getHasInt().get(0),
                "Property values are not equal.");
    }


    @Test
    @Order(3)
    @DisplayName("Functional DatatypeProperty")
    void functionalDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setHasIntFunctionalProp(15);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(15,  loadedC.getHasIntFunctionalProp(),
                "Property values are not equal.");
    }

    @Test
    @Order(4)
    @DisplayName("Functional DatatypeProperty 2")
    void functionalDatatypeProperty2() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setHasIntFunctionalProp2(15);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(15,  loadedC.getHasIntFunctionalProp2(),
                "Property values are not equal.");
    }

    @Test
    @Order(5)
    @DisplayName("Inverse Functional DatatypeProperty")
    void inverseFunctionalDatatypeProperty() throws Exception {
        //inverse functional is not existing in datatype so this should be normal property
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addHasIntInverseFunctionalProp(10);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasIntInverseFunctionalProp().size(),"Property value is not in the model.");
        assertEquals(10,  loadedC.getHasIntInverseFunctionalProp().get(0),
                "Property values are not equal.");
    }

    @Test
    @Order(6)
    @DisplayName("Equivalent  DatatypeProperty")
    void equivalentDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addHasInt(10);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasIntEquivalent().size(),"Property value is not in the model.");
        assertEquals(10,  loadedC.getHasIntEquivalent().get(0),
                "Property values are not equal.");
    }

    @Test
    @Order(7)
    @DisplayName("Subproperty  DatatypeProperty")
    void subPropertyDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addHasInt(15);
        c.addHasIntSubproperty(20);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(2,loadedC.getHasInt().size(),"Property value with subproperty is not in the model.");
        assertEquals(1,loadedC.getHasIntSubproperty().size(),"Subproperty value is not in the model.");
        assertTrue(loadedC.getHasInt().contains(15)&& loadedC.getHasInt().contains(20),"Property values are missing:" + loadedC.toString());
    }

    @Test
    @Order(8)
    @DisplayName("EquivalentOfFunctional  DatatypeProperty")
    void equivalentOfFunctionalDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setHasIntFunctionalProp(25);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(25,  loadedC.getHasIntFunctionalProp(),
                "Property values are not equal.");
    }

    @Test
    @Order(9)
    @DisplayName("9.EquivalentOfEquivalent  DatatypeProperty")
    void equivalentOfEquivalentDatatypeProperty() throws Exception {

        Class2 c = factory.createClass2(Class2InstanceName);
        c.addHasInt2(45);
        factory.addToModel(c);

        Class2 loadedC = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasInt2().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getHasIntEquivalent2().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getHasIntEquivalentOfEquivalent().size(),"Property value is not in the model.");
        assertEquals(45,  loadedC.getHasIntEquivalentOfEquivalent().get(0),
                "Property values are not equal.");
    }

    @Test
    @Order(10)
    @DisplayName("10. SubPropertyOf Functional  DatatypeProperty")
    void subPropertyOfFunctionalDatatypeProperty() throws Exception {
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setHasIntFunctionalProp(75);
        c.addHasIntSubPropertyOfFunctional(95);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(95,loadedC.getHasIntFunctionalProp(),"Property value with subproperty is not in the model.");
        assertEquals(1,loadedC.getHasIntSubPropertyOfFunctional().size(),"Subproperty value is not in the model.");

    }

    @Test
    @Order(11)
    @DisplayName("11. Equivalent Of Subproperty Functional  DatatypeProperty")
    void equivalentOfSubpropertyDatatypeProperty() throws Exception {
        Class2 c = factory.createClass2(Class2InstanceName);
        c.addHasIntSubproperty2(800);
        factory.addToModel(c);

        Class2 loadedC = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getHasIntSubproperty2().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getHasIntEquivalentOfSubproperty().size(),"Property value is not in the model.");
        assertEquals(800,  loadedC.getHasIntEquivalentOfSubproperty().get(0),
                "Property values are not equal.");
    }

    @Test
    @Order(12)
    @DisplayName("12. SubpropertyOfEquivalent Functional  DatatypeProperty")
    void subpropertyOfEquivalentDatatypeProperty() throws Exception {
        Class2 c = factory.createClass2(Class2InstanceName);
        c.addHasInt2(45);
        c.addHasIntSubpropertyOfEquivalent(36);
        factory.addToModel(c);

        Class2 loadedC = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC,"Class2 is not in the model.");
        assertEquals(2,loadedC.getHasIntEquivalent2().size(),"Property value is not in the model.");
        assertTrue(loadedC.getHasIntEquivalent2().contains(45)&& loadedC.getHasIntEquivalent2().contains(36),"Property values are missing:" + loadedC.toString());
        assertEquals(1,loadedC.getHasIntSubpropertyOfEquivalent().size(),"Property value is not in the model.");
        assertEquals(36,  loadedC.getHasIntSubpropertyOfEquivalent().get(0),
                "Property values are not equal.");
    }

    @Test
    @Order(13)
    @DisplayName("13. Subproperty Of DoubleEquivalents Functional  DatatypeProperty")
    void subpropertyOfDOubleEquivalentsDatatypeProperty() throws Exception {
        Class2 c = factory.createClass2(Class2InstanceName);
        c.addHasInt2(46);
        c.addHasIntSubpropertyOfDOubleEquivalents(65);
        factory.addToModel(c);

        Class2 loadedC = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC,"Class2 is not in the model.");
        assertEquals(1,loadedC.getHasIntSubpropertyOfDOubleEquivalents().size(),"Property value is not in the model.");
        assertEquals(2,loadedC.getHasIntEquivalentOfEquivalent().size(),"Property value is not in the model.");
        assertTrue(loadedC.getHasIntEquivalentOfEquivalent().contains(46)&& loadedC.getHasIntEquivalentOfEquivalent().contains(65),"Property values are missing:" + loadedC.toString());
        assertEquals(2,loadedC.getHasIntEquivalent2().size(),"Property value is not in the model.");
        assertTrue(loadedC.getHasIntEquivalent2().contains(46)&& loadedC.getHasIntEquivalent2().contains(65),"Property values are missing:" + loadedC.toString());
        assertEquals(65,  loadedC.getHasIntSubpropertyOfDOubleEquivalents().get(0),
                "Property values are not equal.");

    }

    @Test
    @Order(14)
    @Disabled
    @DisplayName("14. Domain Is Union Functional DatatypeProperty")
    void domainIsUnionFunctional() throws Exception {

    }

    @Test
    @Order(15)
    @DisplayName("15. Domain Is Abstract Union Functional DatatypeProperty")
    void domainIsAbstractUnionFunctional() throws Exception {
        String testUnion1="testUnion1";
        String testUnion2="testUnion2";
        Class1 c1 = factory.createClass1(Class1InstanceName);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        c1.setDomainIsAbstractUnionFunctional(testUnion1);
        c2.setDomainIsAbstractUnionFunctional(testUnion2);
        factory.addToModel(c1);
        factory.addToModel(c2);

        Class1 loadedC1 = factory.getClass1FromModel(Class1InstanceName);
        Class2 loadedC2 = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC1,"Class1 is not in the model.");
        assertNotNull(loadedC2,"Class2 is not in the model.");

        assertEquals(testUnion1,  loadedC1.getDomainIsAbstractUnionFunctional(),
                "Property values are not equal.");

        assertEquals(testUnion2,  loadedC2.getDomainIsAbstractUnionFunctional(),
                "Property values are not equal.");

    }

    @Test
    @Order(16)
    @DisplayName("16. Domain Is Abstract Intersection Of Functional DatatypeProperty")
    void domainIsAbstractIntersectionOfFunctional() throws Exception {
        String testUnion1="testUnion1";
        String testUnion2="testUnion2";
        Class1 c1 = factory.createClass1(Class1InstanceName);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        //c1.set(testUnion1);
        c2.setDomainIsAbstractUnionFunctional(testUnion2);
        factory.addToModel(c1);
        factory.addToModel(c2);

    }

    @Test
    @Disabled
    @Order(17)
    @DisplayName("17. Domain Is Intersection Of Functional DatatypeProperty")
    void domainIsIntersectionOfFunctional() throws Exception {


    }

    @Test
    @Order(18)
    @DisplayName("18. Domain Is Abstract Complement Of Functional DatatypeProperty")
    void domainIsAbstractComplementOfFunctional() throws Exception {


    }

    @Test
    @Order(19)
    @DisplayName("19. Domain Is Complement Of Functional DatatypeProperty")
    void domainIsComplementOfFunctional() throws Exception {


    }
}
