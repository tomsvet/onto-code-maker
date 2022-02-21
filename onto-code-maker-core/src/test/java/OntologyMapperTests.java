import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.mapper.OntologyMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OntologyMapperTests {
    String ex = "http://example.org/";
    Model model = new TreeModel();
    IRI classPerson = Values.iri(ex, "Person");
    IRI classMen = Values.iri(ex, "Men");
    IRI classWoman = Values.iri(ex, "Woman");
    IRI hasAgeProperty = Values.iri(ex,"hasAge");

    @BeforeEach
    void setUp() {

        model.add(classPerson, RDF.TYPE, RDFS.CLASS);
        model.add(classMen, RDF.TYPE, OWL.CLASS);
        model.add(classWoman, RDF.TYPE, OWL.CLASS);

        model.add(classMen, DC.CREATOR,Values.literal("Tomas"));
        model.add(classMen, RDFS.LABEL,Values.literal("Men"));
        model.add(classMen, RDFS.COMMENT,Values.literal("This is men"));
        model.add(classMen,RDFS.SUBCLASSOF,classPerson);
        model.add(classWoman,RDFS.SUBCLASSOF,classPerson);
        //model.add(hasAgeProperty,RDF.TYPE,OWL.DATATYPEPROPERTY);
    }

    @Test
    @Order(1)
    @DisplayName("Simple test get all subjects from model")
    void testGetAllSubjects(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<Resource> subjects = mapper.getAllSubjects(RDF.TYPE, OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(Resource subject:subjects){
            assertTrue(subject.isIRI());
            IRI subjectIRI = (IRI)subject;
            assertTrue(subjectIRI.getLocalName().equals("Men")||subjectIRI.getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(2)
    @DisplayName("Simple test get all IRI subjects from model")
    void testGetAllIRISubjects(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<IRI> subjects = mapper.getAllIRISubjects(RDF.TYPE, OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(IRI subject:subjects){
            assertTrue(subject.getLocalName().equals("Men")||subject.getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(3)
    @DisplayName("Simple test get all IRI subjects from model (predicate list)")
    void testGetAllIRISubjectsPredicateList(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<IRI> subjects = mapper.getAllIRISubjects(Arrays.asList(RDF.TYPE),OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(IRI subject:subjects){
            assertTrue(subject.getLocalName().equals("Men")||subject.getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("Simple test get all objects")
    void testGetAllObjects(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<Value> objects = mapper.getAllObjects(RDFS.LABEL, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected subjects and result.");
        Value object = objects.iterator().next();
        assertTrue(object.isLiteral());
        Literal subjectLiteral = (Literal)object;
        assertEquals(subjectLiteral.stringValue(),"Men","Literals are not equal.");
    }

    @Test
    @Order(5)
    @DisplayName("Simple test get all IRI objects from model")
    void testGetAllIRIObjects(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<IRI> objects = mapper.getAllIRIObjects(RDFS.SUBCLASSOF, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected objects and result.");
        IRI object = objects.iterator().next();
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());
    }

    @Test
    @Order(6)
    @DisplayName("Simple test get all Literal objects from model")
    void testGetAllLiteralObjects(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<Literal> objects = mapper.getAllLiteralObjects(RDFS.SUBCLASSOF, classMen);
        assertEquals(0, objects.size(),
                "Different size of expected objects and result.");
        objects = mapper.getAllLiteralObjects( RDFS.COMMENT, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected objects and result.");
        Literal object = objects.iterator().next();
        assertEquals(object.stringValue(),"This is men","Literals are not equal.");
    }

    @Test
    @Order(7)
    @DisplayName("Simple test get all Literal objects from model with list of predicates")
    void testGetAllLiteralObjectsPredicateList(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<Literal> objects = mapper.getAllLiteralObjects(Arrays.asList(RDFS.COMMENT,RDFS.LABEL),classMen);
        assertEquals(2, objects.size(),
                "Different size of expected objects and result.");
    }

    @Test
    @Order(8)
    @DisplayName("Simple test get all IRI objects from model with list of predicates")
    void testGetAllIRIObjectsPredicateList(){
        OntologyMapper mapper = new OntologyMapper(model);
        Set<IRI> objects = mapper.getAllIRIObjects(Arrays.asList(RDFS.SUBCLASSOF,RDF.TYPE),classMen);
        assertEquals(2, objects.size(),
                "Different size of expected objects and result.");
        for(IRI object:objects){
            assertTrue(object.getLocalName().equals("Person")||object.getLocalName().equals("Class"));
        }
    }

    @Test
    @Order(9)
    @DisplayName("Simple test get first IRI object from model with list of predicates")
    void testGetFirstIRIObject(){
        OntologyMapper mapper = new OntologyMapper(model);
        IRI object = mapper.getFirstIRIObject(RDFS.SUBCLASSOF, classMen);
        assertNotNull(object);
        assertTrue(object.equals(classPerson),"Objects are not equals. Object is: " + object.getLocalName());

    }

    @Test
    @Order(10)
    @DisplayName("Simple test get first Literal object from model with list of predicates")
    void testGetFirstLiteralObject(){
        OntologyMapper mapper = new OntologyMapper(model);
        Literal object = mapper.getFirstLiteralObject(RDFS.LABEL, classMen);
        assertNotNull(object);
        assertEquals(object.stringValue(),"Men","Literals are not equal.");
    }

    @Test
    @Order(20)
    @DisplayName("Simple getClasses should work")
    void testGetClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        assertEquals(3,  mapper.getClasses().size(),
                "Method getClasses doesn't work.");
    }

    @Test
    @Order(21)
    @DisplayName("Simple getClasses should work")
    void testMapClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        assertEquals(3, mappedClasses.size(),
                "Method getClasses doesn't work.");
    }

    private ClassRepresentation getTestedClass(OntologyMapper mapper,IRI classIRI){
        mapper.mapClasses();
        ClassRepresentation testedClass= null;
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        for(ClassRepresentation cl: mappedClasses){
            if(cl.getValueIRI().equals(classIRI)){
                testedClass = cl;
            }
        }
        assertNotNull(testedClass);
        return testedClass;
    }

    @Test
    @Order(22)
    @DisplayName("Simple test for mapComments method, should work")
    void testMapComments() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = getTestedClass(mapper,classMen);
        mapper.mapComments(testedClass);
        assertEquals(1,testedClass.getComments().size(),
                "Method mapComments doesn't work.");
    }

    @Test
    @Order(23)
    @DisplayName("Simple test for mapLabels method, should work")
    void testMapLabels() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = getTestedClass(mapper,classMen);
        mapper.mapLabels(testedClass);
        assertEquals(1,testedClass.getLabels().size(),
                "Method mapLabels doesn't work.");
    }

    @Test
    @Order(24)
    @DisplayName("Simple test for mapCreator method, should work")
    void testMapCreator() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = getTestedClass(mapper,classMen);
        mapper.mapCreator(testedClass);
        assertFalse( testedClass.getCreator().isEmpty(),
                "Method mapCreator doesn't work.");

        assertEquals(testedClass.getCreator(),"Tomas","The creator does not equal.");
    }

    @Test
    @Order(25)
    @DisplayName("Simple test for mapClassHierarchy method, should work")
    void testMapClassHierarchy(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = getTestedClass(mapper,classMen);
        mapper.mapClassHierarchy(testedClass);
        assertEquals(1,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertEquals(0,testedClass.getSubClasses().size(),
                "Number of subclasses classes doesn't equal.");
    }

    @Test
    @Order(26)
    @Disabled
    @DisplayName("Simple test for mapEquivalentClasses method, should work")
    void testMapEquivalentClasses(){

    }

    @Test
    @Order(27)
    @Disabled
    @DisplayName("Simple test for mapProperties method, should work")
    void testMapProperties(){

    }

}
