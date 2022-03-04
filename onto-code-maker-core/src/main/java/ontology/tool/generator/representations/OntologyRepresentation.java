package ontology.tool.generator.representations;

import java.util.ArrayList;
import java.util.List;

public class OntologyRepresentation extends EntityRepresentation {

    private String priorVersion;
    private List<String> imports = new ArrayList<>();

    public OntologyRepresentation(String namespace, String name) {
        super(namespace, name);
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
