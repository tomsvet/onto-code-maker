package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;

import java.util.ArrayList;
import java.util.List;

public class EntityRepresentation {
    private String name;
    private String namespace;
    private List<String> comments = new ArrayList<>();
    private List<String> labels = new ArrayList<>();
    private String creator;

    public EntityRepresentation(String namespace,String name){
        this.namespace = namespace;
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public String getStringIRI(){
        return namespace + name;
    }

    public IRI getValueIRI(){
        return Values.iri(namespace + name);
    }

    public void addCommentProperty(String comment){
        comments.add(comment);
    }

    public List<String> getComments() {
        return comments;
    }

    public void addLabelProperty(String label){
        labels.add(label);
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }
}
