package ${package};

<#list imports as item>
    import ${item};
</#list>

public class ${className} extends {
<#list properties as property>
    /**
    *
    **/
    <#if property.isPrivate ==true>private<#else>public</#if> ${property.type} ${property.name} <#if property.isValue() == true> = ${property.getValue()} </#if>;

</#list>
}