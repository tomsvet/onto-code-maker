package ${package};

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
<#list imports as item>
    import ${item};
</#list>

public class ${className} extends {
<#list properties as property>
    /**
    *
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> IRI ${property.name} <#if property.isValue() == true> = Values.iri("${property.getValue()}")</#if>;

</#list>
}