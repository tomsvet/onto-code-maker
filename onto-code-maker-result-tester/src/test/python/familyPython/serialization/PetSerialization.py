
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Pet import Pet
from .SerializationModel import SerializationModel

class PetSerialization(SerializationModel):

    def addToModel(self,ontology, pet):
        ontology.add((pet.getIri(),RDF.type, pet.getClassIRI()))



    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Pet.CLASS_IRI) in ontology:
            pet =  Pet(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
            return pet
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Pet.CLASS_IRI)):
            if isinstance(s,URIRef) :
                pet = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(pet)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Pet.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



