import ontology.generator.classes.examples.family1.OntologyFactory;
import ontology.tool.parser.OntologyParser;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllVariantsOfObjectPropertiesTests {
    public static Model model = new TreeModel();
    OntologyFactory factory;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        String[] inputFiles = {"src/main/resources/ontologies/family1.owl"};
        OntologyParser ontoParser = new OntologyParser();
        model.addAll(ontoParser.parseOntology(inputFiles, RDFFormat.RDFXML.getName()));
        factory = new OntologyFactory(model);
    }


    @Test
    @Order(1)
    @DisplayName("")
    void testAllInstancesWithoutInstance() throws Exception {

    }




}
