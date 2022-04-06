package unitTests;

import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.NormalClassRepresentation;
import ontology.tool.generator.representations.OntologyRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import ontology.tool.mapper.OntologyMapper;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;

import java.util.List;
import java.util.stream.Collectors;

import static ontology.tool.generator.OntologyGenerator.ENTITY_EQUIVALENCE_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MapperTests extends ModelSetUp {

   static IRI testOnt = Values.iri(ex, "testOntology.owl");
   static IRI hasAge = Values.iri(ex, "hasAge");
   static IRI hasAgeDatatype = Values.iri("http://www.w3.org/2001/XMLSchema#nonNegativeInteger");
   static IRI hasDog = Values.iri(ex, "hasDog");
   static IRI classDog = Values.iri(ex, "Dog");

    @BeforeAll
    public static void setUp() {
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
    }

    @Test
    @Order(1)
    @DisplayName("1.Simple getClasses should work")
    void testGetClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        assertEquals(5,  mapper.getClasses().size(),
                "Method getClasses doesn't work.");
    }

    @Test
    @Order(2)
    @DisplayName("2.Simple mapClasses should work")
    void testMapClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        assertEquals(5, mappedClasses.size(),
                "Method getClasses doesn't work.");
    }




    private ClassRepresentation mapAndGetTestedClass(OntologyMapper mapper, IRI classIRI){
        mapper.mapClasses();
        return getTestedClass(mapper,classIRI);
    }

    private ClassRepresentation getTestedClass(OntologyMapper mapper, IRI classIRI){
        ClassRepresentation testedClass= null;
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        for(ClassRepresentation cl: mappedClasses){
            if(cl.getResourceValue().equals(classIRI)){
                testedClass = cl;
            }
        }
        assertNotNull(testedClass);
        return testedClass;
    }

    @Test
    @Order(3)
    @DisplayName("3.Simple test for mapClassHierarchy method, should work")
    void testMapClassHierarchy(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        try {
            mapper.mapClassHierarchy(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1,testedClass.getSuperClasses().size(),
                "Number of super classes doesn't equal.");
        assertEquals(0,testedClass.getSubClasses().size(),
                "Number of subclasses classes doesn't equal.");
    }

    @Test
    @Order(4)
    @DisplayName("4.Simple test for mapEquivalentClasses method, should work")
    void testMapEquivalentClasses(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classPerson);
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
    @Order(5)
    @DisplayName("5.Test for mapEquivalentClasses method with 3 equivalent classes, should work")
    void testMapEquivalentClasses2(){
        IRI classHuman2 = Values.iri(ex, "Human2");
        IRI classHuman3 = Values.iri(ex, "Human3");
        model.add(classHuman2,RDF.TYPE,OWL.CLASS);
        model.add(classHuman2,OWL.EQUIVALENTCLASS,classHuman);

        model.add(classHuman3,RDF.TYPE,OWL.CLASS);
        model.add(classPerson,OWL.EQUIVALENTCLASS,classHuman3);

        OntologyMapper mapper = new OntologyMapper(model);
        mapper.mapClasses();
        List<ClassRepresentation> mappedClasses = mapper.getMappedClasses();
        for(ClassRepresentation mappedClass:mappedClasses){
            try {
                mapper.mapEquivalentClasses(mappedClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ClassRepresentation testedClass = getTestedClass(mapper,classPerson);
        assertNotNull(testedClass.getEquivalentClass());
        assertEquals(4,testedClass.getEquivalentClass().getEquivalentClasses().size(),
                "ClasPerson: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass2 = getTestedClass(mapper,classHuman);
        assertNotNull(testedClass2.getEquivalentClass());
        assertEquals(testedClass2.getEquivalentClass(),testedClass.getEquivalentClass(),
                "ClasHuman: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass2.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman: number of equivalent classes doesn't equal.");
        ClassRepresentation testedClass4 = getTestedClass(mapper,classHuman3);
        assertNotNull(testedClass4.getEquivalentClass());
        assertEquals(testedClass4.getEquivalentClass(),testedClass.getEquivalentClass(),
                "ClasHuman23: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass4.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman23: number of equivalent classes doesn't equal.");

        ClassRepresentation testedClass3 = getTestedClass(mapper,classHuman2);
        assertNotNull(testedClass3.getEquivalentClass());
        assertEquals(testedClass.getEquivalentClass(),testedClass3.getEquivalentClass(),
                "ClasHuman2: Equivalent classes doesn't equal.");
        assertEquals(4,testedClass3.getEquivalentClass().getEquivalentClasses().size(),
                "ClassHuman2: number of equivalent classes doesn't equal.");
    }




    @Test
    @Order(6)
    @DisplayName("Simple test for mapComments method, should work")
    void testMapComments() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapComments(testedClass);
        assertEquals(1,testedClass.getComments().size(),
                "Method mapComments doesn't work.");
    }

    @Test
    @Order(7)
    @DisplayName("Simple test for mapLabels method, should work")
    void testMapLabels() {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        mapper.mapLabels(testedClass);
        assertEquals(1,testedClass.getLabels().size(),
                "Method mapLabels doesn't work.");
    }

    @Test
    @Order(8)
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
    @Order(9)
    @DisplayName("Simple test for get Ontology, should work")
    void testGetOWLOntology() {
        OntologyMapper mapper = new OntologyMapper(model);
        OntologyRepresentation ont = mapper.getOWLOntology();
        assertNotNull(ont);
        assertEquals( ont.getValueIRI(),testOnt,
                "Ontology is not correct. Ontology is:" + ont.getStringIRI());
    }

    @Test
    @Order(10)
    @DisplayName("Simple test for mapping Ontology information")
    void testMapOntologyInformations() {
        IRI testOntPrior = Values.iri(ex, "testOntologyPrior.owl");
        IRI testOntImport = Values.iri(ex, "imports.owl");
        model.add(testOnt,OWL.PRIORVERSION,testOntPrior);
        model.add(testOnt,OWL.IMPORTS,testOntImport);

        OntologyMapper mapper = new OntologyMapper(model);
        OntologyRepresentation ont = mapper.getOWLOntology();

        mapper.mapOntologyInformations(ont);

        assertEquals(ont.getImports().size(),1,"Number of imports is not 1.");
        assertEquals(ont.getImports().get(0),testOntImport.stringValue(),"Import is not correct.");
        assertEquals( ont.getPriorVersion(),testOntPrior.stringValue(), "Ontology prior is not correct. Prior is:" + ont.getPriorVersion());
    }



    @Test
    @Order(11)
    @DisplayName("11.Simple test map Datatype Property")
    void testMapDataTypeProperty(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        try {
            mapper.mapProperties(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2,testedClass.getProperties().size(),
                "Tested class doesn't have one property.");
        PropertyRepresentation prop = testedClass.getProperties().get(0);
        assertEquals(prop.getValueIRI(),hasAge,"Tested class has wrong property.");
        assertEquals(prop.getRangeResource(),hasAgeDatatype,"Tested class has range value.");
        assertFalse(prop.isFunctional(),"Property is functional.");
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
    @Order(12)
    @DisplayName("12.Test map Equivalent Datatype Property")
    void testMapEquivalentDataTypeProperty() throws Exception {
        IRI age = Values.iri(ex,"age");
        model.add(age,OWL.EQUIVALENTPROPERTY,hasAge);
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        mapper.mapProperties(testedClass);
        assertEquals(3,testedClass.getProperties().size(),
                "Tested class doesn't have two properties.");
        PropertyRepresentation eqProp = findProperty(testedClass.getProperties(),age);

        assertNotNull(eqProp,"Tested class doesn't have correct property.");
        assertEquals(eqProp.getRangeResource(),hasAgeDatatype,"Equivalent property has range value.");
        assertFalse(eqProp.isFunctional(),"Equivalent property is functional.");
    }

    @Test
    @Order(13)
    @DisplayName("13.Simple test map Object Property")
    void testMapObjectProperty() throws Exception {
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        mapper.mapProperties(testedClass);

        assertEquals(3,testedClass.getProperties().size(),
                "Tested class doesn't have two properties.");

        PropertyRepresentation objectProp = findProperty(testedClass.getProperties(),hasDog);

        assertNotNull(objectProp,"Tested class doesn't have correct property.");
        assertEquals(objectProp.getRangeResource(),classDog,"Property has wrong range value.");
        assertFalse(objectProp.isFunctional(),"Functional property is set.");

    }

    @Test
    @Order(14)
    @DisplayName("14.Test map Equivalent Object Property")
    void testMapEquivalentObjectProperty() throws Exception {
        IRI dog = Values.iri(ex,"dog");
        model.add(dog,OWL.EQUIVALENTPROPERTY,hasDog);
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        mapper.mapProperties(testedClass);

        assertEquals(4,testedClass.getProperties().size(),
                "Tested class doesn't have four properties.");

        PropertyRepresentation eqProp = findProperty(testedClass.getProperties(),dog);

        assertNotNull(eqProp,"Tested class doesn't have correct property.");
        assertEquals(eqProp.getRangeResource(),classDog,"Property has wrong range value.");
        assertFalse(eqProp.isFunctional(),"Functional property is set.");

    }

    @Test
    @Order(15)
    @DisplayName("15.Simple test map Functional Property")
    void testMapFunctionalProperty() throws Exception {
        model.add(hasDog,RDF.TYPE,OWL.FUNCTIONALPROPERTY);

        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        mapper.mapProperties(testedClass);

        PropertyRepresentation objProp = findProperty(testedClass.getProperties(),hasDog);

        assertNotNull(objProp,"Tested class doesn't have correct property.");
        assertEquals(objProp.getRangeResource(),classDog,"Property has wrong range value.");
        assertTrue(objProp.isFunctional(),"Functional property is not set.");

    }

    @Test
    @Order(16)
    @DisplayName("16.Simple test map Inverse Property")
    void testMapInversProperty() throws Exception {
        IRI hasHuman = Values.iri(ex,"hasHuman");
        model.add(hasHuman,OWL.INVERSEOF,hasDog);

        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        mapper.mapProperties(testedClass);

        ClassRepresentation testedClassDog = getTestedClass(mapper,classDog);
        PropertyRepresentation inverseProp = findProperty(testedClassDog.getProperties(),hasHuman);

        assertNotNull(inverseProp,"Tested class doesn't have correct property.");
        assertEquals(inverseProp.getRangeResource(),classHuman,"Property has wrong range value.");
        assertFalse(inverseProp.isFunctional(),"Functional property is set.");


    }

    @Test
    @Order(17)
    @DisplayName("17.Simple test map Inverse Functional Property")
    void testMapInverseFunctionalProperty(){
        model.add(hasDog,RDF.TYPE,OWL.INVERSEFUNCTIONALPROPERTY);

        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classHuman);
        try {
            mapper.mapProperties(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PropertyRepresentation objProp = findProperty(testedClass.getProperties(),hasDog);
        assertNotNull(objProp,"Tested class doesn't have correct property.");
        ClassRepresentation rangeClass = objProp.getRangeClass();
        assertNotNull(rangeClass,"Range class doesn't exist.");
        assertEquals(2,rangeClass.getProperties().size(),
                "Range class doesn't have one inverse property.");

        //List<String> listOfPropertiesNames = rangeClass.getProperties().parallelStream().map(rangeC-> rangeC.getStringIRI()).collect(Collectors.toList());
        IRI humanProp = Values.iri(hasDog.getNamespace() + "human");

        PropertyRepresentation invFuncProp = findProperty(rangeClass.getProperties(),humanProp);

        assertNotNull(invFuncProp,"Range class doesn't have correct property.");
        //assertEquals(invFuncProp.getStringIRI(),hasDog.getNamespace() + "human","Range class has wrong range value.");
        assertTrue(invFuncProp.isFunctional(),"Inverse functional property is not set.");


    }

    @Test
    @Order(18)
    @DisplayName("Simple test map Property to Equivalent Classes")
    void testMapPropertyToEquivalentClass(){
        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classPerson);
        try {
            mapper.mapEquivalentClasses(testedClass);
            mapper.mapProperties(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(4,testedClass.getProperties().size(),
                "Tested class doesn't have 4 properties.");
        //PropertyRepresentation prop = testedClass.getProperties().get(0);
        PropertyRepresentation prop = findProperty(testedClass.getProperties(),hasAge);

        assertEquals(prop.getValueIRI(),hasAge,"Tested class has wrong property.");
        assertEquals(prop.getRangeResource(),hasAgeDatatype,"Tested class has range value.");
        assertFalse(prop.isFunctional(),"Property doesn't have just one value.");

        prop = findProperty(testedClass.getProperties(),hasDog);

        assertEquals(prop.getValueIRI(),hasDog,"Tested class has wrong property.");
        assertEquals(prop.getRangeResource(),classDog,"Tested class has range value.");
        assertTrue(prop.isFunctional(),"Property doesn't have just one value.");
    }

    @Test
    @Order(19)
    @DisplayName("Simple test map SubProperty")
    void testMapSubProperty(){
        IRI hasWife = Values.iri(ex,"hasWife");
        IRI hasSpouse = Values.iri(ex,"hasSpouse");
        //object property
        model.add(hasWife,RDF.TYPE, OWL.OBJECTPROPERTY);
        model.add(hasWife, RDFS.DOMAIN,classMen);
        model.add(hasWife,RDFS.RANGE,classWoman);

        model.add(hasWife,RDF.TYPE, OWL.OBJECTPROPERTY);
        model.add(hasSpouse,RDFS.SUBPROPERTYOF,hasWife);

        OntologyMapper mapper = new OntologyMapper(model);
        ClassRepresentation testedClass = mapAndGetTestedClass(mapper,classMen);
        try {
            mapper.mapProperties(testedClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(2,testedClass.getProperties().size(),
                "Tested class doesn't have hasWife property.");
        PropertyRepresentation subProp = findProperty(testedClass.getProperties(),hasSpouse);

        assertNotNull(subProp,"Tested class doesn't have correct property.");
        // assertEquals(eqProp.getRangeIRI(),hasAgeDatatype,"Equivalent property has range value.");
        // assertTrue(eqProp.isFunctional(),"Equivalent property doesn't have just one value.");

    }



    //todo specialitky ako ekvivalencia s subclasss atd

}

