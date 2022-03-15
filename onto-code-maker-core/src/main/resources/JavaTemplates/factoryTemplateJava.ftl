package ${package};

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ${serializationPackage}.*;
import ${entityPackage}.*;


public class ${classFileName?cap_first}{

    private Model ontology;
<#list serializationClasses as serialization>
    private ${serialization.getSerializationClassName()?cap_first} ${serialization.getSerializationClassName()?uncap_first};
</#list>


    public ${classFileName?cap_first}(Model ontology){
        this.ontology = ontology;
        <#list serializationClasses as serialization>
         this.${serialization.getSerializationClassName()?uncap_first} = new ${serialization.getSerializationClassName()?cap_first}();
        </#list>

    }

    public Model getOntology(){
        return ontology;
    }

    public void addToModel(${entityClassName} entity){
        <#list serializationClasses as serialization>
        if (entity instanceof ${serialization.name?cap_first}){
            ${serialization.getSerializationClassName()?uncap_first}.addToModel(ontology, (${serialization.name?cap_first}) entity);
        }
        </#list>
    }

<#list serializationClasses as serialization>
    public ${serialization.name?cap_first} create${serialization.name?cap_first}(String name){
        return new ${serialization.name?cap_first}(Values.iri(name));
    }

    public ${serialization.name?cap_first} get${serialization.name?cap_first}FromModel(String name){
        return ${serialization.getSerializationClassName()?uncap_first}.getInstanceFromModel(ontology,Values.iri(name));
    }

    public Collection<${serialization.name?cap_first}> getAll${serialization.name?cap_first}InstancesFromModel(){
        return ${serialization.getSerializationClassName()?uncap_first}.getAllInstancesFromModel(ontology);
    }

    public void remove${serialization.name?cap_first}FromModel(String name){
        ${serialization.getSerializationClassName()?uncap_first}.removeInstanceFromModel(ontology,Values.iri(name));
    }
</#list>
}