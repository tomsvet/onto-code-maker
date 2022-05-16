
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Child import Child
from .SerializationModel import SerializationModel

class ChildSerialization(SerializationModel):

    def addToModel(self,ontology, child):
        ontology.add((child.getIri(),RDF.type, child.getClassIRI()))
        self.addPropertiesToModel(ontology,child)


    def addPropertiesToModel(self,ontology, child):
        hasParentPom = set(child.getHasParent())
        for x in child.getHasFather():
            hasParentPom.remove(x)
        super().setRDFCollection(ontology,child.getIri(),Vocabulary.HASPARENT_PROPERTY_IRI,hasParentPom)
        if child.getParent() is not None:
            ontology.add((child.getIri(),Vocabulary.PARENT_PROPERTY_IRI,child.getParent().getIri()))

        hasFatherPom = set(child.getHasFather())
        super().setRDFCollection(ontology,child.getIri(),Vocabulary.HASFATHER_PROPERTY_IRI,hasFatherPom)
        pass

    def setProperties(self,ontology, child, nestingLevel):
        hasParent = super().getAllResourceObjects(ontology,Vocabulary.HASPARENT_PROPERTY_IRI,child.getIri())
        for propValue in hasParent:
            if isinstance(propValue,URIRef):
                from .ParentSerialization import ParentSerialization
                hasParentInstance = ParentSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasParentInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                child.addHasParent(hasParentInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .ParentSerialization import ParentSerialization
                        hasParentInstance = ParentSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasParentInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        child.addHasParent(hasParentInstance)
        parent = super().getFirstIriObject(ontology,Vocabulary.PARENT_PROPERTY_IRI,child.getIri())
        if parent is None:
            # check inverse functional property
            parent = super().getSubjectOfCollectionValue(ontology,Vocabulary.HASCHILD_PROPERTY_IRI,child.getIri())
        if parent is not None:
            from .ParentSerialization import ParentSerialization
            parentInstance = ParentSerialization().getInstanceFromModel(ontology, parent,nestingLevel)
            child.setParent(parentInstance)

        hasFather = super().getAllResourceObjects(ontology,Vocabulary.HASFATHER_PROPERTY_IRI,child.getIri())
        for propValue in hasFather:
            if isinstance(propValue,URIRef):
                from .ParentSerialization import ParentSerialization
                hasFatherInstance = ParentSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasFatherInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                child.addHasFather(hasFatherInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .ParentSerialization import ParentSerialization
                        hasFatherInstance = ParentSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasFatherInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        child.addHasFather(hasFatherInstance)
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Child.CLASS_IRI) in ontology:
            child =  Child(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, child,nestingLevel)
            return child
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Child.CLASS_IRI)):
            if isinstance(s,URIRef) :
                child = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(child)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Child.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



