package ontology.tool.generator.representations;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.OWL;

public class RestrictionRepresentation extends DefaultClassRepresentation {

    private RESTRICTION_IN_TYPE restrictionIn;

    public enum RESTRICTION_IN_TYPE {
        UNIONOF,
        EQUIVALENT,
        INTERSECTIONOF,
        COMPLEMENT,
        SUBCLASS,
        DISJOINTWITH
    }
    private String className;
    private IRI onProperty;
    private String value;
    private String type;

    public RestrictionRepresentation() {
        super(CLASS_TYPE.RESTRICTION);
    }

    public RestrictionRepresentation(String name) {
        super(name,CLASS_TYPE.RESTRICTION);
    }

    public String getClassName(){
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setOnProperty(IRI onProperty){
        this.onProperty = onProperty;
    }

    public IRI getOnProperty(){
        return onProperty;
    }

    public void setClassName(IRI onProperty) {
        this.onProperty = onProperty;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public void setRestrictionIn(RESTRICTION_IN_TYPE restrictionIn) {
        this.restrictionIn = restrictionIn;
    }

    public RESTRICTION_IN_TYPE getRestrictionIn(){
        return restrictionIn;
    }
}
