
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Parent import Parent
from .SerializationModel import SerializationModel

class ParentSerialization(SerializationModel):

    def addToModel(self,ontology, parent):
        ontology.add((parent.getIri(),RDF.type, parent.getClassIRI()))
        self.addPropertiesToModel(ontology,parent)


    def addPropertiesToModel(self,ontology, parent):
        hasChildPom = set(parent.getHasChild())
        super().setRDFCollection(ontology,parent.getIri(),Vocabulary.HASCHILD_PROPERTY_IRI,hasChildPom)
        pass

    def setProperties(self,ontology, parent, nestingLevel):
        hasChild = super().getAllResourceObjects(ontology,Vocabulary.HASCHILD_PROPERTY_IRI,parent.getIri())
        for propValue in hasChild:
            if isinstance(propValue,URIRef):
                from .ChildSerialization import ChildSerialization
                hasChildInstance = ChildSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasChildInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                parent.addHasChild(hasChildInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .ChildSerialization import ChildSerialization
                        hasChildInstance = ChildSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasChildInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        parent.addHasChild(hasChildInstance)
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Parent.CLASS_IRI) in ontology:
            parent =  Parent(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, parent,nestingLevel)
            return parent
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Parent.CLASS_IRI)):
            if isinstance(s,URIRef) :
                parent = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(parent)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Parent.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



