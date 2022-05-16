
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Cat import Cat
from .SerializationModel import SerializationModel

class CatSerialization(SerializationModel):

    def addToModel(self,ontology, cat):
        ontology.add((cat.getIri(),RDF.type, cat.getClassIRI()))
        self.addPropertiesToModel(ontology,cat)


    def addPropertiesToModel(self,ontology, cat):
        pass

    def setProperties(self,ontology, cat, nestingLevel):
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Cat.CLASS_IRI) in ontology:
            cat =  Cat(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, cat,nestingLevel)
            return cat
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Cat.CLASS_IRI)):
            if isinstance(s,URIRef) :
                cat = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(cat)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Cat.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



