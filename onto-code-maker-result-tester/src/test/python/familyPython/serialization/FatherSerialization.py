
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Father import Father
from .SerializationModel import SerializationModel

class FatherSerialization(SerializationModel):

    def addToModel(self,ontology, father):
        ontology.add((father.getIri(),RDF.type, father.getClassIRI()))



    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Father.CLASS_IRI) in ontology:
            father =  Father(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
            return father
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Father.CLASS_IRI)):
            if isinstance(s,URIRef) :
                father = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(father)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Father.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



