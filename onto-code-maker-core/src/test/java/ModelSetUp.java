import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.junit.jupiter.api.BeforeEach;

public class ModelSetUp {
    String ex = "http://example.org/";
    Model model = new TreeModel();
    IRI classPerson = Values.iri(ex, "Person");
    IRI classHuman = Values.iri(ex, "Human");
    IRI classMen = Values.iri(ex, "Men");
    IRI classWoman = Values.iri(ex, "Woman");

    @BeforeEach
    void setUp() {
        model.add(classHuman, RDF.TYPE, RDFS.CLASS);
        model.add(classPerson, RDF.TYPE, RDFS.CLASS);
        model.add(classMen, RDF.TYPE, OWL.CLASS);
        model.add(classWoman, RDF.TYPE, OWL.CLASS);

        model.add(classMen, DC.CREATOR,Values.literal("Tomas"));
        model.add(classMen, RDFS.LABEL,Values.literal("Men"));
        model.add(classMen, RDFS.COMMENT,Values.literal("This is men"));
        model.add(classMen,RDFS.SUBCLASSOF,classPerson);
        model.add(classWoman,RDFS.SUBCLASSOF,classPerson);
        model.add(classPerson,OWL.EQUIVALENTCLASS,classHuman);
    }
}
