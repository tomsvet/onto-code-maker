
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Men import Men
from .SerializationModel import SerializationModel

class MenSerialization(SerializationModel):

    def addToModel(self,ontology, men):
        ontology.add((men.getIri(),RDF.type, men.getClassIRI()))
        self.addPropertiesToModel(ontology,men)


    def addPropertiesToModel(self,ontology, men):
        if men.getHasWife() is not None and men.getHasWife() is not men.getHasSpouse():
            ontology.add((men.getIri(),Vocabulary.HASWIFE_PROPERTY_IRI,men.getHasWife().getIri()))
            ontology.add((men.getHasWife().getIri() ,Vocabulary.HASHUSBAND_PROPERTY_IRI,men.getIri()))

        hasSpousePom = set(men.getHasSpouse())
        super().setRDFCollection(ontology,men.getIri(),Vocabulary.HASSPOUSE_PROPERTY_IRI,hasSpousePom)
        from .HumanSerialization import HumanSerialization
        HumanSerialization().addPropertiesToModel(ontology, men)
        pass

    def setProperties(self,ontology, men, nestingLevel):
        hasWife = super().getFirstIriObject(ontology,Vocabulary.HASWIFE_PROPERTY_IRI,men.getIri())
        if hasWife is None:
            # check inverse functional property
            hasWife = super().getSubjectOfCollectionValue(ontology,Vocabulary.MEN_PROPERTY_IRI,men.getIri())
        if hasWife is None:
            # check inverse property hasHusband
            hasWife = super().getSubjectOfCollectionValue(ontology,Vocabulary.HASHUSBAND_PROPERTY_IRI,men.getIri())
            #hasWife = super().getFirstIRISubject(ontology,Vocabulary.HASHUSBAND_PROPERTY_IRI,men.getIri())

        if hasWife is not None:
            from .WomanSerialization import WomanSerialization
            hasWifeInstance = WomanSerialization().getInstanceFromModel(ontology, hasWife,nestingLevel)
            men.setHasWife(hasWifeInstance)

        hasSpouse = super().getAllResourceObjects(ontology,Vocabulary.HASSPOUSE_PROPERTY_IRI,men.getIri())
        for propValue in hasSpouse:
            if isinstance(propValue,URIRef):
                from .WomanSerialization import WomanSerialization
                hasSpouseInstance = WomanSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasSpouseInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                men.addHasSpouse(hasSpouseInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .WomanSerialization import WomanSerialization
                        hasSpouseInstance = WomanSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasSpouseInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        men.addHasSpouse(hasSpouseInstance)
        from .HumanSerialization import HumanSerialization
        HumanSerialization().setProperties(ontology, men,nestingLevel)
        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Men.CLASS_IRI) in ontology:
            men =  Men(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, men,nestingLevel)
            return men
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Men.CLASS_IRI)):
            if isinstance(s,URIRef) :
                men = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(men)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Men.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



