package ontology.generator.classes.examples;

import org.eclipse.rdf4j.model.IRI;

abstract public class OntoEntity {
    private IRI iri;

    public OntoEntity(IRI iri){
        this.iri = iri;
    }

    public IRI getIri(){
        return iri;
    }


    abstract public IRI getClassIRI();

}
