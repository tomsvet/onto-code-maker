<#global seqDatatypes = ["java.net.URL", "java.util.Date", "java.time.Duration","java.sql.Timestamp","java.lang.Integer","java.time.LocalTime"]>
<#macro value property litName>
<#if property.rangeDatatype == "java.net.URL">
new java.net.URL(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.util.Date">
${litName}.calendarValue().toGregorianCalendar().getTime()
<#elseif property.rangeDatatype == "java.time.Duration">
Duration.parse(${litName}.stringValue())
<#elseif property.rangeDatatype == "java.sql.Timestamp">
new java.sql.Timestamp(${litName}.calendarValue().toGregorianCalendar().getTimeInMillis())
<#elseif property.rangeDatatype == "Integer">
Integer.valueOf(${litName}.intValue())
<#elseif property.rangeDatatype == "java.time.LocalTime">
LocalTime.of(${litName}.calendarValue().getHour(),hasTime.calendarValue().getMinute(),hasTime.calendarValue().getSecond())
<#else>
${litName}.${property.rangeDatatype?uncap_first}Value()
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
import java.util.*;
<#if classRep ??>
<#list classRep.properties as property>
<#if property.rangeDatatype == "java.time.Duration">
import ${property.rangeDatatype};
<#elseif property.rangeDatatype == "java.time.LocalTime">
import ${property.rangeDatatype};
</#if>
</#list>
</#if>

import java.util.stream.Collectors;
import ${entityPackage}.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

<#if isInterface ==false>
import java.time.LocalTime;
import ${rawPackage}.${vocabularyFileName};
<#else>
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;

</#if>

public <#if isInterface ==true>abstract class ${classFileName}<T> <#else>class ${classFileName?cap_first} extends ${serializationModelName}<${classRep.getDatatypeValue()?cap_first}></#if>{

<#if isInterface ==true>
    abstract public void addToModel(Model model, T entity);

    abstract public T getInstanceFromModel(Model model, IRI name, int nestingLevel)throws Exception;

    abstract public Collection<T> getAllInstancesFromModel(Model model,int nestingLevel) throws Exception;

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

      public IRI getFirstIRISubject(Model model,IRI predicate, Resource object){
              Set<Resource> subjects = getAllSubjects(model,predicate,object);
              for(Resource subject : subjects){
                  if(subject.isIRI()){
                      return (IRI) subject;
                  }
              }
              return null;
          }

    public Set<Resource> getAllSubjects(Model model,IRI predicate, Resource object){
        return model.filter(null, predicate, object).subjects();
    }

     public Set<IRI> getAllIRISubjects(Model model,IRI predicate, Resource object){
        Set<IRI> allSubjectsIRIs = new HashSet<>();
        Set<Resource> subjects = getAllSubjects(model,predicate,object);
        for(Resource subject:subjects){
            if(subject.isIRI()){
                allSubjectsIRIs.add((IRI) subject);
            }
        }
        return allSubjectsIRIs;
     }

     public Set<Resource> getAllResourceObjects(Model model,IRI predicate, IRI subject){
             Set<Resource> allObjectsIRIs = new HashSet<>();
             Set<Value> objects = getAllObjects(model,predicate,subject);
             for(Value object:objects){
                 if(object.isResource()){
                     allObjectsIRIs.add((Resource)object);
                 }
             }
             return allObjectsIRIs;
     }

     public Model getModelRDFCollection(Model model,Resource node){
        return RDFCollections.getCollection(model, node, new LinkedHashModel());
     }

    public List<Value> getRDFCollection(Model model,Resource node){
        List<Value> retValues = new ArrayList<>();
        RDFCollections.asValues(model, node, retValues);
        return retValues;
    }

    public void setRDFCollection(Model model,IRI subject,IRI predicate,List<OntoEntity> values){
        List<IRI> irisValues = values.stream().map( entity -> entity.getIri()).collect(Collectors.toList());
        if(!irisValues.isEmpty()){
            Resource head = Values.bnode();
            model.addAll( RDFCollections.asRDF(irisValues, head, new LinkedHashModel()));
            model.add(subject, predicate, head);
        }
    }

    public IRI getSubjectOfCollectionValue(Model model,IRI predicate,Resource object){
            if( model.contains(null,predicate,object)){
                return getFirstIRISubject(model,predicate,object);
            }else {
                Set<Resource> objs = model.filter(null, null, object).subjects();
                for (Resource obj : objs) {
                    if (obj.isBNode() && model.contains(null, predicate, obj)) {
                        return getFirstIRISubject(model, predicate, obj);
                    }else if(obj.isBNode()){
                        IRI retVal = getSubjectOfCollectionValue(model, predicate, obj);
                        if(retVal != null){
                            return retVal;
                        }
                    }
                }
            }

            return null;
        }

<#else>
    @Override
    public void addToModel(Model model, ${classRep.getDatatypeValue()?cap_first} ${classRep.name?uncap_first}) {
        model.add(${classRep.name?uncap_first}.getIri(),RDF.TYPE, ${classRep.name?uncap_first}.getClassIRI());
        <#assign propSize =  classRep.properties?size>
        <#assign superClassesNum =  classRep.superClasses?size>
        <#if  propSize gt 0 || superClassesNum gt 0 >
        addPropertiesToModel(model,${classRep.name?uncap_first});
        </#if>

    }

   <#if  propSize gt 0 || superClassesNum gt 0 >
    protected void addPropertiesToModel(Model model,  ${classRep.getDatatypeValue()?cap_first} ${classRep.name?uncap_first}) {
        <#list classRep.properties as property>
        <#if property.isEquivalentTo ??>
        <#else>
        <#if property.isFunctional() == true>
        <#if property.type == "DATATYPE">
        if(${classRep.name?uncap_first}.get${property.name?cap_first}() != null){
        <@compress_single_line>
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>);
        </@compress_single_line>
        }
        <#else>
        <#if property.isSuperProperty() == true>
        if(${classRep.name?uncap_first}.get${property.name?cap_first}() != null
        <@compress_single_line>
        <#list property.getSubProperties() as subProperty>
        <#if property.isFunctional() == true>
         && ${classRep.name?uncap_first}.get${property.name?cap_first}() != ${classRep.name?uncap_first}.get${subProperty.name?cap_first}();
        <#else>
         && !${classRep.name?uncap_first}.get${subProperty.name?cap_first}().contains(${classRep.name?uncap_first}.get${property.name?cap_first}())
         </#if>
        </#list>
        </@compress_single_line>
        ){
        <#else>
        if(${classRep.name?uncap_first}.get${property.name?cap_first}() != null ){
        </#if>
            <@compress_single_line>
            model.add(${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>);
            </@compress_single_line>
        }
        </#if>
        <#else>
        List<OntoEntity> ${property.name?uncap_first}Pom = new ArrayList<>();
                ${property.name?uncap_first}Pom.addAll(${classRep.name?uncap_first}.get${property.name?cap_first}());
        <#if property.isSuperProperty() == true>
        <#list property.getSubProperties() as subProperty>
        ${property.name?uncap_first}Pom.removeAll(${classRep.name?uncap_first}.get${subProperty.name?cap_first}());
        </#list>
        </#if>
        setRDFCollection(model,${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${property.name?uncap_first}Pom);
        </#if>
        </#if>
        </#list>
        <#list classRep.getSuperClasses() as superClass>
        <#if superClass.getClassType().getName() == "Normal">
        <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        new ${superClass.getSerializationClassName()?cap_first}().addPropertiesToModel(model, ${classRep.name?uncap_first});
        </#if>
        </#if>
        </#list>
    }
    </#if>

    protected void setProperties(Model model,${classRep.getDatatypeValue()?cap_first} ${classRep.name?uncap_first},int nestingLevel) throws Exception{
        <#list classRep.properties as property>

        <#if property.type == "DATATYPE">
        Literal ${property.name?uncap_first} = super.getFirstLiteralObject(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        if ( ${property.name?uncap_first} != null ){
            <@compress single_line=true> ${classRep.name?uncap_first}.set${property.name?cap_first}(<@value property=property litName=property.name?uncap_first/>);
            </@compress>

        }
        <#else>
    <#if ! property.isEquivalentTo??>
        <#if property.isFunctional() ==true>IRI<#else>Set<Resource></#if> ${property.name?uncap_first} = super.<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllResourceObjects</#if>(model,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri());
        <#if property.isFunctional() ==true>
        <#if property.rangeClass.getClassType().getName() =="Normal">
         <#list property.getEquivalentProperties() as eqProp>
         if ( ${property.name?uncap_first} == null){
              // check equivalent property ${eqProp.name}
               ${property.name?uncap_first} = super.getFirstIriObject(model,${vocabularyFileName}.${eqProp.getConstantName()},${classRep.name?uncap_first}.getIri());
         }
         </#list>
        <#if property.isInverseFunctionalTo() ==true || property.isInverseFunctionalOf() == true>
        if ( ${property.name?uncap_first} == null){
            // check inverse functional property
            ${property.name?uncap_first} = super.getSubjectOfCollectionValue(model,${vocabularyFileName}.<#if property.isInverseFunctionalTo() ==true>${property.getInverseFunctionalTo().getConstantName()}<#else>${property.getInverseFunctionalOf().getConstantName()}</#if>,${classRep.name?uncap_first}.getIri());
        }
        </#if>
        <#if property.isInverseTo() ==true>
        <#list property.getInverseTo() as inverseProp>
        if ( ${property.name?uncap_first} == null){
            // check inverse property ${inverseProp.name}
            ${property.name?uncap_first} = super.getSubjectOfCollectionValue(model,${vocabularyFileName}.${inverseProp.getConstantName()},${classRep.name?uncap_first}.getIri());
            //${property.name?uncap_first} = super.getFirstIRISubject(model,${vocabularyFileName}.${inverseProp.getConstantName()},${classRep.name?uncap_first}.getIri());
        }
        </#list>
        <#elseif property.isInverseOf() == true>
         if ( ${property.name?uncap_first} == null){
            // check inverse property ${property.isInverseOf().name}
            ${property.name?uncap_first} = super.getFirstIRISubject(model,${vocabularyFileName}.${property.getInverseOf().getConstantName()},${classRep.name?uncap_first}.getIri());
         }
         </#if>
        if ( ${property.name?uncap_first} != null ){
            ${property.rangeClass.getDatatypeValue()?cap_first} ${property.name?uncap_first}Instance = new ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, ${property.name?uncap_first},nestingLevel);
            ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance);
        }
        <#else>
        if ( ${property.name?uncap_first} != null ){
            <@abstractClass property=property rangeClass=property.rangeClass/>
        }
        </#if>
        <#else>
         <#list property.getEquivalentProperties() as eqProp>
         // check equivalent ${eqProp.name}
         ${property.name?uncap_first}.addAll(super.getAllResourceObjects(model,${vocabularyFileName}.${eqProp.getConstantName()},${classRep.name?uncap_first}.getIri()));
         </#list>
        <#if property.isInverseTo() ==true>
        <#list property.getInverseTo() as inverseProp>
         // add also all values from inverse property ${inverseProp.name}
         ${property.name?uncap_first}.addAll(super.getAllIRISubjects(model,${vocabularyFileName}.${property.getInverseTo().getConstantName()},${classRep.name?uncap_first}.getIri()));
         </#list>
        <#elseif property.isInverseOf() == true>
        // add also all values from inverse property ${property.getInverseOf().name}
         ${property.name?uncap_first}.addAll(super.getAllIRISubjects(model,${vocabularyFileName}.${property.getInverseOf().getConstantName()},${classRep.name?uncap_first}.getIri()));
        </#if>
        for(Resource propValue:${property.name?uncap_first}){
        <#if property.rangeClass.getClassType().getName() =="Normal">
            if(propValue.isIRI()) {
                ${property.rangeClass.getDatatypeValue()?cap_first} ${property.name?uncap_first}Instance = new ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, (IRI) propValue,nestingLevel);
                if(${property.name?uncap_first}Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                ${classRep.name?uncap_first}.add${property.name?cap_first}(${property.name?uncap_first}Instance);
        <#else>
                <@abstractClass property=property rangeClass=property.rangeClass/>
        </#if>
            }else if(propValue.isBNode()){
                List<Value> listOfValues = super.getRDFCollection(model,(BNode)propValue);
                for(Value value:listOfValues){
                    if(value.isIRI()){
                        ${property.rangeClass.getDatatypeValue()?cap_first} ${property.name?uncap_first}Instance = new ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, (IRI)value,nestingLevel);
                        if(${property.name?uncap_first}Instance == null) throw new Exception("Instance of " + propValue.stringValue() + " is not in model.");
                        ${classRep.name?uncap_first}.add${property.name?cap_first}(${property.name?uncap_first}Instance);
                    }
                 }
            }
        }
        </#if>
        </#if>
        </#if>
        </#list>

        <@innerSetProperties classRep=classRep/>
    }

    @Override
    public ${classRep.getDatatypeValue()?cap_first} getInstanceFromModel(Model model,IRI instanceIri,int nestingLevel) throws Exception{
        Model statements = model.filter(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        if(statements.size() != 0){
            ${classRep.name?cap_first} ${classRep.name?uncap_first} = new ${classRep.name?cap_first}(instanceIri);
            if(nestingLevel > 0){
                nestingLevel--;
                setProperties(model, ${classRep.name?uncap_first},nestingLevel);
            }
            return ${classRep.name?uncap_first};
        }

        return null;
    }

    @Override
    public Collection<${classRep.getDatatypeValue()?cap_first}> getAllInstancesFromModel(Model model,int nestingLevel)throws Exception{
        Model statements = model.filter(null,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        Collection<${classRep.getDatatypeValue()?cap_first}> allInstances = new ArrayList<>();
        for(Statement statement:statements){
            Resource subject = statement.getSubject();
            if(subject.isIRI()){
                IRI iri = (IRI) subject;
                ${classRep.getDatatypeValue()?cap_first} ${classRep.name?uncap_first} = getInstanceFromModel(model,iri,nestingLevel);
                allInstances.add(${classRep.name?uncap_first});
            }
        }

                <@innerAllInstances classRep=classRep/>

        return allInstances;
    }

    @Override
    public void removeInstanceFromModel(Model model,IRI instanceIri) {
        Set<Value> rdfCollections = model.filter(instanceIri,null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(o -> o.isBNode()).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(instanceIri,null,(BNode)node);
        }
        model.remove(instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI);
        model.remove(instanceIri,null,null);
    }

    @Override
    public void updateInstanceInModel(Model model,${classRep.getDatatypeValue()?cap_first} ${classRep.name?uncap_first}){
        Set<Value> rdfCollections = model.filter(${classRep.name?uncap_first}.getIri(),null,null).objects();
        List<Value> listOfnodes = rdfCollections.stream().filter(Value::isBNode).collect(Collectors.toList());
        for(Value node:listOfnodes){
            model.removeAll(getModelRDFCollection(model,(BNode)node));
            model.remove(${classRep.name?uncap_first}.getIri(),null,node);
        }

        Model statements = model.filter(${classRep.name?uncap_first}.getIri(),null,null);
        statements.removeIf(event -> !event.getPredicate().equals(RDF.TYPE));

        <#if  propSize gt 0 || superClassesNum gt 0 >
        addPropertiesToModel(model,${classRep.name?uncap_first});
        </#if>
    }


</#if>

}

<#macro abstractClass property rangeClass>
<#if rangeClass.isUnionOf() == true>
    <#list rangeClass.getUnionOf() as unionClass>
         <#if unionClass.getClassType().getName() == "Normal">
        ${unionClass.name?cap_first} ${property.name?uncap_first}Instance${unionClass.name?cap_first} = new ${unionClass.getSerializationClassName()?cap_first}().getInstanceFromModel(model, ${property.name?uncap_first},nestingLevel);
        if(${property.name?uncap_first}Instance${unionClass.name?cap_first} != null){
            ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(${property.name?uncap_first}Instance${unionClass.name?cap_first});
            <#if property.isFunctional() == true>continue;<#else>break;</#if>
        }
    <#else>
    //todo
     </#if>
    </#list>
<#elseif rangeClass.isIntersectionOf() == true>
     <#list rangeClass.getIntersectionOf() as intersectionClass>
        <#if intersectionClass.getClassType().getName() == "Normal">
            SerializationModel instance =  new ${serializationFactory?cap_first}().getSerializationInstance(model,${property.name?uncap_first});
            if(instance != null){
                ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(instance.getInstanceFromModel(model, ${property.name?uncap_first},nestingLevel));
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
        new ${superClass.getSerializationClassName()?cap_first}().setProperties(model, ${classRep.name?uncap_first},nestingLevel);
         </#if>
          <#else>
                  <#if superClass.isUnionOf() == true>
                       <#list superClass.getUnionOf() as unionClass>
                       //todo
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
        allInstances.addAll( new ${subClass.getSerializationClassName()}().getAllInstancesFromModel(model,nestingLevel));
         <#else>
         <#if subClass.isUnionOf() == true>
              <#list subClass.getUnionOf() as unionClass>
                    //todo
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
                   //todo
                </#list>
             <#elseif superClass.isIntersectionOf() == true>
             <#elseif superClass.isComplementOf() == true>
             </#if>
          </#if>
         </#list>
</#macro>