package ${package};

import org.eclipse.rdf4j.model.IRI;
<#list imports as item>
    import ${item};
</#list>

<#if isAbstract ==true>abstract </#if>public class ${className}<#if isExtended ==true>extends ${extendClass}</#if> {

<#if isAbstract ==true>
    private IRI iri;
<#else>
    public static IRI CLASS_IRI = Vocabulary.${vocabularyClassConstant};
</#if>
<#list properties as property>

    /**
    *
    **/
   <#if property.isPrivate ==true>private<#else>public</#if> ${property.type} ${property.name} <#if property.isValue() == true> = ${property.getValue()} </#if>;
</#list>

    public ${className}(IRI iri){
            <#if isAbstract ==true>
            this.iri = iri;
            <#else>
            super(iri);
            </#if>
    }

<#if isAbstract ==true>
    public IRI getIri(){
            return iri;
    }

    abstract public IRI getClassIRI();
<#else>
    @Override
    public IRI getClassIRI() {
        return CLASS_IRI;
    }
</#if>

<#list properties as property>
    public void set${property.name}?cap_first(${property.type} ${property.name}){
        this.${property.name} = ${property.name};
    }

    public ${property.type} get${property.name}?cap_first(){
            return ${property.name};
    }
</#list>

}