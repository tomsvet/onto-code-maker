package unitTests;

import ontology.tool.generator.representations.*;
import ontology.tool.mapper.OntologyMapper;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ontology.tool.generator.OntologyGenerator.ENTITY_EQUIVALENCE_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapperTests extends ModelSetUp {

    BNode firstBnode = Values.bnode("1234");
    BNode secondBnode = Values.bnode("12345");
    BNode thirdBnode = Values.bnode("1234533");

   /*static IRI testOnt = Values.iri(ex, "testOntology.owl");
   static IRI hasAge = Values.iri(ex, "hasAge");
   static IRI hasAgeDatatype = Values.iri("http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
   static IRI hasDog = Values.iri(ex, "hasDog");
   static IRI classDog = Values.iri(ex, "Dog");*/

   /* @BeforeEach
    void init() throws FileNotFoundException {
        model = new TreeModel();
        ModelSetUp.setUp();

        model.add(testOnt,RDF.TYPE,OWL.ONTOLOGY);

        model.add(classDog, RDF.TYPE, OWL.CLASS);

        //datatype property
        model.add(hasAge,RDF.TYPE, OWL.DATATYPEPROPERTY);
        model.add(hasAge, RDFS.DOMAIN,classHuman);
        model.add(hasAge,RDFS.RANGE,hasAgeDatatype);

        //object property
        model.add(hasDog,RDF.TYPE, OWL.OBJECTPROPERTY);
        model.add(hasDog, RDFS.DOMAIN,classHuman);
        model.add(hasDog,RDFS.RANGE,classDog);

        String[] inputFiles = {"src/main/test/resources/inputs/family.owl"};
        OntologyParser ontoParser = new OntologyParser();
        model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
    }*/

    @BeforeAll
    public static void setUp() {
        //model reset
        /*model = new TreeModel();
        ModelSetUp.setUp();

        model.add(testOnt,RDF.TYPE,OWL.ONTOLOGY);

        model.add(classDog, RDF.TYPE, OWL.CLASS);

        //datatype property
        model.add(hasAge,RDF.TYPE, OWL.DATATYPEPROPERTY);
        model.add(hasAge, RDFS.DOMAIN,classHuman);
        model.add(hasAge,RDFS.RANGE,hasAgeDatatype);

        //object property
        model.add(hasDog,RDF.TYPE, OWL.OBJECTPROPERTY);
        model.add(hasDog, RDFS.DOMAIN,classHuman);
        model.add(hasDog,RDFS.RANGE,classDog);*/
        String filename = "src/test/resources/inputs/family.owl";
        Path file = Paths.get(filename);
        try {
            InputStream inputStream = Files.newInputStream(file);
            model = Rio.parse(inputStream, "", RDFFormat.RDFXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertyRepresentation findProperty(List<PropertyRepresentation> properties,IRI lookingFor){
        for(PropertyRepresentation property:properties){
            if(property.getValueIRI().equals(lookingFor)){
                return property;
            }
        }
        return null;
    }


    @Test
    @Order(1)
    @DisplayName("1.Simple test for get Ontology, should work")
    void testGetOWLOntologies() {
        OntologyMapper mapper = new OntologyMapper(model);
        List<OntologyRepresentation> ontologies = mapper.getOWLOntologies();
        assertEquals( ontologies.size(),1,
                "Number of ontologies is not correct ");
        assertEquals( ontologies.get(0).getValueIRI(),testOnt,
                "Ontology is not correct. Ontology is:" + ontologies.get(0).getStringIRI());
    }

    @Test
    @Order(2)
    @DisplayName("2. Simple test for mapping Ontology information")
    void testMapOntologyInformations() {

        OntologyMapper mapper = new OntologyMapper(model);
        List<OntologyRepresentation> ontologies = mapper.getOWLOntologies();
        assertEquals( ontologies.size(),1,
                "Number of ontologies is not correct ");
        //assertEquals(ontologies.get(0).getImports().size(),1,"Number of imports is not 1.");
        //assertEquals(ontologies.get(0).getImports().get(0),testOntImport.stringValue(),"Import is not correct.");
        assertEquals( ontologies.get(0).getPriorVersion(),testOntPrior.stringValue(), "Ontology prior is not correct. Prior is:" + ontologies.get(0).getPriorVersion());
        assertEquals(ontologies.get(0).getComments().size(),1,"Number of comments is not 1.");
        assertEquals(ontologies.get(0).getLabels().size(),1,"Number of labels is not 1.");
        assertEquals(ontologies.get(0).getLabels().get(0),"Family","Label value is not correct.");
    }


    @Test
    @Order(3)
    @DisplayName("3.Simple mapClasses ")
    void testGetClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        assertEquals(16,  mapper.getMappedClasses().size(),
                "Number of mapped class is different than expected.");
    }




    @Test
    @Order(4)
    @DisplayName("4.Simple test for mapClassHierarchy method, should work")
    void testMapClassHierarchy(){
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMen);
        try {
            mapper.mapClassHierarchy(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClassHuman = mapper.getMappedClasses().get(classHuman);

        assertEquals(1,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertEquals(0,testedClass.getSubClasses().size(),
                "Number of subclasses classes doesn't equal.");
        assertTrue(testedClassHuman.getSubClasses().contains(testedClass),"Person does not have set subclass");
        assertNotEquals(0,testedClassHuman.getSubClasses().size(),
                "Number of subclasses classes doesn't equal.");
    }

    @Test
    @Order(5)
    @DisplayName("5.Simple test for mapEquivalentClasses method, should work")
    void testMapEquivalentClasses(){
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classPerson);
        try {
            mapper.mapEquivalentClasses(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        List<Resource> allIRIs = mapper.getAllEquivalentResources(testedClass);
        assertTrue(allIRIs.contains(classHuman),"Equivalent class is not correct.");
        String firstName = ENTITY_EQUIVALENCE_PREFIX + classPerson.getLocalName() + classHuman.getLocalName();
        String secondName = ENTITY_EQUIVALENCE_PREFIX + classHuman.getLocalName() + classPerson.getLocalName();
        assertTrue(testedClass.getEquivalentClass().getName().equals(firstName) || testedClass.getEquivalentClass().getName().equals(secondName) ,
                "Name of equivalent class is not good. Name is: " + testedClass.getEquivalentClass().getName());

    }

    @Test
    @Order(6)
    @DisplayName("6.Test for mapEquivalentClasses method with 3 equivalent classes, should work")
    void testMapEquivalentClasses2() throws Exception {
        IRI classHuman2 = Values.iri(ex, "Human2");
        IRI classHuman3 = Values.iri(ex, "Human3");
        model.add(classHuman2,RDF.TYPE,OWL.CLASS);
        model.add(classHuman2,OWL.EQUIVALENTCLASS,classHuman);

        model.add(classHuman3,RDF.TYPE,OWL.CLASS);
        model.add(classPerson,OWL.EQUIVALENTCLASS,classHuman3);

        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        /*List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        for(ClassRepresentation mappedClass:mappedClasses){
            try {
                mapper.mapEquivalentClasses(mappedClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classPerson);
        mapper.mapEquivalentClasses(testedClass);

        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(4,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "ClasPerson: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass2 = mapper.getMappedClasses().get(classHuman);
        assertNotNull(testedClass2.getEquivalentClass());
        assertEquals(testedClass2.getEquivalentClass(),testedClass.getEquivalentClass(),
                "ClasHuman: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass2.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass4 = mapper.getMappedClasses().get(classHuman3);
        assertNotNull(testedClass4.getEquivalentClass());
        assertEquals(testedClass4.getEquivalentClass(),testedClass.getEquivalentClass(),
                "ClasHuman23: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass4.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman23: number of equivalent classes doesn't equal.");

        ClassRepresentation testedClass3 = mapper.getMappedClasses().get(classHuman2);
        assertNotNull(testedClass3.getEquivalentClass());
        assertEquals(testedClass.getEquivalentClass(),testedClass3.getEquivalentClass(),
                "ClasHuman2: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass3.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman2: number of equivalent classes doesn't equal.");
    }




    @Test
    @Order(7)
    @DisplayName("7.Simple test for mapComments method, should work")
    void testMapComments() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMen);
        mapper.mapComments(testedClass);

        assertEquals(1,testedClass.getComments().size(),
                "Method mapComments doesn't work.");
    }

    @Test
    @Order(8)
    @DisplayName("8.Simple test for mapLabels method, should work")
    void testMapLabels() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMen);
        mapper.mapLabels(testedClass);
        assertEquals(1,testedClass.getLabels().size(),
                "Method mapLabels doesn't work.");
    }

    @Test
    @Order(9)
    @DisplayName("9.Simple test for mapCreator method, should work")
    void testMapCreator() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMen);
        mapper.mapCreator(testedClass);
        assertFalse( testedClass.getCreator().isEmpty(),
                "Method mapCreator doesn't work.");

        assertEquals(testedClass.getCreator(),"Tomas","The creator does not equal.");
    }



    @Test
    @Order(10)
    @DisplayName("10.Simple test union of")
    void testMapUnionOfClasses(){
        OntologyMapper mapper = new OntologyMapper(model);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classPet);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation unionAbstractClass = abs.get();
        assertTrue(unionAbstractClass.isUnionOf(),"Abstract class is not unionOf");

        assertEquals(2,unionAbstractClass.getUnionOf().size(),"Number of union classes doesn't equal.");
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassCat = mapper.getMappedClasses().get(classCat);
        assertTrue(unionAbstractClass.getUnionOf().contains(testedClassDog),"Union abstract class does not contain Dog class.");
        assertTrue(unionAbstractClass.getSubClasses().contains(testedClassDog),"Union does not have set subclasses .");
        assertTrue(testedClassDog.getSuperClasses().contains(unionAbstractClass),"Union does not have set superclasses .");


        assertTrue(unionAbstractClass.getUnionOf().contains(testedClassCat),"Union abstract class does not contain Cat class.");
        assertTrue(unionAbstractClass.getSubClasses().contains(testedClassCat),"Union does not have set subclasses .");
        assertTrue(testedClassCat.getSuperClasses().contains(unionAbstractClass),"Union does not have set superclasses .");

    }

    @Test
    @Order(11)
    @DisplayName("11.Simple test map intersection of classes")
    void testMapIntersectionOfClass(){
        OntologyMapper mapper = new OntologyMapper(model);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMother);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation intersectionAbstractClass = abs.get();
        assertTrue(intersectionAbstractClass.isIntersectionOf(),"Abstract class is not intersectionOf");

        assertEquals(2,intersectionAbstractClass.getIntersectionOf().size(),"Number of intersection classes doesn't equal.");
        ClassRepresentation testedClassParent = mapper.getMappedClasses().get(classParent);
        ClassRepresentation testedClassWoman = mapper.getMappedClasses().get(classWoman);
        assertTrue(intersectionAbstractClass.getIntersectionOf().contains(testedClassParent),"Intersection abstract class does not contain Parent class.");
        assertTrue(intersectionAbstractClass.getSuperClasses().contains(testedClassParent),"Intersection does not have set subclasses .");
        assertTrue(testedClassParent.getSubClasses().contains(intersectionAbstractClass),"Intersection does not have set superclasses .");

        assertTrue(intersectionAbstractClass.getIntersectionOf().contains(testedClassWoman),"Intersection abstract class does not contain Woman class.");
        assertTrue(intersectionAbstractClass.getSuperClasses().contains(testedClassWoman),"Intersection does not have set subclasses .");
        assertTrue(testedClassWoman.getSubClasses().contains(intersectionAbstractClass),"Intersection does not have set superclasses .");


    }

    @Test
    @Order(12)
    @DisplayName("12.Simple test map complement of classes")
    void testMapComplementOfClass(){
        OntologyMapper mapper = new OntologyMapper(model);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classChildlessPerson);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation complementAbstractClass = abs.get();
        assertTrue(complementAbstractClass.isComplementOf(),"Abstract class is not complementOf");

        ClassRepresentation testedClassParent = mapper.getMappedClasses().get(classParent);
        assertEquals(testedClassParent,complementAbstractClass.getComplementOf(),"Complement classes doesn't equal.");
        //assertEquals(complementAbstractClass,complementAbstractClass.getComplementOf(),"Number of complement classes doesn't equal.");

       // assertTrue(testedClassParent.getSuperClasses().contains(intersectionAbstractClass),"Intersection does not have set superclasses .");

    }

    @Test
    @Order(13)
    @DisplayName("13.Simple test map disjoint with classes")
    void testMapDisjointWith(){
        OntologyMapper mapper = new OntologyMapper(model);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classFather);
        ClassRepresentation testedClassMother = mapper.getMappedClasses().get(classMother);
        assertNotNull(testedClass.getDisjointWith());
        assertEquals(1,testedClass.getDisjointWith().size(),
                "Number of disjoint classes doesn't equal.");

        assertEquals(testedClassMother,testedClass.getDisjointWith().get(0),
                "Disjoint class is not correct.");
    }

    @Test
    @Order(14)
    @DisplayName("14.Second test union of")
    void testMapUnionOfSameClasses(){
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        IRI classPet2 = Values.iri(ex, "Pet2");
        newModel.add(classPet2,RDF.TYPE, OWL.CLASS);
        newModel.add(thirdBnode,RDF.TYPE, OWL.CLASS);
        newModel.add(classPet2,OWL.EQUIVALENTCLASS, thirdBnode);
        newModel.add(thirdBnode,OWL.UNIONOF, firstBnode);
        newModel.add(firstBnode,RDF.FIRST,classDog);
        newModel.add(firstBnode,RDF.REST,secondBnode);
        newModel.add(secondBnode,RDF.FIRST,classCat);
        newModel.add(secondBnode,RDF.REST,RDF.NIL);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classPet2);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation unionAbstractClass = abs.get();
        assertTrue(unionAbstractClass.isUnionOf(),"Abstract class is not unionOf");

        assertEquals(2,unionAbstractClass.getUnionOf().size(),"Number of union classes doesn't equal.");
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassCat = mapper.getMappedClasses().get(classCat);
        assertTrue(unionAbstractClass.getUnionOf().contains(testedClassDog),"Union abstract class does not contain Dog class.");
        assertTrue(unionAbstractClass.getSubClasses().contains(testedClassDog),"Union does not have set subclasses .");
        assertEquals(1,testedClassDog.getSuperClasses().size(),"Union have set superclasses .");


        assertTrue(unionAbstractClass.getUnionOf().contains(testedClassCat),"Union abstract class does not contain Cat class.");
        assertTrue(unionAbstractClass.getSubClasses().contains(testedClassCat),"Union does not have set subclasses .");
        //assertFalse(testedClassCat.getSuperClasses().contains(unionAbstractClass),"Union does not have set superclasses .");
        assertEquals(1,testedClassCat.getSuperClasses().size(),"Union have set superclasses .");

    }

    @Test
    @Order(15)
    @DisplayName("15.Second test intersection of")
    void testMapIntersectionOfSameClasses(){
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        IRI classMother2 = Values.iri(ex, "Mother2");
        newModel.add(classMother2,RDF.TYPE, OWL.CLASS);
        newModel.add(thirdBnode,RDF.TYPE, OWL.CLASS);
        newModel.add(classMother2,OWL.EQUIVALENTCLASS, thirdBnode);
        newModel.add(thirdBnode,OWL.INTERSECTIONOF, firstBnode);
        newModel.add(firstBnode,RDF.FIRST,classParent);
        newModel.add(firstBnode,RDF.REST,secondBnode);
        newModel.add(secondBnode,RDF.FIRST,classWoman);
        newModel.add(secondBnode,RDF.REST,RDF.NIL);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMother2);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation intersectionAbstractClass = abs.get();
        assertTrue(intersectionAbstractClass.isIntersectionOf(),"Abstract class is not intersectionOf");

        assertEquals(2,intersectionAbstractClass.getIntersectionOf().size(),"Number of intersection classes doesn't equal.");
        ClassRepresentation testedClassParent = mapper.getMappedClasses().get(classParent);
        ClassRepresentation testedClassWoman = mapper.getMappedClasses().get(classWoman);
        assertTrue(intersectionAbstractClass.getIntersectionOf().contains(testedClassParent),"Intersection abstract class does not contain Parent class.");
        assertTrue(intersectionAbstractClass.getSuperClasses().contains(testedClassParent),"Intersection does not have set subclasses .");
        //assertFalse(testedClassParent.getSubClasses().contains(intersectionAbstractClass),"Intersection have set subclasses .");
        assertEquals(1,testedClassParent.getSubClasses().size(),"Intersection have set superclasses .");


        assertTrue(intersectionAbstractClass.getIntersectionOf().contains(testedClassWoman),"Intersection abstract class does not contain Woman class.");
        assertTrue(intersectionAbstractClass.getSuperClasses().contains(testedClassWoman),"Intersection does not have set subclasses .");
        //assertFalse(testedClassWoman.getSubClasses().contains(intersectionAbstractClass),"Intersection have set superclasses .");
        assertEquals(1,testedClassWoman.getSubClasses().size(),"Intersection have set superclasses .");

    }


    @Test
    @Order(16)
    @DisplayName("16.Test duplicate class names")
    void testDuplicateClassNames(){
        IRI classMother2 = Values.iri("http://www.example.org/Family#", "Mother");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classMother2,RDF.TYPE, OWL.CLASS);
        newModel.add(classMother2,OWL.EQUIVALENTCLASS, classMother);
        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassRepresentation testedClassMother = mapper.getMappedClasses().get(classMother);
        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMother2);
        assertNotNull(testedClass);
        assertEquals("Mother2",testedClass.getName(),
                "Duplicate name was changed.");
        assertEquals("Mother",testedClassMother.getName(),
                "Normal name was not changed.");

    }

    @Test
    @Order(17)
    @DisplayName("17.Test collection of subclasses")
    void testMapCollectionOfSubClasses(){
        //setup collection Of subclasses
        IRI classBoy = Values.iri(ex, "Boy");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBoy,RDF.TYPE, OWL.CLASS);
        newModel.add(classBoy,RDFS.SUBCLASSOF, firstBnode);
        newModel.add(firstBnode,RDF.FIRST,classChild);
        newModel.add(firstBnode,RDF.REST,secondBnode);
        newModel.add(secondBnode,RDF.FIRST,classMen);
        newModel.add(secondBnode,RDF.REST,RDF.NIL);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classBoy);
        ClassRepresentation testedClassMen = mapper.getMappedClasses().get(classMen);
        ClassRepresentation testedClassChild = mapper.getMappedClasses().get(classChild);

        assertEquals(2,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertTrue(testedClass.getSuperClasses().contains(testedClassMen),"Boy does not have set superclass");
        assertTrue(testedClass.getSuperClasses().contains(testedClassChild),"Boy does not have set superclass");

        assertTrue(testedClassMen.getSubClasses().contains(testedClass),"Men does not have set subclass");
        assertTrue(testedClassChild.getSubClasses().contains(testedClass),"Child does not have set subclass");

    }

    @Test
    @Order(18)
    @DisplayName("18.Test more subclasses")
    void testMapMoreSubClasses(){
        //setup collection Of subclasses
        IRI classBoy = Values.iri(ex, "Boy");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBoy,RDF.TYPE, OWL.CLASS);
        newModel.add(classBoy,RDFS.SUBCLASSOF, classChild);
        newModel.add(classBoy,RDFS.SUBCLASSOF, classMen);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classBoy);
        ClassRepresentation testedClassMen = mapper.getMappedClasses().get(classMen);
        ClassRepresentation testedClassChild = mapper.getMappedClasses().get(classChild);

        assertEquals(2,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertTrue(testedClass.getSuperClasses().contains(testedClassMen),"Boy does not have set superclass");
        assertTrue(testedClass.getSuperClasses().contains(testedClassChild),"Boy does not have set superclass");

        assertTrue(testedClassMen.getSubClasses().contains(testedClass),"Men does not have set subclass");
        assertTrue(testedClassChild.getSubClasses().contains(testedClass),"Child does not have set subclass");

    }


    @Test
    @Order(19)
    @DisplayName("19.Test collection of equivalent classes")
    void testMapCollectionOfEquivalentClasses(){
        //setup collection Of subclasses
        IRI classBulldog = Values.iri(ex, "Bulldog");
        IRI classLabrador = Values.iri(ex, "Labrador");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBulldog,RDF.TYPE, OWL.CLASS);
        newModel.add(classLabrador,RDF.TYPE, OWL.CLASS);

        newModel.add(classBulldog,OWL.EQUIVALENTCLASS, firstBnode);
        newModel.add(firstBnode,RDF.FIRST,classLabrador);
        newModel.add(firstBnode,RDF.REST,secondBnode);
        newModel.add(secondBnode,RDF.FIRST,classDog);
        newModel.add(secondBnode,RDF.REST,RDF.NIL);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClassBulldog = mapper.getMappedClasses().get(classBulldog);
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassLabrador = mapper.getMappedClasses().get(classLabrador);


        assertNotNull(testedClassBulldog.getEquivalentClass());
        assertNotNull(testedClassDog.getEquivalentClass());
        assertNotNull(testedClassLabrador.getEquivalentClass());
        assertEquals(3,testedClassBulldog.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");

        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassBulldog),"Equivalent class is not correct.");
        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassDog),"Equivalent class is not correct.");
        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassLabrador),"Equivalent class is not correct.");

    }

    @Test
    @Order(20)
    @DisplayName("20.Test more equivalent classes ")
    void testMapMoreEquivalentClasses(){
        //setup collection Of subclasses
        IRI classBulldog = Values.iri(ex, "Bulldog");
        IRI classLabrador = Values.iri(ex, "Labrador");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBulldog,RDF.TYPE, OWL.CLASS);
        newModel.add(classLabrador,RDF.TYPE, OWL.CLASS);

        newModel.add(classBulldog,OWL.EQUIVALENTCLASS, classLabrador);
        newModel.add(classBulldog,OWL.EQUIVALENTCLASS, classDog);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClassBulldog = mapper.getMappedClasses().get(classBulldog);
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassLabrador = mapper.getMappedClasses().get(classLabrador);


        assertNotNull(testedClassBulldog.getEquivalentClass());
        assertNotNull(testedClassDog.getEquivalentClass());
        assertNotNull(testedClassLabrador.getEquivalentClass());
        assertEquals(3,testedClassBulldog.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");

        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassBulldog),"Equivalent class is not correct.");
        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassDog),"Equivalent class is not correct.");
        assertTrue(testedClassBulldog.getEquivalentClass().getEquivalentClasses().contains(testedClassLabrador),"Equivalent class is not correct.");

    }

    @Test
    @Order(21)
    @DisplayName("21.Test collection of disjoint with classes")
    void testMapCollectionOfDisjointWithClasses(){
        //setup collection Of subclasses
        IRI classBulldog = Values.iri(ex, "Bulldog");
        IRI classLabrador = Values.iri(ex, "Labrador");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBulldog,RDF.TYPE, OWL.CLASS);
        newModel.add(classLabrador,RDF.TYPE, OWL.CLASS);

        newModel.add(classBulldog,OWL.DISJOINTWITH, firstBnode);
        newModel.add(firstBnode,RDF.FIRST,classLabrador);
        newModel.add(firstBnode,RDF.REST,secondBnode);
        newModel.add(secondBnode,RDF.FIRST,classDog);
        newModel.add(secondBnode,RDF.REST,RDF.NIL);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClassBulldog = mapper.getMappedClasses().get(classBulldog);
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassLabrador = mapper.getMappedClasses().get(classLabrador);

        assertEquals(2,testedClassBulldog.getDisjointWith().size(),"Disjoint was not set correct");
        assertTrue(testedClassBulldog.getDisjointWith().contains(testedClassDog),"Class is not disjoint with classDog");
        assertTrue(testedClassBulldog.getDisjointWith().contains(testedClassLabrador),"Class is not disjoint with classLabrador");

    }

    @Test
    @Order(22)
    @DisplayName("22.Test more disjoint with classes")
    void testMapMoreDisjointWithClasses(){
        //setup collection Of subclasses
        IRI classBulldog = Values.iri(ex, "Bulldog");
        IRI classLabrador = Values.iri(ex, "Labrador");
        TreeModel newModel = new TreeModel();
        newModel.addAll(model);
        newModel.add(classBulldog,RDF.TYPE, OWL.CLASS);
        newModel.add(classLabrador,RDF.TYPE, OWL.CLASS);

        newModel.add(classBulldog,OWL.DISJOINTWITH, classLabrador);
        newModel.add(classBulldog,OWL.DISJOINTWITH, classDog);

        OntologyMapper mapper = new OntologyMapper(newModel);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClassBulldog = mapper.getMappedClasses().get(classBulldog);
        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classDog);
        ClassRepresentation testedClassLabrador = mapper.getMappedClasses().get(classLabrador);

        assertEquals(2,testedClassBulldog.getDisjointWith().size(),"Disjoint was not set correct");
        assertTrue(testedClassBulldog.getDisjointWith().contains(testedClassDog),"Class is not disjoint with classDog");
        assertTrue(testedClassBulldog.getDisjointWith().contains(testedClassLabrador),"Class is not disjoint with classLabrador");

    }

    @Test
    @Order(23)
    @DisplayName("23.Simple test map Restriction Property")
    void testMapRestriction(){
        OntologyMapper mapper = new OntologyMapper(model);
        try {
            mapper.mapping();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classDoggy);
        ClassRepresentation testedClassHuman = mapper.getMappedClasses().get(classHuman);
        PropertyRepresentation hasDogProperty = mapper.getMappedProperties().get(hasDog);


       /* assertNotNull(testedClass.getEquivalentClass());
        assertEquals(2,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        Optional<ClassRepresentation> abs = testedClass.getEquivalentClass().getEquivalentClasses().stream().filter(c -> c.getClassType().equals(DefaultClassRepresentation.CLASS_TYPE.ABSTRACT)).findFirst();
        assertTrue(abs.isPresent());
        ClassRepresentation restrictionAbstractClass = abs.get();*/

        //assertTrue(testedClass.getEquivalentClass().getEquivalentClasses().contains());
        //assertEquals(1,testedClass.getEquivalentClass().getRestrictions().size(),"Restriction was not set correct");
        RestrictionRepresentation restriction = testedClass.getRestrictions().get(0);
        assertEquals(hasDog,restriction.getOnProperty(),"Restriction onProperty was not set correct");
        assertEquals(RestrictionRepresentation.RESTRICTION_IN_TYPE.EQUIVALENT,restriction.getRestrictionIn(),"Restriction in was not set correct");
        assertEquals(OWL.SOMEVALUESFROM.stringValue(),restriction.getType(),"Restriction type was not set correct");
        assertEquals(classHuman.stringValue(),restriction.getValue(),"Restriction in was not set correct");


    }


    @Test
    @Order(30)
    @DisplayName("30.Simple test map Datatype Property")
    void testMapDataTypeProperty(){
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapDataTypeProperties();

        assertEquals(3,mapper.getCollectionOfMappedProperties().size(),
                "Datatype property was not mapped.");

        PropertyRepresentation property = mapper.getMappedProperties().get(hasLuckyNumbers);
        assertEquals(property.getType(), PropertyRepresentation.PROPERTY_TYPE.DATATYPE,"Property has wrong type.");
        assertEquals(property.getClassName(),"Human","Property has wrong name.");
        assertEquals(property.getRangeResource(),hasAgeDatatype,"Property has wrong range value.");
        assertFalse(property.isFunctional(),"Property is functional.");

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classHuman);
        PropertyRepresentation result = findProperty(testedClass.getProperties(), hasLuckyNumbers);
        assertNotNull(result,"Tested class does not have  hasLuckyNumbers property.");
    }

    @Test
    @Order(31)
    @DisplayName("31.Test map Equivalent Datatype Property")
    void testMapEquivalentDataTypeProperty() throws Exception {
        IRI age = Values.iri(ex,"age");
        model.add(age,RDF.TYPE,OWL.DATATYPEPROPERTY);
        model.add(age,OWL.EQUIVALENTPROPERTY,hasAge);
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapDataTypeProperties();
        mapper.mapPropertyRelationShips();

        assertEquals(3,mapper.getMappedProperties().size(),
                "2 datatype properties were not mapped.");

        PropertyRepresentation property = mapper.getMappedProperties().get(hasAge);
        PropertyRepresentation eqProperty = mapper.getMappedProperties().get(age);
        assertNotNull(eqProperty,"Expected property was not mapped.");
        assertEquals(eqProperty.getType(), PropertyRepresentation.PROPERTY_TYPE.DATATYPE,"Property has wrong type.");
        assertEquals(eqProperty.getClassName(),"Human","Property has wrong name.");
        assertEquals(eqProperty.getRangeResource(),hasAgeDatatype,"Equivalent property has range value.");
        assertFalse(eqProperty.isFunctional(),"Equivalent property is functional.");
        assertEquals(eqProperty.getIsEquivalentTo().getResourceValue(),property.getResourceValue(),"Wrong equivalent to value.");

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classHuman);
        PropertyRepresentation result = findProperty(testedClass.getProperties(), age);
        assertNotNull(result,"Tested class does not have  age property.");
    }

    @Test
    @Order(32)
    @DisplayName("32.Simple test map Object Property")
    void testMapObjectProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();

        assertEquals(9,mapper.getCollectionOfMappedProperties().size(),
                "Object property was not mapped.");

        PropertyRepresentation property = mapper.getMappedProperties().get(hasCat);
        assertEquals(property.getType(), PropertyRepresentation.PROPERTY_TYPE.OBJECT,"Property has wrong type.");
        assertEquals(property.getClassName(),"Human","Property has wrong class name.");
        assertEquals(property.getRangeResource(),classCat,"Property has wrong range value.");
        assertEquals(property.getRangeClass().getResourceValue(),classCat,"Range class doesn't exist.");
        assertFalse(property.isFunctional(),"Property is functional.");

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classHuman);
        PropertyRepresentation result = findProperty(testedClass.getProperties(), hasCat);
        assertNotNull(result,"Tested class does not have  hasDog property.");

    }

    @Test
    @Order(33)
    @DisplayName("33.Test map Equivalent Object Property")
    void testMapEquivalentObjectProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();
        mapper.mapPropertyRelationShips();

        PropertyRepresentation property = mapper.getMappedProperties().get(hasDog);
        PropertyRepresentation eqProperty = mapper.getMappedProperties().get(hasDogEq);
        assertNotNull(eqProperty,"Expected property was not mapped.");
        assertEquals(eqProperty.getType(), PropertyRepresentation.PROPERTY_TYPE.OBJECT,"Property has wrong type.");
        assertEquals(eqProperty.getClassName(),"Human","Property has wrong name.");
        assertEquals(eqProperty.getRangeResource(),classDog,"Equivalent property has range value.");
        assertFalse(eqProperty.isFunctional(),"Equivalent property is functional.");
        assertEquals(eqProperty.getIsEquivalentTo().getResourceValue(),property.getResourceValue(),"Wrong equivalent to value.");

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classHuman);
        PropertyRepresentation result = findProperty(testedClass.getProperties(), hasDogEq);
        assertNotNull(result,"Tested class does not have  hasDogEq property.");

    }

    @Test
    @Order(34)
    @DisplayName("34.Simple test map Functional DatatypeProperty")
    void testMapFunctionalDataTypeProperty() throws Exception {

        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapDataTypeProperties();

        PropertyRepresentation datatypeProp = mapper.getMappedProperties().get(hasAge);
        assertNotNull(datatypeProp,"Property doesn't have correct value.");
        assertEquals(datatypeProp.getRangeResource(),hasAgeDatatype,"Property has wrong range value.");
        assertTrue(datatypeProp.isFunctional(),"Functional datatype property is not set.");

    }

    @Test
    @Order(35)
    @DisplayName("35.Simple test map Functional ObjectProperty")
    void testMapFunctionalObjectProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();

        PropertyRepresentation objProp = mapper.getMappedProperties().get(hasDog);
        assertNotNull(objProp,"Tested class doesn't have correct property.");
        assertEquals(objProp.getRangeResource(),classDog,"Property has wrong range value.");
        assertTrue(objProp.isFunctional(),"Functional property is not set.");
    }

    @Test
    @Order(36)
    @DisplayName("36.Simple test map Inverse Property")
    void testMapInverseProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();
        mapper.mapPropertyRelationShips();

        PropertyRepresentation inverseProp = mapper.getMappedProperties().get(hasHusband);
        PropertyRepresentation property = mapper.getMappedProperties().get(hasWife);

        assertNotNull(inverseProp,"Property was not mapped.");
        assertEquals(inverseProp.getType(), PropertyRepresentation.PROPERTY_TYPE.OBJECT,"Inverse property has wrong type.");
        assertEquals(inverseProp.getRangeResource(),classMen,"Property has wrong range value.");
        assertEquals(inverseProp.getClassName(),"Woman","Property has wrong name.");
        assertFalse(inverseProp.isFunctional(),"Functional property is set.");
        assertEquals(property.getInverseTo().get(0).getResourceValue(),hasHusband,"InverseTo value is not set");
        assertEquals(inverseProp.getInverseOf().getResourceValue(),hasWife,"InverseOf value is not set");

        ClassRepresentation testedClassDog = mapper.getMappedClasses().get(classWoman);
        PropertyRepresentation result = findProperty(testedClassDog.getProperties(), hasHusband);
        assertNotNull(result,"Tested class does not have  hasHusband property.");
    }

    @Test
    @Order(37)
    @DisplayName("37.Simple test map Inverse Functional Property")
    void testMapInverseFunctionalProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();
        mapper.mapPropertyRelationShips();

        PropertyRepresentation property = mapper.getMappedProperties().get(hasWife);
        assertNotNull(property,"Tested class doesn't have property hasWife");

        IRI menProp = Values.iri(hasWife.getNamespace() + "men");
        PropertyRepresentation inverseFuncProp = mapper.getMappedProperties().get(menProp);

        assertNotNull(inverseFuncProp,"Inverse functional property doesn't exist.");
        assertEquals(inverseFuncProp.getType(), PropertyRepresentation.PROPERTY_TYPE.OBJECT,"Inverse property has wrong type.");
        assertEquals(inverseFuncProp.getRangeResource(),classMen,"Property has wrong range value.");
        assertEquals(inverseFuncProp.getClassName(),classWoman.getLocalName(),"Property has wrong name.");
        assertTrue(inverseFuncProp.isFunctional(),"Inverse functional property is not set.");
        assertEquals(property.getInverseFunctionalTo().getResourceValue(),menProp,"InverseFunctionalTo value is not set");
        assertEquals(inverseFuncProp.getInverseFunctionalOf().getResourceValue(),hasWife,"InverseFunctionalOf value is not set");

        ClassRepresentation testedClassWoman = mapper.getMappedClasses().get(classWoman);
        PropertyRepresentation result = findProperty(testedClassWoman.getProperties(), menProp);
        assertNotNull(result,"Tested class does not have  menProp property.");
    }

    @Test
    @Order(38)
    @DisplayName("38. Simple test map SubObjectProperty")
    void testMapSubObjectProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapObjectProperties();
        mapper.mapPropertyRelationShips();

        PropertyRepresentation property = mapper.getMappedProperties().get(hasWife);
        PropertyRepresentation subProp = mapper.getMappedProperties().get(hasSpouse);

        assertNotNull(subProp,"Mapped sub property doesn't exist.");
        assertEquals(subProp.getType(), PropertyRepresentation.PROPERTY_TYPE.OBJECT,"Inverse property has wrong type.");
        assertEquals(subProp.getRangeResource(),classWoman,"Property has wrong range value.");
        assertEquals(subProp.getClassName(),classMen.getLocalName(),"Property has wrong name.");
        assertFalse(subProp.isFunctional(),"Inverse functional property is not set.");
        assertEquals(subProp.getSuperProperties().size(),1,
                "Subproperty doesn't have super property.");
        assertEquals(subProp.getSuperProperties().get(0).getResourceValue(),hasWife,
                "Subproperty has wrong super property value.");
        assertEquals(property.getSubProperties().size(),1,
                "Property doesn't have sub property.");
        assertEquals(property.getSubProperties().get(0).getResourceValue(),hasSpouse,
                "Property has wrong subproperty value.");

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classMen);
        PropertyRepresentation result = findProperty(testedClass.getProperties(), hasSpouse);
        assertNotNull(result,"Tested class does not have  hasSpouse property.");
    }

    @Test
    @Order(39)
    @DisplayName("39. Simple test map SubDataTypeProperty")
    void testMapSubDataTypeProperty() throws Exception {

        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        mapper.mapDataTypeProperties();
        mapper.mapPropertyRelationShips();

        PropertyRepresentation property = mapper.getMappedProperties().get(hasAge);
        PropertyRepresentation subProp = mapper.getMappedProperties().get(age);

        assertNotNull(subProp,"Mapped sub property doesn't exist.");
        assertEquals(subProp.getClassName(),classHuman.getLocalName(),"Property has wrong domain value.");
        assertEquals(subProp.getType(), PropertyRepresentation.PROPERTY_TYPE.DATATYPE,"Inverse property has wrong type.");
        assertEquals(subProp.getRangeResource(),hasAgeDatatype,"Property has wrong range value.");
        assertFalse(subProp.isFunctional(),"Inverse functional property is not set.");
        assertEquals(subProp.getSuperProperties().size(),1,
                "Subproperty doesn't have super property.");
        assertEquals(subProp.getSuperProperties().get(0).getResourceValue(),hasAge,
                "Subproperty has wrong super property value.");
        assertEquals(property.getSubProperties().size(),1,
                "Property doesn't have sub property.");
        assertEquals(property.getSubProperties().get(0).getResourceValue(),age,
                "Property has wrong subproperty value.");
    }

    @Test
    @Order(40)
    @DisplayName("40.Simple test map Property to Equivalent Classes")
    void testMapPropertyToEquivalentClass() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapping();

        ClassRepresentation testedClass = mapper.getMappedClasses().get(classPerson);
        PropertyRepresentation prop = findProperty(testedClass.getProperties(),hasAge);

        assertEquals(prop.getValueIRI(),hasAge,"Tested class has wrong property.");
        assertEquals(prop.getRangeResource(),hasAgeDatatype,"Tested class has range value.");
        assertTrue(prop.isFunctional(),"Property doesn't have just one value.");

        prop = findProperty(testedClass.getProperties(),hasDog);

        assertEquals(prop.getValueIRI(),hasDog,"Tested class has wrong property.");
        assertEquals(prop.getRangeResource(),classDog,"Tested class has range value.");
        assertTrue(prop.isFunctional(),"Property doesn't have just one value.");
    }




    //todo specialitky ako ekvivalencia s subclasss atd

}

