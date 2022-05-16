
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Mother import Mother
from .SerializationModel import SerializationModel

class MotherSerialization(SerializationModel):

    def addToModel(self,ontology, mother):
        ontology.add((mother.getIri(),RDF.type, mother.getClassIRI()))



    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Mother.CLASS_IRI) in ontology:
            mother =  Mother(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
            return mother
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Mother.CLASS_IRI)):
            if isinstance(s,URIRef) :
                mother = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(mother)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Mother.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



