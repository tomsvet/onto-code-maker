<#macro value property litName>
<#if property.rangeDatatype == "java.net.URL">
new java.net.URL(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.util.Date">
new java.util.Date(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.time.Duration">
new java.time.Duration(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.sql.Timestamp">
new java.sql.Timestamp(${litName}.longValue())
<#elseif property.rangeDatatype == "java.lang.Integer">
new java.lang.Integer(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.time.LocalTime">
new LocalTime(${litName}.stringValue())
<#else>
${litName}.${property.rangeDatatype}Value()
</#if>
</#macro>

<#macro propertyType property classRep>
<#if property.type == "DATATYPE">
Values.literal(${classRep.name?uncap_first}.get${property.name?cap_first}())
<#else>
${classRep.name?uncap_first}.get${property.name?cap_first}().getIri()
</#if>
</#macro>

<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>

package ${package};

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.Values;
import java.util.Collection;

<#if isInterface ==false>
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.ArrayList;
import java.time.LocalTime;
</#if>

<#if isInterface ==true>abstract class ${classFileName}<T> <#else>public class ${classFileName?cap_first} extends ${serializationModelName}<${classRep.name?cap_first}></#if>{

<#if isInterface ==true>
    abstract public void addToModel(Model model, T entity);

    abstract public T getInstanceFromModel(Model model, IRI name);

    abstract public Collection<T> getAllInstancesFromModel(Model model);

    abstract public void removeInstanceFromModel(Model model, IRI name);

    public Literal getFirstLiteralObject(Model model, IRI predicate, IRI instanceIri){
            Set<Value> objects = model.filter(instanceIri,predicate,null).objects();
            for(Value object : objects){
                if(object.isLiteral()){
                    return (Literal) object;
                }
            }
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

     public Set<IRI> getAllIRIObjects(Model model,IRI predicate, IRI subject){
             Set<IRI> allObjectsIRIs = new HashSet<>();
             Set<Value> objects = getAllObjects(model,predicate,subject);
             for(Value object:objects){
                 if(object.isIRI()){
                     allObjectsIRIs.add((IRI) object);
                 }
             }
             return allObjectsIRIs;
         }

     public Set<Value> getAllObjects(Model model,IRI predicate, IRI subject){
        return model.filter(subject, predicate, null).objects();
      }
<#else>
    @Override
    public void addToModel(Model model, ${classRep.name?cap_first} ${classRep.name?uncap_first}) {
        model.add(${classRep.name?uncap_first}.getIri(),RDF.TYPE, ${classRep.name?uncap_first}.getClassIRI());

        <#list classRep.properties as property>
        <#if property.isFunctional() == true>
        <@compress_single_line>
        model.add(${classRep.name?uncap_first}.getIri(),Vocabulary.${property.getConstantName()},<@propertyType property=property classRep=classRep/>);
        </@compress_single_line>
        <#else>
         for(${property.rangeDatatype} propValue:${classRep.name?uncap_first}.get${property.name?cap_first}){
         <@compress_single_line>
         model.add(${classRep.name?uncap_first}.getIri(),Vocabulary.${property.getConstantName()}, <@propertyType property=property classRep=classRep/>);
         </@compress_single_line>
         }
        </#if>
        </#list>
    }

    @Override
    public ${classRep.name?cap_first} getInstanceFromModel(Model model,IRI instanceIri) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        if(statements.size() != 0){
            ${classRep.name?cap_first} ${classRep.name?uncap_first} = new ${classRep.name?cap_first}(instanceIri);
            <#list classRep.properties as property>

            <#if property.type == "DATATYPE">
            Literal ${property.name?uncap_first} = super.getFirstLiteralObject(model,Vocabulary.${property.getConstantName()},instanceIri);
            if ( ${property.name?uncap_first} != null ){
                <@compress single_line=true> ${classRep.name?uncap_first}.set${property.name?cap_first}(<@value property=property litName=property.name?uncap_first/>);
                </@compress>

            }
            <#else>
             <#if property.isFunctional() ==true>IRI<#else>Set<IRI></#if> ${property.name?uncap_first} = super.<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllIRIObjects</#if>(model,Vocabulary.${property.getConstantName()},instanceIri);
             <#if property.isFunctional() ==true>
             if ( ${property.name?uncap_first} != null ){
                ${property.name?uncap_first}Instance = ${property.className}Serialization.getInstanceFromModel(model, ${property.name?uncap_first});
                ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance);
             }
             <#else>
             for(IRI propValue:${property.name?uncap_first}){
                ${property.name?uncap_first}Instance = ${property.className}Serialization.getInstanceFromModel(model, propValue);
                ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance);
             }
            </#if>
            </#if>
            </#list>
            return ${classRep.name?uncap_first};
        }

        return null;
    }

    @Override
    public Collection<${classRep.name?cap_first}> getAllInstancesFromModel(Model model) {
        Model statements = model.filter(null,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        Collection<${classRep.name?cap_first}> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                ${classRep.name?cap_first} ${classRep.name?uncap_first} = getInstanceFromModel(model,iri);
                allInstances.add(${classRep.name?uncap_first});
            }

        }
        return humans;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        model.remove(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);

        Model statements = model.filter(instanceIri,null,null);
        for(Statement statement:statements){
            model.remove(statement);
        }
    }

</#if>

}