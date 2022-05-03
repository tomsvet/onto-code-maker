package ontology.tool.mapper.representations;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;

import java.util.ArrayList;
import java.util.List;

/**
 *  OntologyRepresentation.java
 *
 *  Representation of Ontology in inner model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class OntologyRepresentation extends EntityRepresentation {

    private String priorVersion;
    private List<String> imports = new ArrayList<>();

    public OntologyRepresentation(String namespace, String name) {
        super(namespace, name);
    }

    public IRI getValueIRI(){
        return Values.iri(super.getNamespace() + super.getName());
    }

    public String getStringIRI(){
        return super.getNamespace() + super.getName();
    }

    public void setPriorVersion(String priorVersion) {
        this.priorVersion = priorVersion;
    }

    public String getPriorVersion(){
        return priorVersion;
    }

    public void addImports(String importValue){
        this.imports.add(importValue);
    }

    public List<String> getImports(){
        return imports;
    }
}
