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
<#macro settingProperty property>
<#if property.isSubProperty() ==true>
<#list property.getSuperProperties() as subProp>
<#if subProp.isFunctional() == true>
        if(!this.${subProp.name?uncap_first}.equals(${property.name})){
            set${subProp.name?cap_first}(${property.name});
        }
<#else>
        if(!this.${subProp.name?uncap_first}.contains(${property.name})){
            add${subProp.name?cap_first}(${property.name});
        }
</#if>
</#list>
</#if>
<#if property.isEquivalentTo??>
<#if property.isEquivalentTo.isFunctional() == true>
        if(!this.${property.isEquivalentTo.name?uncap_first}.equals(${property.name})){
            set${property.isEquivalentTo.name?cap_first}(${property.name});
        }
<#else>
        if(!this.${property.isEquivalentTo.name?uncap_first}.contains(${property.name})){
            add${property.isEquivalentTo.name?cap_first}(${property.name});
        }
</#if>
</#if>
<#list property.equivalentProperties as eqProp>
<#if eqProp.isFunctional() == true>
        if(!this.${eqProp.name?uncap_first}.equals(${property.name})){
            set${eqProp.name?cap_first}(${property.name});
        }
<#else>
        if(!this.${eqProp.name?uncap_first}.contains(${property.name})){
            add${eqProp.name?cap_first}(${property.name});
        }
</#if>
</#list>
</#macro>

package ${package};

import org.eclipse.rdf4j.model.IRI;
<#list imports as item>
    import ${item};
</#list>

/**
<#if classRep??>
<#list classRep.labels as label>
*  ${label}
</#list>
*
<#list classRep.comments as comment>
*  ${comment}
</#list>
*
<#if classRep.creator??>
*  Author of class ${classRep.creator}
</#if>
</#if>
*   Generated by OntoCodeMaker
**/
public<#if isInterface == true > interface<#else> class</#if> ${className}<#if isExtended == true><#if extendClass??> extends ${extendClass}<#else> implements <#list classRep.getSuperClasses() as superClass> ${superClass.name}Int<#sep>,</#list></#if></#if> {

<#if isInterface ==false>
    <#if extendedInterface == true>
    // IRI instance
    protected IRI iri;
    </#if>
    // IRI Constant of Class
    public static IRI CLASS_IRI = ${vocabularyFileName}.${classRep.getConstantName()};
</#if>
<#if isInterface ==false>
<#list classRep.properties as property>

    /**
    * Property ${property.getStringIRI()}
    <@variableComment property=property/>
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> ${property.name} <#if property.isValue() == true> = ${property.getValue()} </#if>;
</#list>

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
    </#if>
<#else>
    <#if extendedInterface == true>
    public IRI getIri(){
        return iri;
    }
    </#if>

    public IRI getClassIRI() {
        return CLASS_IRI;
    }
</#if>

<#if isInterface ==false>
<#list classRep.properties as property>
    <#if property.isFunctional() == true>
    public void set${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        this.${property.name} = ${property.name};

        <@settingProperty property=property/>
    }
    <#else>
    public void add${property.name?cap_first}(${property.rangeDatatype} ${property.name}){
        if (this.${property.name} == null) this.${property.name} = new ArrayList<>();
        this.${property.name}.add(${property.name});

        <@settingProperty property=property/>
    }
    </#if>

    public <#if property.isFunctional() == true>${property.rangeDatatype}<#else>List<${property.rangeDatatype}></#if> get${property.name?cap_first}(){
        return ${property.name};
    }

</#list>
</#if>
}