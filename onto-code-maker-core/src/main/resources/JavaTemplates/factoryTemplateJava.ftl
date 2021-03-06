package ${package};

import org.eclipse.rdf4j.model.*;
<#if typeOfFactory == "Serialization">
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.Set;
</#if>
import org.eclipse.rdf4j.model.util.Values;
import java.util.Collection;
import ${serializationPackage}.*;
import ${entityPackage}.*;

/**
<#if typeOfFactory == "Ontology">
* This Code is generated from these ontologies
<#list ontologies as ontology>
* Ontology: ${ontology.name}
<#if ontology.priorVersion??>* Prior version: ${ontology.priorVersion} </#if>
<#list ontology.imports as import>
*   Import: ${import}
</#list>
*
<#list ontology.comments as comment>
*   ${comment}
</#list>
<#list ontology.labels as label>
*   ${label}
</#list>
<#if ontology.creator??>
*   Author of ontology ${ontology.creator}
</#if>
</#list>
</#if>
*
*
*   Generated by OntoCodeMaker
**/
public class ${classFileName?cap_first}{

<#if typeOfFactory == "Serialization">
    public SerializationModel getSerializationInstance(OntoEntity entity){
        <#list serializationClasses as serialization>
            if (entity.getClass() == ${serialization.name?cap_first}.class){
                return new ${serialization.getSerializationClassName()?cap_first}();
            }
        </#list>
        return null;
    }

    public SerializationModel getSerializationInstance(IRI classIri){
        <#list serializationClasses as serialization>
            if(classIri.equals(${vocabularyFileName}.${serialization.getConstantName()})){
                return new ${serialization.getSerializationClassName()?cap_first}();
            }
        </#list>
        return null;
    }

    public SerializationModel getSerializationInstance(Model model,IRI instanceIri){
       IRI classIri = getFirstIriObject(model, RDF.TYPE, instanceIri);
       <#list serializationClasses as serialization>
            if(classIri.equals(${vocabularyFileName}.${serialization.getConstantName()})){
                return new ${serialization.getSerializationClassName()?cap_first}();
            }
       </#list>
       return null;
    }

    public IRI getFirstIriObject(Model model,IRI predicate, IRI instanceIri){
        Set<Value> objects = model.filter(instanceIri,predicate,null).objects();
        for(Value object : objects){
            if(object.isIRI()){
                return (IRI) object;
            }
        }
        return null;
    }
<#else>
    private Model ontology;
    private ${serializationFactory?cap_first} serializationFactory;

    public ${classFileName?cap_first}(Model ontology){
        this.ontology = ontology;
        this.serializationFactory = new ${serializationFactory?cap_first}();
    }

    public Model getOntology(){
        return ontology;
    }

    public void addToModel(${entityClassName} entity){
            serializationFactory.getSerializationInstance(entity).addToModel(ontology, entity);
    }

    public void updateInstanceInModel(${entityClassName} entity){
        serializationFactory.getSerializationInstance(entity).updateInstanceInModel(ontology, entity);
    }

<#list serializationClasses as serialization>
    public ${serialization.name?cap_first} create${serialization.name?cap_first}(String name){
        return new ${serialization.name?cap_first}(Values.iri(name));
    }

    public ${serialization.name?cap_first} get${serialization.name?cap_first}FromModel(String name) throws Exception{
        return (${serialization.name?cap_first}) serializationFactory.getSerializationInstance(${vocabularyFileName}.${serialization.getConstantName()}).getInstanceFromModel(ontology,Values.iri(name),2);
    }

    public Collection<${serialization.name?cap_first}> getAll${serialization.name?cap_first}InstancesFromModel() throws Exception{
        return serializationFactory.getSerializationInstance(${vocabularyFileName}.${serialization.getConstantName()}).getAllInstancesFromModel(ontology,2);
    }

    public void remove${serialization.name?cap_first}FromModel(String name){
        serializationFactory.getSerializationInstance(${vocabularyFileName}.${serialization.getConstantName()}).removeInstanceFromModel(ontology,Values.iri(name));
    }
</#list>

</#if>
}