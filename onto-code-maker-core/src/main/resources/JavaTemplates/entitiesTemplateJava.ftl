<#macro variableComment property>
<#if property.isSubProperty() ==true>
    * The property is SubProperty of <#list property.getSuperProperties() as subProp> ${subProp.name}<#sep>,</#list>
</#if>
<#if property.isEquivalentTo??>
    * The property is equivalent to ${property.getIsEquivalentTo().name}
</#if>
<#list property.comments as comment>
*  ${comment}
</#list>
<#list property.labels as label>
*  ${label}
</#list>
</#macro>
<#macro settingProperty property valueName>
    <#if property.isSubProperty() == true>
        <#list property.getSuperProperties() as superProp>
            <#if superProp.isFunctional() == true>
        if(this.${superProp.name?uncap_first} == null || !this.${superProp.name?uncap_first}.equals(${valueName})){
                <#if property.isEquivalentTo??>
            set${superProp.name?cap_first}(${valueName});
                <#else>
            this.${superProp.name?uncap_first} = ${valueName};
                </#if>
        }
            <#else>
        if(!this.${superProp.name?uncap_first}.contains(${valueName})){
                <#if property.isEquivalentTo??>
            add${superProp.name?cap_first}(${valueName});
                <#else>
            this.${superProp.name?uncap_first}.add(${valueName});
                </#if>
        }
            </#if>
         <@settingProperty property=superProp valueName=valueName/>
        </#list>
    </#if>
<#if property.isEquivalentTo??>
<#if property.isEquivalentTo.isFunctional() == true>
        if(this.${property.isEquivalentTo.name?uncap_first} == null || !this.${property.isEquivalentTo.name?uncap_first}.equals(${valueName})){
            this.${property.isEquivalentTo.name?uncap_first} = ${valueName};
        }
<#else>
        if(!this.${property.isEquivalentTo.name?uncap_first}.contains(${valueName})){
            this.${property.isEquivalentTo.name?uncap_first}.add(${valueName});
        }
</#if>
</#if>
<#list property.equivalentProperties as eqProp>
<#if eqProp.isFunctional() == true>
        if(this.${eqProp.name?uncap_first} == null || !this.${eqProp.name?uncap_first}.equals(${valueName})){
            this.${eqProp.name?uncap_first} = ${valueName};
        }
<#else>
        if(!this.${eqProp.name?uncap_first}.contains(${valueName})){
            this.${eqProp.name?uncap_first}.add(${valueName});
        }
</#if>
</#list>
</#macro>
<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>
package ${package};

import org.eclipse.rdf4j.model.*;
import java.util.List;
<#if isInterface ==false>
import java.util.ArrayList;
    <#if isAbstract ==false>
import ${rawPackage}.${vocabularyFileName};
    </#if>
</#if>

/**
*  <@compress_single_line>
<#if classRep?? && isInterface == false && isAbstract == false>This is the class representing the ${classRep.name}(${classRep.getFullName()}) class from ontology<#elseif mainInterface == true>This is a base class for all the generated entities.
<#elseif isEquivalent ??> This is interface representing equivalence of classes <#list classRep.getEquivalentClass().equivalentClasses as eqClass> ${eqClass.getName()}<#sep>,</#list>
<#elseif classRep.isUnionOf() == true> This is an interface that represents union of <#list classRep.getUnionOf() as union> ${union.getName()}<#sep>,</#list>.
<#elseif classRep.isIntersectionOf() == true> This is an abstract class that represents intersection of <#list classRep.getIntersectionOf() as inter> ${inter.getName()}<#sep>, </#list>.
<#elseif classRep.isComplementOf() == true> This is an abstract class that represents complement of ${classRep.getComplementOf().getName()}.
<#elseif isInterface == true> This is interface for class ${className} </#if>
</@compress_single_line>
<#if classRep??>
<#assign superClassesNum =  classRep.superClasses?size>
    <#if superClassesNum gt 0>
*  This class is subclass of <#list classRep.getSuperClasses() as superClass> ${superClass.getName()}<#sep>, </#list>
    </#if>
</#if>
*
<#if classRep?? && isInterface == false>
<#list classRep.labels as label>
*  ${label}
</#list>
<#list classRep.comments as comment>
*  ${comment}
</#list>
<#if classRep.creator??>
*  Author of class ${classRep.creator}
</#if>
</#if>
<#if classRep??>
<#assign disjointSize =  classRep.getDisjointWith()?size>
<#if disjointSize gt 0>
*   This class is disjoint with classes:
<#list classRep.getDisjointWith() as disjointWith>
*        ${disjointWith.getName()}(${disjointWith.getFullName()})
</#list>.
*
</#if>
<#assign restSize =  classRep.restrictions?size>
<#if restSize gt 0>
*   Restrictions:
</#if>
<#list classRep.restrictions as restriction>
<@compress_single_line>
*
<#if restriction.getRestrictionIn() == "EQUIVALENT">    This class is equivalent with restriction: </#if>
<#if restriction.getRestrictionIn() == "UNIONOF">   Restriction of union: </#if>
<#if restriction.getRestrictionIn() == "INTERSECTIONOF">    Restriction of intersection: </#if>
<#if restriction.getRestrictionIn() == "COMPLEMENT">    Restriction of complement: </#if>
<#if restriction.getRestrictionIn() == "SUBCLASS">  Restriction of subclass: </#if>
    ${restriction.onProperty}  ${restriction.type}  ${restriction.value}
</@compress_single_line>
</#list>
</#if>
*   Generated by OntoCodeMaker
**/
<@compress_single_line>
public<#if isInterface == true > interface<#else><#if isAbstract == true> abstract</#if> class</#if> ${className}
<#if mainInterface == false>
    <#if isExtends == true >
 extends <#list extendClasses as extendClass> ${extendClass}<#sep>,</#list>
    </#if>

<#if isImplements == true >
 implements <#list implementClasses as implementClass> ${implementClass}<#sep>,</#list>
</#if>

</#if> {
</@compress_single_line>

<#if isInterface ==false>
    <#if extendedInterface == true>
    // IRI instance
    protected IRI iri;
    </#if>
    <#if isAbstract == false>
    // IRI Constant of Class
    public static IRI CLASS_IRI = ${vocabularyFileName}.${classRep.getConstantName()};
    </#if>
</#if>
<#if isInterface ==false>
<#list classRep.properties as property>

    /**
    * Property ${property.getStringIRI()}
    <@variableComment property=property/>
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> ${property.name}<#if property.isFunctional() == false> = new ArrayList<>()</#if>;
</#list>

<@superVariables classRep=classRep/>

    public ${className}(IRI iri){
            <#if extendedInterface == false>
            super(iri);
            <#else>
            this.iri = iri;
            </#if>
    }
</#if>

<#if isInterface ==true>
    <#if mainInterface == true>
    public IRI getIri();

    public IRI getClassIRI();
    <#else>
        <#list classRep.properties as property>
            <#if ! property.isEquivalentTo ??>
                <#if property.isFunctional() == true>
    void set${property.name?cap_first}(${property.rangeDatatype} ${property.name});
                <#else>
    void add${property.name?cap_first}(${property.rangeDatatype} ${property.name});
                </#if>
            </#if>
    <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> get${property.name?cap_first}();
        </#list>
    </#if>
<#else>
    <#if extendedInterface == true>
    public IRI getIri(){
        return iri;
    }
    </#if>

    <#if isAbstract == false>
    public IRI getClassIRI() {
        return CLASS_IRI;
    }
    </#if>
</#if>

<#if isInterface ==false>
    <#list classRep.properties as property>
        <#if ! property.isEquivalentTo ??>
            <#if property.isFunctional() == true>
    public void set${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        this.${property.name} = ${property.name};
        <@settingProperty property=property valueName=property.getName()/>
    }
            <#else>
    public void add${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        this.${property.name}.add(${property.name});
        <@settingProperty property=property valueName=property.getName()/>
    }
            </#if>
        </#if>
    public <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> get${property.name?cap_first}(){
        return ${property.name};
    }

    </#list>

    <@setterAndGetters classRep=classRep />

</#if>
}

<#macro superVariables classRep >
<#list classRep.getSuperClasses() as superClass>
    <#if superClass.hasInterface || superClass.getClassType().getName() == "Abstract">
        <#list superClass.properties as property>
    /**
    * Property ${property.getStringIRI()}. This property is from super class.
    <@variableComment property=property/>
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> ${property.name}<#if property.isFunctional() == false> = new ArrayList<>()</#if>;

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
    public void set${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        this.${property.name} = ${property.name};
        <@settingProperty property=property valueName=property.getName()/>
    }
                <#else>
    public void add${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        this.${property.name}.add(${property.name});
        <@settingProperty property=property valueName=property.getName()/>
    }
                </#if>
            </#if>
    public <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> get${property.name?cap_first}(){
        return ${property.name};
    }
        </#list>
        <@setterAndGetters classRep=superClass />
    </#if>
</#list>
</#macro>