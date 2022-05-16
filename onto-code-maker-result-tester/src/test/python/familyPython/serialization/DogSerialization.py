
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Dog import Dog
from .SerializationModel import SerializationModel

class DogSerialization(SerializationModel):

    def addToModel(self,ontology, dog):
        ontology.add((dog.getIri(),RDF.type, dog.getClassIRI()))
        self.addPropertiesToModel(ontology,dog)


    def addPropertiesToModel(self,ontology, dog):
        pass

    def setProperties(self,ontology, dog, nestingLevel):
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Dog.CLASS_IRI) in ontology:
            dog =  Dog(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, dog,nestingLevel)
            return dog
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Dog.CLASS_IRI)):
            if isinstance(s,URIRef) :
                dog = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(dog)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Dog.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



