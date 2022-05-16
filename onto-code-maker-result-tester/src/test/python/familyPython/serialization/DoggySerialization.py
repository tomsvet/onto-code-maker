
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Doggy import Doggy
from .SerializationModel import SerializationModel

class DoggySerialization(SerializationModel):

    def addToModel(self,ontology, doggy):
        ontology.add((doggy.getIri(),RDF.type, doggy.getClassIRI()))



    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Doggy.CLASS_IRI) in ontology:
            doggy =  Doggy(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
            return doggy
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Doggy.CLASS_IRI)):
            if isinstance(s,URIRef) :
                doggy = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(doggy)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Doggy.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



