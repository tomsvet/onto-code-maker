import ontology.generator.classes.examples.family1.OntologyFactory;
import ontology.generator.classes.examples.family1.Vocabulary;
import ontology.generator.classes.examples.family1.entities.*;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sparqlbuilder.core.Prefix;
import org.eclipse.rdf4j.sparqlbuilder.core.SparqlBuilder;
import org.eclipse.rdf4j.sparqlbuilder.core.Variable;
import org.eclipse.rdf4j.sparqlbuilder.core.query.Queries;
import org.eclipse.rdf4j.sparqlbuilder.core.query.SelectQuery;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Iri;
import org.eclipse.rdf4j.sparqlbuilder.rdf.Rdf;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Family1Tests {

    public static Model model = new TreeModel();
    OntologyFactory factory;

    String MartinHuman = "http://example.com/Martin";
    String MatusPerson = "http://example.com/Matus";
    String LukasPerson = "http://example.com/Lukas";

    String JurajMen = "http://example.com/Juraj";
    String MarekMen = "http://example.com/Marek";
    String TomasMen = "http://example.com/Tomas";
    String JanMen = "http://example.com/Jan";
    String FilipMen = "http://example.com/Filip";
    String JakubMen = "http://example.com/Jakub";
    String RomanMen = "http://example.com/Roman";

    String JanChild = "http://example.com/child/Janko";
    String TomasChild = "http://example.com/child/Tomas";
    String FilipChild = "http://example.com/child/Filip";
    String MatusChild = "http://example.com/child/Matus";
    String FelixParent = "http://example.com/parent/Felix";
    String MarekParent = "http://example.com/parent/Marek";
    String LukasParent = "http://example.com/parent/Lukas";

    String MonikaWoman = "http://example.com/Monika";
    String JanaWoman = "http://example.com/Jana";
    String VeronikaWoman = "http://example.com/Veronika";

    String MnaukaCat = "http://example.com/Mnauka";
    String TomCat = "http://example.com/Tom";

    String HauDog = "http://example.com/HauDog";


    @BeforeEach
    void setUp() throws FileNotFoundException {
        String[] inputFiles = {"src/main/resources/ontologies/family1.owl"};
        OntologyParser ontoParser = new OntologyParser();
        model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        factory = new OntologyFactory(model);
    }

    @Test
    @Order(1)
    @DisplayName("Get All instances when model is without instances.")
    void testAllInstancesWithoutInstance() throws Exception {
        assertEquals(new ArrayList<>(),factory.getAllHumanInstancesFromModel(),"Human class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllPersonInstancesFromModel(),"Person class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllCatInstancesFromModel(),"Cat class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllMenInstancesFromModel(),"Men class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllWomanInstancesFromModel(),"Woman class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllDogInstancesFromModel(),"Dog class is not empty.");

    }

    @Test
    @Order(2)
    @DisplayName("Get instance when model is without instances.")
    void testInstanceWhenModelIsEmpty() throws Exception {
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(3)
    @DisplayName("Simple test create Class")
    void testCreateInstance() {
        Human h = factory.createHuman(MartinHuman);

        assertEquals(MartinHuman, h.getIri().stringValue(), "Problem create Instance of Human Class");
    }

    @Test
    @Order(4)
    @DisplayName("Update instance when instance is not in model.")
    void testUpdateNonExistInstance() throws Exception {
        Human h = factory.createHuman(MartinHuman);
        factory.updateInstanceInModel(h);
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(5)
    @DisplayName("Remove instance when instance is not in model.")
    void testRemoveNonExistInstance() throws Exception {
        factory.removeMenFromModel(MartinHuman);
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(6)
    @DisplayName("Simple test to add class instance to model")
    void testAddToModel() {
        Human h = factory.createHuman(MartinHuman);
        int startSize = model.size();
        factory.addToModel(h);

        // +2 because Human has property
        assertEquals(startSize +2 ,  model.size(),
                "Problem add instance to model.");

        Model resultModel = model.filter(Values.iri(MartinHuman), RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);
        assertEquals(1,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(7)
    @DisplayName("Simple test to get instance from model")
    void testGetFromModel() throws Exception {
        Human human = factory.getHumanFromModel(MartinHuman);
        assertNotNull(human,"Problem get instance from model.");
        assertEquals(human.getIri().stringValue(), MartinHuman ,
                "Problem get instance from model.");
    }

    @Test
    @Order(8)
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
    @Order(9)
    @DisplayName("Simple test to remove instance from model")
    void testRemoveFromModel() {
        factory.removeHumanFromModel(MartinHuman);
        Model resultModel = model.filter(Values.iri(MartinHuman),RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);

        assertEquals(0,  resultModel.size(),
                "Problem remove instance from model.");
    }

    @Test
    @Order(10)
    @DisplayName("Get all Mens instances from model")
    void testGetAllInstancesFromModel() {
        Men m1 = factory.createMen(MarekMen);
        factory.addToModel(m1);
        Men m2 = factory.createMen(JurajMen);
        factory.addToModel(m2);

        Collection<Men> mens = null;
        try {
            mens = factory.getAllMenInstancesFromModel();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(mens,"Mens is null.");
        assertEquals(2, mens.size(),
                "Problem get all Human instances from model.");
        //assertTrue(mens.contains(m1)&& mens.contains(m2),"Model doesn't contains Marek or Juraj.");
    }

    @Test
    @Order(11)
    @Disabled
    @DisplayName("Simple test to equivalent class")
    void testEquivalentOfToModel() throws Exception {
        Person p = factory.createPerson(MatusPerson);
        factory.addToModel(p);
        Human human = factory.getHumanFromModel(MatusPerson);
        assertNotNull(human,"Problem get instance from model.");
        assertEquals(human.getIri().stringValue(), MatusPerson ,
                "Problem get instance from model.");
    }

    @Test
    @Order(10)
    @Disabled
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

    /**
     *  Properties test
     *
     */

    @Test
    @Order(11)
    @DisplayName("Simple test to add class with properties to model")
    void testSetProperties() {
        Cat c = factory.createCat(MnaukaCat);
        factory.addToModel(c);
        //Dog d = factory.createDog(HauDog);
        Human h = factory.createHuman(MartinHuman);
        h.setHasAge(35);
        h.addHasCat(c);
        //m.setHasDog(d);
        factory.addToModel(h);

        Model resultModel = model.filter(Values.iri(MartinHuman),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(12)
    @DisplayName("Simple test to get class with properties from model")
    void testGetProperties() throws Exception {
        Human h = factory.getHumanFromModel(MartinHuman);
        assertNotNull(h,"Martin is not in Model ");
        assertEquals(35,  h.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(1,  h.getHasCat().size(),
                "Has cat properties is empty.");
        assertEquals(MnaukaCat,  h.getHasCat().get(0).getIri().stringValue(),
                "Has cat properties is wrong.");
    }

    @Test
    @Order(13)
    @DisplayName("Simple test to add subclass with properties of super class to model")
    void testSetSubClassProperties() throws Exception {
        Cat c = factory.getCatFromModel(MnaukaCat);
        factory.addToModel(c);
        //Dog d = factory.createDog(HauDog);
        Men m = factory.createMen(JanMen);
        m.setHasAge(45);
        m.addHasCat(c);
        //m.setHasDog(d);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(MartinHuman),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(14)
    @DisplayName("Simple test to get class with properties from superclass")
    void testGetSubClassProperties() throws Exception {
        Men m = factory.getMenFromModel(JanMen);
        assertNotNull(m,"Martin is not in Model ");
        assertEquals(45,  m.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(1,  m.getHasCat().size(),
                "Has cat properties is empty.");
        assertEquals(MnaukaCat,  m.getHasCat().get(0).getIri().stringValue(),
                "Has cat properties is wrong.");
    }

    @Test
    @Order(15)
    @DisplayName("Simple test to add properties of equivalent class to model")
    void testSetPropertiesOfEquivalentClass() throws Exception {
        Cat c = factory.getCatFromModel(MnaukaCat);
        factory.addToModel(c);
        Person m = factory.createPerson(LukasPerson);
        m.setHasAge(20);
        m.addHasCat(c);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(LukasPerson),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");

    }


    @Test
    @Order(16)
    @DisplayName("Simple test to get class with properties of equivalent class")
    void testGetPropertiesOfEquivalentClass() throws Exception {
        Person p = factory.getPersonFromModel(LukasPerson);
        assertNotNull(p,"Lukas is not in Model ");
        assertEquals(20,  p.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(1,  p.getHasCat().size(),
                "Has cat properties is empty.");
        assertEquals(MnaukaCat,  p.getHasCat().get(0).getIri().stringValue(),
                "Has cat properties is wrong.");
    }

    @Test
    @Order(17)
    @DisplayName("Simple test to set functional property")
    void testFunctionalProperty() throws Exception {
        Dog d = factory.createDog(HauDog);
        factory.addToModel(d);
        Men m = factory.createMen(TomasMen);
        m.setHasAge(38);
        m.setHasDog(d);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(TomasMen),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem set functional property to model.");

        Men p = factory.getMenFromModel(TomasMen);

        assertNotNull(m,"Men is null.");
        assertNotNull(m.getHasDog(),"Has dog properties is null.");
        assertEquals(HauDog,  m.getHasDog().getIri().stringValue(),
                "Has dog properties has wrong value.");
    }

    @Test
    @Order(18)
    @DisplayName("Simple test to set inverse functional property")
    void testInverseFunctionalProperty() throws Exception {
        Child ch = factory.createChild(JanChild);
        factory.addToModel(ch);
        Child chT = factory.createChild(TomasChild);
        factory.addToModel(chT);
        Parent p = factory.createParent(FelixParent);
        p.addHasChild(ch);
        p.addHasChild(chT);
        factory.addToModel(p);

        Model resultModel = model.filter(Values.iri(FelixParent),null, null);
        assertEquals(2,  resultModel.size(),
                "Problem set inverse functional property to model.");

       Parent p2 = factory.getParentFromModel(FelixParent);

        assertNotNull(p2,"Men is null.");
        assertEquals(2,  p2.getHasChild().size(),
                "Has child property has wrong value.");
        assertTrue(TomasChild.equals(p2.getHasChild().get(0).getIri().stringValue()) || JanChild.equals(p2.getHasChild().get(0).getIri().stringValue()),
                "Has child property has wrong value.2");

        Child tc = factory.getChildFromModel(TomasChild);
        assertNotNull(tc.getParent(),"Parent in child property is null.");
        assertEquals(FelixParent,   tc.getParent().getIri().stringValue(),
                "Parent property has wrong value.");
    }

    @Test
    @Order(19)
    @DisplayName("Simple test to set inverse functional and functional property")
    void testInverseFunctionalAndFunctionalProperty() throws Exception {
        Woman w = factory.createWoman(JanaWoman);
        factory.addToModel(w);
        Men m = factory.createMen(FilipMen);
        m.setHasAge(38);
        m.setHasWife(w);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(FilipMen),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem set equivalent property to model 2.");

        Men t = factory.getMenFromModel(FilipMen);
        assertNotNull(t,"Men is null.");
        assertNotNull(t.getHasWife(),"Has wife properties is null.");
        assertEquals(JanaWoman,  t.getHasWife().getIri().stringValue(),
                "Has dog properties has wrong value.");

        Woman w2 = factory.getWomanFromModel(JanaWoman);
        assertNotNull(w2,"Woman is null.");
        assertNotNull(w2.getMen(),"Men property is null.");
        assertEquals(FilipMen,  w2.getMen().getIri().stringValue(),
                "Men property has wrong value.");
    }

    @Test
    @Order(20)
    @DisplayName("Simple test to set equivalent property from model")
    void testSetEquivalentProperty() {
        Dog d = factory.createDog(HauDog);
        factory.addToModel(d);
        Men m = factory.createMen(RomanMen);
        m.setHasAge(38);
        m.setHasDog(d);
        factory.addToModel(m);

        Model resultModel = model.filter(Values.iri(RomanMen),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem set equivalent property to model 2.");
    }

    @Test
    @Order(21)
    @DisplayName("Simple test to get equivalent property from model")
    void testGetEquivalentProperty() {
        Men m = null;
        try {
            m = factory.getMenFromModel(RomanMen);
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
    @Order(22)
    @DisplayName("Simple test to update instance properties")
    void testUpdateProperty() {
        Human m = null;
        try {
            m = factory.getHumanFromModel(MartinHuman);
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

        try {
            m = factory.getHumanFromModel(MartinHuman);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(m,"Men is null.");
        assertEquals(40,  m.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(1,  m.getHasCat().size(),
                "Has cat properties is empty.");
        assertEquals(TomCat,  m.getHasCat().get(0).getIri().stringValue(),
                "Has cat properties is wrong.");
    }

    @Test
    @Order(23)
    @DisplayName("Simple test to subPropertyOf property. Has father")
    void testSubPropertyOfProperty() throws Exception {
        Child ch = factory.createChild(FilipChild);
        Parent p = factory.createParent(MarekParent);
        factory.addToModel(p);
        ch.addHasParent(p);
        factory.addToModel(ch);

        Child fc = factory.getChildFromModel(FilipChild);
        assertNotNull(fc,"Child instance is null.");
        assertEquals(1,fc.getHasParent().size(),"Has father in child property doesn't have one value.");
        assertEquals(0,fc.getHasFather().size(),"Has father in child property doesn't have one value.");
        assertEquals(MarekParent,   fc.getHasParent().get(0).getIri().stringValue(),
                "Has father property has wrong value.");
    }

    @Test
    @Order(24)
    @DisplayName("Simple test to set subPropertyOf property. Has father")
    void testSetSubPropertyOfProperty() throws Exception {
        Child ch = factory.createChild(MatusChild);
        Parent p = factory.createParent(LukasParent);
        factory.addToModel(p);
        ch.addHasFather(p);
        factory.addToModel(ch);

        Child fc = factory.getChildFromModel(MatusChild);
        assertNotNull(fc,"Child instance is null.");
        assertEquals(1,fc.getHasFather().size(),"Has father in child property doesn't have one value.");
        assertEquals(1,fc.getHasParent().size(),"Has parent in child property doesn't have one value.");
        assertEquals(LukasParent,   fc.getHasParent().get(0).getIri().stringValue(),
                "Has father property has wrong value.");



        Parent pl = factory.getParentFromModel(LukasParent);
        assertNotNull(pl,"Instance parent Lukas is null.");
        assertEquals(0,   pl.getHasChild().size(),
                "Has child property is not empty.");

    }

    @Test
    @Order(25)
    @DisplayName("Simple test to inverse property of property")
    void testInversePropertyOfProperty() throws Exception {
        Woman w = factory.createWoman(VeronikaWoman);
        factory.addToModel(w);
        Men m = factory.createMen(JakubMen);
        m.setHasWife(w);
        factory.addToModel(m);

        Woman w2 = factory.getWomanFromModel(VeronikaWoman);

        assertEquals(1,w2.getHasHusband().size(),"Has husband property in woman class is null.");
        assertEquals(JakubMen,   w2.getHasHusband().get(0).getIri().stringValue(),
                "Has husband property has wrong value.");

    }


}
