from rdflib import URIRef
<#list imports as item>
from ${item.lib} import ${item.item}
</#list>

class ${classFileName?cap_first}:
    def __init__(self, ontology):
        self.ontology = ontology
        <#list serializationClasses as serialization>
            self.${serialization.getSerializationClassName()?uncap_first} = ${serialization.getSerializationClassName()?cap_first}()
        </#list>

    def getOntology(self):
        return self.ontology

    <#list serializationClasses as serialization>

    def create${serialization.name?cap_first}(name):
        return ${serialization.name?cap_first}(URIRef(name))

    def addToModel(self,entity){
        if isinstance(entity, ${serialization.name?cap_first}):
            self.${serialization.getSerializationClassName()?uncap_first}.addToModel(ontology, entity)

    def get${serialization.name?cap_first}FromModel(self,name):
        return self.${serialization.getSerializationClassName()?uncap_first}.getInstanceFromModel(ontology,URIRef(name))

    def getAll${serialization.name?cap_first}InstancesFromModel(self):
        return self.${serialization.getSerializationClassName()?uncap_first}.getAllInstancesFromModel(self.ontology)

    def remove${serialization.name?cap_first}FromModel(self,name):
        self.${serialization.getSerializationClassName()?uncap_first}.removeInstanceFromModel(self.ontology,URIRef(name))

    </#list>