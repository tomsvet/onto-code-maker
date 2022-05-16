
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.ChildlessPerson import ChildlessPerson
from .SerializationModel import SerializationModel

class ChildlessPersonSerialization(SerializationModel):

    def addToModel(self,ontology, childlessPerson):
        ontology.add((childlessPerson.getIri(),RDF.type, childlessPerson.getClassIRI()))



    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,ChildlessPerson.CLASS_IRI) in ontology:
            childlessPerson =  ChildlessPerson(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
            return childlessPerson
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,ChildlessPerson.CLASS_IRI)):
            if isinstance(s,URIRef) :
                childlessPerson = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(childlessPerson)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,ChildlessPerson.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



