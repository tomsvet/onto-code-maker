package unitTests;

import ontology.tool.mapper.ModelManager;
import org.eclipse.rdf4j.model.*;
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

    @Test
    @Order(1)
    @DisplayName("Simple test get all subjects from model")
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
    @DisplayName("Simple test get all IRI subjects from model")
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
    @Order(3)
    @DisplayName("Simple test get all IRI subjects from model (predicate list)")
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
    @Order(4)
    @DisplayName("Simple test get all objects")
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
    @Order(5)
    @DisplayName("Simple test get all IRI objects from model")
    void testGetAllIRIObjects(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> objects = modelManager.getAllIRIObjects(RDFS.SUBCLASSOF, classMen);
        assertEquals(1, objects.size(),
                "Different size of expected objects and result.");
        IRI object = objects.iterator().next();
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());
    }

    @Test
    @Order(6)
    @DisplayName("Simple test get all Literal objects from model")
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
    @Order(7)
    @DisplayName("Simple test get all Literal objects from model with list of predicates")
    void testGetAllLiteralObjectsPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Set<Literal> objects = modelManager.getAllLiteralObjects(Arrays.asList(RDFS.COMMENT,RDFS.LABEL),classMen);
        assertEquals(2, objects.size(),
                "Different size of expected objects and result.");
    }

    @Test
    @Order(8)
    @DisplayName("Simple test get all IRI objects from model with list of predicates")
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
    @Order(9)
    @DisplayName("Simple test get first IRI object from model")
    void testGetFirstIRIObject(){
        ModelManager modelManager = new ModelManager(model);
        IRI object = modelManager.getFirstIRIObject(RDFS.SUBCLASSOF, classMen);
        assertNotNull(object);
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());

    }

    @Test
    @Order(10)
    @DisplayName("Simple test get first Literal object from model")
    void testGetFirstLiteralObject(){
        ModelManager modelManager = new ModelManager(model);
        Literal object = modelManager.getFirstLiteralObject(RDFS.LABEL, classMen);
        assertNotNull(object);
        assertEquals(object.stringValue(),"Men","Literals are not equal.");
    }

    @Test
    @Order(11)
    void testExistStatementWithIRI(){
        ModelManager modelManager = new ModelManager(model);
        assertTrue( modelManager.existStatementWithIRI(classHuman,RDF.TYPE,RDFS.CLASS),"Human RDFS class is not found.");
        assertFalse( modelManager.existStatementWithIRI(classHuman,RDF.TYPE,OWL.CLASS),"Human OWL class is found.");
    }

    @Test
    @Order(12)
    void getFirstIRISubject(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getFirstIRISubject(OWL.EQUIVALENTCLASS, classHuman);
        assertNotNull(subject);
        assertEquals(subject,classPerson,"The correct subject is not found. The subject is: " + subject.getLocalName());

        IRI subject2 = modelManager.getFirstIRISubject(OWL.EQUIVALENTCLASS, classMen);
        assertNull(subject2,"The subject is not null.");
    }


    @Test
    @Order(13)
    void getFirstIRISubjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        IRI subject = modelManager.getFirstIRISubject(Arrays.asList(OWL.EQUIVALENTCLASS,RDFS.SUBCLASSOF), classPerson);
        assertNotNull(subject);
        assertEquals(subject,classMen,"The correct subject is not found. The subject is: " + subject.getLocalName());

        IRI subject2 = modelManager.getFirstIRISubject(Arrays.asList(RDF.TYPE,RDFS.SUBCLASSOF), classHuman);
        assertNull(subject2,"The subject is not null.");
    }

    @Test
    @Order(14)
    @DisplayName("Simple test get first IRI object from model with list of predicates")
    void testGetFirstIRIObjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        IRI object = modelManager.getFirstIRIObject(Arrays.asList(OWL.EQUIVALENTCLASS,RDFS.SUBCLASSOF), classMen);
        assertNotNull(object);
        assertEquals(object,classPerson,"Objects are not equals. Object is: " + object.getLocalName());

        IRI object2 = modelManager.getFirstIRIObject(Arrays.asList(DC.CREATOR,RDFS.COMMENT), classMen);
        assertNull(object2,"The subject is not null.");
    }

    @Test
    @Order(15)
    @DisplayName("Simple test get first Literal object from model with list of predicates")
    void testGetFirstLiteralObjectPredicateList(){
        ModelManager modelManager = new ModelManager(model);
        Literal object = modelManager.getFirstLiteralObject(Arrays.asList(RDFS.SUBCLASSOF,RDFS.LABEL), classMen);
        assertNotNull(object);
        assertEquals(object.stringValue(),"Men","Literals are not equal.");

        Literal object2 = modelManager.getFirstLiteralObject(Arrays.asList(RDFS.SUBCLASSOF,RDFS.LABEL), classPerson);
        assertNull(object2,"The subject is not null.");
    }

    @Test
    @Order(16)
    @DisplayName("Simple test get all subjects with list of objects")
    void testGetAllIRISubjectsWithObjectsList(){
        ModelManager modelManager = new ModelManager(model);
        Set<IRI> subjects = modelManager.getAllIRISubjects(RDF.TYPE, Arrays.asList(OWL.CLASS,RDFS.CLASS));
        assertEquals(4, subjects.size(),
                "Different size of expected subjects and result.");
    }

}

