
import ontology.generator.classes.examples.Human;
import ontology.generator.classes.examples.OntologyFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.*;

import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class OntologyGeneratorTests {
    String ex = "http://example.org/";
   public static Model model = new TreeModel();
    String testHuman = "http://example.org/Martin";

    @BeforeEach
    void setUp() {
        IRI classHuman = Values.iri(ex, "Human");
        IRI classMen = Values.iri(ex, "Men");
        IRI instanceJuraj = Values.iri(ex,"Juraj");

        model.add(classHuman, RDF.TYPE, RDFS.CLASS);
        model.add(classMen, RDF.TYPE, OWL.CLASS);
        model.add(instanceJuraj,RDF.TYPE,classHuman);

         /*for (Statement statement: model) {
            System.out.println(statement);
        }*/
    }

    @Test
    @Order(1)
    @DisplayName("Simple test create Class")
    void testCreateInstance() {
        OntologyFactory factory = new OntologyFactory(model);
        Human h = factory.createHuman(testHuman);

        assertTrue(testHuman.equals(h.getIri().stringValue()),"Problem create Instance of Human Class");
    }

    @Test
    @Order(2)
    @DisplayName("Simple test to add class instance to model")
    void testAddToModel() {
        OntologyFactory factory = new OntologyFactory(model);
        Human h = factory.createHuman(testHuman);
        factory.addToModel(h);

        assertEquals(4,  model.size(),
                "Problem add instance to model.");
    }

    @Test
    @Order(3)
    @DisplayName("Simple test to get instance from model")
    void testGetFromModel() {
        OntologyFactory factory = new OntologyFactory(model);
        Human human = factory.getHumanFromModel(testHuman);

        assertEquals(human.getIri().stringValue(), testHuman ,
                        "Problem get instance from model.");
    }

    @Test
    @Order(4)
    @DisplayName("Simple test to get instance from model")
    void testGetAllFromModel() {
        OntologyFactory factory = new OntologyFactory(model);
        Collection<Human> humans = factory.getAllHumanInstancesFromModel();
        /*assertEquals(3,  model.size(),
                        "Problem add instance to model.");*/
        assertEquals(humans.size(), 2 ,
                "Problem get all Human instances from model.");
    }

    @Test
    @Order(5)
    @DisplayName("Simple test to remove instance from model")
    void testRemoveToModel() {
        OntologyFactory factory = new OntologyFactory(model);
        factory.removeHumanFromModel(testHuman);

        assertEquals(3,  model.size(),
                "Problem remove instance from model.");
    }
}
