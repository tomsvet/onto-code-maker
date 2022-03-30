
import ontology.generator.classes.examples.family1.Vocabulary;
import ontology.generator.classes.examples.family1.entities.*;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
import org.junit.jupiter.api.*;
import ontology.generator.classes.examples.family1.OntologyFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collection;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
public class OntologyGeneratorTests {
    String ex = "http://example.com/";
    public static Model model = new TreeModel();
    String MartinHuman = "http://example.com/Martin";
    String JurajMen = "http://example.com/Juraj";
    String MarekMen = "http://example.com/Marek";
    String MonikaWoman = "http://example.com/Monika";
    String MnaukaCat = "http://example.com/Mnauka";
    String HauDog = "http://example.com/HauDog";
    String TomasMen = "http://example.com/Tomas";
    String TomCat = "http://example.com/Tom";
    OntologyFactory factory;
    @BeforeEach
    void setUp() {
        factory = new OntologyFactory(model);
       /* InputStream inputStream = Files.newInputStream(file);

        Model newModel = Rio.parse(inputStream, "", format);*/

    }

    @Test
    @Order(1)
    @DisplayName("Simple test create Class")
    void testCreateInstance() {
        Human h = factory.createHuman(MartinHuman);

        assertTrue(MartinHuman.equals(h.getIri().stringValue()),"Problem create Instance of Human Class");
    }

    @Test
    @Order(2)
    @DisplayName("Simple test to add class instance to model")
    void testAddToModel() {
        Human h = factory.createHuman(MartinHuman);
        factory.addToModel(h);

        assertEquals(2,  model.size(),
                "Problem add instance to model.");

        Model resultModel = model.filter(Values.iri(MartinHuman),RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);
        assertEquals(1,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(3)
    @DisplayName("Simple test to get instance from model")
    void testGetFromModel() throws Exception {
        Human human = factory.getHumanFromModel(MartinHuman);

        assertEquals(human.getIri().stringValue(), MartinHuman ,
                        "Problem get instance from model.");
    }

    @Test
    @Order(4)
    @DisplayName("Simple test to get instance from model")
    void testGetAllFromModel() {
        Collection<Human> humans = null;
        try {
            humans = factory.getAllHumanInstancesFromModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(humans,"Humans is null.");
        assertEquals(1, humans.size() ,
                "Problem get all Human instances from model.");
    }

    @Test
    @Order(5)
    @DisplayName("Simple test to remove instance from model")
    void testRemoveToModel() {
        factory.removeHumanFromModel(MartinHuman);
        Model resultModel = model.filter(Values.iri(MartinHuman),RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);

        assertEquals(0,  resultModel.size(),
                "Problem remove instance from model.");
    }

    @Test
    @Order(6)
    @DisplayName("Simple test to subClassOf")
    void testSubClassOfToModel() {
        Men m1 = factory.createMen(MarekMen);
        factory.addToModel(m1);
        Men m2 = factory.createMen(JurajMen);
        factory.addToModel(m2);
        Woman w = factory.createWoman(MonikaWoman);
        factory.addToModel(w);
        Collection<Human> humans = null;
        try {
            humans = factory.getAllHumanInstancesFromModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(humans,"Humans is null.");
        assertEquals(3, humans.size(),
                "Problem get all Human instances from model.");
    }

    @Test
    @Order(7)
    @DisplayName("Simple test to add class with properties to model")
    void testSetProperties() {
        Cat c = factory.createCat(MnaukaCat);
        factory.addToModel(c);
        //Dog d = factory.createDog(HauDog);
        Men m = factory.createMen(MartinHuman);
        m.setHasAge(35);
        m.addHasCat(c);
        //m.setHasDog(d);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(MartinHuman),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(8)
    @DisplayName("Simple test to get class with properties from model")
    void testGetProperties() throws Exception {
        Men m = factory.getMenFromModel(MartinHuman);
        assertEquals(35,  m.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(1,  m.getHasCat().size(),
                "Has cat properties is empty.");
        assertEquals(MnaukaCat,  m.getHasCat().get(0).getIri().stringValue(),
                "Has cat properties is wrong.");
    }

    @Test
    @Order(9)
    @DisplayName("Simple test to set equivalent property from model")
    void testSetEquivalentProperty() {
        Dog d = factory.createDog(HauDog);
        factory.addToModel(d);
        Men m = factory.createMen(TomasMen);
        m.setHasAge(38);
        m.setHasDog(d);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(TomasMen),null, null);
        assertEquals(4,  resultModel.size(),
                "Problem set equivalent property to model 2.");
    }

    @Test
    @Order(10)
    @DisplayName("Simple test to get equivalent property from model")
    void testGetEquivalentProperty() {
        Men m = null;
        try {
            m = factory.getMenFromModel(TomasMen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(m,"Men is null.");
        assertNotNull(m.getHasDog(),"Has dog properties is null.");
        assertNotNull(m.getHasDogEq(),"Equivalent Has dog properties is null.");

        assertEquals(HauDog,  m.getHasDogEq().getIri().stringValue(),
                "equivalent has dog properties is wrong.");
    }


    @Test
    @Order(11)
    @DisplayName("Simple test to get equivalent property from model")
    void testUpdateProperty() {
        Men m = null;
        try {
            m = factory.getMenFromModel(MartinHuman);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(m,"Men is null.");
        assertEquals(35,  m.getHasAge(),
                "Has age properties is wrong.");
        m.setHasAge(40);
        Cat c = factory.createCat(TomCat);
        factory.addToModel(c);
        m.getHasCat().remove(0);
        m.addHasCat(c);
        factory.updateInstanceInModel(m);
    }


}
