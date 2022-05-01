package unitTests;

import ontology.tool.mapper.ModelManager;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModelManagerTests extends ModelSetUp {
    static BNode firstBnode = Values.bnode("1234");
    static BNode secondBnode = Values.bnode("12345");

    @BeforeAll
    public static void setUp() {
        model = new TreeModel();
        ModelSetUp.setUp();

        model.add(classMother, RDF.TYPE, RDFS.CLASS);
        model.add(classParent, RDF.TYPE, RDFS.CLASS);
        model.add(classMother,OWL.INTERSECTIONOF, firstBnode);
        model.add(firstBnode,RDF.FIRST,classWoman);
        model.add(firstBnode,RDF.REST,secondBnode);
        model.add(secondBnode,RDF.FIRST,classParent);
        model.add(secondBnode,RDF.REST,RDF.NIL);
    }

    @Test
    @Order(1)
    @DisplayName("1.Simple test get all subjects from model")
    void testGetAllSubjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<Resource> subjects = modelManager.getAllSubjects(RDF.TYPE, OWL.CLASS);
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
    @DisplayName("2.Simple test get all subjects from model (predicate list)")
    void testGetAllSubjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<Resource> subjects = modelManager.getAllSubjects(Arrays.asList(RDFS.SUBCLASSOF,RDF.TYPE),OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(Resource subject:subjects){
            assertTrue(subject.isIRI(),"SUbject is not IRI");
            assertTrue(((IRI)subject).getLocalName().equals("Men")||((IRI)subject).getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(3)
    @DisplayName("3.Simple test get all IRI subjects from model")
    void testGetAllIRISubjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> subjects = modelManager.getAllIRISubjects(RDF.TYPE, OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(IRI subject:subjects){
            assertTrue(subject.getLocalName().equals("Men")||subject.getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(4)
    @DisplayName("4.Simple test get all IRI subjects from model (predicate list)")
    void testGetAllIRISubjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> subjects = modelManager.getAllIRISubjects(Arrays.asList(RDFS.SUBCLASSOF,RDF.TYPE),OWL.CLASS);
        assertEquals(2, subjects.size(),
                "Different size of expected subjects and result.");
        for(IRI subject:subjects){
            assertTrue(subject.getLocalName().equals("Men")||subject.getLocalName().equals("Woman"));
        }
    }

    @Test
    @Order(5)
    @DisplayName("5.Simple test get all objects")
    void testGetAllObjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<Value> objects = modelManager.getAllObjects(RDFS.LABEL, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected subjects and result.");
        Value object = objects.iterator().next();
        assertTrue(object.isLiteral());
        Literal subjectLiteral = (Literal)object;
        assertEquals(subjectLiteral.stringValue(),"Men","Literals are not equal.");
    }

    @Test
    @Order(6)
    @DisplayName("6. Simple test get all objects")
    void testGetAllObjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<Value> objects = modelManager.getAllObjects(Arrays.asList(RDFS.LABEL,RDFS.COMMENT), classMen);
        assertEquals(2, objects.size(),
                "Different size of expected subjects and result.");
        Value object = objects.iterator().next();
        assertTrue(object.isLiteral());
        Literal subjectLiteral = (Literal)object;
        assertTrue(subjectLiteral.stringValue().equals("Men") ||subjectLiteral.stringValue().equals("This is men") ,"Literals are not equal.");
    }

    @Test
    @Order(7)
    @DisplayName("7.Simple test get all IRI objects from model")
    void testGetAllIRIObjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> objects = modelManager.getAllIRIObjects(RDFS.SUBCLASSOF, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected objects and result.");
        IRI object = objects.iterator().next();
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());
    }

    @Test
    @Order(8)
    @DisplayName("8.Simple test get all Literal objects from model")
    void testGetAllLiteralObjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<Literal> objects = modelManager.getAllLiteralObjects(RDFS.SUBCLASSOF, classMen);
        assertEquals(0, objects.size(),
                "Different size of expected objects and result.");
        objects = modelManager.getAllLiteralObjects( RDFS.COMMENT, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected objects and result.");
        Literal object = objects.iterator().next();
        assertEquals(object.stringValue(),"This is men","Literals are not equal.");
    }

    @Test
    @Order(9)
    @DisplayName("9.Simple test get all Literal objects from model with list of predicates")
    void testGetAllLiteralObjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<Literal> objects = modelManager.getAllLiteralObjects(Arrays.asList(RDFS.COMMENT,RDFS.LABEL),classMen);
        assertEquals(2, objects.size(),
                "Different size of expected objects and result.");
    }

    @Test
    @Order(10)
    @DisplayName("10.Simple test get all IRI objects from model with list of predicates")
    void testGetAllIRIObjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> objects = modelManager.getAllIRIObjects(Arrays.asList(RDFS.SUBCLASSOF,RDF.TYPE),classMen);
        assertEquals(2, objects.size(),
                "Different size of expected objects and result.");
        for(IRI object:objects){
            assertTrue(object.getLocalName().equals("Person")||object.getLocalName().equals("Class"));
        }
    }

    @Test
    @Order(11)
    @DisplayName("11.Simple test get first IRI object from model")
    void testGetFirstIRIObject(){
        ModelManager modelManager = new ModelManager(model);
        IRI object = modelManager.getFirstIRIObject(RDFS.SUBCLASSOF, classMen);
        assertNotNull(object);
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());

    }

    @Test
    @Order(12)
    @DisplayName("12.Simple test get first Literal object from model")
    void testGetFirstLiteralObject(){
        ModelManager modelManager = new ModelManager(model);
        Literal object = modelManager.getFirstLiteralObject(RDFS.LABEL, classMen);
        assertNotNull(object);
        assertEquals(object.stringValue(),"Men","Literals are not equal.");
    }

    @Test
    @Order(13)
    @DisplayName("13.Simple test get first Resource object from model")
    void testGetFirstResourceObject(){
        ModelManager modelManager = new ModelManager(model);
        Resource object = modelManager.getFirstResource(RDFS.SUBCLASSOF, classMen);
        assertNotNull(object,"Object does not exist");
        assertTrue(object.isIRI(),"Object is not IRI");
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + ((IRI)object).getLocalName());
    }


    @Test
    @Order(14)
    @DisplayName("14.Simple test if statement with IRI exist")
    void testExistStatementWithIRI(){
        ModelManager modelManager = new ModelManager(model);
        assertTrue( modelManager.existStatementWithIRI(classHuman,RDF.TYPE,RDFS.CLASS),"Human RDFS class is not found.");
        assertFalse( modelManager.existStatementWithIRI(classHuman,RDF.TYPE,OWL.CLASS),"Human OWL class is found.");
    }

    @Test
    @Order(15)
    @DisplayName("15.Simple test get First IRI subject")
    void getFirstIRISubject(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getFirstIRISubject(OWL.EQUIVALENTCLASS, classHuman);
        assertNotNull(subject);
        assertEquals(subject,classPerson,"The correct subject is not found. The subject is: " + subject.getLocalName());

        IRI subject2 = modelManager.getFirstIRISubject(OWL.EQUIVALENTCLASS, classMen);
        assertNull(subject2,"The subject is not null.");
    }


    @Test
    @Order(16)
    @DisplayName("16.Simple test get First IRI subject from predicate list")
    void getFirstIRISubjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getFirstIRISubject(Arrays.asList(OWL.EQUIVALENTCLASS,RDFS.SUBCLASSOF), classPerson);
        assertNotNull(subject);
        assertEquals(subject,classMen,"The correct subject is not found. The subject is: " + subject.getLocalName());

        IRI subject2 = modelManager.getFirstIRISubject(Arrays.asList(RDF.TYPE,RDFS.SUBCLASSOF), classHuman);
        assertNull(subject2,"The subject is not null.");
    }

    @Test
    @Order(17)
    @DisplayName("17.Simple test get first IRI object from model with list of predicates")
    void testGetFirstIRIObjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        IRI object = modelManager.getFirstIRIObject(Arrays.asList(OWL.EQUIVALENTCLASS,RDFS.SUBCLASSOF), classMen);
        assertNotNull(object);
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());

        IRI object2 = modelManager.getFirstIRIObject(Arrays.asList(DC.CREATOR,RDFS.COMMENT), classMen);
        assertNull(object2,"The subject is not null.");
    }

    @Test
    @Order(18)
    @DisplayName("18.Simple test get first Literal object from model with list of predicates")
    void testGetFirstLiteralObjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Literal object = modelManager.getFirstLiteralObject(Arrays.asList(RDFS.SUBCLASSOF,RDFS.LABEL), classMen);
        assertNotNull(object);
        assertEquals(object.stringValue(),"Men","Literals are not equal.");

        Literal object2 = modelManager.getFirstLiteralObject(Arrays.asList(RDFS.SUBCLASSOF,RDFS.LABEL), classPerson);
        assertNull(object2,"The subject is not null.");
    }

    @Test
    @Order(19)
    @DisplayName("19.Simple test get all subjects with list of objects")
    void testGetAllIRISubjectsWithObjectsList(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> subjects = modelManager.getAllIRISubjects(RDF.TYPE, Arrays.asList(OWL.CLASS,RDFS.CLASS));
        assertEquals(6, subjects.size(),
                "Different size of expected subjects and result.");
    }

    @Test
    @Order(20)
    @DisplayName("20. Simple test get statement")
    void testGetStatementWithIRI(){
        ModelManager modelManager = new ModelManager(model);
        Statement statement = modelManager.getStatementWithIRI(classHuman, RDF.TYPE, RDFS.CLASS);
        assertNotNull(statement,"Statement is not exist");
    }

    @Test
    @Order(21)
    @DisplayName("21 Simple test get first statement with predicate from predicate lists")
    void testGetFirstStatementWithIRIPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Statement statement = modelManager.getFirstStatementWithIRI(classHuman, Arrays.asList(RDF.TYPE,RDFS.SUBCLASSOF), RDFS.CLASS);
        assertNotNull(statement,"Statement is not exist");
    }

    @Test
    @Order(22)
    @DisplayName("22. Simple test if subject is type of Collection")
    void testIsCollection(){
        ModelManager modelManager = new ModelManager(model);
        boolean result = modelManager.isCollection(firstBnode);
        assertTrue(result,"First bnode is not collection");
    }

    @Test
    @Order(23)
    @DisplayName("23. Simple test if subject is not type of Collection")
    void testIsNotCollection(){
        ModelManager modelManager = new ModelManager(model);
        boolean result = modelManager.isCollection(classMen);
        assertFalse(result,"First bnode is not collection");
    }

    @Test
    @Order(24)
    @DisplayName("24. Simple test get Collection")
    void testGetRDFCollection(){
        ModelManager modelManager = new ModelManager(model);
        Set<Value> collection = modelManager.getRDFCollection(firstBnode);
        assertEquals(collection.size(),2,"Collection size is wrong");
        for(Value oneValue:collection){
            assertTrue(oneValue.equals(classParent)|| oneValue.equals(classWoman),"Wrong value in collection");
        }
    }

    @Test
    @Order(25)
    @DisplayName("25. Simple test set Collection")
    void testSetRDFCollection(){
        ModelManager modelManager = new ModelManager(model);
        modelManager.setRDFCollection(classParent,OWL.UNIONOF,Arrays.asList(classMother,classFather));
        assertTrue(modelManager.existStatementWithIRI(classParent,OWL.UNIONOF,null),"Collection is not exist");

    }

    @Test
    @Order(26)
    @DisplayName("26 Simple test get subject of value from Collection")
    void testGetSubjectOfValueCollection(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getSubjectOfValue(OWL.INTERSECTIONOF, classWoman);
        assertNotNull(subject);
        assertEquals(subject,classMother,"Wrong collection subject");
    }

    @Test
    @Order(27)
    @DisplayName("27.Simple test get first Resource object from model (return null)")
    void testGetFirstResourceObjectNull(){
        ModelManager modelManager = new ModelManager(model);
        Resource object = modelManager.getFirstResource(RDFS.SUBCLASSOF, classParent);
        assertNull(object,"Object is exist");
    }

    @Test
    @Order(28)
    @DisplayName("28.Simple test get first IRI object from model")
    void testGetFirsSubject(){
        ModelManager modelManager = new ModelManager(model);
        Resource subject = modelManager.getFirstSubject(OWL.EQUIVALENTCLASS, classHuman);
        assertNotNull(subject,"Subject does not exist");
        assertTrue(subject.isIRI(),"Subject is not IRI");
        assertEquals(subject,classPerson,"Subjects are not equals. Subject is: " + ((IRI)subject).getLocalName());
    }

    @Test
    @Order(29)
    @DisplayName("29 Simple test get subject of value from Collection2")
    void testGetSubjectOfValueCollection2(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getSubjectOfValue(OWL.INTERSECTIONOF, classParent);
        assertNotNull(subject);
        assertEquals(subject,classMother,"Wrong collection subject");
    }

    @Test
    @Order(30)
    @DisplayName("30.Simple test get subject of value - normal value")
    void testGetSubjectOfValue(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getSubjectOfValue(OWL.EQUIVALENTCLASS, classHuman);
        assertNotNull(subject);
        assertEquals(subject,classPerson,"Wrong collection subject");
    }

}

