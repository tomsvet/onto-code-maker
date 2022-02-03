import ontology.tool.OntoCodeMaker;
import ontology.tool.generator.ClassRepresentation;
import ontology.tool.mapper.OntologyMapper;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class OntologyMapperTests {
    String ex = "http://example.org/";

    Model model = new TreeModel();

    @BeforeEach
    void setUp() {
        IRI classPerson = Values.iri(ex, "Person");
        IRI classMen = Values.iri(ex, "Men");

        model.add(classPerson, RDF.TYPE, RDFS.CLASS);
        model.add(classMen, RDF.TYPE, OWL.CLASS);
    }

    @Test
    @DisplayName("Simple getClasses should work")
    void testGetClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        assertEquals(2,  mapper.getClasses().size(),
                "Method getClasses is not working.");
    }

    @Test
    @DisplayName("Simple getClasses should work")
    void testMapClasses() {
        OntologyMapper mapper = new OntologyMapper(model);
        List<ClassRepresentation> mappedClasses = mapper.mapClasses();
        assertEquals(2, mappedClasses.size(),
                "Method getClasses is not working.");

       // assertEquals(mappedClasses.get(0).getClassName(),"Person");
        String name = mappedClasses.get(0).getClassName();
        assertTrue(name.equals("Men") || name.equals("Person"));
    }
}
