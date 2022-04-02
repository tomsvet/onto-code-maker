import ontology.generator.classes.examples.allVariantsOfObjectProperties.ObjectPropertiesTestFactory;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.Class1;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.Class2;

import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.Class3;
import ontology.generator.classes.examples.family1.OntologyFactory;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;


// argument to run onto-code-maker-result-tester/src/main/resources/ontologies/allVariantsOfObjectProperties.owl -d onto-code-maker-result-tester/src/main/java/ontology/generator/classes/examples/allVariantsOfObjectProperties  -p ontology.generator.classes.examples.allVariantsOfObjectProperties

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllVariantsOfObjectPropertiesTests {
    public Model model = new TreeModel();
    ObjectPropertiesTestFactory factory;

    String Class1InstanceName = "http://example.com/Class1Instance";
    String Class2InstanceName = "http://example.com/Class2Instance";
    String Class2InstanceName2 = "http://example.com/Class2Instance2";
    String Class2InstanceName3 = "http://example.com/Class2Instance3";

    @BeforeEach
    void setUp() throws FileNotFoundException {
        String[] inputFiles = {"src/main/resources/ontologies/family1.owl"};
        OntologyParser ontoParser = new OntologyParser();
        //model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        factory = new ObjectPropertiesTestFactory(model);
    }


    @Test
    @Order(1)
    @DisplayName("1.Create instances")
    void testCreateAllInstances(){
        Class1 h = factory.createClass1(Class1InstanceName);
        assertEquals(Class1InstanceName, h.getIri().stringValue(), "Problem create Instance of Class1");
        Class2 c2 = factory.createClass2(Class2InstanceName);
        assertEquals(Class2InstanceName, c2.getIri().stringValue(), "Problem create Instance of Class1");
    }

    @Test
    @Order(2)
    @DisplayName("2.Normal Object Property")
    void normalObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getNormalObjectProperty().size(),"Normal Object Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getNormalObjectProperty().get(0).getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(3)
    @DisplayName("3.Functional Object Property")
    void functionalObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setFunctionalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getFunctionalObjectProperty().getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(4)
    @DisplayName("4.Functional Object Property2")
    void functionalObjectProperty2() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setFunctionalObjectProperty2(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getFunctionalObjectProperty2().getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(5)
    @DisplayName("5.Inverse functional Object Property")
    void inverseFunctionalObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addInverseFunctionalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getInverseFunctionalObjectProperty().size(),"Normal Object Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getInverseFunctionalObjectProperty().get(0).getIri().stringValue(),
                "Property values are not equal.");
        Class2 loadedC2 = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC2,"Class2 is not in the model.");
        assertEquals(Class1InstanceName,  loadedC2.getClass1().getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(6)
    @DisplayName("6.Inverse Functional Object Property 2")
    void inverseFunctionalObjectProperty2() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addInverseFunctionalObjectProperty2(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getInverseFunctionalObjectProperty2().size(),"Normal Object Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getInverseFunctionalObjectProperty2().get(0).getIri().stringValue(),
                "Property values are not equal.");
        Class2 loadedC2 = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC2,"Class2 is not in the model.");
        assertEquals(Class1InstanceName,  loadedC2.getInverseFunctionalObjectProperty2Inverse().getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(7)
    @DisplayName("7.Equivalent Object Property ")
    void equivalentObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getEquivalentObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getEquivalentObjectProperty().get(0).getIri().stringValue(),
                "Equivalent Property values are not equal.");
        assertEquals(Class2InstanceName,  loadedC.getNormalObjectProperty().get(0).getIri().stringValue(),
                "Normal Property values are not equal.");
    }

    @Test
    @Order(8)
    @DisplayName("8.Subproperty Object Property 2")
    void subPropertyObjectProperty2() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class2 c3 = factory.createClass2(Class2InstanceName2);
        factory.addToModel(c3);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c3);
        c.addSubPropertyOfObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(2,loadedC.getNormalObjectProperty().size(),"Property value is not in the model.");
        //assertTrue(loadedC.getNormalObjectProperty().contains(Class2InstanceName) && loadedC.getNormalObjectProperty().contains(Class2InstanceName2),"Normal Property values are not equal.");
        assertEquals(1,loadedC.getSubPropertyOfObjectProperty().size(),"Subject Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getSubPropertyOfObjectProperty().get(0).getIri().stringValue(),
                "Subject Property values are not equal.");
    }

    @Test
    @Order(9)
    @DisplayName("9.Inverse of Object Property")
    void inverseOfObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        factory.addToModel(c);

        Class2 loadedC = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getInverseOfObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class1InstanceName,loadedC.getInverseOfObjectProperty().get(0).getIri().stringValue(),"Property values is not equal");
    }


    @Test
    @Order(10)
    @DisplayName("10.Equivalent To Functional Object Property")
    void equivalentToFunctionalObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setFunctionalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(Class2InstanceName,loadedC.getEquivalentToFunctionalObjectProperty().getIri().toString(),"Property values are not equal");

    }

    @Test
    @Order(11)
    @DisplayName("11.Equivalent To Subproperty Object Property")
    void equivalentToSubpropertyObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addSubPropertyOfObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getEquivalentToSubpropertyObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName,loadedC.getEquivalentToSubpropertyObjectProperty().get(0).getIri().stringValue(),"Property values are not equal");
    }

    @Test
    @Order(12)
    @DisplayName("12.Equivalent To InverseFunctional Object Property")
    void equivalentToInverseFunctionalObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addInverseFunctionalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getEquivalentToInverseFunctionalObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName,loadedC.getEquivalentToInverseFunctionalObjectProperty().get(0).getIri().stringValue(),"Property values are not equal");

    }

    @Test
    @Order(13)
    @DisplayName("13.Equivalent To Equivalent Object Property")
    void equivalentToEquivalentObjectProperty() throws Exception {
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(1,loadedC.getNormalObjectProperty().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getEquivalentObjectProperty().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getEquivalentToEquivalentObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName,  loadedC.getEquivalentToEquivalentObjectProperty().get(0).getIri().toString(),
                "Property values are not equal.");
    }

    @Test
    @Order(14)
    @DisplayName("14.Subproperty Of Equivalent Object Property")
    void subPropertyOfEquivalentObjectProperty() throws Exception {
        Class2 c3 = factory.createClass2(Class2InstanceName2);
        factory.addToModel(c3);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        c.addSubPropertyOfEquivalentObjectProperty(c3);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(2,loadedC.getEquivalentObjectProperty().size(),"Property value is not in the model.");
       // assertTrue(loadedC.getEquivalentObjectProperty().contains(c2)&& loadedC.getEquivalentObjectProperty().contains(c3),"Property values are missing:" + loadedC.toString());
        assertEquals(1,loadedC.getSubPropertyOfEquivalentObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName2,  loadedC.getSubPropertyOfEquivalentObjectProperty().get(0).getIri().toString(),
                "Property values are not equal.");
    }

    @Test
    @Order(15)
    @DisplayName("15.Subproperty Of Functional Object Property")
    void subPropertyOfFunctionalObjectProperty() throws Exception {
        Class2 c3 = factory.createClass2(Class2InstanceName2);
        factory.addToModel(c3);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.setFunctionalObjectProperty(c2);
        c.addSubPropertyOfFunctionalObjectProperty(c3);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertNotNull(loadedC.getFunctionalObjectProperty(),"Class1 is not in the model.");
        //assertEquals(,loadedC.getFunctionalObjectProperty().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getSubPropertyOfFunctionalObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName2,  loadedC.getSubPropertyOfFunctionalObjectProperty().get(0).getIri().toString(),
                "Property values are not equal.");
    }

    @Test
    @Order(16)
    @DisplayName("16.Subproperty Of Inverse Functional Object Property")
    void subPropertyOfInverseFunctionalObjectProperty() throws Exception {
        Class2 c3 = factory.createClass2(Class2InstanceName2);
        factory.addToModel(c3);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addInverseFunctionalObjectProperty(c2);
        c.addSubPropertyOfInverseFunctionalObjectProperty(c3);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(2,loadedC.getInverseFunctionalObjectProperty().size(),"Property value is not in the model.");
        //assertTrue(loadedC.getInverseFunctionalObjectProperty().contains(c2)&& loadedC.getInverseFunctionalObjectProperty().contains(c3),"Property values are missing:" + loadedC.toString());
        assertEquals(1,loadedC.getSubPropertyOfInverseFunctionalObjectProperty().size(),"Property value is not in the model.");
        assertEquals(Class2InstanceName2,  loadedC.getSubPropertyOfInverseFunctionalObjectProperty().get(0).getIri().toString(),
                "Property values are not equal.");


        Class2 loadedC2 = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC2,"Class2 is not in the model.");
        assertEquals(Class1InstanceName,  loadedC2.getClass1().getIri().stringValue(),
                "Property values are not equal.");
    }

    @Test
    @Order(17)
    @DisplayName("17.Subproperty Of subProperty Functional Object Property")
    void subPropertyOfSubpropertyObjectProperty() throws Exception {
        Class2 c4 = factory.createClass2(Class2InstanceName3);
        factory.addToModel(c4);
        Class2 c3 = factory.createClass2(Class2InstanceName2);
        factory.addToModel(c3);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c2);
        Class1 c = factory.createClass1(Class1InstanceName);
        c.addNormalObjectProperty(c2);
        c.addSubPropertyOfObjectProperty(c3);
        c.addSubPropertyOfSubpropertyObjectProperty(c4);
        factory.addToModel(c);

        Class1 loadedC = factory.getClass1FromModel(Class1InstanceName);
        assertNotNull(loadedC,"Class1 is not in the model.");
        assertEquals(3,loadedC.getNormalObjectProperty().size(),"Property value is not in the model.");
        //assertTrue(loadedC.getInverseFunctionalObjectProperty().contains(c2)&& loadedC.getInverseFunctionalObjectProperty().contains(c3),"Property values are missing:" + loadedC.toString());
        assertEquals(2,loadedC.getSubPropertyOfObjectProperty().size(),"Property value is not in the model.");
        assertEquals(1,loadedC.getSubPropertyOfSubpropertyObjectProperty().size(),"Property value is not in the model.");

        assertEquals(Class2InstanceName3,  loadedC.getSubPropertyOfSubpropertyObjectProperty().get(0).getIri().toString(),
                "Property values are not equal.");
    }

    @Test
    @Order(18)
    @DisplayName("18. Domain Is Abstract Union Functional Object Property")
    void domainIsAbstractUnionFunctional() throws Exception {
        Class3 c4 = factory.createClass3(Class2InstanceName3);
        factory.addToModel(c4);
        Class3 c3 = factory.createClass3(Class2InstanceName2);
        factory.addToModel(c3);

        Class1 c1 = factory.createClass1(Class1InstanceName);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        c1.setDomainIsAbstractUnionFunctional(c3);
        c2.setDomainIsAbstractUnionFunctional(c4);
        factory.addToModel(c1);
        factory.addToModel(c2);

        Class1 loadedC1 = factory.getClass1FromModel(Class1InstanceName);
        Class2 loadedC2 = factory.getClass2FromModel(Class2InstanceName);
        assertNotNull(loadedC1,"Class1 is not in the model.");
        assertNotNull(loadedC2,"Class2 is not in the model.");

        assertEquals(c3.getIri(),  loadedC1.getDomainIsAbstractUnionFunctional().getIri(),
                "Property values are not equal.");

        assertEquals(c4.getIri(),  loadedC2.getDomainIsAbstractUnionFunctional().getIri(),
                "Property values are not equal.");
    }

    @Test
    @Order(19)
    @DisplayName("19. Domain Is Abstract Union Functional Object Property")
    void rangeIsAbstractUnionFunctional() throws Exception {

        Class1 c1 = factory.createClass1(Class1InstanceName);
        Class2 c2 = factory.createClass2(Class2InstanceName);
        factory.addToModel(c1);
        factory.addToModel(c2);

        Class3 c4 = factory.createClass3(Class2InstanceName3);
        Class3 c3 = factory.createClass3(Class2InstanceName2);
        c3.setRangeIsAbstractUnionFunctional(c1);
        c4.setRangeIsAbstractUnionFunctional(c2);
        factory.addToModel(c3);
        factory.addToModel(c4);

        Class3 loadedC1 = factory.getClass3FromModel(Class2InstanceName2);
        Class3 loadedC2 = factory.getClass3FromModel(Class2InstanceName3);
        assertNotNull(loadedC1,"Class3 is not in the model.");
        assertNotNull(loadedC2,"Class3 is not in the model.");

        assertEquals(c1.getIri(),  loadedC1.getRangeIsAbstractUnionFunctional().getIri(),
                "Property values are not equal.");
        assertEquals(c2.getIri(),  loadedC2.getRangeIsAbstractUnionFunctional().getIri(),
                "Property values are not equal.");
    }
}
