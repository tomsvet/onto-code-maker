
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Woman import Woman
from .SerializationModel import SerializationModel

class WomanSerialization(SerializationModel):

    def addToModel(self,ontology, woman):
        ontology.add((woman.getIri(),RDF.type, woman.getClassIRI()))
        self.addPropertiesToModel(ontology,woman)


    def addPropertiesToModel(self,ontology, woman):
        hasHusbandPom = set(woman.getHasHusband())
        super().setRDFCollection(ontology,woman.getIri(),Vocabulary.HASHUSBAND_PROPERTY_IRI,hasHusbandPom)
        if woman.getMen() is not None:
            ontology.add((woman.getIri(),Vocabulary.MEN_PROPERTY_IRI,woman.getMen().getIri()))

        from .HumanSerialization import HumanSerialization
        HumanSerialization().addPropertiesToModel(ontology, woman)
        pass

    def setProperties(self,ontology, woman, nestingLevel):
        hasHusband = super().getAllResourceObjects(ontology,Vocabulary.HASHUSBAND_PROPERTY_IRI,woman.getIri())
        # add also all values from inverse property hasWife
        for p in super().getAllIRISubjects(ontology,Vocabulary.HASWIFE_PROPERTY_IRI,woman.getIri()):
            hasHusband.add(p)
        for propValue in hasHusband:
            if isinstance(propValue,URIRef):
                from .MenSerialization import MenSerialization
                hasHusbandInstance = MenSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasHusbandInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                woman.addHasHusband(hasHusbandInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .MenSerialization import MenSerialization
                        hasHusbandInstance = MenSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasHusbandInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        woman.addHasHusband(hasHusbandInstance)
        men = super().getFirstIriObject(ontology,Vocabulary.MEN_PROPERTY_IRI,woman.getIri())
        if men is None:
            # check inverse functional property
            men = super().getSubjectOfCollectionValue(ontology,Vocabulary.HASWIFE_PROPERTY_IRI,woman.getIri())
        if men is not None:
            from .MenSerialization import MenSerialization
            menInstance = MenSerialization().getInstanceFromModel(ontology, men,nestingLevel)
            woman.setMen(menInstance)

        from .HumanSerialization import HumanSerialization
        HumanSerialization().setProperties(ontology, woman,nestingLevel)
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Woman.CLASS_IRI) in ontology:
            woman =  Woman(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, woman,nestingLevel)
            return woman
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Woman.CLASS_IRI)):
            if isinstance(s,URIRef) :
                woman = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(woman)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Woman.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



