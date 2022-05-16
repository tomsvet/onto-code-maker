<#macro variableComment property>
<#if property.isSubProperty() ==true>
        # The property is SubProperty of <#list property.getSuperProperties() as subProp> ${subProp.name}<#sep>,</#list>
</#if>
<#if property.isEquivalentTo??>
        # The property is equivalent to ${property.getIsEquivalentTo().name}
</#if>
<#list property.comments as comment>
        #  ${comment}
</#list>
<#list property.labels as label>
        #  ${label}
</#list>
</#macro>
<#macro settingProperty property valueName>
<#if property.isSubProperty() ==true>
    <#list property.getSuperProperties() as superProp>
        <#if superProp.isFunctional() == true>
        if not self.${superProp.name?uncap_first} == ${valueName}:
            <#if property.isEquivalentTo??>
            set${superProp.name?cap_first}(${valueName})
            <#else>
            self.${superProp.name?uncap_first} = ${valueName}
            </#if>
        <#else>
        if not ${valueName} in self.${superProp.name?uncap_first}:
            <#if property.isEquivalentTo??>
            add${superProp.name?cap_first}(${valueName})
            <#else>
            self.${superProp.name?uncap_first}.add(${valueName})
            </#if>
        </#if>
        <@settingProperty property=superProp valueName=valueName/>
    </#list>
</#if>
<#if property.isEquivalentTo??>
    <#if property.isEquivalentTo.isFunctional() == true>
    if not (self.${property.isEquivalentTo.name?uncap_first} is None) or not self.${property.isEquivalentTo.name?uncap_first} == ${valueName}:
        self.${property.isEquivalentTo.name?uncap_first} = ${valueName}
    <#else>
    if not self.${valueName} in self.${property.isEquivalentTo.name?uncap_first}:
        self.${property.isEquivalentTo.name?uncap_first}.add(${valueName})
    </#if>
</#if>
<#list property.equivalentProperties as eqProp>
    <#if eqProp.isFunctional() == true>
    if not (self.${eqProp.name?uncap_first} is None) or not self.${eqProp.name?uncap_first} == ${valueName}:
        self.${eqProp.name?uncap_first} = ${valueName}
    <#else>
        if not self.${valueName} in self.${eqProp.name?uncap_first}:
            self.${eqProp.name?uncap_first}.add(${valueName})
    </#if>
</#list>
</#macro>
<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>
from rdflib import URIRef
<#if isMainClass == false>
from ${vocabularyFileName} import ${vocabularyFileName}
</#if>
<#if isExtends == true > <#list extendClasses as extendClass>
from .${extendClass} import ${extendClass}
</#list></#if>


##
#  <@compress_single_line>
<#if classRep?? && isMainClass == false>This is the class representing the ${classRep.name}(${classRep.getFullName()}) class from ontology<#elseif isMainClass == true>This is a base class for all the generated entities.
<#elseif isEquivalent ??> This is interface representing equivalence of classes <#list classRep.getEquivalentClass().equivalentClasses as eqClass> ${eqClass.getName()}<#sep>,</#list>
<#elseif classRep.isUnionOf() == true> This is an interface that represents union of <#list classRep.getUnionOf() as union> ${union.getName()}<#sep>,</#list>
<#elseif classRep.isIntersectionOf() == true> This is an abstract class that represents intersection of <#list classRep.getIntersectionOf() as inter> ${inter.getName()}<#sep>, </#list>
<#elseif classRep.isComplementOf() == true> This is an abstract class that represents complement of ${classRep.getComplementOf().getName()}
<#elseif isInterface == true> This is interface for class ${className} </#if>
</@compress_single_line>
<#if classRep??>
<#assign superClassesNum =  classRep.superClasses?size>
    <#if superClassesNum gt 0>
#  This class is subclass of <#list classRep.getSuperClasses() as superClass> ${superClass.getName()}<#sep>, </#list>
    </#if>
</#if>
#
<#if classRep?? && isMainClass == false>
    <#list classRep.labels as label>
#  ${label}
    </#list>
    <#list classRep.comments as comment>
#  ${comment}
    </#list>
    <#if classRep.creator??>
#  Author of class ${classRep.creator}
    </#if>
</#if>
<#if isAbstractClass==false && classRep??>
    <#assign disjointSize =  classRep.getDisjointWith()?size>
    <#if disjointSize gt 0>
#   This class is disjoint with classes:
        <#list classRep.getDisjointWith() as disjointWith>
#        ${disjointWith.getName()}(${disjointWith.getFullName()})
        </#list>
#
    </#if>
    <#assign restSize =  classRep.restrictions?size>
    <#if restSize gt 0>
#   Restrictions:
    </#if>
    <#list classRep.restrictions as restriction>
    <@compress_single_line>
#
    <#if restriction.getRestrictionIn() == "EQUIVALENT">    This class is equivalent with restriction: </#if>
    <#if restriction.getRestrictionIn() == "UNIONOF">   Restriction of union: </#if>
    <#if restriction.getRestrictionIn() == "INTERSECTIONOF">    Restriction of intersection: </#if>
    <#if restriction.getRestrictionIn() == "COMPLEMENT">    Restriction of complement: </#if>
    <#if restriction.getRestrictionIn() == "SUBCLASS">  Restriction of subclass: </#if>
    ${restriction.onProperty}  ${restriction.type}  ${restriction.value}
    </@compress_single_line>
    </#list>
</#if>
#   Generated by OntoCodeMaker
##

<@compress_single_line>
class ${className}
<#if isMainClass == false>
    <#if isExtends == true >
 (<#list extendClasses as extendClass> ${extendClass}<#sep>,</#list>)
    </#if>
</#if>:
</@compress_single_line>

<#if isMainClass == true >
    def __init__(self,iri):
        self.iri = iri

    def getIri(self):
        return self.iri
<#else>

<#if isMainClass == false>
 <#if  isAbstractClass==false>
    CLASS_IRI = ${vocabularyFileName}.${classRep.getConstantName()}
</#if>
    def __init__(self,iri):
        <#if isExtends == true && extendClasses?size = 1>
        super().__init__(iri)
        <#else>
     <#list extendClasses as extendClass>
        ${extendClass}.__init__(iri)
     </#list>
        </#if>

        <#if !classRep.getEquivalentClass()?? ||  isAbstractClass==true>
            <#list classRep.properties as property>
        # Property ${property.getStringIRI()} ${classRep.properties?size}
        <@variableComment property=property/>
        self.${property.name} <#if property.isValue() == true> = ${property.getValue()}<#else> = <#if property.isFunctional() == true>None<#else>set()</#if> </#if>

            </#list>
        </#if>
    <#if isAbstractClass == false>
    def getClassIRI(self):
        return ${className}.CLASS_IRI
    </#if>

<#if !classRep.getEquivalentClass()?? ||  isAbstractClass==true>
<#list classRep.properties as property>
    <#if ! property.isEquivalentTo ??>

    <#if property.isFunctional() == true>
    def set${property.name?cap_first}(self, ${property.name}):
        self.${property.name} = ${property.name}
        <@settingProperty property=property valueName=property.getName()/>

    <#else>
    def add${property.name?cap_first}(self, ${property.name}):
        self.${property.name}.add(${property.name})
        <@settingProperty property=property valueName=property.getName()/>

    </#if>
    </#if>

    def get${property.name?cap_first}(self):
        return self.${property.name}
</#list>
</#if>
</#if>

</#if>

<#macro superVariables classRep >
<#list classRep.getSuperClasses() as superClass>
    <#if superClass.hasInterface || superClass.getClassType().getName() == "Abstract">
        <#list superClass.properties as property>
        ##
        # Property ${property.getStringIRI()}. This property is from super class.
        <@variableComment property=property/>
        ##
        self.${property.name} <#if property.isValue() == true> = ${property.getValue()}<#else> = set() </#if>

        </#list>
    <@superVariables classRep=superClass />
    </#if>
</#list>
</#macro>

<#macro setterAndGetters classRep >
<#list classRep.getSuperClasses() as superClass>
    <#if superClass.hasInterface || superClass.getClassType().getName() == "Abstract">
        <#list superClass.properties as property>
            <#if ! property.isEquivalentTo ??>
                <#if property.isFunctional() == true>
    def set${property.name?cap_first}(self, ${property.name}):
        self.${property.name} = ${property.name}
        <@settingProperty property=property valueName=property.getName()/>
                <#else>
    def add${property.name?cap_first}(self, ${property.name}):
        self.${property.name}.add(${property.name})
        <@settingProperty property=property valueName=property.getName()/>
                </#if>
            </#if>

    def get${property.name?cap_first}(self):
        return self.${property.name}

        </#list>
        <@setterAndGetters classRep=superClass />
    </#if>
</#list>
</#macro>
