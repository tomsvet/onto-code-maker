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
public class OntologyMapperTests extends ModelSetUp{

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }



    @Test
    @Order(1)
    @DisplayName("Simple getClasses should work")
    void testGetClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        assertEquals(4,  mapper.getClasses().size(),
                "Method getClasses doesn't work.");
    }

    @Test
    @Order(2)
    @DisplayName("Simple getClasses should work")
    void testMapClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        assertEquals(4, mappedClasses.size(),
                "Method getClasses doesn't work.");
    }

    private ClassRepresentation mapAndGetTestedClass(OntologyMapper mapper,IRI classIRI){
        mapper.mapClasses();
        return getTestedClass(mapper,classIRI);
    }

    private ClassRepresentation getTestedClass(OntologyMapper mapper,IRI classIRI){
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
    @Order(3)
    @DisplayName("Simple test for mapComments method, should work")
    void testMapComments() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapComments(testedClass);
        assertEquals(1,testedClass.getComments().size(),
                "Method mapComments doesn't work.");
    }

    @Test
    @Order(4)
    @DisplayName("Simple test for mapLabels method, should work")
    void testMapLabels() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapLabels(testedClass);
        assertEquals(1,testedClass.getLabels().size(),
                "Method mapLabels doesn't work.");
    }

    @Test
    @Order(5)
    @DisplayName("Simple test for mapCreator method, should work")
    void testMapCreator() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapCreator(testedClass);
        assertFalse( testedClass.getCreator().isEmpty(),
                "Method mapCreator doesn't work.");

        assertEquals(testedClass.getCreator(),"Tomas","The creator does not equal.");
    }

    @Test
    @Order(6)
    @DisplayName("Simple test for mapClassHierarchy method, should work")
    void testMapClassHierarchy(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapClassHierarchy(testedClass);
        assertEquals(1,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertEquals(0,testedClass.getSubClasses().size(),
                "Number of subclasses classes doesn't equal.");
    }

    @Test
    @Order(7)
    @DisplayName("Simple test for mapEquivalentClasses method, should work")
    void testMapEquivalentClasses(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classPerson);
        mapper.mapEquivalentClasses(testedClass);
        assertEquals(1,testedClass.getEquivalentClasses().size(),
                "Number of equivalent classes doesn't equal.");
        assertEquals(testedClass.getEquivalentClasses().get(0).getValueIRI(),classHuman,"Equivalent class is not correct.");

        //test connection from the other side
        ClassRepresentation testedClass2 = getTestedClass(mapper,classHuman);
        assertEquals(1,testedClass2.getEquivalentClasses().size(),
                "2.Number of equivalent classes doesn't equal.");
        assertEquals(testedClass2.getEquivalentClasses().get(0).getValueIRI(),classPerson,"2. Equivalent class is not correct.");
    }

    @Test
    @Order(8)
    @DisplayName("Test for mapEquivalentClasses method with 3 equivalent classes, should work")
    void testMapEquivalentClasses2(){
        IRI classHuman2 = Values.iri(ex, "Human2");
        model.add(classHuman2,RDF.TYPE,OWL.CLASS);
        model.add(classHuman2,OWL.EQUIVALENTCLASS,classHuman);

        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        for(ClassRepresentation mappedClass:mappedClasses){
            mapper.mapEquivalentClasses(mappedClass);
        }
        ClassRepresentation testedClass = getTestedClass(mapper,classPerson);
        assertEquals(2,testedClass.getEquivalentClasses().size(),
                "ClasPerson: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass2 = getTestedClass(mapper,classHuman);
        assertEquals(2,testedClass2.getEquivalentClasses().size(),
                "ClassHuman: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass3 = getTestedClass(mapper,classHuman2);
        assertEquals(2,testedClass3.getEquivalentClasses().size(),
                "ClassHuman2: number of equivalent classes doesn't equal.");
    }

    @Test
    @Order(9)
    @Disabled
    @DisplayName("Simple test for mapProperties method, should work")
    void testMapProperties(){

    }

}
