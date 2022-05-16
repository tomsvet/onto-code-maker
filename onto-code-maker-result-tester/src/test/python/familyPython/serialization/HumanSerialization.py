
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Human import Human
from .SerializationModel import SerializationModel

class HumanSerialization(SerializationModel):

    def addToModel(self,ontology, human):
        ontology.add((human.getIri(),RDF.type, human.getClassIRI()))
        self.addPropertiesToModel(ontology,human)


    def addPropertiesToModel(self,ontology, human):
        hasLuckyNumbersPom = set(human.getHasLuckyNumbers())
        super().setLiteralsRDFCollection(ontology,human.getIri(),Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,hasLuckyNumbersPom)
        if human.getHasAge() is not None:
            ontology.add((human.getIri(),Vocabulary.HASAGE_PROPERTY_IRI,Literal(human.getHasAge())))

        hasCatPom = set(human.getHasCat())
        super().setRDFCollection(ontology,human.getIri(),Vocabulary.HASCAT_PROPERTY_IRI,hasCatPom)
        if human.getHasDog() is not None:
            ontology.add((human.getIri(),Vocabulary.HASDOG_PROPERTY_IRI,human.getHasDog().getIri()))

        agePom = set(human.getAge())
        super().setLiteralsRDFCollection(ontology,human.getIri(),Vocabulary.AGE_PROPERTY_IRI,agePom)
        pass

    def setProperties(self,ontology, human, nestingLevel):
        hasLuckyNumbers = super().getAllObjects(ontology,Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,human.getIri())
        for propValue in hasLuckyNumbers:
            if isinstance(propValue,Literal):
                human.addHasLuckyNumbers(str(propValue))
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for literalValue in listOfValues :
                    if isinstance(literalValue,Literal):
                        human.addHasLuckyNumbers(str(literalValue))

        hasAge = super().getFirstLiteralObject(ontology,Vocabulary.HASAGE_PROPERTY_IRI,human.getIri())
        if hasAge is not None:
            human.setHasAge(str(hasAge))
        hasCat = super().getAllResourceObjects(ontology,Vocabulary.HASCAT_PROPERTY_IRI,human.getIri())
        for propValue in hasCat:
            if isinstance(propValue,URIRef):
                from .CatSerialization import CatSerialization
                hasCatInstance = CatSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasCatInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                human.addHasCat(hasCatInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .CatSerialization import CatSerialization
                        hasCatInstance = CatSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasCatInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        human.addHasCat(hasCatInstance)
        hasDog = super().getFirstIriObject(ontology,Vocabulary.HASDOG_PROPERTY_IRI,human.getIri())
        if hasDog is None:
            # check equivalent property hasDogEq
            hasDog = super().getFirstIriObject(ontology,Vocabulary.HASDOGEQ_PROPERTY_IRI,human.getIri())

        if hasDog is not None:
            from .DogSerialization import DogSerialization
            hasDogInstance = DogSerialization().getInstanceFromModel(ontology, hasDog,nestingLevel)
            human.setHasDog(hasDogInstance)

        age = super().getAllObjects(ontology,Vocabulary.AGE_PROPERTY_IRI,human.getIri())
        for propValue in age:
            if isinstance(propValue,Literal):
                human.addAge(str(propValue))
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for literalValue in listOfValues :
                    if isinstance(literalValue,Literal):
                        human.addAge(str(literalValue))

        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Human.CLASS_IRI) in ontology:
            human =  Human(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, human,nestingLevel)
            return human
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Human.CLASS_IRI)):
            if isinstance(s,URIRef) :
                human = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(human)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Human.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



