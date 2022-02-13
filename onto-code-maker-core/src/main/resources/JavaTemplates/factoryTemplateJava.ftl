package ${package};

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;


public class ${ontologyName}?cap_firstFactory {

    private Model ontology;
<#list serializationClasses as serialization>
    private ${serialization}?cap_firstSerialization ${serialization}?uncap_firstSerialization;
</#list>


    public ${ontologyName}?cap_firstFactory(Model ontology){
        this.ontology = ontology;
        <#list serializationClasses as serialization>
         this.${serialization}?uncap_firstSerialization = new ${serialization}?cap_firstSerialization();
        </#list>

    }

    public Model getOntology(){
        return ontology;
    }

<#list serializationClasses as serialization>
    public ${serialization}?cap_first create${serialization}?cap_first(String name){
        return new ${serialization}?cap_first(Values.iri(name));
    }

    public void addToModel(OntoEntity entity){
        if (entity instanceof ${serialization}?cap_first){
            ${serialization}?uncap_firstSerialization.addToModel(ontology, (${serialization}?cap_first) entity);
        }
    }

    public ${serialization}?cap_first get${serialization}?cap_firstFromModel(String name){
        return ${serialization}?uncap_firstSerialization.getInstanceFromModel(ontology,Values.iri(name));
    }

    public Collection<${serialization}?cap_first> getAll${serialization}?cap_firstInstancesFromModel(){
        return ${serialization}?uncap_firstSerialization.getAllInstancesFromModel(ontology);
    }

    public void remove${serialization}?cap_firstFromModel(String name){
        ${serialization}?uncap_firstSerialization.removeInstanceFromModel(ontology,Values.iri(name));
    }
</#list>
}