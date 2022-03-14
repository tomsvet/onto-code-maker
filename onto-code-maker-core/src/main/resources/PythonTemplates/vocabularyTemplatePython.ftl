from rdflib import URIRef


class ${className}:
<#list properties as property>

    # A constant representing the ${property.constantOf} ${property.objectName}
    ${property.name} = URIRef("${property.getValue()}")
</#list>