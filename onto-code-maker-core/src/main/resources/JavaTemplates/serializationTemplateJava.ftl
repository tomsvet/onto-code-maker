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

<#macro propertyType property classRep propValue>
<#if property.type == "DATATYPE">
Values.literal(${classRep.name?uncap_first}.get${property.name?cap_first}())
<#else>
${propValue}.getIri()
</#if>
</#macro>

<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>

package ${package};

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

<#if isInterface ==false>
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.util.Values;
import java.util.ArrayList;
import java.time.LocalTime;
import ${entityPackage}.*;
import ${rawPackage}.${vocabularyFileName};
</#if>

public <#if isInterface ==true>abstract class ${classFileName}<T> <#else>class ${classFileName?cap_first} extends ${serializationModelName}<${classRep.name?cap_first}></#if>{

<#if isInterface ==true>
    abstract public void addToModel(Model model, T entity);

    abstract public T getInstanceFromModel(Model model, IRI name)throws Exception;

    abstract public Collection<T> getAllInstancesFromModel(Model model) throws Exception;

    abstract public void removeInstanceFromModel(Model model, IRI name);

    abstract public void updateInstanceInModel(Model model, T entity);

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
        <#assign propSize =  classRep.properties?size>
        <#assign superClassesNum =  classRep.superClasses?size>
        <#if  propSize gt 0 || superClassesNum gt 0 >
        addPropertiesToModel(model,${classRep.name?uncap_first});
        </#if>

    }

   <#if  propSize gt 0 || superClassesNum gt 0 >
    protected void addPropertiesToModel(Model model,  ${classRep.name?cap_first} ${classRep.name?uncap_first}) {
        <#list classRep.properties as property>
        <#if property.isFunctional() == true>
        <#if property.type == "DATATYPE">
        <@compress_single_line>
        model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>);
        </@compress_single_line>
        <#else>
        if(${classRep.name?uncap_first}.get${property.name?cap_first}() != null){
            <@compress_single_line>
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>);
            </@compress_single_line>
        }
        </#if>
        <#else>
        for(${property.rangeDatatype} propValue:${classRep.name?uncap_first}.get${property.name?cap_first}()){
        <@compress_single_line>
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()}, <@propertyType property=property classRep=classRep propValue="propValue"/>);
        </@compress_single_line>
        }
        </#if>

        </#list>
        <#list classRep.getSuperClasses() as superClass>
        <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        new ${superClass.getSerializationClassName()?cap_first}().addPropertiesToModel(model, ${classRep.name?uncap_first});
        </#if>
        </#list>
    }
    </#if>

    protected void setProperties(Model model,${classRep.name?cap_first} ${classRep.name?uncap_first}) throws Exception{
        <#list classRep.properties as property>

        <#if property.type == "DATATYPE">
        Literal ${property.name?uncap_first} = super.getFirstLiteralObject(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        if ( ${property.name?uncap_first} != null ){
            <@compress single_line=true> ${classRep.name?uncap_first}.set${property.name?cap_first}(<@value property=property litName=property.name?uncap_first/>);
            </@compress>

        }
        <#else>
        <#if property.isFunctional() ==true>IRI<#else>Set<IRI></#if> ${property.name?uncap_first} = super.<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllIRIObjects</#if>(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        <#if property.isFunctional() ==true>
        <#if property.rangeClass.getClassType().getName() =="Normal">
        if ( ${property.name?uncap_first} != null ){
            ${property.rangeClass.name?cap_first} ${property.name?uncap_first}Instance = new ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, ${property.name?uncap_first});
            ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance);
        }
        <#else>
        if ( ${property.name?uncap_first} != null ){
            <@abstractClass property=property rangeClass=property.rangeClass/>
        }
        </#if>
        <#else>
        for(IRI propValue:${property.name?uncap_first}){
        <#if property.rangeClass.getClassType().getName() =="Normal">
            ${property.rangeClass.name?cap_first} ${property.name?uncap_first}Instance = new ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, propValue);
            if(${property.name?uncap_first}Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
            ${classRep.name?uncap_first}.add${property.name?cap_first}(${property.name?uncap_first}Instance);
        <#else>
            <@abstractClass property=property rangeClass=property.rangeClass/>
        </#if>
        }

         </#if>
        </#if>
        </#list>

        <@innerSetProperties classRep=classRep/>
    }

    @Override
    public ${classRep.name?cap_first} getInstanceFromModel(Model model,IRI instanceIri) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        if(statements.size() != 0){
            ${classRep.name?cap_first} ${classRep.name?uncap_first} = new ${classRep.name?cap_first}(instanceIri);

            setProperties(model, ${classRep.name?uncap_first});

            return ${classRep.name?uncap_first};
        }

        return null;
    }

    @Override
    public Collection<${classRep.name?cap_first}> getAllInstancesFromModel(Model model)throws Exception{
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

                <@innerAllInstances classRep=classRep/>

        return allInstances;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        model.remove(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);

        Model statements = model.filter(instanceIri,null,null);
        for(Statement statement:statements){
            model.remove(statement);
        }
    }

    @Override
    public void updateInstanceInModel(Model model,${classRep.name?cap_first} ${classRep.name?uncap_first}){
        <#list classRep.properties as property>

        <#if property.type == "DATATYPE">
        Literal ${property.name?uncap_first} = super.getFirstLiteralObject(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        if ( ${property.name?uncap_first} == null || !${property.name?uncap_first}.equals(<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>)){
            model.remove(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${property.name?uncap_first});
            <@compress_single_line>
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>);
            </@compress_single_line>
        }
        <#else>
        <#if property.isFunctional() ==true>IRI<#else>Set<IRI></#if> ${property.name?uncap_first} = super.<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllIRIObjects</#if>(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        <#if property.isFunctional() ==true>
        if (( ${property.name?uncap_first} == null && ${classRep.name?uncap_first}.get${property.name?cap_first}() != null ) ||( ${property.name?uncap_first} != null && !${property.name?uncap_first}.equals(${classRep.name?uncap_first}.get${property.name?cap_first}().getIri()))){
            model.remove(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${property.name?uncap_first});
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.get${property.name?cap_first}().getIri());

        }
        <#else>
        for(IRI propValue:${property.name?uncap_first}){
            model.remove(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},propValue);
        }
        for(${property.rangeClass.name?cap_first} propValue:${classRep.name?uncap_first}.get${property.name?cap_first}()){
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},propValue.getIri());
        }
        </#if>
        </#if>
        </#list>

        <@innerAbstractUpdate classRep=classRep/>
    }


</#if>

}

<#macro abstractClass property rangeClass>
<#if rangeClass.isUnionOf() == true>
    <#list rangeClass.getUnionOf() as unionClass>
         <#if unionClass.getClassType().getName() == "Normal">
        ${unionClass.name?cap_first} ${property.name?uncap_first}Instance${unionClass.name?cap_first} = new ${unionClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, ${property.name?uncap_first});
        if(${property.name?uncap_first}Instance${unionClass.name?cap_first} != null){
            ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(${property.name?uncap_first}Instance${unionClass.name?cap_first});
            <#if property.isFunctional() == true>continue;<#else>break;</#if>
        }
    <#else>
    <@abstractClass property=property rangeClass=unionClass />
     </#if>
    </#list>
<#elseif rangeClass.isIntersectionOf() == true>
     <#list rangeClass.getIntersectionOf() as intersectionClass>
        <#if intersectionClass.getClassType().getName() == "Normal">
            SerializationModel instance =  new ${serializationFactory?cap_first}().getSerializationInstance(model,${property.name?uncap_first});
            if(instance != null){
                ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(instance.getInstanceFromModel(model, ${property.name?uncap_first}));
            }
        <#else>
            <@abstractClass property=property rangeClass=intersectionClass/>
        </#if>
     </#list>
<#elseif rangeClass.isComplementOf() == true>
    <#if rangeClass.complementOf.getClassType().getName() == "Normal">
            //need to do
    <#else>
        <@abstractClass property=property rangeClass=rangeClass.getComplementOf()/>
    </#if>
</#if>
</#macro>

<#macro innerSetProperties classRep>
<#list classRep.getSuperClasses() as superClass>
          <#if superClass.getClassType().getName() == "Normal">
         <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        new ${superClass.getSerializationClassName()?cap_first}().setProperties(model, ${classRep.name?uncap_first});
         </#if>
          <#else>
                  <#if superClass.isUnionOf() == true>
                       <#list superClass.getUnionOf() as unionClass>
                         <@innerSetProperties classRep=unionClass />
                       </#list>
                  <#elseif superClass.isIntersectionOf() == true>
                    <#list superClass.getIntersectionOf() as intersectionClass>
                        <@innerSetProperties classRep=intersectionClass />
                    </#list>
                   <#elseif superClass.isComplementOf() == true>
                    <@innerSetProperties classRep=superClass.getComplementOf() />
                     </#if>
         </#if>
         </#list>
</#macro>

<#macro innerAllInstances classRep>
<#list classRep.getSubClasses() as subClass>
         <#if subClass.getClassType().getName() == "Normal">
        allInstances.addAll( new ${subClass.getSerializationClassName()}().getAllInstancesFromModel(model));
         <#else>
         <#if subClass.isUnionOf() == true>
              <#list subClass.getUnionOf() as unionClass>
                <@innerAllInstances classRep=unionClass/>
              </#list>
         <#elseif subClass.isIntersectionOf() == true>
            <#list subClass.getIntersectionOf() as intersectionClass>

                       </#list>
          <#elseif subClass.isComplementOf() == true>
              <@innerAllInstances  classRep=subClass.getComplementOf()/>
          </#if>
          </#if>
   </#list>
</#macro>

<#macro innerAbstractUpdate classRep>
    <#list classRep.getSuperClasses() as superClass>
          <#if superClass.getClassType().getName() == "Normal">
         <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        new ${superClass.getSerializationClassName()?cap_first}().updateInstanceInModel(model, ${classRep.name?uncap_first});
         </#if>
         <#else>
             <#if superClass.isUnionOf() == true>
                <#list superClass.getUnionOf() as unionClass>
                    <@innerAbstractUpdate classRep=unionClass />
                </#list>
             <#elseif superClass.isIntersectionOf() == true>
             <#elseif superClass.isComplementOf() == true>
             </#if>
          </#if>
         </#list>
</#macro>