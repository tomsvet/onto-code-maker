
from rdflib.graph import URIRef, Literal,BNode,Collection
from rdflib.namespace import RDF
from Vocabulary import Vocabulary
from entities.Person import Person
from .SerializationModel import SerializationModel

class PersonSerialization(SerializationModel):

    def addToModel(self,ontology, person):
        ontology.add((person.getIri(),RDF.type, person.getClassIRI()))
        self.addPropertiesToModel(ontology,person)


    def addPropertiesToModel(self,ontology, person):
        hasLuckyNumbersPom = set(person.getHasLuckyNumbers())
        super().setLiteralsRDFCollection(ontology,person.getIri(),Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,hasLuckyNumbersPom)
        if person.getHasAge() is not None:
            ontology.add((person.getIri(),Vocabulary.HASAGE_PROPERTY_IRI,Literal(person.getHasAge())))

        hasCatPom = set(person.getHasCat())
        super().setRDFCollection(ontology,person.getIri(),Vocabulary.HASCAT_PROPERTY_IRI,hasCatPom)
        if person.getHasDog() is not None:
            ontology.add((person.getIri(),Vocabulary.HASDOG_PROPERTY_IRI,person.getHasDog().getIri()))

        agePom = set(person.getAge())
        super().setLiteralsRDFCollection(ontology,person.getIri(),Vocabulary.AGE_PROPERTY_IRI,agePom)
        pass

    def setProperties(self,ontology, person, nestingLevel):
        hasLuckyNumbers = super().getAllObjects(ontology,Vocabulary.HASLUCKYNUMBERS_PROPERTY_IRI,person.getIri())
        for propValue in hasLuckyNumbers:
            if isinstance(propValue,Literal):
                person.addHasLuckyNumbers(str(propValue))
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for literalValue in listOfValues :
                    if isinstance(literalValue,Literal):
                        person.addHasLuckyNumbers(str(literalValue))

        hasAge = super().getFirstLiteralObject(ontology,Vocabulary.HASAGE_PROPERTY_IRI,person.getIri())
        if hasAge is not None:
            person.setHasAge(str(hasAge))
        hasCat = super().getAllResourceObjects(ontology,Vocabulary.HASCAT_PROPERTY_IRI,person.getIri())
        for propValue in hasCat:
            if isinstance(propValue,URIRef):
                from .CatSerialization import CatSerialization
                hasCatInstance = CatSerialization().getInstanceFromModel(ontology, propValue,nestingLevel)
                if hasCatInstance is None:
                    print("Instance of " + str(propValue) + " is not in model.")
                person.addHasCat(hasCatInstance)
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for value in listOfValues:
                    if isinstance(value,URIRef):
                        from .CatSerialization import CatSerialization
                        hasCatInstance = CatSerialization().getInstanceFromModel(ontology,value,nestingLevel)
                        if hasCatInstance is None:
                            print("Instance of " + str(propValue) + " is not in model.")
                        person.addHasCat(hasCatInstance)
        hasDog = super().getFirstIriObject(ontology,Vocabulary.HASDOG_PROPERTY_IRI,person.getIri())
        if hasDog is None:
            # check equivalent property hasDogEq
            hasDog = super().getFirstIriObject(ontology,Vocabulary.HASDOGEQ_PROPERTY_IRI,person.getIri())

        if hasDog is not None:
            from .DogSerialization import DogSerialization
            hasDogInstance = DogSerialization().getInstanceFromModel(ontology, hasDog,nestingLevel)
            person.setHasDog(hasDogInstance)

        age = super().getAllObjects(ontology,Vocabulary.AGE_PROPERTY_IRI,person.getIri())
        for propValue in age:
            if isinstance(propValue,Literal):
                person.addAge(str(propValue))
            elif isinstance(propValue,BNode):
                listOfValues = super().getRDFCollection(ontology,propValue)
                for literalValue in listOfValues :
                    if isinstance(literalValue,Literal):
                        person.addAge(str(literalValue))

        pass

    def getInstanceFromModel(self,ontology, instanceIri,nestingLevel):
        if (instanceIri,RDF.type,Person.CLASS_IRI) in ontology:
            person =  Person(instanceIri)
            if nestingLevel > 0:
                nestingLevel -= 1
                self.setProperties(ontology, person,nestingLevel)
            return person
        return None

    def getAllInstancesFromModel(self,ontology,nestingLevel):
        allInstances = set()
        for s, p, o in ontology.triples((None,RDF.type,Person.CLASS_IRI)):
            if isinstance(s,URIRef) :
                person = self.getInstanceFromModel(ontology,s,nestingLevel)
                allInstances.add(person)
        return allInstances

    def removeInstanceFromModel(self,ontology, subject):
        ontology.remove((subject,RDF.type,Person.CLASS_IRI))
        for statement in ontology.triples((subject,None,None)):
            ontology.remove(statement)



