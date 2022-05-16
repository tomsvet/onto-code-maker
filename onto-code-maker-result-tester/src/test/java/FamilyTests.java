import ontology.generator.classes.examples.family.FamilyFactory;
import ontology.generator.classes.examples.family.Vocabulary;
import ontology.generator.classes.examples.family.entities.*;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  FamilyTests.java
 *
 *  Unit tests for Generated source code.
 *  Source code was generated from Family ontology.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FamilyTests {

    public static Model model = new TreeModel();
    FamilyFactory factory;

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

    String JurajChildlessPerson = "http://example.com/childlessperson/Juraj";

    String FelixFather = "http://example.com/father/Felix";
    String MonikaMother = "http://example.com/mother/Monika";


    String MonikaWoman = "http://example.com/Monika";
    String JanaWoman = "http://example.com/Jana";
    String VeronikaWoman = "http://example.com/Veronika";

    String TomPet = "http://example.com/pet/Tom";

    String MnaukaCat = "http://example.com/Mnauka";
    String TomCat = "http://example.com/Tom";

    String HauDog = "http://example.com/HauDog";

    String TomasDoggy = "http://example.com/doggy/Tomas";


    @BeforeEach
    void setUp() throws Exception {
        String[] inputFiles = {"src/main/resources/ontologies/family.owl"};
        OntologyParser ontoParser = new OntologyParser();
        model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        factory = new FamilyFactory(model);
    }

    @Test
    @Order(1)
    @DisplayName("1.Get All instances when model is without instances.")
    void testAllInstancesWithoutInstance() throws Exception {
        assertEquals(new ArrayList<>(),factory.getAllHumanInstancesFromModel(),"Human class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllPersonInstancesFromModel(),"Person class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllCatInstancesFromModel(),"Cat class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllMenInstancesFromModel(),"Men class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllWomanInstancesFromModel(),"Woman class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllDogInstancesFromModel(),"Dog class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllChildInstancesFromModel(),"Child class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllChildlessPersonInstancesFromModel(),"ChildlessPerson class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllDoggyInstancesFromModel(),"Doggy class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllFatherInstancesFromModel(),"Father class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllMotherInstancesFromModel(),"Mother class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllParentInstancesFromModel(),"Parent class is not empty.");
        assertEquals(new ArrayList<>(),factory.getAllPetInstancesFromModel(),"Pet class is not empty.");



    }

    @Test
    @Order(2)
    @DisplayName("2.Get instance when model is without instances.")
    void testInstanceWhenModelIsEmpty() throws Exception {
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(3)
    @DisplayName("3.Simple test create Class")
    void testCreateInstance() {
        assertEquals(MartinHuman, factory.createHuman(MartinHuman).getIri().stringValue(), "Problem create Instance of Human Class");
        assertEquals(TomCat, factory.createCat(TomCat).getIri().stringValue(), "Problem create Instance of Cat Class");
        assertEquals(HauDog, factory.createDog(HauDog).getIri().stringValue(), "Problem create Instance of Dog Class");
        assertEquals(JanChild, factory.createChild(JanChild).getIri().stringValue(), "Problem create Instance of Child Class");
        assertEquals(JurajChildlessPerson, factory.createChildlessPerson(JurajChildlessPerson).getIri().stringValue(), "Problem create Instance of ChildlessPerson Class");
        assertEquals(FelixFather, factory.createFather(FelixFather).getIri().stringValue(), "Problem create Instance of Father Class");
        assertEquals(TomasDoggy, factory.createDoggy(TomasDoggy).getIri().stringValue(), "Problem create Instance of Doggy Class");
        assertEquals(FelixParent, factory.createParent(FelixParent).getIri().stringValue(), "Problem create Instance of Parent Class");
        assertEquals(MonikaMother, factory.createMother(MonikaMother).getIri().stringValue(), "Problem create Instance of Mother Class");
        assertEquals(TomasMen, factory.createMen(TomasMen).getIri().stringValue(), "Problem create Instance of Men Class");
        assertEquals(MonikaWoman, factory.createWoman(MonikaWoman).getIri().stringValue(), "Problem create Instance of Woman Class");
        assertEquals(LukasPerson, factory.createPerson(LukasPerson).getIri().stringValue(), "Problem create Instance of Person Class");
        assertEquals(TomPet, factory.createPet(TomPet).getIri().stringValue(), "Problem create Instance of Pet Class");
    }

    @Test
    @Order(4)
    @DisplayName("4.Update instance when instance is not in model.")
    void testUpdateNonExistInstance() throws Exception {
        Human h = factory.createHuman(MartinHuman);
        factory.updateInstanceInModel(h);
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(5)
    @DisplayName("5.Remove instance when instance is not in model.")
    void testRemoveNonExistInstance() throws Exception {
        factory.removeMenFromModel(MartinHuman);
        assertNull(factory.getMenFromModel(MartinHuman),"Get men instance is not null.");
    }

    @Test
    @Order(6)
    @DisplayName("6.Simple test to add class instance to model")
    void testAddToModel() {
        Human h = factory.createHuman(MartinHuman);
        int startSize = model.size();
        factory.addToModel(h);

        // +2 because Human has property
        assertEquals(startSize +1 ,  model.size(),
                "Problem add instance to model.");

        Model resultModel = model.filter(Values.iri(MartinHuman), RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);
        assertEquals(1,  resultModel.size(),
                "Problem add instance to model 2.");

    }

    @Test
    @Order(7)
    @DisplayName("7.Simple test to get instance from model")
    void testGetFromModel() throws Exception {
        Human human = factory.getHumanFromModel(MartinHuman);
        assertNotNull(human,"Problem get instance from model.");
        assertEquals(human.getIri().stringValue(), MartinHuman ,
                "Problem get instance from model.");
    }

    @Test
    @Order(8)
    @DisplayName("8.Simple test to get instance from model")
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
    @DisplayName("9.Simple test to remove instance from model")
    void testRemoveFromModel() {
        factory.removeHumanFromModel(MartinHuman);
        Model resultModel = model.filter(Values.iri(MartinHuman),RDF.TYPE, Vocabulary.HUMAN_CLASS_IRI);

        assertEquals(0,  resultModel.size(),
                "Problem remove instance from model.");
    }

    @Test
    @Order(10)
    @DisplayName("10.Get all Mens instances from model")
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
    }

    @Test
    @Order(11)
    @DisplayName("11.Simple test equivalent Person class")
    void testSimpleEquivalentOfToModel() throws Exception {
        Person p = factory.createPerson(MatusPerson);
        factory.addToModel(p);
        Person person = factory.getPersonFromModel(MatusPerson);
        assertNotNull(person,"Problem get instance from model.");
        assertEquals(person.getIri().stringValue(), MatusPerson ,
                "Problem get instance from model.");
        assertTrue(person instanceof EquivalenceHumanPerson,"Person is not equivalence with human");
    }

    @Test
    @Order(12)
    @DisplayName("12.Simple test to subClassOf")
    void testSimpleSubClassOfToModel() throws Exception {
        Men m1 = factory.createMen(MarekMen);
        factory.addToModel(m1);
        Men m2 = factory.createMen(JurajMen);
        factory.addToModel(m2);
        Woman w = factory.createWoman(MonikaWoman);
        factory.addToModel(w);

        Collection<Men> mens = factory.getAllMenInstancesFromModel();
        Woman woman = factory.getWomanFromModel(MonikaWoman);
        assertNotNull(woman,"Woman is null.");
        assertEquals(2, mens.size(),
                "Problem get all mens instances from model.");
        for(Men m:mens){
            assertTrue(m instanceof HumanInt,"Men is not instance of Human");
            assertTrue(m instanceof EquivalenceHumanPerson,"Men is not instance of EQHuman");
        }
        assertTrue(woman instanceof HumanInt,"Woman is not instance of Human");

    }

    @Test
    @Order(13)
    @DisplayName("13.Simple test to class defined with equivalent restriction")
    void testClassDefinedEquivalentOfRestriction() throws Exception {
        Doggy d = factory.createDoggy(TomasDoggy);
        factory.addToModel(d);
        Doggy doggy = factory.getDoggyFromModel(TomasDoggy);
        assertNotNull(doggy,"Problem get instance from model.");
        assertEquals(TomasDoggy,doggy.getIri().stringValue(),
                "Problem get instance from model.");
        factory.removeDoggyFromModel(TomasDoggy);
        doggy = factory.getDoggyFromModel(TomasDoggy);
        assertNull(doggy,"Problem with remove instance from model.");

    }

    @Test
    @Order(14)
    @DisplayName("14.Simple test to class defined with equivalent union of")
    void testClassDefinedEquivalentOfUnion() throws Exception {
        Pet p = factory.createPet(TomPet);
        factory.addToModel(p);
        Pet pet = factory.getPetFromModel(TomPet);
        assertNotNull(pet,"Problem get instance from model.");
        assertEquals(TomPet,pet.getIri().stringValue(),
                "Problem get instance from model.");
        assertTrue(pet instanceof EquivalencePetUnionOfCatDog,"Pet is not instance of equivalence");
        factory.removePetFromModel(TomPet);
        pet = factory.getPetFromModel(TomPet);
        assertNull(pet,"Problem with remove instance from model.");
    }

    @Test
    @Order(15)
    @DisplayName("15. Simple test to class defined with equivalent of intersection")
    void testClassDefinedEquivalentOfIntersection() throws Exception {
        Mother m = factory.createMother(MonikaMother);
        factory.addToModel(m);
        Mother mother = factory.getMotherFromModel(MonikaMother);
        assertNotNull(mother,"Problem get instance from model.");
        assertEquals(MonikaMother,mother.getIri().stringValue(),
                "Problem get instance from model.");
        assertTrue(mother instanceof EquivalenceIntersectionOfParentWomanMother,"Mother is not instance of equivalence");
        factory.removeMotherFromModel(MonikaMother);
        mother = factory.getMotherFromModel(MonikaMother);
        assertNull(mother,"Problem with remove instance from model.");
    }

    @Test
    @Order(16)
    @DisplayName("16. Simple test to class defined with equivalent of complement")
    void testClassDefinedEquivalentOfComplement() throws Exception {
        ChildlessPerson ch = factory.createChildlessPerson(JurajChildlessPerson);
        factory.addToModel(ch);
        ChildlessPerson childless = factory.getChildlessPersonFromModel(JurajChildlessPerson);
        assertNotNull(childless,"Problem get instance from model.");
        assertEquals(JurajChildlessPerson,childless.getIri().stringValue(),
                "Problem get instance from model.");
        assertTrue(childless instanceof EquivalenceChildlessPersonComplementOfParent,"ChildlessPerson is not instance of equivalence");
        factory.removeChildlessPersonFromModel(JurajChildlessPerson);
        childless = factory.getChildlessPersonFromModel(JurajChildlessPerson);
        assertNull(childless,"Problem with remove instance from model.");
    }




    /**
     *  Properties test
     *
     */

    @Test
    @Order(30)
    @DisplayName("30.Simple test datatype property")
    void testDataTypeProperties() throws Exception {
        Human h = factory.createHuman(MartinHuman);
        h.setHasAge(35);
        h.addHasLuckyNumbers(42);
        h.addHasLuckyNumbers(55);
        factory.addToModel(h);

        Model resultModel = model.filter(Values.iri(MartinHuman),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");

        Human human = factory.getHumanFromModel(MartinHuman);
        assertNotNull(human,"Martin is not in Model ");
        assertEquals(35,  human.getHasAge(),
                "Has age properties is wrong.");
        assertEquals(2,  human.getHasLuckyNumbers().size(),
                "Has lucky numbers property is empty.");

        assertTrue(human.getHasLuckyNumbers().contains(42) &&human.getHasLuckyNumbers().contains(55),"Values in hasLuckyNumbers property are not correct." );
        factory.removeHumanFromModel(MartinHuman);
    }

    @Test
    @Order(31)
    @DisplayName("31.Simple test to add class with properties to model")
    void testSetProperties() {
        Cat c = factory.createCat(MnaukaCat);
        factory.addToModel(c);
        Human h = factory.createHuman(MartinHuman);
        h.setHasAge(35);
        h.addHasCat(c);
        factory.addToModel(h);

        Model resultModel = model.filter(Values.iri(MartinHuman),null, null);
        assertEquals(3,  resultModel.size(),
                "Problem add instance to model 2.");
    }

    @Test
    @Order(32)
    @DisplayName("32.Simple test to get class with properties from model")
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
    @Order(33)
    @DisplayName("33.Simple test to add subclass with properties of super class to model")
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
    @Order(34)
    @DisplayName("34.Simple test to get class with properties from superclass")
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
    @Order(35)
    @DisplayName("35.Simple test to add properties of equivalent class to model")
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
    @Order(36)
    @DisplayName("36.Simple test to get class with properties of equivalent class")
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
    @Order(37)
    @DisplayName("37.Simple test to set functional property")
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
    @Order(38)
    @DisplayName("38.Simple test to set inverse functional property")
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
    @Order(39)
    @DisplayName("39.Simple test to set inverse functional and functional property")
    void testInverseFunctionalAndFunctionalProperty() throws Exception {
        Woman w = factory.createWoman(JanaWoman);
        factory.addToModel(w);
        Men m = factory.createMen(FilipMen);
        m.setHasAge(38);
        m.setHasWife(w);
        factory.addToModel(m);
        Model resultModel2 = model.filter(null, Values.iri("http://www.ontocodemaker.org/Family#hasWife"), null);
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
    @Order(40)
    @DisplayName("40.Simple test to set equivalent property from model")
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
    @Order(41)
    @DisplayName("41.Simple test to get equivalent property from model")
    void testGetEquivalentProperty() {
        Men m = null;
        try {
            m = factory.getMenFromModel(RomanMen);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(m,"Men is null.");
        assertNotNull(m.getHasDog(),"Has dog properties is null.");
        assertEquals(1,m.getHasDogEq().size(),"Equivalent Has dog properties is not set.");

        assertEquals(HauDog,  m.getHasDogEq().get(0).getIri().stringValue(),
                "equivalent has dog properties is wrong.");
    }


    @Test
    @Order(42)
    @DisplayName("42.Simple test to update instance properties")
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
    @Order(43)
    @DisplayName("43.Simple test to subPropertyOf property. Has father")
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
    @Order(44)
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
    @Order(45)
    @DisplayName("45.Simple test to inverse property of property")
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
