<#macro propertyType property classRep>
<#if property.type == "DATATYPE">
Literal(${classRep.name?uncap_first}.get${property.name?cap_first}())
<#else>
${classRep.name?uncap_first}.get${property.name?cap_first}().getIri()
</#if>
</#macro>

<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>

from rdflib import URIRef, Literal
<#list imports as item>
from ${item.lib} import ${item.item}
</#list>

class ${classFileName?cap_first}<#if isSerializationModel == false>${serializationModelName}</#if>:

<#if isSerializationModel ==true>
     def getFirstLiteralObject(ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,Literal) :
                return object
        return None

     def getFirstIriObject(ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,URIRef) :
                return object
        return None

     def getAllIRIObjects(ontology, predicate, subject):
        allObjectsIRIs = set()
        for object in getAllObjects(model,predicate,subject):
            if isinstance(object,URIRef):
                allObjectsIRIs.add( object)
        return allObjectsIRIs

     def getAllObjects(ontology, predicate, subject):
        return ontology.objects(subject,predicate)

<#else>
    def addToModel(ontology, ${classRep.name?uncap_first}):
        ontology.add(${classRep.name?uncap_first}.getIri(),RDF.TYPE, ${classRep.name?uncap_first}.getClassIRI())

        <#list classRep.properties as property>
        <#if property.isFunctional() == true>
        <@compress_single_line>
        ontology.add(${classRep.name?uncap_first}.getIri(),Vocabulary.${property.getConstantName()},<@propertyType property=property classRep=classRep/>)
        </@compress_single_line>
        <#else>
        for propValue in ${classRep.name?uncap_first}.get${property.name?cap_first}):
             <@compress_single_line>
             ontology.add(${classRep.name?uncap_first}.getIri(),Vocabulary.${property.getConstantName()}, <@propertyType property=property classRep=classRep/>)
             </@compress_single_line>
        </#if>
        </#list>

    def getInstanceFromModel(ontology, instanceIri):
        if (instanceIri,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI) in ontology:
            ${classRep.name?uncap_first} =  ${classRep.name?cap_first}(instanceIri)
            <#list classRep.properties as property>
            <#if property.type == "DATATYPE">
            ${property.name?uncap_first} = super().getFirstLiteralObject(model,Vocabulary.${property.getConstantName()},instanceIri)
            if ${property.name?uncap_first} != None:
                <@compress single_line=true> ${classRep.name?uncap_first}.set${property.name?cap_first}(property.name?uncap_first)
                </@compress>
            <#else>
            ${property.name?uncap_first} = super().<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllIRIObjects</#if>(model,Vocabulary.${property.getConstantName()},instanceIri)
            <#if property.isFunctional() ==true>
            if ${property.name?uncap_first} != None:
                ${property.name?uncap_first}Instance = ${property.className}Serialization.getInstanceFromModel(model, ${property.name?uncap_first})
                ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance)
            <#else>
            for propValue in ${property.name?uncap_first}:
                ${property.name?uncap_first}Instance = ${property.className}Serialization.getInstanceFromModel(model, propValue)
                ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance)

            </#if>
            </#if>
            </#list>
            return ${classRep.name?uncap_first}
        return None

    def getAllInstancesFromModel(ontology):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI)):
            if isinstance(s,URIRef) :
                ${classRep.name?uncap_first} = getInstanceFromModel(ontology,s)
                allInstances.add(${classRep.name?uncap_first})
        return allInstances

    def removeInstanceFromModel(ontology, subject):
        ontology.remove((subject,RDF.TYPE,${classRep.name?cap_first}.CLASS_IRI))
        for statement in ontology.triples(subject,None,None):
            ontology.remove(statement)

</#if>