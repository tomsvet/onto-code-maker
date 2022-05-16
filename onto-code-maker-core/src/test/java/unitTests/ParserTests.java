package unitTests;

import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/***
 *  ParserTests.java
 *
 *  Unit tests for OntologyParser class.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParserTests {
    OntologyParser ontoParser = new OntologyParser();

    @Test
    @Order(1)
    @DisplayName("1. Test parse Family ontology")
    void testParseFamily() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/family.owl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()).isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("2. Test parse Collection ontology")
    void testParseCollection() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/collections.owl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()).isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("4. Test set Mime type as input value")
    void testMimeType() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/collections.owl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getDefaultMIMEType()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()).isEmpty());
    }


    @Test
    @Order(4)
    @DisplayName("4. Test parse Family ontology in Turtle")
    void testParseFamilyTurtle() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyTurtle.ttl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.TURTLE.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.TURTLE.getName()).isEmpty());
    }

    @Test
    @Order(5)
    @DisplayName("5. Test parse Family ontology in Turtle")
    void testParseFamilyTurtleEmptyFormat() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyTurtle.ttl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, ""));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.TURTLE.getName()).isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("6. Test parse Family ontology in JSONLD")
    void testParseFamilyJSONLD() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyJSONLD.jsonld"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.JSONLD.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.JSONLD.getName()).isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("7. Test parse Family ontology in NTriples")
    void testParseFamilyNTriples() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyN-Triples.nt"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.NTRIPLES.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.NTRIPLES.getName()).isEmpty());
    }

    @Test
    @Order(8)
    @DisplayName("8. Test parse Family ontology in TRIG")
    void testParseFamilyTrig() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyTrig.trig"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.TRIG.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.TRIG.getName()).isEmpty());
    }

    @Test
    @Order(9)
    @DisplayName("9. Test parse Family ontology in RDF")
    void testParseFamilyRDF() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/formats/familyRDF.rdf"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()).isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("10. Test unsupported format")
    void testUnsupportedFormat() {
        String[] inputFiles = {"src/test/resources/inputs/family.owl"};
        assertThrows(Exception.class,() -> ontoParser.parseOntology(inputFiles, RDFFormat.BINARY.getName()));
    }

    @Test
    @Order(11)
    @DisplayName("11. Test non existing file")
    void testNonExistingFile() {
        String[] inputFiles = {"src/test/resources/inputs/family2.owl"};
        assertThrows(Exception.class,() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
    }

    @Test
    @Order(12)
    @DisplayName("12. Test more files")
    void testParseTwoFiles() throws Exception {
        String[] inputFiles = {"src/test/resources/inputs/family.owl","src/test/resources/inputs/collections.owl"};
        assertDoesNotThrow(() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        assertFalse(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()).isEmpty());
    }

    @Test
    @Order(13)
    @DisplayName("13. Test wrong syntax exception")
    void testWrongSyntax() {
        String[] inputFiles = {"src/test/resources/inputs/formats/wrongSyntax.owl"};
        assertThrows(Exception.class,() -> ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
    }
}
