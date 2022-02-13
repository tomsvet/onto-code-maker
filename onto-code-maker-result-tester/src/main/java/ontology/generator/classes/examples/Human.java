package ontology.generator.classes.examples;

import org.eclipse.rdf4j.model.IRI;

public class Human extends OntoEntity {

    public static IRI CLASS_IRI = Vocabulary.HUMAN_CLASS_IRI;

    public Human(IRI iri) {
        super(iri);
    }

    @Override
    public IRI getClassIRI() {
        return CLASS_IRI;
    }

}
