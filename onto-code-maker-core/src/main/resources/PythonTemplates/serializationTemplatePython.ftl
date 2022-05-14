<#macro propertyType property classRep propValue>
<#if property.type == "DATATYPE">
Literal(${classRep.name?uncap_first}.get${property.name?cap_first}())
<#else>
${propValue}.getIri()
</#if>
</#macro>

<#macro compress_single_line>
    <#local captured><#nested></#local>
${ captured?replace("\\n|\\r", "", "rm") }
</#macro>

from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
<#if vocabularyFileName??>
from ${vocabularyFileName} import ${vocabularyFileName}
</#if>
<#if classRep??>
from entities.${classRep.name?cap_first} import ${classRep.name?cap_first}
</#if>
<#if isSerializationModel == false>
from .${serializationModelName} import ${serializationModelName}
</#if>

class ${classFileName?cap_first}<#if isSerializationModel == false>(${serializationModelName})</#if>:

<#if isSerializationModel ==true>
    def getFirstLiteralObject(self,ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,Literal):
                return object
        return None

    def getAllLiteralObjects(self,ontology, predicate, subject):
        allObjectsIRIs = set()
        for  object in self.getAllObjects(ontology,predicate,subject):
            if isinstance(object,Literal):
                allObjectsIRIs.add(object)
        return allObjectsIRIs

    def getFirstIriObject(self,ontology, predicate, subject):
        for object in ontology.objects(subject,predicate):
            if isinstance(object,URIRef) :
                return object
        return None

    def getAllIRIObjects(self,ontology, predicate, subject):
        allObjectsIRIs = set()
        for object in self.getAllObjects(ontology,predicate,subject):
            if isinstance(object,URIRef):
                allObjectsIRIs.add( object)
        return allObjectsIRIs

    def getAllObjects(self,ontology, predicate, subject):
        return ontology.objects(subject,predicate)

    def getAllSubjects(self,ontology,predicate,object):
        return ontology.subjects(predicate,object)

    def getFirstIRISubject(self, ontology, predicate, object):
        for subject in ontology.subjects(predicate,object):
            if isinstance(subject,URIRef):
                return subject
        return None

    def getAllIRISubjects(self, ontology, predicate, object):
        allSubjectsIRIs = set()
        for subject in ontology.subjects(predicate,object):
            if isinstance(subject,URIRef):
                allSubjectsIRIs.add( subject)
        return allSubjectsIRIs

    def getAllResourceObjects(self, ontology, predicate, subject):
        allObjectsIRIs = set()
        for object in ontology.objects(subject,predicate):
            if type(subject) in [BNode, URIRef]:
                allObjectsIRIs.add(object)
        return allObjectsIRIs

    def getModelRDFCollection(self, ontology, node):
        return Collection(ontology, node)

    def getRDFCollection(self,ontology,node):
        return Collection(ontology, node)

    def setRDFCollection(self,ontology, subject, predicate, objects):
        if(len(objects) != 0):
            bnode_list = BNode()
            ontology.add((subject, predicate, bnode_list))
            os = Collection(ontology, bnode_list)
            for item in objects:
                os.append(item.getIri())

    def setLiteralsRDFCollection(self,ontology, subject, predicate, objects):
        if(len(objects) != 0):
            bnode_list = BNode()
            ontology.add((subject, predicate, bnode_list))
            os = Collection(ontology, bnode_list)
            for item in objects:
                os.append(Literal(item))

    def getSubjectOfCollectionValue(self,ontology, predicate, object):
        if (None, predicate, object) in ontology:
            return self.getFirstIRISubject(ontology,predicate,object)
        else:
            for sub, pre, obj in ontology.triples((None,None,object)):
                if isinstance(sub,BNode) and (None, predicate, sub) in ontology:
                    return self.getFirstIRISubject(ontology,predicate,sub)
                elif isinstance(sub,BNode):
                    retVal = self.getSubjectOfCollectionValue(ontology, predicate, sub)
                    if retVal is not None:
                        return retVal

<#else>
    def addToModel(self,ontology, ${classRep.name?uncap_first}):
        ontology.add((${classRep.name?uncap_first}.getIri(),RDF.type, ${classRep.name?uncap_first}.getClassIRI()))
<#assign propSize =  classRep.properties?size>
<#assign superClassesNum =  classRep.superClasses?size>
        <#if  propSize gt 0 || superClassesNum gt 0 >
        self.addPropertiesToModel(ontology,${classRep.name?uncap_first})
        </#if>


<#if  propSize gt 0 || superClassesNum gt 0 >
    def addPropertiesToModel(self,ontology, ${classRep.name?uncap_first}):
    <#list classRep.properties as property>
        <@addProperties property=property classRep=classRep/>
    </#list>
        <@innerAddProperties classRep=classRep/>
        pass

</#if>
 <#if  propSize gt 0 || superClassesNum gt 0 >
    def setProperties(self,ontology, ${classRep.name?uncap_first}, nestingLevel):
<#list classRep.properties as property>
        <@setProperties property=property classRep=classRep/>
</#list>
        <@innerSetProperties classRep=classRep/>
        pass
</#if>

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,${classRep.name?cap_first}.CLASS_IRI) in ontology:
            ${classRep.name?uncap_first} =  ${classRep.name?cap_first}(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                 <#if  propSize gt 0 || superClassesNum gt 0 >
                self.setProperties(ontology, ${classRep.name?uncap_first},nestingLevel)
                </#if>
            return ${classRep.name?uncap_first}
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,${classRep.name?cap_first}.CLASS_IRI)):
            if isinstance(s,URIRef) :
                ${classRep.name?uncap_first} = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(${classRep.name?uncap_first})
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,${classRep.name?cap_first}.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)

</#if>


<#macro addProperties property classRep>
    <#if ! property.isEquivalentTo ??>
        <#if property.isFunctional() == true>
            <#if property.type == "DATATYPE">
        if ${classRep.name?uncap_first}.get${property.name?cap_first}() is not None:
        <@compress_single_line>
            ontology.add((${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>))
        </@compress_single_line>

            <#else>
                <#if property.isSuperProperty() == true>
        <@compress_single_line>
        if ${classRep.name?uncap_first}.get${property.name?cap_first}() is not None
                    <#list property.getSubProperties() as subProperty>
                        <#if property.isFunctional() == true>
 and ${classRep.name?uncap_first}.get${property.name?cap_first}() is not ${classRep.name?uncap_first}.get${subProperty.name?cap_first}()
                        <#else>
 and ${classRep.name?uncap_first}.get${property.name?cap_first}() not in ${classRep.name?uncap_first}.get${subProperty.name?cap_first}()
                        </#if>
                    </#list>
:
        </@compress_single_line>
                <#else>
        if ${classRep.name?uncap_first}.get${property.name?cap_first}() is not None:
                </#if>
            <@compress_single_line>
            ontology.add((${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},<@propertyType property=property classRep=classRep propValue= classRep.name?uncap_first + ".get" + property.name?cap_first+ "()"/>))
            </@compress_single_line>
            <#list property.getInverseTo() as inverseTo>
            ontology.add((${classRep.name?uncap_first}.get${property.name?cap_first}().getIri() ,${vocabularyFileName}.${inverseTo.getConstantName()},${classRep.name?uncap_first}.getIri()))
            </#list>

            </#if>
        <#else>
        ${property.name?uncap_first}Pom = set(${classRep.name?uncap_first}.get${property.name?cap_first}())
            <#if property.isSuperProperty() == true>
                <#list property.getSubProperties() as subProperty>
        for x in ${classRep.name?uncap_first}.get${subProperty.name?cap_first}():
            ${property.name?uncap_first}Pom.remove(x)
                </#list>
            </#if>
             <#list property.getEquivalentProperties() as eqProperty>
                 <#list eqProperty.getSubProperties() as subProperty>
        for x in ${classRep.name?uncap_first}.get${subProperty.name?cap_first}():
            ${property.name?uncap_first}Pom.remove(x)
                 </#list>
              </#list>
            <#if property.type == "DATATYPE">
        super().setLiteralsRDFCollection(ontology,${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${property.name?uncap_first}Pom)
            <#else>
        super().setRDFCollection(ontology,${classRep.name?uncap_first}.getIri(),${vocabularyFileName}.${property.getConstantName()},${property.name?uncap_first}Pom)
            </#if>
            <#list property.getInverseTo() as inverseTo>
        for pom in ${property.name?uncap_first}Pom:
            ontology.add((pom.getIri(),${vocabularyFileName}.${inverseTo.getConstantName()},${classRep.name?uncap_first}.getIri()))

            </#list>
        </#if>
    </#if>
</#macro>

<#macro innerAddProperties classRep>
<#list classRep.getSuperClasses() as superClass>
    <#if superClass.getClassType().getName() == "Normal">
         <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        from .${superClass.getSerializationClassName()?cap_first} import ${superClass.getSerializationClassName()?cap_first}
        ${superClass.getSerializationClassName()?cap_first}().addPropertiesToModel(ontology, ${classRep.name?uncap_first})
         </#if>
    <#else>
        <#if superClass.isUnionOf() == true>
            <#list superClass.getProperties() as property>
                <@addProperties property=property classRep=classRep/>
            </#list>
        <#elseif superClass.isIntersectionOf() == true>
            <#list superClass.getProperties() as property>
                <@addProperties property=property classRep=classRep/>
            </#list>
        <#elseif superClass.isComplementOf() == true>
            <#list superClass.getProperties() as property>
                <@addProperties property=property classRep=classRep/>
            </#list>
         </#if>
    </#if>
</#list>
</#macro>

<#macro setProperties property classRep>
<#if property.type == "DATATYPE">
    <#if ! property.isEquivalentTo??>
        ${property.name?uncap_first} = super().<#if property.isFunctional() ==true>getFirstLiteralObject<#else>getAllObjects</#if>(ontology,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri())
        <#if property.isFunctional() ==true>
        if ${property.name?uncap_first} is not None:
            <@compress single_line=true> ${classRep.name?uncap_first}.set${property.name?cap_first}(str(${property.name?uncap_first}))
            </@compress>

        <#else>
            <#list property.getEquivalentProperties() as eqProp>
        # check equivalent ${eqProp.name}
        for p in super().getAllObjects(ontology,${vocabularyFileName}.${eqProp.getConstantName()},${classRep.name?uncap_first}.getIri()):
            ${property.name?uncap_first}.add(p)
            </#list>
        for propValue in ${property.name?uncap_first}:
            if isinstance(propValue,Literal):
                <@compress single_line=true> ${classRep.name?uncap_first}.add${property.name?cap_first}(str(propValue))
                </@compress>

            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for literalValue in listOfValues :
                    if isinstance(literalValue,Literal):
                        <@compress single_line=true> ${classRep.name?uncap_first}.add${property.name?cap_first}(str(literalValue))</@compress>

        </#if>
    </#if>
 <#else>
    <#if ! property.isEquivalentTo??>
        ${property.name?uncap_first} = super().<#if property.isFunctional() ==true>getFirstIriObject<#else>getAllResourceObjects</#if>(ontology,${vocabularyFileName}.${property.getConstantName()},${classRep.name?uncap_first}.getIri())
        <#if property.isFunctional() ==true>
            <#if property.rangeClass.getClassType().getName() =="Normal">
                <#list property.getEquivalentProperties() as eqProp>
        if ${property.name?uncap_first} is None:
            # check equivalent property ${eqProp.name}
            ${property.name?uncap_first} = super().getFirstIriObject(ontology,${vocabularyFileName}.${eqProp.getConstantName()},${classRep.name?uncap_first}.getIri())

                </#list>
                <#if property.isInverseFunctionalTo() ==true || property.isInverseFunctionalOf() == true>
        if ${property.name?uncap_first} is None:
            # check inverse functional property
            ${property.name?uncap_first} = super().getSubjectOfCollectionValue(ontology,${vocabularyFileName}.<#if property.isInverseFunctionalTo() ==true>${property.getInverseFunctionalTo().getConstantName()}<#else>${property.getInverseFunctionalOf().getConstantName()}</#if>,${classRep.name?uncap_first}.getIri())
                </#if>
                <#if property.isInverseTo() ==true>
                    <#list property.getInverseTo() as inverseProp>
        if ${property.name?uncap_first} is None:
            # check inverse property ${inverseProp.name}
            ${property.name?uncap_first} = super().getSubjectOfCollectionValue(ontology,${vocabularyFileName}.${inverseProp.getConstantName()},${classRep.name?uncap_first}.getIri())
            #${property.name?uncap_first} = super().getFirstIRISubject(ontology,${vocabularyFileName}.${inverseProp.getConstantName()},${classRep.name?uncap_first}.getIri())

                    </#list>
                <#elseif property.isInverseOf() == true>
        if ${property.name?uncap_first} is None:
            # check inverse property ${property.isInverseOf().name}
            ${property.name?uncap_first} = super().getFirstIRISubject(ontology,${vocabularyFileName}.${property.getInverseOf().getConstantName()},${classRep.name?uncap_first}.getIri())

                </#if>
        if ${property.name?uncap_first} is not None:
            from .${property.rangeClass.getSerializationClassName()?cap_first} import ${property.rangeClass.getSerializationClassName()?cap_first}
            ${property.name?uncap_first}Instance = ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(ontology, ${property.name?uncap_first},nestingLevel)
            ${classRep.name?uncap_first}.set${property.name?cap_first}(${property.name?uncap_first}Instance)

            <#else>
        if ${property.name?uncap_first} is not None:
            <@abstractClass property=property rangeClass=property.rangeClass isCycle=false/>

            </#if>
        <#else>
            <#list property.getEquivalentProperties() as eqProp>
        # check equivalent ${eqProp.name}
        for p in super().getAllResourceObjects(ontology,${vocabularyFileName}.${eqProp.getConstantName()},${classRep.name?uncap_first}.getIri()):
            ${property.name?uncap_first}.add(p)
            </#list>
             <#if property.isInverseOf() == true>
        # add also all values from inverse property ${property.getInverseOf().name}
        for p in super().getAllIRISubjects(ontology,${vocabularyFileName}.${property.getInverseOf().getConstantName()},${classRep.name?uncap_first}.getIri()):
            ${property.name?uncap_first}.add(p)
                    </#if>
        for propValue in ${property.name?uncap_first}:
                    <#if property.rangeClass.getClassType().getName() =="Normal">
            if isinstance(propValue,URIRef):
                from .${property.rangeClass.getSerializationClassName()?cap_first} import ${property.rangeClass.getSerializationClassName()?cap_first}
                ${property.name?uncap_first}Instance = ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(ontology, propValue,nestingLevel)
                if ${property.name?uncap_first}Instance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                ${classRep.name?uncap_first}.add${property.name?cap_first}(${property.name?uncap_first}Instance)
                    <#else>
                <@abstractClass property=property rangeClass=property.rangeClass isCycle=true/>
                    </#if>
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .${property.rangeClass.getSerializationClassName()?cap_first} import ${property.rangeClass.getSerializationClassName()?cap_first}
                        ${property.name?uncap_first}Instance = ${property.rangeClass.getSerializationClassName()?cap_first}().getInstanceFromModel(ontology,value,nestingLevel)
                        if ${property.name?uncap_first}Instance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        ${classRep.name?uncap_first}.add${property.name?cap_first}(${property.name?uncap_first}Instance)
            </#if>
        </#if>
    </#if>
</#macro>

<#macro innerSetProperties classRep>
<#list classRep.getSuperClasses() as superClass>
    <#if superClass.getClassType().getName() == "Normal">
        <#if superClass.properties?size gt 0 || superClass.superClasses?size gt 0>
        from .${superClass.getSerializationClassName()?cap_first} import ${superClass.getSerializationClassName()?cap_first}
        ${superClass.getSerializationClassName()?cap_first}().setProperties(ontology, ${classRep.name?uncap_first},nestingLevel)
         </#if>
    <#else>
        <#if superClass.isUnionOf() == true>
            <#list superClass.getProperties() as property>
                <@setProperties property=property classRep=classRep/>
            </#list>
        <#elseif superClass.isIntersectionOf() == true>
            <#list superClass.getProperties() as property>
                <@setProperties property=property classRep=classRep/>
            </#list>
        <#elseif superClass.isComplementOf() == true>
            <#list superClass.getProperties() as property>
                <@setProperties property=property classRep=classRep/>
            </#list>
         </#if>
    </#if>
</#list>
</#macro>

<#macro abstractClass property rangeClass isCycle>
<#if rangeClass.isUnionOf() == true>
    <#list rangeClass.getUnionOf() as unionClass>
         <#if unionClass.getClassType().getName() == "Normal">
            from .${unionClass.getSerializationClassName()?cap_first} import ${unionClass.getSerializationClassName()?cap_first}
            ${property.name?uncap_first}Instance${unionClass.name?cap_first} = ${unionClass.getSerializationClassName()?cap_first}().getInstanceFromModel(ontology, ${property.name?uncap_first},nestingLevel)
            if ${property.name?uncap_first}Instance${unionClass.name?cap_first} is not None:
                ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(${property.name?uncap_first}Instance${unionClass.name?cap_first})
                 <#if isCycle == true><#if property.isFunctional() == true>continue<#else>break</#if></#if>
        <#else>

     </#if>
    </#list>
<#elseif rangeClass.isIntersectionOf() == true>
     <#list rangeClass.getIntersectionOf() as intersectionClass>
        <#if intersectionClass.getClassType().getName() == "Normal">
                from .${serializationFactory?cap_first} import ${serializationFactory?cap_first}
                instance =  ${serializationFactory?cap_first}().getSerializationInstance(ontology,${property.name?uncap_first})
                if instance is not None:
                    ${classRep.name?uncap_first}.<#if property.isFunctional() == true>set<#else>add</#if>${property.name?cap_first}(instance.getInstanceFromModel(ontology, ${property.name?uncap_first},nestingLevel))

        <#else>
                <@abstractClass property=property rangeClass=intersectionClass isCycle=false/>
        </#if>
     </#list>
<#elseif rangeClass.isComplementOf() == true>
    <#if rangeClass.complementOf.getClassType().getName() == "Normal">
            #need to do
    <#else>
        <@abstractClass property=property rangeClass=rangeClass.getComplementOf() isCycle=false/>
    </#if>
</#if>
</#macro>
