package ${package};

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import java.util.Collection;

<#if isInterface ==false>
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.ArrayList;
</#if>

public <#if isInterface ==true>interface ${classFileName}<T> <#else>class ${classFileName?cap_first} implements ${serializationModelName}<${className?cap_first}></#if>{

<#if isInterface ==true>
    public void addToModel(Model model, T entity);

    public T getInstanceFromModel(Model model, IRI name);

    public Collection<T> getAllInstancesFromModel(Model model);

    public void removeInstanceFromModel(Model model, IRI name);
<#else>
    @Override
    public void addToModel(Model model, ${className?cap_first} ${className?uncap_first}) {
        model.add(${className?uncap_first}.getIri(),RDF.TYPE, ${className?uncap_first}.getClassIRI());
        //model.add
    }

    @Override
    public ${className?cap_first} getInstanceFromModel(Model model,IRI instanceIri) {
        Model statements = model.filter(instanceIri,RDF.TYPE,${className?cap_first}.CLASS_IRI);
        if(statements.size() != 0){
            ${className?cap_first} ${className?uncap_first} = new ${className?cap_first}(instanceIri);
            //model.filter(instanceIri,PROPERTY,null);
            //add properties if exist
            return ${className?uncap_first};
        }


        return null;
    }

    @Override
    public Collection<${className?cap_first}> getAllInstancesFromModel(Model model) {
        Model statements = model.filter(null,RDF.TYPE,${className?cap_first}.CLASS_IRI);
        Collection<${className?cap_first}> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                ${className?cap_first} ${className?uncap_first} = new ${className?cap_first}(iri);
                //add properties if exist
                allInstances.add(${className?uncap_first});
            }

        }
        return humans;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        model.remove(instanceIri,RDF.TYPE,${className?cap_first}.CLASS_IRI);

        Model statements = model.filter(instanceIri,null,null);
        for(Statement statement:statements){
            model.remove(statement);
        }
    }

</#if>

}