package ${package};

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
<#list imports as item>
    import ${item};
</#list>

public class ${className} {
<#list properties as property>
    /**
    *   A constant representing the ${property.constantOf} ${property.objectName}
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> static final IRI ${property.name} <#if property.isValue() == true> = Values.iri("${property.getValue()}")</#if>;

</#list>
}